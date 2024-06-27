package dev.covector.masignshop;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Painting;

import com.garbagemule.MobArena.MobArena;
import com.garbagemule.MobArena.framework.Arena;

public class ItemFrameBreakListener implements Listener
{
    private final MobArena mobarena;

    public ItemFrameBreakListener(MobArena mobarena) {
        this.mobarena = mobarena;
    }

    @EventHandler
    public void onHangingBreak(HangingBreakByEntityEvent event) {
        // if (!(event.getRemover() instanceof Player)) {
        //     return;
        // }

        if (!(event.getEntity() instanceof ItemFrame || event.getEntity() instanceof Painting)) {
            return;
        }

        if (!isInRunningMobArena(event.getEntity().getLocation())) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onItemInFrameRemove(EntityDamageByEntityEvent event) {
        // if (!(event.getDamager() instanceof Player)) {
        //     return;
        // }

        if (!(event.getEntity() instanceof ItemFrame)) {
            return;
        }

        if (!isInRunningMobArena(event.getEntity().getLocation())) {
            return;
        }

        event.setCancelled(true);
    }

    private boolean isInRunningMobArena(Location loc) {
        Arena arena = mobarena.getArenaMaster().getArenaAtLocation(loc);
        return arena != null && arena.isRunning();
    }
}