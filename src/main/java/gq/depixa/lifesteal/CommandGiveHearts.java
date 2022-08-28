package gq.depixa.lifesteal;

import org.bukkit.Bukkit;
import org.bukkit.Material;
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

public class CommandGiveHearts implements CommandExecutor {
    Main plugin = Main.getPlugin();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) return false;
        Player player = (Player) sender;
        Player target = Bukkit.getPlayerExact(args[0]);
        Integer hearts;
        if (target == null) return false;
        try {
            hearts = Integer.parseInt(args[1]);
        } catch (Exception e) {
            return false;
        }
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
        if (player == target) {
            sender.sendMessage("§6Depixa Lifesteal §8| §eYou gave yourself §a" + hearts + " §eheart items.");
        } else {
            target.sendMessage("§6Depixa Lifesteal §8| §eYou were given §a" + hearts + " §eheart items.");
        }
        return true;
    }
}
