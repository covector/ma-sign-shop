package dev.covector.masignshop;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.configuration.ConfigurationSection;

import com.garbagemule.MobArena.things.ThingManager;
import com.garbagemule.MobArena.things.Thing;

import java.util.Random;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.ArrayList;

public class ItemsWrapper
{
    private boolean isRandom;
    private List<List<String>> items;
    private List<Double> prob;

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
        } else {
            ConfigurationSection itemsSection = section.getConfigurationSection("items");
            List<String> itemList = new ArrayList<String>();
            List<Double> probList = new ArrayList<Double>();
            for (String key: itemsSection.getKeys(false)) {
                ConfigurationSection subsection = itemsSection.getConfigurationSection(key);
                itemList.add(subsection.getString("items"));
                probList.add(subsection.getDouble("prob"));
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
            giveItems(items.get(index), player, thingman);
        } else {
            giveItems(items.get(0), player, thingman);
        }
    }

    private void giveItems(List<String> itemList, Player player, ThingManager thingman) {
        for (String item : itemList) {
            Thing thing = thingman.parse(item);
            thing.giveTo(player);
        }
    }
}