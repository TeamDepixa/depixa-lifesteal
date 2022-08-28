package gq.depixa.lifesteal;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class EventListener implements Listener {
    Main plugin = Main.getPlugin();
    private Configuration playerData = Main.getPlayerConfig();
    FileConfiguration config = playerData.getCustomConfig();

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        if (victim.getKiller() != null) {
            Player killer = victim.getKiller();
            AttributeInstance victimHealth = victim.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            AttributeInstance killerHealth = killer.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            Double victimCurrentHealth = victimHealth.getBaseValue();
            Double killerCurrentHealth = killerHealth.getBaseValue();
            killerHealth.setBaseValue(killerCurrentHealth + 2);
            if (victimCurrentHealth <= 2) {
                config.set(victim.getUniqueId() + ".eliminated", true);
                playerData.saveCustomConfig();
                victim.kickPlayer("§eYou have been eliminated!");
                return;
            }
            victimHealth.setBaseValue(victimCurrentHealth - 2);
        } else {
            AttributeInstance victimHealth = victim.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            Double victimCurrentHealth = victimHealth.getBaseValue();
            victimHealth.setBaseValue(victimCurrentHealth - 2);
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
            event.getDrops().add(heart);
            if (victimCurrentHealth <= 2) {
                config.set(victim.getUniqueId() + ".eliminated", true);
                playerData.saveCustomConfig();
                victim.kickPlayer("§eYou have been eliminated!");
                return;
            }
        }
    }

    @EventHandler
    public void prePlayerJoin(AsyncPlayerPreLoginEvent event) {
        if (config.getBoolean(event.getUniqueId() + ".eliminated")) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, "§eYou have been eliminated!");
        }
    }

    @EventHandler
    public void onPlayerUse(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getMaterial() == Material.AIR) return;
            if (event.getItem().getType() == Material.valueOf(plugin.getConfig().getString("heart-item").toUpperCase())) {
                if (event.getItem().getItemMeta().getDisplayName().equals("§c❤ Heart")) {
                    AttributeInstance health = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
                    Double currentHealth = health.getBaseValue();
                    health.setBaseValue(currentHealth + 2);
                    player.sendMessage("§6Depixa Lifesteal §8| §eYou consumed a heart.");
                    event.getItem().setAmount(event.getItem().getAmount() - 1);
                }
            }
        }
    }

    @EventHandler
    public void prepareItemCraft(PrepareItemCraftEvent event) {
        for (ItemStack stack : event.getInventory()) {
            if (event.getInventory().getResult().equals(stack)) continue;
            if (stack == null) continue;
            if (stack.getItemMeta().getDisplayName().equals("§c❤ Heart")) {
                event.getInventory().setResult(null);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        config.set(event.getPlayer().getUniqueId() + ".username", event.getPlayer().getName());
        playerData.saveCustomConfig();
    }
}
