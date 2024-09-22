package dev.covector.masignshop;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.garbagemule.MobArena.events.ArenaStartEvent;

public class ArenaStartListener implements Listener  {
    private final SignInfoManager signInfoManager;

    public ArenaStartListener(SignInfoManager signInfoManager) {
        this.signInfoManager = signInfoManager;
    }

    @EventHandler
    public void onArenaStart(ArenaStartEvent event) {
        for (Player player : event.getArena().getAllPlayers()) {
            signInfoManager.resetPity(player);
        }
    }

    public void unregister() {
        ArenaStartEvent.getHandlerList().unregister(this);
    }
}
