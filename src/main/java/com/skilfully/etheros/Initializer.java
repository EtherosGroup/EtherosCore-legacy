package com.skilfully.etheros;

import com.skilfully.etheros.config.ConfigManager;
import com.skilfully.etheros.data.GlobalData;
import com.skilfully.etheros.exceptions.NoLanguageException;
import com.skilfully.etheros.utils.di.annotations.Autowired;
import com.skilfully.etheros.utils.di.annotations.PostConstruct;
import com.skilfully.etheros.utils.di.annotations.Service;
import com.skilfully.etheros.utils.messenger.Messenger;

import java.io.IOException;

@Service
public class Initializer {

    private final Messenger messenger;
    private final ConfigManager configManager;

    @Autowired
    public Initializer(Messenger messenger, ConfigManager configManager) {
        this.messenger = messenger;
        this.configManager = configManager;
    }

    @PostConstruct
    public void init() throws IOException, NoLanguageException {
        messenger.setPluginName(GlobalData.pluginName);
        configManager.loadConfig();
    }
}
