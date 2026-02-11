package ru.ilezzov.plugin;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.bstats.velocity.Metrics;
import org.slf4j.Logger;
import ru.ilezzov.plugin.command.MainCommand;
import ru.ilezzov.plugin.file.config.Config;
import ru.ilezzov.plugin.file.FileManager;
import ru.ilezzov.plugin.event.PreLoginListener;
import ru.ilezzov.plugin.file.message.Messages;
import ru.ilezzov.plugin.manager.PlaceholderManager;
import ru.ilezzov.plugin.model.Response;
import ru.ilezzov.plugin.properties.MyProperties;
import ru.ilezzov.plugin.version.VersionDate;
import ru.ilezzov.plugin.version.VersionManager;
import ru.ilezzov.plugin.version.VersionType;

import java.nio.file.Path;
import java.util.function.Consumer;

import static ru.ilezzov.plugin.logging.Lang.*;
import static ru.ilezzov.plugin.message.PluginMessage.*;
import static ru.ilezzov.plugin.utils.LegacySerialize.serializeToANSI;

@Plugin(id = "versionblocked", name = "VersionBlocked", version = ru.ilezzov.plugin.BuildConstants.VERSION, description = ru.ilezzov.plugin.BuildConstants.DESCRIPTION, url = "https://t.me/ilezovofficial", authors = {"ILeZzoV"})
public class Main {
    private final Metrics.Factory metricsFactory;
    private final ProxyServer proxyServer;
    private final Logger logger;
    private final Path dataDirectory;

    private static MyProperties properties;

    private static String currentVersion;
    private static String configFile;
    private static String messageFile;

    private static VersionManager versionManager;

    private static FileManager<Config> configFileManager;
    private static Config config;

    private static FileManager<Messages> messagesFileManager;
    private static Messages messages;

    private PlaceholderManager placeholder;

    private final int pluginId = 29480;
    private Metrics metrics;

    @Inject
    public Main(final ProxyServer proxyServer, final Logger logger, @DataDirectory final Path dataDirectory, final Metrics.Factory metricsFactory) {
        this.proxyServer = proxyServer;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
        this.metricsFactory = metricsFactory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        logger.info(STARTING_PLUGIN);
        properties = new MyProperties(this.logger);

        configFile = properties.getConfigurationFile();
        currentVersion = properties.getCurrentVersion();

        configFileManager = new FileManager<>(dataDirectory, configFile, Config.class);
        if (!loadFile(configFileManager, value -> config = value, configFile)) {
            logger.info(STOPPING_PLUGIN);
            return;
        }

        messageFile = getMessagesFilePath();

        messagesFileManager = new FileManager<>(dataDirectory, messageFile, Messages.class);
        if (!loadFile(messagesFileManager, value -> messages = value, messageFile)) {
            logger.info(STOPPING_PLUGIN);
            return;
        }

        this.placeholder = new PlaceholderManager();

        versionManager = new VersionManager();
        if (config.checkUpdates) {
            if(!checkLatestVersion()) {
                logger.info(STOPPING_PLUGIN);
                return;
            }
        }

        final EventManager eventManager = this.proxyServer.getEventManager();
        eventManager.register(this, new PreLoginListener(this.logger));

        final CommandManager commandManager = this.proxyServer.getCommandManager();
        final CommandMeta commandMeta = commandManager.metaBuilder("versionblocked")
                .aliases("vb", "versblock")
                .plugin(this)
                .build();
        commandManager.register(commandMeta, new MainCommand(proxyServer));
        loadMetrics();
    }

    private void loadMetrics() {
        metricsFactory.make(this, pluginId);
    }

    public static boolean reloadFiles() {
        final Response<FileManager.ConfigStatus> configResponse = configFileManager.load();
        if (!configResponse.success()) {
            return false;
        }

        final Response<FileManager.ConfigStatus> messagesResponse = messagesFileManager.load();
        if (!messagesResponse.success()) {
            return false;
        }

        config = configFileManager.getFileObject();
        messages = messagesFileManager.getFileObject();
        return true;
    }

    private <T> boolean loadFile(final FileManager<T> manager, final Consumer<T> setter, final String fileName) {
        final Response<FileManager.ConfigStatus> response = manager.load();
        if (!response.success()) {
            final Exception e = response.error();

            if (e == null) {
                logger.error(response.message());
            } else {
                logger.error(response.message(), response.error());
            }
            return false;
        }

        final FileManager.ConfigStatus status = response.data();
        if (status == FileManager.ConfigStatus.CREATED) {
            logger.info(FILE_CREATED.formatted(fileName));
        }

        if (status == FileManager.ConfigStatus.UPDATED) {
            logger.info(FILE_UPDATED.formatted(fileName));
        }

        setter.accept(manager.getFileObject());
        logger.info(FILE_LOADED.formatted(fileName));

        return true;
    }

    private boolean checkLatestVersion() {
        final Response<Void> loadResponse = versionManager.load();

        if (!loadResponse.success()) {
            final Exception e = loadResponse.error();
            this.placeholder.addPlaceholder("<error>", loadResponse.message());

            if (e == null) {
                logger.info(serializeToANSI(hasErrorCheckVersion(placeholder)));
            } else {
                logger.error(serializeToANSI(hasErrorCheckVersion(placeholder)), loadResponse.error());
            }
            versionManager = null;
            return true;
        }

        final Response<VersionType> versionTypeResponse = versionManager.check();
        this.placeholder.addPlaceholder("<error>", versionTypeResponse.message());
        if (!versionTypeResponse.success()) {
            logger.error(serializeToANSI(hasErrorCheckVersion(placeholder)), loadResponse.error());
            return true;
        }

        final VersionType versionType = versionTypeResponse.data();
        final VersionDate versionDate = versionManager.getVersionDate();

        final String latestVersion = versionDate.latest.version;
        final String downloadLink = versionDate.latest.downloadUrl;

        this.placeholder.addPlaceholder("<latest_vers>", latestVersion);
        this.placeholder.addPlaceholder("<current_vers>", currentVersion);
        this.placeholder.addPlaceholder("<download_link>", downloadLink);
        switch (versionType) {
            case LATEST -> {
                logger.info(serializeToANSI(useLatestVersion(placeholder)));
            }
            case SUPPORTED -> {
                logger.info(serializeToANSI(useSupportedVersion(placeholder)));
            }
            case BLACKLIST -> {
                logger.info(serializeToANSI(useBlacklistVersion(placeholder)));
                return false;
            }
            case OUTDATED -> {
                logger.info(serializeToANSI(useOutdatedVersion(placeholder)));
                return false;
            }
        }
        return true;
    }

    private String getMessagesFilePath() {
        return "messages/" + config.lang + ".yml";
    }

    public static MyProperties getProperties() {
        return properties;
    }

    public static Config getConfig() {
        return config;
    }

    public static Messages getMessages() {
        return messages;
    }
}
