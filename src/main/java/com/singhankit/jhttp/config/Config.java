package com.singhankit.jhttp.config;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author Ankit Singh
 */
 interface Config {

    String CONFIG_FILE = "config.properties";

    Optional<InputStream> read() ;

    static InputStream readConfig(Config... configs) {
        return Arrays.stream(configs)
                     .map(Config::read)
                     .filter(Optional::isPresent)
                     .map(Optional::get)
                     .findFirst()
                     .orElseThrow(() -> new IllegalStateException(CONFIG_FILE + " not found"));
    }
}
