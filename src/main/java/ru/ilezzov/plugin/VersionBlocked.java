package ru.ilezzov.plugin;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;
import ru.ilezzov.plugin.config.Config;
import ru.ilezzov.plugin.config.ConfigManager;
import ru.ilezzov.plugin.model.Response;
import ru.ilezzov.plugin.properties.MyProperties;
import ru.ilezzov.plugin.version.VersionDate;
import ru.ilezzov.plugin.version.VersionManager;
import ru.ilezzov.plugin.version.VersionType;

import java.nio.file.Path;

import static ru.ilezzov.plugin.logging.Lang.*;
import static ru.ilezzov.plugin.utils.LegacySerialize.serializeToANSI;

@Plugin(id = "versionblocked", name = "VersionBlocked", version = BuildConstants.VERSION, description = BuildConstants.DESCRIPTION, url = "https://t.me/ilezovofficial", authors = {"ILeZzoV"})
public class VersionBlocked {

    @Inject
    private static Logger logger;
    @Inject
    private static ProxyServer proxyServer;
    @DataDirectory
    private static Path dataDirectory;

    private static MyProperties properties;

    private static String currentVersion;
    private static String configFile;

    private static VersionManager versionManager;
    private static ConfigManager configManager;

    private static Config config;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        logger.info(STARTING_PLUGIN);
        properties = new MyProperties();

        configFile = properties.getConfigurationFile();
        currentVersion = properties.getCurrentVersion();

        configManager = new ConfigManager(dataDirectory, configFile);
        if (!loadConfigFile()) {
            return;
        }

        versionManager = new VersionManager();
        if(!checkLatestVersion()) {
            return;
        }


    }

    public static Logger getLogger() {
        return logger;
    }

    private static boolean loadConfigFile() {
        final Response<ConfigManager.ConfigStatus> response = configManager.load();
        if (!response.success()) {
            final Exception e = response.error();

            if (e == null) {
                logger.error(response.message());
            } else {
                logger.error(response.message(), response.error());
            }
            return false;
        }

        final ConfigManager.ConfigStatus status = response.data();
        if (status == ConfigManager.ConfigStatus.CREATED) {
            logger.info(FILE_CREATED.formatted(configFile));
        }

        if (status == ConfigManager.ConfigStatus.UPDATED) {
            logger.info(FILE_UPDATED.formatted(configFile));
        }

        config = configManager.getConfig();
        logger.info(FILE_LOADED.formatted(configFile));

        return true;
    }

    private static boolean checkLatestVersion() {
        final Response<Void> loadResponse = versionManager.load();

        if (!loadResponse.success()) {
            final Exception e = loadResponse.error();

            if (e == null) {
                logger.error(FAILED_VERSION_CHECK.formatted(loadResponse.message()));
            } else {
                logger.error(FAILED_VERSION_CHECK.formatted(loadResponse.message()), loadResponse.error());
            }
            versionManager = null;
            return true;
        }
        logger.info(VERSION_LOADED);

        final Response<VersionType> versionTypeResponse = versionManager.check();
        if (!versionTypeResponse.success()) {
            logger.error(FAILED_VERSION_CHECK.formatted(versionTypeResponse.message(), versionTypeResponse.error()));
            return true;
        }

        final VersionType versionType = versionTypeResponse.data();
        final VersionDate versionDate = versionManager.getVersionDate();

        final String latestVersion = versionDate.latest.version;
        final String downloadLink = versionDate.latest.downloadUrl;

        switch (versionType) {
            case LATEST -> {
                final String message = serializeToANSI(LATEST_VERSION.formatted(latestVersion));
                logger.info(message);
            }
            case SUPPORTED -> {
                final String message = serializeToANSI(SUPPORTED_VERSION.formatted(currentVersion, latestVersion, downloadLink));
                logger.warn(message);
            }
            case BLACKLIST -> {
                final String message = serializeToANSI(BLACKLIST_VERSION.formatted(currentVersion, latestVersion, downloadLink));
                logger.error(message);
                return false;
            }
            case OUTDATED -> {
                final String message = serializeToANSI(OUTDATED_VERSION.formatted(currentVersion, latestVersion, downloadLink));
                logger.error(message);
                return false;
            }
        }
        return true;
    }

    public static MyProperties getProperties() {
        return properties;
    }

    public static Config getConfig() {
        return config;
    }
}
