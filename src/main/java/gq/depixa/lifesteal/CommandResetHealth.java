package gq.depixa.lifesteal;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class CommandResetHealth implements CommandExecutor {
    Configuration playerData = new Configuration("playerdata");
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        playerData.reloadCustomConfig();
        FileConfiguration config = playerData.getCustomConfig();
        if (args.length != 1) {
            return false;
        }
        if (args[0].equals("all")) {
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                AttributeInstance health = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
                config.set(player.getUniqueId() + ".maxHealth", 20);
                health.setBaseValue(20);
            }
            sender.sendMessage("§6Depixa Lifesteal §8| §eYou reset the health of all currently online players.");
            playerData.saveCustomConfig();
        } else {
            Player target = Bukkit.getPlayerExact(args[0]);
            if (target != null) {
                AttributeInstance health = target.getAttribute(Attribute.GENERIC_MAX_HEALTH);
                config.set(target.getUniqueId() + ".maxHealth", 20);
                health.setBaseValue(20);
                sender.sendMessage("§6Depixa Lifesteal §8| §eYou reset §a" + args[0] + "§e's health.");
                playerData.saveCustomConfig();
            }
        }
        return true;
    }
}
