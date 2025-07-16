package com.singhankit.jhttp.config;

import com.singhankit.jhttp.Util;

import java.io.InputStream;
import java.util.Optional;

/**
 * @author Ankit Singh
 */
class EnvironmentConfig implements Config {
    private static final String ENV_CONFIG_FILE = "jhttp.config.file";

    @Override
    public Optional<InputStream> read() {
        String path = System.getenv(ENV_CONFIG_FILE);
        if(Util.isEmpty(path)) {
            return Optional.empty();
        }
        return new FilesystemConfig().read();
    }
}
