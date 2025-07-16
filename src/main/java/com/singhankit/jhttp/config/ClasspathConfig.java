package com.singhankit.jhttp.config;

import java.io.InputStream;
import java.util.Optional;

/**
 * @author Ankit Singh
 */
class ClasspathConfig implements Config {

    @Override
    public Optional<InputStream> read() {
        return Optional.ofNullable(this.getClass().getClassLoader().getResourceAsStream(CONFIG_FILE));
    }

}
