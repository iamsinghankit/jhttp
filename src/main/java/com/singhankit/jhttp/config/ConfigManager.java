package com.singhankit.jhttp.config;

import com.singhankit.jhttp.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static com.singhankit.jhttp.config.Config.CONFIG_FILE;
import static com.singhankit.jhttp.config.Config.readConfig;

/**
 * @author Ankit Singh
 */
public class ConfigManager {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigManager.class);
    private static ConfigManager INSTANCE;
    private final Properties config;

    private ConfigManager() {
        config = readConfiguration();
    }

    public static synchronized ConfigManager of() {
        if(INSTANCE == null) {
            INSTANCE = new ConfigManager();
        }
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

    private Properties readConfiguration() {
        InputStream stream = readConfig(new ClasspathConfig(), new EnvironmentConfig(), new FilesystemConfig());
        Properties config = new Properties();
        try {
            config.load(stream);
            LOG.info("Successfully loaded '" + CONFIG_FILE + "' file");
            return config;
        } catch(IOException e) {
            throw new IllegalStateException(e);
        }
    }

}
