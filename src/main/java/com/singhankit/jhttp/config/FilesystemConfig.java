package com.singhankit.jhttp.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;

import static com.singhankit.jhttp.config.ConfigHelper.getFile;

/**
 * @author Ankit Singh
 */
class FilesystemConfig implements Config {

    private String profile;

    FilesystemConfig(String profile) {
        this.profile = profile;
    }

    FilesystemConfig() {
        this("");
    }
    @Override
    public Optional<InputStream> read()  {
        try {
            Path file  = Path.of(getFile(profile));
            return Optional.of(Files.newInputStream(file, StandardOpenOption.READ));
        }catch(IOException ex){
            return Optional.empty();
        }
    }
}
