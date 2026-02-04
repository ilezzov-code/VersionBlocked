package ru.ilezzov.plugin.properties;

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

import org.slf4j.Logger;
import ru.ilezzov.plugin.VersionBlocked;
import ru.ilezzov.plugin.model.Response;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static ru.ilezzov.plugin.logging.Lang.*;


public class MyProperties {
    private final Logger logger = VersionBlocked.getLogger();
    private final Properties properties = new Properties();

    private String currentVersion;
    private String versionFileUrl;
    private String configurationFile;

    public MyProperties() {
        final Response<Void> loadResponse = load();

        if (!loadResponse.success()) {
            logger.error(loadResponse.message(), loadResponse.error());
            return;
        }

        logger.info(FILE_LOADED.formatted(".properties"));
        loadValues();
    }

    public String getCurrentVersion() {
        return currentVersion;
    }

    public String getVersionFileUrl() {
        return versionFileUrl;
    }

    private Response<Void> load() {
        try (final InputStream in = MyProperties.class.getClassLoader().getResourceAsStream(".properties")) {
            properties.load(in);
            return Response.ok();
        } catch (final IOException e) {
            return Response.error(IO_ERROR, e);
        }
    }

    private void loadValues() {
        this.currentVersion = properties.getProperty("current-version");
        this.versionFileUrl = properties.getProperty("version-file-url");
        this.configurationFile = properties.getProperty("configuration-file");
    }

    public String getConfigurationFile() {
        return configurationFile;
    }
}
