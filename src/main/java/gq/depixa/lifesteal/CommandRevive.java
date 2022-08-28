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
    private Configuration playerData = Main.getPlayerConfig();
    FileConfiguration config = playerData.getCustomConfig();
    Utils utils = new Utils();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) {
            return false;
        }
        if (args[0].equals("all")) {
            for (String key : config.getKeys(false)) {
                config.set(key + ".eliminated", null);
            }
            sender.sendMessage("§6Depixa Lifesteal §8| §aYou revived all eliminated players.");
        } else {
            UUID targetId = utils.getUniqueId(args[0]);
            if (targetId == null) {
                sender.sendMessage("§6Depixa Lifesteal §8| §cThis player isn't eliminated.");
                return true;
            }
            OfflinePlayer target = Bukkit.getOfflinePlayer(targetId);
            if (target != null) {
                if (config.getBoolean(target.getUniqueId() + ".eliminated")) {
                    config.set(target.getUniqueId() + ".eliminated", null);
                    sender.sendMessage("§6Depixa Lifesteal §8| §eYou revived §a" + args[0] + "§e.");
                } else {
                    sender.sendMessage("§6Depixa Lifesteal §8| §cThis player isn't eliminated.");
                }
            }
        }
        playerData.saveCustomConfig();
        return true;
    }
}
