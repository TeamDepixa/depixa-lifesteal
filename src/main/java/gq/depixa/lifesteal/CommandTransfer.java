package gq.depixa.lifesteal;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

    public class CommandTransfer implements CommandExecutor {
    Main plugin = Main.getPlugin();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) {
            return false;
        }
        Player player = (Player) sender;
        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) return false;
        if (target.equals(player)) {
            sender.sendMessage("§6Depixa Lifesteal §8| §cYou cannot transfer yourself hearts.");
            return true;
        }
        AttributeInstance playerHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        Double currentHealth = playerHealth.getBaseValue();
        AttributeInstance targetHealth = target.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        Double targetCurrentHealth = targetHealth.getBaseValue();
        Integer hearts;
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
        Integer maxHearts = plugin.getConfig().getInt("max-hearts");
        if (maxHearts != null && maxHearts != 0 && maxHearts * 2 <= targetCurrentHealth) {
            Material heartMaterial = Material.valueOf(plugin.getConfig().getString("heart-item").toUpperCase());
            ItemStack heart = new ItemStack(heartMaterial, hearts);
            ItemMeta heartMeta = heart.getItemMeta();
            heartMeta.setDisplayName("§c❤ Heart");
            List<String> lores = new ArrayList<String>();
            lores.add("§cClick me to get an extra heart.");
            heart.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
            heartMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            heartMeta.setLore(lores);
            heart.setItemMeta(heartMeta);
            target.getInventory().addItem(heart);
        } else {
            targetHealth.setBaseValue(targetCurrentHealth + health);
        }
        sender.sendMessage("§6Depixa Lifesteal §8| §eYou transferred §a" + args[1] + " §ehearts to §a" + target.getDisplayName() + "§e.");
        target.sendMessage("§6Depixa Lifesteal §8| §eYou were transferred §a" + args[1] + " §ehearts by §a" + player.getDisplayName() + "§e.");
        return true;
    }
}
