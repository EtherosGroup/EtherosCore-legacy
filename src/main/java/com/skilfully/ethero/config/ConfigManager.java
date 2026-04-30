package com.skilfully.ethero.config;

import com.skilfully.ethero.EtherosCore;
import com.skilfully.ethero.config.entity.PluginConfig;
import com.skilfully.ethero.data.GlobalData;
import com.skilfully.ethero.exceptions.NoLanguageException;
import com.skilfully.ethero.service.ECMessenger;
import com.skilfully.ethero.utils.config.YamlConfig;
import com.skilfully.ethero.utils.messenger.Messenger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigManager {

    public static YamlConfig config;
    public static YamlConfig plugin_language_config;
    public static Map<String, YamlConfig> languages_config = new HashMap<>();

    private static final Messenger messenger = ECMessenger.getMessenger();

    public static void loadConfig() throws IOException, NoLanguageException {
        File workDirectory = YamlConfig.createWorkDirectory(GlobalData.pluginName);
        if (workDirectory == null) {
            throw new RuntimeException("创建工作目录失败");
        }
        File file = YamlConfig.extractFileFromJarResources(
                EtherosCore.getPlugin(EtherosCore.class),
                "setting.yml",
                new File(workDirectory, "setting.yml"),
                false
        );

        if (file == null) {
            throw new RuntimeException("加载配置文件失败:1");
        }

        config = new YamlConfig(file);

        List<String> languages = config.getStringList("server.languages");
        if (!languages.isEmpty()) {
            for (String language : languages) {
                File languageFile = YamlConfig.extractFileFromJarResources(
                        EtherosCore.getPlugin(EtherosCore.class),
                        language,
                        new File(workDirectory, "Languages/" + language),
                        false
                );
                if (languageFile == null) continue;
                YamlConfig languageConfig = new YamlConfig(languageFile);
                messenger.consoleInfo(languageConfig.getString("language_loaded", null));
                languages_config.put(language, languageConfig);
            }
            File languageFile = new File(YamlConfig.getWorkDirectory(GlobalData.pluginName), languages.get(0));
            if (!languageFile.exists()) {
                languageFile = YamlConfig.extractFileFromJarResources(
                        EtherosCore.getPlugin(EtherosCore.class),
                        "zh-CN.yml",
                        new File(workDirectory, "Languages/zh-CN.yml"),
                        false
                );

                if (languageFile == null) {
                    throw new NoLanguageException("没有任何可用的语言文件");
                }

            }
            plugin_language_config = new YamlConfig(languageFile);
        } else {
            File languageFile = YamlConfig.extractFileFromJarResources(
                    EtherosCore.getPlugin(EtherosCore.class),
                    "zh-CN.yml",
                    new File(workDirectory, "Languages/zh-CN.yml"),
                    false
            );
            if (languageFile == null) {
                throw new NoLanguageException("没有任何可用的语言文件");
            }
            plugin_language_config = new YamlConfig(languageFile);
        }
        messenger.consoleInfo(plugin_language_config.getString("plugin_language_loaded", null));
        ECMessenger.getMessenger().setPrefix(plugin_language_config.getString("prefix", null));
    }

    public static PluginConfig getPluginConfig() {
        PluginConfig pluginConfig = new PluginConfig();
        // server
        PluginConfig.Server server = new PluginConfig.Server();
        server.setId(config.getString("server.id", null));
        server.setName(config.getString("server.name", null));
        server.setLanguages(config.getStringList("server.languages"));

        PluginConfig.Server.PluginUpdate pluginUpdate = new PluginConfig.Server.PluginUpdate();
        pluginUpdate.setEnabled(config.getBoolean("server.plugin-update.enabled", true));
        pluginUpdate.setRepositories(config.getStringList("server.plugin-update.repositories"));

        PluginConfig.Server.PluginUpdate.AutoUpdate autoUpdate = new PluginConfig.Server.PluginUpdate.AutoUpdate();
        autoUpdate.setEnabled(config.getBoolean("server.plugin-update.auto-update.enabled", false));
        autoUpdate.setType(config.getString("server.plugin-update.auto-update.type", "new-version"));
        autoUpdate.setNoUpdates(config.getStringList("server.plugin-update.auto-update.no-updates"));
        pluginUpdate.setAutoUpdate(autoUpdate);

        server.setPluginUpdate(pluginUpdate);
        pluginConfig.setServer(server);

        PluginConfig.Group group = new PluginConfig.Group();
        group.setEnabled(config.getBoolean("group.enabled", false));

        PluginConfig.Group.ProxyServer proxyServer = new PluginConfig.Group.ProxyServer();
        proxyServer.setAddress(config.getString("group.proxy-server.address", null));
        group.setProxyServer(proxyServer);

        PluginConfig.Group.ProxyConfigUse proxyConfigUse = new PluginConfig.Group.ProxyConfigUse();
        proxyConfigUse.setServerLanguages(config.getBoolean("group.proxy-server.server-languages", true));
        proxyConfigUse.setPluginUpdateRepositories(config.getBoolean("group.proxy-server.plugin-update-repositories", true));
        group.setProxyConfigUse(proxyConfigUse);

        pluginConfig.setGroup(group);

        return pluginConfig;
    }

}
