package com.skilfully.ethero;

import com.skilfully.ethero.exceptions.NoLanguageException;
import com.skilfully.ethero.config.ConfigManager;

import java.io.IOException;

public class Initializer {

    public static void init() throws IOException, NoLanguageException {
        ConfigManager.loadConfig();
    }

}
