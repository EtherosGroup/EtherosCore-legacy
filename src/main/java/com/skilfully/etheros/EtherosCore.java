package com.skilfully.etheros;

import com.skilfully.etheros.utils.di.ApplicationContext;
import org.bukkit.plugin.java.JavaPlugin;

public final class EtherosCore extends JavaPlugin {

    @Override
    public void onEnable() {
        ApplicationContext.run(EtherosCore.class);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
