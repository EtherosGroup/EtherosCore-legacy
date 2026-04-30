package com.skilfully.ethero;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class EtherosCore extends JavaPlugin {

    @Override
    public void onEnable() {
        try {
            Initializer.init();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
