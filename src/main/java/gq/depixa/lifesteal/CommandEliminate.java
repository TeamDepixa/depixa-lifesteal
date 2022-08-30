package gq.depixa.lifesteal;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class CommandEliminate implements CommandExecutor {
    Main plugin = Main.getPlugin();
    private Configuration playerData = Main.getPlayerConfig();
    FileConfiguration config = playerData.getCustomConfig();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) {
            return false;
        }
        if (args[0].equals("all")) {
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                config.set(player.getUniqueId() + ".eliminated", true);
                Bukkit.broadcastMessage("§6Depixa Lifesteal §8| §a" + player.getDisplayName() + " §ewas eliminated.");
                if (player.hasPermission("dls.bypass")) {
                    player.sendMessage("§6Depixa Lifesteal §8| §eYou have been eliminated, but you have bypass enabled.");
                } else {
                    player.kickPlayer("§eYou have been eliminated!");
                }
            }
            sender.sendMessage("§6Depixa Lifesteal §8| §eYou eliminated all currently online players.");
        } else {
            Player target = Bukkit.getPlayerExact(args[0]);
            if (target != null) {
                if (!config.getBoolean(target.getUniqueId() + ".eliminated")) {
                    config.set(target.getUniqueId() + ".eliminated", true);
                    Bukkit.broadcastMessage("§6Depixa Lifesteal §8| §a" + target.getDisplayName() + " §ewas eliminated.");
                    if (target.hasPermission("dls.bypass")) {
                        target.sendMessage("§6Depixa Lifesteal §8| §eYou have been eliminated, but you have bypass enabled.");
                    } else {
                        target.kickPlayer("§eYou have been eliminated!");
                    }
                } else {
                    sender.sendMessage("§6Depixa Lifesteal §8| §cThis player is already eliminated.");
                }
            }
        }
        playerData.saveCustomConfig();
        return true;
    }
}
