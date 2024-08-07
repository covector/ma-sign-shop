package dev.covector.masignshop;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import java.util.logging.Logger;
import org.bukkit.entity.Player;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.ChatColor;

import com.garbagemule.MobArena.things.ThingManager;

public class SignInfoManager
{
    private final ThingManager thingman;
    private ArrayList<SignInfo> signInfos;
    private final Logger logger;

    public SignInfoManager(FileConfiguration customConfig, ThingManager thingman) {
        this.thingman = thingman;
        this.logger = Bukkit.getServer().getLogger();
        loadSignInfos(customConfig);
    }

    public void loadSignInfos(FileConfiguration customConfig) {
        this.signInfos = new ArrayList<SignInfo>();
        for (String key : customConfig.getKeys(false)) {
            ConfigurationSection section = customConfig.getConfigurationSection(key);
            if (section == null) {
                logger.warning("Sign " + key + " is empty in config.yml!");
                continue;
            }

            String world = section.getString("world");
            if (world == null) {
                logger.warning("Sign " + key + " is missing world in config.yml!");
                continue;
            }

            String[] coordinate = section.getString("coordinate").split(",");
            if (coordinate.length != 3) {
                logger.warning("Sign " + key + " has invalid coordinate format in config.yml!");
                continue;
            }
            int x, y, z;
            try {
                x = Integer.parseInt(coordinate[0].strip());
                y = Integer.parseInt(coordinate[1].strip());
                z = Integer.parseInt(coordinate[2].strip());
            } catch (NumberFormatException e) {
                logger.warning("Sign " + key + " has invalid coordinate in config.yml!");
                continue;
            }

            int cost = section.getInt("level-cost");

            boolean hasItems = section.getConfigurationSection("items") != null || section.getString("items") != null;
            ItemsWrapper itemsWrapped = hasItems ? new ItemsWrapper(section) : null;

            boolean clearInv = section.getBoolean("clear-inventory");

            signInfos.add(new SignInfo(key, world, x, y, z, cost, itemsWrapped, clearInv));
        }
    }

    public void buy(Player player, String world, int signX, int signY, int signZ) {
        SignInfo signInfo = getSignInfo(world, signX, signY, signZ);
        if (signInfo == null) {
            // Just a random sign
            return;
        }

        if (!enoughLevel(player, signInfo)) {
            player.sendMessage(ChatColor.RED + "You don't have enough level to buy that!");
            return;
        }

        player.setLevel(player.getLevel() - signInfo.getCost());

        if (signInfo.isClearInventory()) {
            player.getInventory().clear();
        }

        if (signInfo.getItems() != null) {
            signInfo.getItems().giveItems(player, thingman);
        }

        player.sendMessage(ChatColor.GREEN +"You have bought " + signInfo.getName() + "!");
    }

    private SignInfo getSignInfo(String world, int signX, int signY, int signZ) {
        for (SignInfo signInfo : signInfos) {
            if (signInfo.getWorld().equals(world) && signInfo.getX() == signX && signInfo.getY() == signY && signInfo.getZ() == signZ) {
                return signInfo;
            }
        }
        return null;
    }

    private boolean enoughLevel(Player player, SignInfo signInfo) {
        return player.getLevel() >= signInfo.getCost();
    }
}