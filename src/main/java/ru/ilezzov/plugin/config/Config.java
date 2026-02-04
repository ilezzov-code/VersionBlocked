package ru.ilezzov.plugin.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

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

@JsonIgnoreProperties(ignoreUnknown = true)
public class Config {
    public boolean checkUpdates;
    public String configVersion;

    public VersionFilterSection versionFilter;
    public MessagesSection messages;
    public LoggingSection logging;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class VersionFilterSection {
        public int minVersionProtocol;
        public int maxVersionProtocol;
        public List<Integer> blockedProtocols;
        public boolean kickConnected;
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

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LoggingSection {
        public boolean enable;
        public String format;;
    }

    public Config() {}
}