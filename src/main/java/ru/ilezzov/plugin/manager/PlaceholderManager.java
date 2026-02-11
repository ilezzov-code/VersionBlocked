package ru.ilezzov.plugin.manager;

import ru.ilezzov.plugin.Main;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class PlaceholderManager {
    private static final Pattern patter = Pattern.compile("<(.*?)>");

    private final Map<String, Object> placeholders = new HashMap<>();

    public PlaceholderManager() {
        addPlaceholder("<prefix>", Main.getMessages().plugin.prefix);
        addPlaceholder("<prefix_error>", Main.getMessages().plugin.prefixError);
    }

    public PlaceholderManager addPlaceholder(final String key, final Object value) {
        this.placeholders.put(key, value);
        return this;
    }

    public Map<String, Object> getPlaceholders() {
        return this.placeholders;
    }

    public String replacePlaceholder(final String message) {
        final StringBuilder result = new StringBuilder(message.length());
        final Matcher matcher = patter.matcher(message);

        while (matcher.find()) {
            final String key = matcher.group();
            final Object value = this.placeholders.get(key);

            if (value == null) {
                continue;
            }

            matcher.appendReplacement(result, Matcher.quoteReplacement(value.toString()));
        }
        matcher.appendTail(result);

        return result.toString();
    }

}
