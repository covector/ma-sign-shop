package dev.covector.masignshop;

public class SignInfo
{
    private final String name;
    private final String world;
    private final int x;
    private final int y;
    private final int z;
    private final int cost;
    private final String items;

    public SignInfo(String name, String world, int x, int y, int z, int cost, String items) {
        this.name = name;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.cost = cost;
        this.items = items;
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

    public String getItems() {
        return items;
    }
}