package dev.covector.masignshop;

import org.bukkit.entity.Player;

import java.util.List;

public class SignInfo
{
    private final String name;
    private final String world;
    private final int x;
    private final int y;
    private final int z;
    private final int cost;
    private final ItemsWrapper items;
    private final boolean clearInventory;

    public SignInfo(String name, String world, int x, int y, int z, int cost, ItemsWrapper items, boolean clearInventory) {
        this.name = name;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.cost = cost;
        this.items = items;
        this.clearInventory = clearInventory;
    }

    public String getName() {
        return name;
    }

    public String getWorld() {
        return world;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public int getCost() {
        return cost;
    }

    public ItemsWrapper getItems() {
        return items;
    }

    public boolean isClearInventory() {
        return clearInventory;
    }

    public void resetPity(Player player) {
        if (items != null) {
            items.resetPity(player);
        }
    }

    public ItemsWrapper.ItemStats getStats(Player player) {
        if (items != null) {
            items.getStats(player);
        }
        return null;
    }
}