package ru.ilezzov.plugin.event;

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

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PreLoginEvent;
import net.kyori.adventure.text.Component;
import org.slf4j.Logger;
import ru.ilezzov.plugin.Main;
import ru.ilezzov.plugin.config.Config;
import ru.ilezzov.plugin.utils.LegacySerialize;

public class PreLoginListener {
    private final Logger logger;

    public PreLoginListener(final Logger logger) {
        this.logger = logger;
    }

    @Subscribe(priority = 10)
    public void preLoginEvent(final PreLoginEvent event) {
        final int protocolVersion = event.getConnection().getProtocolVersion().getProtocol();

        final Config config = Main.getConfig();
        final Config.VersionFilterSection versionFilterSection = config.versionFilter;

        final Component reason = LegacySerialize.serialize(config.messages.kickReason);
        final boolean enableLogging = config.logging.enable;
        final String message = config.logging.format.replace("{player}", event.getUsername()).replace("{protocol}", String.valueOf(protocolVersion)).replace("{minecraft_version}", event.getConnection().getProtocolVersion().getVersionIntroducedIn());


        if (protocolVersion < versionFilterSection.minVersionProtocol) {
            kick(event, reason, enableLogging, message);
            return;
        }

        if (versionFilterSection.maxVersionProtocol != -1) {
            if (protocolVersion > versionFilterSection.maxVersionProtocol) {
                kick(event, reason, enableLogging, message);
                return;
            }
        }

        if (versionFilterSection.blockedProtocols.contains(protocolVersion)) {
            kick(event, reason, enableLogging, message);
            return;
        }
    }

    private void kick(final PreLoginEvent event, final Component reason, final boolean enableLogging, final String message) {
        event.setResult(PreLoginEvent.PreLoginComponentResult.denied(reason));

        if (enableLogging) {
            logger.info(message);
        }
    }

}
