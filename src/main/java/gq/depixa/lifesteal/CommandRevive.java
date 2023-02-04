package gq.depixa.lifesteal;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.UUID;

public class CommandRevive implements CommandExecutor {
    Main plugin = Main.getPlugin();
    private Configuration playerData = new Configuration("playerdata");
    Utils utils = new Utils();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        playerData.reloadCustomConfig();
        FileConfiguration config = playerData.getCustomConfig();
        if (args.length != 1) {
            return false;
        }
        if (args[0].equals("all")) {
            for (String key : config.getKeys(false)) {
                config.set(key + ".eliminated", null);
                config.set(key + ".banTime", null);
            }
            sender.sendMessage("§6Depixa Lifesteal §8| §eYou revived all eliminated players.");
        } else {
            UUID targetId = utils.getUniqueId(args[0]);
            if (targetId == null) {
                sender.sendMessage("§6Depixa Lifesteal §8| §cThis player has not joined the server yet.");
                return true;
            }
            OfflinePlayer target = Bukkit.getOfflinePlayer(targetId);
            if (target != null) {
                if (config.getBoolean(target.getUniqueId() + ".eliminated")) {
                    config.set(target.getUniqueId() + ".eliminated", null);
                    config.set(target.getUniqueId() + ".banTime", null);
                    sender.sendMessage("§6Depixa Lifesteal §8| §eYou revived §a" + args[0] + "§e.");
                } else {
                    sender.sendMessage("§6Depixa Lifesteal §8| §cThis player isn't eliminated.");
                }
            } else {
                sender.sendMessage("§6Depixa Lifesteal §8| §cThis player has not joined the server yet.");
            }
        }
        playerData.saveCustomConfig();
        return true;
    }
}
