package com.skilfully.ethero;

import com.skilfully.ethero.exceptions.NoLanguageException;
import com.skilfully.ethero.config.ConfigManager;
import com.skilfully.ethero.service.ECMessenger;
import com.skilfully.ethero.utils.messenger.Messenger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Initializer {

    private static Messenger messenger = ECMessenger.getMessenger();

    public static void init() throws IOException, NoLanguageException {
        ConfigManager.loadConfig();
        showServerInfo();
    }

    private static void showServerInfo() {
        List<String> messages = new ArrayList<>();
        messages.add("==================== EtherosCore ====================");
        messages.add("插件版本：legacy 0.0.1");
        messages.add("当前服务器：id=" + ConfigManager.plugin_config.getServer().getId() + " name=" + ConfigManager.plugin_config.getServer().getName());
        if (ConfigManager.plugin_config.getGroup().isEnabled()) {
            messages.add("已配置加入群组：" + ConfigManager.plugin_config.getGroup().getProxyServer().getAddress());
        } else {
            messages.add("未加入群组");
        }
        messages.add("==================== EtherosCore ====================");
        for (String message : messages) {
            messenger.consoleInfo(message);
        }

    }

}
