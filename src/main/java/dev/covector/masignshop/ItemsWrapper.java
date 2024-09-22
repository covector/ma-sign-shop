package dev.covector.masignshop;

import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import com.garbagemule.MobArena.things.ThingManager;
import com.garbagemule.MobArena.things.Thing;

import java.util.List;
import java.util.UUID;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.ArrayList;

public class ItemsWrapper
{
    private boolean isRandom;
    private List<List<String>> items;
    private List<Double> prob;
    private List<String> names;
    private int hardPityInd = -1;
    private HashMap<UUID, Integer> hardPityCount = new HashMap<UUID, Integer>();
    private HashMap<UUID, List<ItemStat>> stats = new HashMap<UUID, List<ItemStat>>();

    public void construct(String items) {
        this.isRandom = false;
        List<String> itemList = Arrays.stream(items.split(","))
        .map(item -> item.strip())
        .collect(Collectors.toList());
        this.items = Arrays.asList(itemList);
    }

    public void construct(List<String> items, List<Double> prob) {
        this.isRandom = true;
        this.items = items.stream()
        .map(item -> Arrays.stream(item.split(",")).map(subitem -> subitem.strip()).collect(Collectors.toList()))
        .collect(Collectors.toList());
        double total = prob.stream().reduce(0D, (a, b) -> a + b);
        this.prob = prob.stream().map(p -> p / total).collect(Collectors.toList());
    }

    public ItemsWrapper(ConfigurationSection section) {
        if (section.getConfigurationSection("items") == null) {
            construct(section.getString("items"));
            this.names = new ArrayList<String>();
            this.names.add(null);
        } else {
            String hardPityOutcome = section.getString("hard-pity-outcome");
            ConfigurationSection itemsSection = section.getConfigurationSection("items");
            List<String> itemList = new ArrayList<String>();
            List<Double> probList = new ArrayList<Double>();
            this.names = new ArrayList<String>();
            for (String key: itemsSection.getKeys(false)) {
                ConfigurationSection subsection = itemsSection.getConfigurationSection(key);
                itemList.add(subsection.getString("items"));
                probList.add(subsection.getDouble("prob"));
                if (hardPityOutcome != null && key.equals(hardPityOutcome)) {
                    this.hardPityInd = probList.size() - 1;
                }
                this.names.add(key);
            }
            if (hardPityOutcome != null && this.hardPityInd == -1) {
                Bukkit.getServer().getLogger().warning("Hard pity outcome " + hardPityOutcome + " not found in items list!");
            }
            construct(itemList, probList);
        }
    }

    public void giveItems(Player player, ThingManager thingman) {
        if (isRandom) {
            double rand = Math.random();
            int index = 0;
            while (rand > prob.get(index) && index < prob.size()) {
                rand -= prob.get(index);
                index++;
            }
            if (hardPityInd != -1) {
                if (hardPityInd != index) {
                    hardPityCount.put(player.getUniqueId(), hardPityCount.getOrDefault(player.getUniqueId(), 0) + 1);
                } else {
                    hardPityCount.put(player.getUniqueId(), 0);
                }
                if (prob.get(hardPityInd) * hardPityCount.getOrDefault(player.getUniqueId(), 0) >= 1) {
                    index = hardPityInd;
                    hardPityCount.put(player.getUniqueId(), 0);
                }
            }
            incrementStats(player, index);
            giveItems(items.get(index), player, thingman);
        } else {
            incrementStats(player, 0);
            giveItems(items.get(0), player, thingman);
        }
    }

    private void giveItems(List<String> itemList, Player player, ThingManager thingman) {
        for (String item : itemList) {
            Thing thing = thingman.parse(item);
            thing.giveTo(player);
        }
    }

    public void resetPity(Player player) {
        if (hardPityCount.containsKey(player.getUniqueId())) {
            hardPityCount.remove(player.getUniqueId());
        }
        if (this.stats.containsKey(player.getUniqueId())) {
            this.stats.remove(player.getUniqueId());
        }
    }

    private void incrementStats(Player player, int index) {
        if (this.stats.get(player.getUniqueId()) == null) {
            ArrayList<ItemStat> statsCount = new ArrayList<ItemStat>();
            for (String name : names) {
                statsCount.add(new ItemStat(name));
            }
            this.stats.put(player.getUniqueId(), statsCount);
        }
        this.stats.get(player.getUniqueId()).get(index).increment();
    }

    public ItemStats getStats(Player player) {
        int pity = hardPityInd != -1 ? hardPityCount.getOrDefault(player.getUniqueId(), 0) : -1;
        List<ItemStat> internalStats = this.stats.get(player.getUniqueId());
        if (internalStats == null) {
            return null;
        }
        return new ItemStats(internalStats, pity);
    }

    class ItemStat {
        public final String key;
        public int value;
        public ItemStat(String key) {
            this.key = key;
            this.value = 0;
        }
        public void increment() {
            this.value++;
        }
        public String toString() {
            return key == null ? String.valueOf(value) : key + ": " + value;
        }
    }

    public class ItemStats {
        private final List<ItemStat> stats;
        private final int pity;
        public ItemStats(List<ItemStat> stats, int pity) {
            this.stats = stats;
            this.pity = pity;
        }
        public List<ItemStat> getStats() {
            return stats;
        }
        public int getPity() {
            return pity;
        }
    }
}