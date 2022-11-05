package gq.depixa.lifesteal;

import org.bukkit.Bukkit;
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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
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
            if (!config.getBoolean(victim.getUniqueId() + ".eliminated")) {
                Integer maxHearts = plugin.getConfig().getInt("max-hearts");
                if (maxHearts != null && maxHearts != 0 && maxHearts * 2 <= killerCurrentHealth) {
                    killer.sendMessage("§6Depixa Lifesteal §8| §eYou have reached the maximum amount of hearts. A heart has been given to you instead.");
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
                    killer.getInventory().addItem(heart);
                } else {
                    killerHealth.setBaseValue(killerCurrentHealth + 2);
                }
            }
            if (victimCurrentHealth <= 2) {
                if (!config.getBoolean(victim.getUniqueId() + ".eliminated")) {
                    config.set(victim.getUniqueId() + ".eliminated", true);
                    playerData.saveCustomConfig();
                    Bukkit.broadcastMessage("§6Depixa Lifesteal §8| §a" + victim.getDisplayName() + " §ehas been eliminated.");
                }
                if (!victim.hasPermission("dls.bypass")) {
                    victim.kickPlayer("§eYou have been eliminated!");
                } else {
                    victim.sendMessage("§6Depixa Lifesteal §8| §eYou have been eliminated, but you have bypass enabled.");
                }
                return;
            }
            victimHealth.setBaseValue(victimCurrentHealth - 2);
        } else {
            AttributeInstance victimHealth = victim.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            Double victimCurrentHealth = victimHealth.getBaseValue();
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
            if (!config.getBoolean(victim.getUniqueId() + ".eliminated")) {
                if (plugin.getConfig().getBoolean("drop-hearts-on-death")) {
                    event.getDrops().add(heart);
                    if (victimCurrentHealth <= 2) {
                        Bukkit.getScheduler().runTaskLater(plugin, () -> {
                            if (!config.getBoolean(victim.getUniqueId() + ".eliminated")) {
                                config.set(victim.getUniqueId() + ".eliminated", true);
                                playerData.saveCustomConfig();
                                Bukkit.broadcastMessage("§6Depixa Lifesteal §8| §a" + victim.getDisplayName() + " §ehas been eliminated.");
                            }
                            if (!victim.hasPermission("dls.bypass")) {
                                victim.kickPlayer("§eYou have been eliminated!");
                            } else {
                                victim.sendMessage("§6Depixa Lifesteal §8| §eYou have been eliminated, but you have bypass enabled.");
                            }
                        }, 1L);
                        return;
                    }
                    victimHealth.setBaseValue(victimCurrentHealth - 2);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        config.set(event.getPlayer().getUniqueId() + ".username", event.getPlayer().getName());
        playerData.saveCustomConfig();
        if (config.getBoolean(event.getPlayer().getUniqueId() + ".eliminated")) {
            if (event.getPlayer().hasPermission("dls.bypass")) {
                event.getPlayer().sendMessage("§6Depixa Lifesteal §8| §eYou have been eliminated, but you have bypass enabled.");
            } else {
                event.getPlayer().kickPlayer("§eYou have been eliminated!");
            }
        }
        if (config.get(event.getPlayer().getUniqueId() + ".maxHealth") != null) {
            AttributeInstance health = event.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH);
            if (health.getBaseValue() == 2) {
                health.setBaseValue(config.getInt(event.getPlayer().getUniqueId() + ".maxHealth"));
            } else {
                health.setBaseValue(health.getBaseValue() + config.getInt(event.getPlayer().getUniqueId() + ".maxHealth"));
            }
            config.set(event.getPlayer().getUniqueId() + ".maxHealth", null);
            playerData.saveCustomConfig();
        }
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        if (event.getReason().equals("§eYou have been eliminated!")) {
            event.setLeaveMessage("");
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
                    Integer maxHearts = plugin.getConfig().getInt("max-hearts");
                    if (maxHearts != null && maxHearts != 0 && maxHearts * 2 <= currentHealth) {
                        player.sendMessage("§6Depixa Lifesteal §8| §cYou have reached the maximum amount of hearts.");
                    } else {
                        health.setBaseValue(currentHealth + 2);
                        player.sendMessage("§6Depixa Lifesteal §8| §eYou consumed a heart.");
                        event.getItem().setAmount(event.getItem().getAmount() - 1);
                    }
                }
            }
        }
    }

    @EventHandler
    public void prepareItemCraft(PrepareItemCraftEvent event) {
        if (event.getInventory().getResult() == null) return;
        for (ItemStack stack : event.getInventory()) {
            if (event.getInventory().getResult().equals(stack)) continue;
            if (stack == null) continue;
            if (stack.getItemMeta().getDisplayName().equals("§c❤ Heart")) {
                event.getInventory().setResult(null);
            }
        }
    }
}
