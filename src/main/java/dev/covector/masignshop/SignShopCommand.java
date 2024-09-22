package dev.covector.masignshop;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SignShopCommand implements CommandExecutor, TabCompleter {
    private final ConfigManager configManager;
    private final SignInfoManager signInfoManager;

    public SignShopCommand(ConfigManager configManager, SignInfoManager signInfoManager) {
        this.configManager = configManager;
        this.signInfoManager = signInfoManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1 || args.length > 2) {
            return false;
        }

        if (args[0].equals("reload")) {
            reload(sender);
            return true;
        }

        if (args[0].equals("stats")) {
            if (!(sender instanceof Player) && args.length == 1) {
                sender.sendMessage(ChatColor.RED + "Please specify a player.");
                return false;
            }
            Player of = args.length == 1 ? (Player) sender : Bukkit.getPlayer(args[1]);
            if (of == null) {
                sender.sendMessage(ChatColor.RED + "Player not found.");
                return true;
            }
            sendStats(of, (Player) sender);
            return true;
        }

        return false;
    }

    private void reload(CommandSender sender) {
        configManager.loadConfig();
        signInfoManager.loadSignInfos(configManager.getConfig());
        sender.sendMessage(ChatColor.GREEN + "Successfully reloaded MobArena Sign Shop.");
    }

    private void sendStats(Player of, Player to) {
        signInfoManager.sendStats(of, to);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Stream.of("reload", "stats").filter(n -> n.startsWith(args[0])).collect(Collectors.toList());
        }

        if (args.length == 2 && args[0].equals("stats")) {
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).filter(n -> n.startsWith(args[1])).collect(Collectors.toList());
        }

        return Collections.emptyList();
    }
}
