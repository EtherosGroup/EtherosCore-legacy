package com.skilfully.ethero.config.entity;

import lombok.Data;
import java.util.List;

@Data
public class PluginConfig {

    private Server server;
    private Group group;

    @Data
    public static class Server {
        private String id;
        private String name;
        private List<String> languages;
        private PluginUpdate pluginUpdate;

        @Data
        public static class PluginUpdate {
            private boolean enabled;
            private List<String> repositories;
            private AutoUpdate autoUpdate;

            @Data
            public static class AutoUpdate {
                private boolean enabled;
                private String type;
                private List<String> noUpdates;
            }
        }
    }

    @Data
    public static class Group {
        private boolean enabled;
        private ProxyServer proxyServer;
        private ProxyConfigUse proxyConfigUse;

        @Data
        public static class ProxyServer {
            private String address;
        }

        @Data
        public static class ProxyConfigUse {
            private boolean serverLanguages;
            private boolean pluginUpdateRepositories;
        }
    }
}