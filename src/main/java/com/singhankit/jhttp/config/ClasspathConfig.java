package com.singhankit.jhttp.config;

import java.io.InputStream;
import java.util.Optional;

import static com.singhankit.jhttp.config.ConfigHelper.getFile;

/**
 * @author Ankit Singh
 */
class ClasspathConfig implements Config {

    private String profile;

    ClasspathConfig(String profile) {
        this.profile = profile;
    }

    ClasspathConfig() {
        this("");
    }

    @Override
    public Optional<InputStream> read() {
        return Optional.ofNullable(this.getClass().getClassLoader().getResourceAsStream(getFile(profile)));
    }

}
