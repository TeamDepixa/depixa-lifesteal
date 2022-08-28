package gq.depixa.lifesteal;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTransfer implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) {
            return false;
        }
        Player player = (Player) sender;
        Player target = Bukkit.getPlayerExact(args[0]);
        AttributeInstance playerHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        Double currentHealth = playerHealth.getBaseValue();
        AttributeInstance targetHealth = target.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        Double targetCurrentHealth = targetHealth.getBaseValue();
        Integer hearts;
        if (target == null) return false;
        try {
            hearts = Integer.parseInt(args[1]);
        } catch (Exception e) {
            return false;
        }
        Integer health = hearts * 2;
        if (currentHealth <= health) {
            sender.sendMessage("§6Depixa Lifesteal §8| §cYou cannot transfer more hearts than you have or the same amount!");
            return true;
        }
        playerHealth.setBaseValue(currentHealth - health);
        targetHealth.setBaseValue(targetCurrentHealth + health);
        sender.sendMessage("§6Depixa Lifesteal §8| §eYou transferred §a" + args[1] + " §ehearts to §a" + target.getDisplayName() + "§e.");
        target.sendMessage("§6Depixa Lifesteal §8| §eYou were transferred §a" + args[1] + " §ehearts by §a" + player.getDisplayName() + "§e.");
        return true;
    }
}
