package com.singhankit.jhttp.config;

import java.io.InputStream;
import java.util.Optional;

/**
 * @author Ankit Singh
 */
interface Config {

    String CONFIG_FILE = "config";

    Optional<InputStream> read();
}
