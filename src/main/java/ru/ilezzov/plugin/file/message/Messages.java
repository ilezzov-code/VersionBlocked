package ru.ilezzov.plugin.file.message;

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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Messages {
    public PluginSection plugin;
    public MessagesSection messages;
    public String configVersion;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PluginSection {
        public String prefix;
        public String prefixError;
        public String reload;
        public String hasErrorReload;
        public String hasError;
        public String hasErrorCheckVersion;
        public String useLatestVersion;
        public String useOutdatedVersion;
        public String useSupportedVersion;
        public String useBlacklistVersion;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MessagesSection {
        public String mainCommand;
        public String mainCommandReload;
        public String mainCommandReloadError;
        public String mainCommandPermissionError;
        public String kickReason;
        public String checkedPlayers;

    }
}
