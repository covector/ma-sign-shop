package dev.covector.masignshop;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.Action;

public class SignListener implements Listener
{
    private final SignInfoManager signInfoManager;

    public SignListener(SignInfoManager signInfoManager) {
        this.signInfoManager = signInfoManager;
    }

    @EventHandler
    public void on(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        if (block == null) {
            return;
        }
        if (!(block.getState() instanceof Sign)) {
            return;
        }

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        // Bukkit.getServer().getLogger().info("sign at " + block.getLocation().toString() + " clicked by " + event.getPlayer().getName());
        Location loc = block.getLocation();
        signInfoManager.buy(event.getPlayer(), loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }
}