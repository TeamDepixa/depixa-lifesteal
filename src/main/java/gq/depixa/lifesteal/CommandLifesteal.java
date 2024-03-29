package gq.depixa.lifesteal;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandLifesteal implements CommandExecutor {
    Main plugin = Main.getPlugin();
    Configuration config = new Configuration("playerdata");
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§6Depixa Lifesteal §8| §eVersion " + plugin.getDescription().getVersion());
            return true;
        }
        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("dls.main")) {
                sender.sendMessage("§6Depixa Lifesteal §8| §eVersion " + plugin.getDescription().getVersion());
                return true;
            }
            sender.sendMessage("§6Depixa Lifesteal §8| §eReloaded the configuration files.");
            plugin.reloadConfig();
            config.reloadCustomConfig();
        } else {
            sender.sendMessage("§6Depixa Lifesteal §8| §eVersion " + plugin.getDescription().getVersion());;
        }
        return true;
    }
}
