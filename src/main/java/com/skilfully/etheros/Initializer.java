package com.skilfully.etheros;

import com.skilfully.etheros.exceptions.NoLanguageException;
import com.skilfully.etheros.config.ConfigManager;

import java.io.IOException;

public class Initializer {

    public static void init() throws IOException, NoLanguageException {
        ConfigManager.loadConfig();
    }

}
