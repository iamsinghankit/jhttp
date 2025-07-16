package com.singhankit.jhttp.config;

import com.singhankit.jhttp.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Optional;
import java.util.Properties;

import static com.singhankit.jhttp.config.Config.CONFIG_FILE;
import static com.singhankit.jhttp.config.ConfigSettings.PROFILE_ACTIVE;

/**
 * @author Ankit Singh
 */
class ConfigHelper {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigHelper.class);
    private static final String DEFAULT_PROFILE = "default";

    static String getFile(String profile) {
        String file = CONFIG_FILE;
        if(!"".equals(profile)) {
            file = CONFIG_FILE + "-" + profile;
        }
        return file + ".properties";
    }

    static Properties readConfiguration() {
        Properties prop = new Properties();
        boolean defaultStatus = loadConfig(prop);
        var msg = new StringBuilder("Profile activated: ");
        if(defaultStatus) {
            String profile = prop.getProperty(PROFILE_ACTIVE);
            if(Util.isNotEmpty(profile)) {
                boolean profileStatus = loadConfig(prop, profile);
                switch(profileStatus) {
                    case true -> msg.append(profile);
                    case false -> msg.append(DEFAULT_PROFILE);
                }
            } else {
                msg.append(DEFAULT_PROFILE);
            }
        }
        LOG.info(msg.toString());
        return prop;
    }

    private static Optional<InputStream> readConfig(Config... configs) {
        return Arrays.stream(configs)
                     .map(Config::read)
                     .filter(Optional::isPresent)
                     .findFirst()
                     .orElse(Optional.empty());
    }

    private static boolean loadConfig(Properties prop) {
        try {
            Optional<InputStream> stream = readConfig(new ClasspathConfig(), new FilesystemConfig());
            if(stream.isPresent()) {
                prop.load(stream.get());
                LOG.info("Configuration loaded successfully");
                return true;
            } else {
                LOG.warn("No configuration found");
            }
        } catch(IOException ex) {
            LOG.error("Error while loading '{}' profile: {}", DEFAULT_PROFILE, ex.getMessage());
        }
        return false;
    }

    private static boolean loadConfig(Properties prop, String profile) {
        try {
            Optional<InputStream> stream = readConfig(new ClasspathConfig(profile), new FilesystemConfig(profile));
            if(stream.isPresent()) {
                prop.load(stream.get());
                return true;
            }
        } catch(IOException ex) {
            LOG.error("Error while loading '{}' profile: {}", profile, ex.getMessage());
        }
        return false;
    }
}
