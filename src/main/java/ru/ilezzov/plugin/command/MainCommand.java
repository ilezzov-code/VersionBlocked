package ru.ilezzov.plugin.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import ru.ilezzov.plugin.Main;
import ru.ilezzov.plugin.file.config.Config;
import ru.ilezzov.plugin.manager.PlaceholderManager;
import ru.ilezzov.plugin.permission.Permissions;

import java.util.Collection;
import java.util.List;

import static ru.ilezzov.plugin.message.PluginMessage.*;
import static ru.ilezzov.plugin.permission.Permissions.MAIN_COMMAND_RELOAD;

/*
 * Copyright (C) 2024-2026 ILeZzoV
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

public class MainCommand implements SimpleCommand {
    private final ProxyServer proxyServer;
    private final PlaceholderManager placeholder = new PlaceholderManager();

    public MainCommand(final ProxyServer proxyServer) {
        this.proxyServer = proxyServer;
    }

    @Override
    public void execute(final Invocation invocation) {
        final CommandSource source = invocation.source();
        final String[] args = invocation.arguments();

        if (args.length == 0) {
            source.sendMessage(mainCommand(placeholder));
            return;
        }

        switch (args[0]) {
            case "reload" -> {
                if (!source.hasPermission(MAIN_COMMAND_RELOAD)) {
                    source.sendMessage(mainCommandPermissionError(placeholder));
                    return;
                }

                if (Main.reloadFiles()) {
                    source.sendMessage(mainCommandReload(placeholder));
                } else {
                    source.sendMessage(mainCommandReloadError(placeholder));
                }

                if (Main.getConfig().versionFilter.kickConnected) {
                    final int[] response =  checkAllPlayerVersion();
                    this.placeholder.addPlaceholder("<checked_players>", response[0]).addPlaceholder("<kicked_players>", response[1]);
                    source.sendMessage(checkedPlayers(placeholder));

                }
            }
            default -> source.sendMessage(mainCommand(placeholder));
        }
    }

    private int[] checkAllPlayerVersion() {
        final Config config = Main.getConfig();
        final Config.VersionFilterSection versionFilterSection = config.versionFilter;
        final Component reason = kickReason(placeholder);

        final Collection<Player> players = proxyServer.getAllPlayers();

        int checkedPlayer = players.size();
        int kickedPlayer = 0;

        for (final Player player : players) {
            final int protocolVersion = player.getProtocolVersion().getProtocol();

            if (protocolVersion < versionFilterSection.minVersionProtocol) {
                player.disconnect(reason);
                kickedPlayer++;
                continue;
            }

            if (versionFilterSection.maxVersionProtocol != -1) {
                if (protocolVersion > versionFilterSection.maxVersionProtocol) {
                    player.disconnect(reason);
                    kickedPlayer++;
                    continue;
                }
            }

            if (versionFilterSection.blockedProtocols.contains(protocolVersion)) {
                player.disconnect(reason);
                kickedPlayer++;

            }
        }
        return new int[]{checkedPlayer, kickedPlayer};
    }

    @Override
    public List<String> suggest(final Invocation invocation) {
        return List.of("reload");
    }

    @Override
    public boolean hasPermission(final Invocation invocation) {
        return invocation.source().hasPermission(Permissions.MAIN_COMMAND);
    }
}
