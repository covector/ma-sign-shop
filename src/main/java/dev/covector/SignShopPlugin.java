package dev.covector.masignshop;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.Plugin;

import com.garbagemule.MobArena.MobArena;
import com.garbagemule.MobArena.things.ThingManager;

public class SignShopPlugin extends JavaPlugin
{
    @Override
    public void onEnable() {
        Plugin maplugin = getServer().getPluginManager().getPlugin("MobArena");
        if (maplugin == null) {
            getLogger().warning("MobArena Not Loaded!");
            return;
        }
        MobArena mobarena = (MobArena) maplugin;
        ThingManager thingman = mobarena.getThingManager();

        ConfigManager configManager = new ConfigManager(this);
        SignInfoManager signInfoManager = new SignInfoManager(configManager.getConfig(), thingman);
        this.getCommand("mass").setExecutor(new ReloadCommand(configManager, signInfoManager));
        
        Bukkit.getPluginManager().registerEvents(new SignListener(signInfoManager), this);
        Bukkit.getPluginManager().registerEvents(new ItemFrameBreakListener(mobarena), this);
        
        getLogger().info("MobArena Sign Shop Plugin Activated!");
    }

    @Override
    public void onDisable() {
        getLogger().info("MobArena Sign Shop Plugin Deactivated!");
    }
}
