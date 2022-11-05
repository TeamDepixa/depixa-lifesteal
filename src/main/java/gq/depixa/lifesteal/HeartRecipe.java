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
        recipe.shape("123", "456", "789");
        recipe.setIngredient('1', Material.valueOf(plugin.getConfig().getString("heart-recipe.1").toUpperCase()));
        recipe.setIngredient('2', Material.valueOf(plugin.getConfig().getString("heart-recipe.2").toUpperCase()));
        recipe.setIngredient('3', Material.valueOf(plugin.getConfig().getString("heart-recipe.3").toUpperCase()));
        recipe.setIngredient('4', Material.valueOf(plugin.getConfig().getString("heart-recipe.4").toUpperCase()));
        recipe.setIngredient('5', Material.valueOf(plugin.getConfig().getString("heart-recipe.5").toUpperCase()));
        recipe.setIngredient('6', Material.valueOf(plugin.getConfig().getString("heart-recipe.6").toUpperCase()));
        recipe.setIngredient('7', Material.valueOf(plugin.getConfig().getString("heart-recipe.7").toUpperCase()));
        recipe.setIngredient('8', Material.valueOf(plugin.getConfig().getString("heart-recipe.8").toUpperCase()));
        recipe.setIngredient('9', Material.valueOf(plugin.getConfig().getString("heart-recipe.9").toUpperCase()));
        plugin.getServer().addRecipe(recipe);
    }
}
