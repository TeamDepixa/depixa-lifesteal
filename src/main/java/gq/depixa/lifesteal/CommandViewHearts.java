package gq.depixa.lifesteal;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class CommandViewHearts implements CommandExecutor {
    Main plugin = Main.getPlugin();
    Configuration playerData = new Configuration("playerdata");
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) {
            return false;
        }
        playerData.reloadCustomConfig();
        FileConfiguration config = playerData.getCustomConfig();
        if (!plugin.getConfig().getBoolean("view-other-players-hearts") && !sender.hasPermission("dls.viewhearts")) {
            sender.sendMessage("§6Depixa Lifesteal §8| §cYou do not have permission to run this command.");
            return true;
        }
        OfflinePlayer target = (OfflinePlayer) Bukkit.getOfflinePlayer(args[0]);
        if (!target.hasPlayedBefore()) {
            sender.sendMessage("§6Depixa Lifesteal §8| §cThis player has not joined the server yet.");
            return true;
        }
        Integer health = config.getInt(target.getUniqueId() + ".maxHealth");
        sender.sendMessage("§6Depixa Lifesteal §8| §a" + target.getName() + " §ehas §a" + (health/2) + " §ehearts maximum.");
        return true;
    }
}
