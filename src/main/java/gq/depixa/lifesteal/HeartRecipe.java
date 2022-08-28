package gq.depixa.lifesteal;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class HeartRecipe {
    Main plugin = Main.getPlugin();
    public void heartRecipe() {
        Material heartMaterial = Material.valueOf(plugin.getConfig().getString("heart-item").toUpperCase());
        ItemStack heart = new ItemStack(heartMaterial, 1);
        ItemMeta heartMeta = heart.getItemMeta();
        heartMeta.setDisplayName("§c❤ Heart");
        List<String> lores = new ArrayList<String>();
        lores.add("§cClick me to get an extra heart.");
        heart.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        heartMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        heartMeta.setLore(lores);
        heart.setItemMeta(heartMeta);
        NamespacedKey nsKey = new NamespacedKey(plugin, "heart_recipe");
        ShapedRecipe recipe = new ShapedRecipe(nsKey, heart);
        recipe.shape("ABA", "BCB", "ABA");
        recipe.setIngredient('A', Material.DIAMOND_BLOCK);
        recipe.setIngredient('B', Material.NETHERITE_INGOT);
        recipe.setIngredient('C', Material.OBSIDIAN);

        plugin.getServer().addRecipe(recipe);
    }
}
