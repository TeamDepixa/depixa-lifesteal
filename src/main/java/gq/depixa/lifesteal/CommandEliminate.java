package gq.depixa.lifesteal;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CommandEliminate implements CommandExecutor {
    Main plugin = Main.getPlugin();
    Utils utils = new Utils();
    private Configuration playerData = new Configuration("playerdata");
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        playerData.reloadCustomConfig();
        FileConfiguration config = playerData.getCustomConfig();
        if (args.length != 1) {
            return false;
        }
        if (args[0].equals("all")) {
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                config.set(player.getUniqueId() + ".eliminated", true);
                config.set(player.getUniqueId() + ".banTime", System.currentTimeMillis());
                Bukkit.broadcastMessage("§6Depixa Lifesteal §8| §a" + player.getDisplayName() + " §ehas been eliminated.");
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
                    config.set(target.getUniqueId() + ".banTime", System.currentTimeMillis());
                    Bukkit.broadcastMessage("§6Depixa Lifesteal §8| §a" + target.getDisplayName() + " §ehas been eliminated.");
                    if (target.hasPermission("dls.bypass")) {
                        target.sendMessage("§6Depixa Lifesteal §8| §eYou have been eliminated, but you have bypass enabled.");
                    } else {
                        target.kickPlayer("§eYou have been eliminated!");
                    }
                } else {
                    sender.sendMessage("§6Depixa Lifesteal §8| §cThis player is already eliminated.");
                }
            } else {
                UUID targetId = utils.getUniqueId(args[0]);
                if (targetId == null) {
                    sender.sendMessage("§6Depixa Lifesteal §8| §cThis player has not joined the server yet.");
                    return true;
                }
                OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(targetId);
                if (offlineTarget == null) {
                    sender.sendMessage("§6Depixa Lifesteal §8| §cThis player has not joined the server yet.");
                    return true;
                }
                if (!config.getBoolean(targetId + ".eliminated")) {
                    config.set(targetId + ".eliminated", true);
                    config.set(targetId + ".banTime", System.currentTimeMillis());
                    Bukkit.broadcastMessage("§6Depixa Lifesteal §8| §a" + offlineTarget.getName() + " §ehas been eliminated.");
                }
            }
        }
        playerData.saveCustomConfig();
        return true;
    }
}
