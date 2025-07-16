package com.singhankit.jhttp;

import com.singhankit.jhttp.config.ConfigManager;

/**
 * @author Ankit Singh
 */
public class Context {

    public static ConfigManager getConfig(){
        return ConfigManager.of();
    }
}
