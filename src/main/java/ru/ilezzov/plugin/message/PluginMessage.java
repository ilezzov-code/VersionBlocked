package ru.ilezzov.plugin.message;

import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NonNull;
import ru.ilezzov.plugin.Main;
import ru.ilezzov.plugin.manager.PlaceholderManager;
import ru.ilezzov.plugin.utils.LegacySerialize;

import static ru.ilezzov.plugin.Main.getConfig;
import static ru.ilezzov.plugin.Main.getMessages;

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
public class PluginMessage {
    public static Component reload(final PlaceholderManager placeholder) {
        return getComponent(getMessages().plugin.reload, placeholder);
    }

    public static Component hasErrorReload(final PlaceholderManager placeholder) {
        return getComponent(getMessages().plugin.hasErrorReload, placeholder);
    }

    public static Component hasErrorCheckVersion(final PlaceholderManager placeholder) {
        return getComponent(getMessages().plugin.hasErrorCheckVersion, placeholder);
    }

    public static Component useLatestVersion(final PlaceholderManager placeholder) {
        return getComponent(getMessages().plugin.useLatestVersion, placeholder);
    }

    public static Component useOutdatedVersion(final PlaceholderManager placeholder) {
        return getComponent(getMessages().plugin.useOutdatedVersion, placeholder);
    }

    public static Component useSupportedVersion(final PlaceholderManager placeholder) {
        return getComponent(getMessages().plugin.useSupportedVersion, placeholder);
    }

    public static Component useBlacklistVersion(final PlaceholderManager placeholder) {
        return getComponent(getMessages().plugin.useBlacklistVersion, placeholder);
    }

    public static Component hasError(final PlaceholderManager placeholder) {
        return getComponent(getMessages().plugin.hasError, placeholder);
    }

    public static Component mainCommand(final PlaceholderManager placeholder) {
        return getComponent(getMessages().messages.mainCommand, placeholder);
    }

    public static Component mainCommandReload(final PlaceholderManager placeholder) {
        return getComponent(getMessages().messages.mainCommandReload, placeholder);
    }

    public static Component mainCommandReloadError(final PlaceholderManager placeholder) {
        return getComponent(getMessages().messages.mainCommandReloadError, placeholder);
    }

    public static Component mainCommandPermissionError(final PlaceholderManager placeholder) {
        return getComponent(getMessages().messages.mainCommandPermissionError, placeholder);
    }

    public static Component kickReason(final PlaceholderManager placeholder) {
        return getComponent(getMessages().messages.kickReason, placeholder);
    }

    public static Component checkedPlayers(final PlaceholderManager placeholder) {
        return getComponent(getMessages().messages.checkedPlayers, placeholder);
    }

    public static Component loggingKick(final PlaceholderManager placeholder) {
        return getComponent(getConfig().logging.format, placeholder);
    }

    private static Component getComponent(final String text, final PlaceholderManager manager) {
        String message = manager.replacePlaceholder(text);
        return LegacySerialize.serialize(message);
    }

}
