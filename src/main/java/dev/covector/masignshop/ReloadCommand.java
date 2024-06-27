package dev.covector.masignshop;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {
    private final ConfigManager configManager;
    private final SignInfoManager signInfoManager;

    public ReloadCommand(ConfigManager configManager, SignInfoManager signInfoManager) {
        this.configManager = configManager;
        this.signInfoManager = signInfoManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1 || !args[0].equals("reload")) {
            return false;
        }

        reload(sender);
        return true;
    }

    private void reload(CommandSender sender) {
        configManager.loadConfig();
        signInfoManager.loadSignInfos(configManager.getConfig());
        sender.sendMessage(ChatColor.GREEN + "Successfully reloaded MobArena Sign Shop.");
    }
}
