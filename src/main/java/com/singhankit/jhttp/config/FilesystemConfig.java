package com.singhankit.jhttp.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;

/**
 * @author Ankit Singh
 */
class FilesystemConfig implements Config {

    private final Path path;

    FilesystemConfig(Path path) {
        this.path = path;
    }

    FilesystemConfig() {
        this(Path.of(CONFIG_FILE));
    }

    @Override
    public Optional<InputStream> read()  {
        try {
            return Optional.of(Files.newInputStream(path, StandardOpenOption.READ));
        }catch(IOException ex){
            return Optional.empty();
        }
    }
}
