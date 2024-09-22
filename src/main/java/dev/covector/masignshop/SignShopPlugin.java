package dev.covector.masignshop;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.Plugin;

import com.garbagemule.MobArena.MobArena;
import com.garbagemule.MobArena.things.ThingManager;

public class SignShopPlugin extends JavaPlugin
{
    ArenaStartListener arenaStartListener;
    SignListener signListener;
    ItemFrameBreakListener itemFrameBreakListener;

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
        this.getCommand("mass").setExecutor(new SignShopCommand(configManager, signInfoManager));
        
        arenaStartListener = new ArenaStartListener(signInfoManager);
        signListener = new SignListener(signInfoManager);
        itemFrameBreakListener = new ItemFrameBreakListener(mobarena);
        Bukkit.getPluginManager().registerEvents(arenaStartListener, this);
        Bukkit.getPluginManager().registerEvents(signListener, this);
        Bukkit.getPluginManager().registerEvents(itemFrameBreakListener, this);

        getLogger().info("MobArena Sign Shop Plugin Activated!");
    }

    @Override
    public void onDisable() {
        arenaStartListener.unregister();
        signListener.unregister();
        itemFrameBreakListener.unregister();

        getLogger().info("MobArena Sign Shop Plugin Deactivated!");
    }
}
