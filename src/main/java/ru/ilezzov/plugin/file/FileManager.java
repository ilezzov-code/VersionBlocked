package ru.ilezzov.plugin.file;

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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import ru.ilezzov.plugin.model.Response;
import ru.ilezzov.plugin.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.ReadOnlyFileSystemException;
import java.util.Map;

import static ru.ilezzov.plugin.logging.Lang.*;

public class FileManager<T> {
    private static final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    private final Class<T> clazz;

    private final String file;
    private final Path configPath;
    private T fileObject;

    public FileManager(final Path dataFolder, final String file, final Class<T> clazz) {
        this.file = file;
        this.configPath = dataFolder.resolve(file);
        this.clazz = clazz;

        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.KEBAB_CASE);
    }

    public Response<ConfigStatus> load() {
        ConfigStatus status = ConfigStatus.LOADED;

        try (final InputStream in = FileManager.class.getClassLoader().getResourceAsStream(file)) {
            if (in == null) {
                return Response.error(RESOURCE_NOT_FOUD_ERROR.formatted(file));
            }

            final byte[] defaultConfigBytes = in.readAllBytes();

            if (Files.notExists(configPath)) {
                final Path parentDir = configPath.getParent();
                if (parentDir != null) {
                    Files.createDirectories(parentDir);
                }

                Files.write(configPath, defaultConfigBytes);
                status = ConfigStatus.CREATED;
            }

            final File currentFile = configPath.toFile();

            final JsonNode currentFileNode = mapper.readTree(currentFile);
            final JsonNode defaultFileNode = mapper.readTree(defaultConfigBytes);

            final String currentVersion = currentFileNode.path("config-version").asText();
            final String defaultVersion = defaultFileNode.path("config-version").asText();

            if (currentVersion == null || currentVersion.isBlank() || defaultVersion == null || defaultVersion.isBlank()) {
                return Response.error(REQUIRED_KEY_MISSING_ERROR.formatted("config-version"));
            }

            final Response<Integer> checkVersion = Utils.equalsVersion(currentVersion, defaultVersion);

            if (!checkVersion.success()) {
                return Response.error(COMPARING_VERSION_ERROR, checkVersion.error());
            }

            final int versionStatus = checkVersion.data();
            if (versionStatus == 1) {
                createDump(currentFile);

                if (currentFileNode.isObject()) {
                    final ObjectNode currentObjectNode = (ObjectNode) currentFileNode;
                    addMissingKeys(currentObjectNode, defaultFileNode);
                    currentObjectNode.set("config-version", defaultFileNode.get("config-version"));

                    mapper.writerWithDefaultPrettyPrinter().writeValue(currentFile, currentFileNode);
                    status = ConfigStatus.UPDATED;
                } else {
                    return Response.error(STRUCTURE_ERROR);
                }
            }

            this.fileObject = mapper.readValue(currentFile, clazz);
            return Response.ok(status);
        } catch (JsonParseException e) {
            return Response.error(SYNTAX_ERROR.formatted(e.getOriginalMessage()), e);
        } catch (JsonMappingException e) {
            return Response.error(STRUCTURE_ERROR, e);
        } catch (AccessDeniedException | ReadOnlyFileSystemException e) {
            return Response.error(ACCESS_DENIED_ERROR.formatted(configPath.toString()), e);
        } catch (IOException e) {
            return Response.error(IO_ERROR, e);
        }
    }

    private void createDump(final File currentFile) throws IOException {
        final File file = new File(currentFile.getPath().concat(".old"));
        Files.copy(currentFile.toPath(), file.toPath());
    }

    private void addMissingKeys(final ObjectNode currentFileNode, final JsonNode defaultFileNode) {
        for (final Map.Entry<String, JsonNode> entry : defaultFileNode.properties())  {
            final String key = entry.getKey();
            final JsonNode defaultValue = entry.getValue();

            if (!currentFileNode.has(key)) {
                currentFileNode.set(key, defaultValue);
                continue;
            }

            final JsonNode userValue = currentFileNode.get(key);
            if (defaultValue.isObject() && userValue.isObject()) {
                addMissingKeys((ObjectNode) userValue, defaultValue);
            }
        }
    }

    public T getFileObject() {
        return this.fileObject;
    }

    public enum ConfigStatus {
        CREATED, UPDATED, LOADED
    }
}
