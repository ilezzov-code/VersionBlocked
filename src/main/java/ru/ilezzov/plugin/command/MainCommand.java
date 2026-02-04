package ru.ilezzov.plugin.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.units.qual.C;
import ru.ilezzov.plugin.Main;
import ru.ilezzov.plugin.config.Config;
import ru.ilezzov.plugin.permission.Permissions;
import ru.ilezzov.plugin.utils.LegacySerialize;

import java.util.Collection;
import java.util.List;

import static ru.ilezzov.plugin.utils.LegacySerialize.*;

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

    public MainCommand(final ProxyServer proxyServer) {
        this.proxyServer = proxyServer;
    }

    @Override
    public void execute(final Invocation invocation) {
        final CommandSource source = invocation.source();
        final String[] args = invocation.arguments();
        final Config.MessagesSection messagesSection = Main.getConfig().messages;

        if (args.length == 0) {
            source.sendMessage(serialize(messagesSection.mainCommand));
            return;
        }

        switch (args[0]) {
            case "reload" -> {
                if (!source.hasPermission(Permissions.MAIN_COMMAND_RELOAD)) {
                    source.sendMessage(serialize(messagesSection.mainCommandPermissionError));
                    return;
                }

                if (Main.reloadConfigFile()) {
                    source.sendMessage(serialize(messagesSection.mainCommandReload));
                } else {
                    source.sendMessage(serialize(messagesSection.mainCommandReloadError));
                }

                if (Main.getConfig().versionFilter.kickConnected) {
                    final int[] response =  checkAllPlayerVersion();
                    final Component message = LegacySerialize.serialize(messagesSection.checkedPlayers.replace("{checked_players}", String.valueOf(response[0])).replace("{kicked_players}", String.valueOf(response[1])));
                    source.sendMessage(message);

                }
            }
            default -> source.sendMessage(serialize(messagesSection.mainCommand));
        }
    }

    private int[] checkAllPlayerVersion() {
        final Config config = Main.getConfig();
        final Config.VersionFilterSection versionFilterSection = config.versionFilter;
        final Component reason = LegacySerialize.serialize(config.messages.kickReason);

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
