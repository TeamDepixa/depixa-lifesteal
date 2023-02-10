package gq.depixa.lifesteal;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
public class CommandWithdraw implements CommandExecutor {
    Main plugin = Main.getPlugin();
    Configuration playerData = new Configuration("playerdata");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        playerData.reloadCustomConfig();
        FileConfiguration config = playerData.getCustomConfig();
        if (sender instanceof Player) {
            Player player = (Player) sender;
            AttributeInstance health = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            Double currentHealth = health.getBaseValue();
            Integer healthToRemove;
            Integer heartsToRemove;
            try {
                heartsToRemove = Integer.parseInt(args[0]);
            } catch (Exception e) {
                if (args.length == 0) {
                    heartsToRemove = 0;
                } else {
                    return false;
                }
            }
            healthToRemove = heartsToRemove * 2;
            if (currentHealth <= healthToRemove) {
                sender.sendMessage("§6Depixa Lifesteal §8| §cYou cannot remove more hearts than you have or the same amount!");
                return true;
            }

            Material heartMaterial = Material.valueOf(plugin.getConfig().getString("heart-item").toUpperCase());
            ItemStack heart = new ItemStack(heartMaterial, heartsToRemove);
            ItemMeta heartMeta = heart.getItemMeta();
            heartMeta.setDisplayName("§c❤ Heart");
            List<String> lores = new ArrayList<String>();
            lores.add("§cClick me to get an extra heart.");
            heart.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
            heartMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            heartMeta.setLore(lores);
            heart.setItemMeta(heartMeta);
            health.setBaseValue(currentHealth - healthToRemove);
            config.set(player.getUniqueId() + ".maxHealth", currentHealth - healthToRemove);
            player.getInventory().addItem(heart);
            sender.sendMessage("§6Depixa Lifesteal §8| §eYou withdrew §a" + heartsToRemove + " §ehearts.");
            playerData.saveCustomConfig();
        }
        return true;
    }
}
