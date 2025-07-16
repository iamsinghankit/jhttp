package com.singhankit.jhttp.config;

import com.singhankit.jhttp.Util;

import java.util.Properties;

import static com.singhankit.jhttp.config.ConfigHelper.readConfiguration;

/**
 * @author Ankit Singh
 */
public class ConfigManager {
    private static final ConfigManager INSTANCE = new ConfigManager();
    private final Properties config;

    private ConfigManager() {
        config = readConfiguration();
    }

    public static ConfigManager of() {
        return INSTANCE;
    }

    public String getString(String key) {
        return config.getProperty(key);
    }

    public String getString(String key, String defaultValue) {
        return config.getProperty(key, defaultValue);
    }

    public int getInt(String key) {
        return Integer.parseInt(getString(key));
    }

    public int getInt(String key, int defaultValue) {
        String value = getString(key);
        return Util.isEmpty(value) ? defaultValue : Integer.parseInt(value);
    }

    public void set(String key, String value) {
        config.setProperty(key, value);
    }

}
