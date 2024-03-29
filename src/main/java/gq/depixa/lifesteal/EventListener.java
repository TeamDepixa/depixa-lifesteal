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
import java.util.concurrent.TimeUnit;

public class EventListener implements Listener {
    Main plugin = Main.getPlugin();
    private Configuration playerData = new Configuration("playerdata");

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        playerData.reloadCustomConfig();
        FileConfiguration config = playerData.getCustomConfig();
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
                    Double lostHealth = 2.0;
                    if (plugin.getConfig().getDouble("hearts-lost-on-death") > 0) {
                        lostHealth = plugin.getConfig().getDouble("hearts-lost-on-death") * 2;
                    }
                    killerHealth.setBaseValue(killerCurrentHealth + lostHealth);
                    config.set(killer.getUniqueId() + ".maxHealth", killerCurrentHealth + lostHealth);
                    playerData.saveCustomConfig();
                }
            }
            if (victimCurrentHealth <= 2) {
                if (!config.getBoolean(victim.getUniqueId() + ".eliminated")) {
                    config.set(victim.getUniqueId() + ".eliminated", true);
                    config.set(victim.getUniqueId() + ".banTime", System.currentTimeMillis());
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
            Double lostHealth = 2.0;
            if (plugin.getConfig().getDouble("hearts-lost-on-death") > 0) {
                lostHealth = plugin.getConfig().getDouble("hearts-lost-on-death") * 2;
            }
            victimHealth.setBaseValue(victimCurrentHealth - lostHealth);
            config.set(victim.getUniqueId() + ".maxHealth", victimCurrentHealth - lostHealth);
            playerData.saveCustomConfig();
        } else {
            Double lostHealth = 2.0;
            if (plugin.getConfig().getDouble("hearts-lost-on-death") > 0) {
                lostHealth = plugin.getConfig().getDouble("hearts-lost-on-death") * 2;
            }
            AttributeInstance victimHealth = victim.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            Double victimCurrentHealth = victimHealth.getBaseValue();
            Material heartMaterial = Material.valueOf(plugin.getConfig().getString("heart-item").toUpperCase());
            ItemStack heart = new ItemStack(heartMaterial, plugin.getConfig().getInt("hearts-lost-on-death"));
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
                                config.set(victim.getUniqueId() + ".banTime", System.currentTimeMillis());
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
                    victimHealth.setBaseValue(victimCurrentHealth - lostHealth);
                    config.set(victim.getUniqueId() + ".maxHealth", victimCurrentHealth - lostHealth);
                    playerData.saveCustomConfig();
                }
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        playerData.reloadCustomConfig();
        FileConfiguration config = playerData.getCustomConfig();
        config.set(event.getPlayer().getUniqueId() + ".username", event.getPlayer().getName());
        playerData.saveCustomConfig();
        if (config.getBoolean(event.getPlayer().getUniqueId() + ".eliminated")) {
            if (((System.currentTimeMillis() - config.getLong(event.getPlayer().getUniqueId() + ".banTime"))) >= plugin.getConfig().getInt("ban-length") * 1000 && plugin.getConfig().getInt("ban-length") > 0) {
                config.set(event.getPlayer().getUniqueId() + ".eliminated", null);
                config.set(event.getPlayer().getUniqueId() + ".banTime", null);
                event.getPlayer().sendMessage("§eYou have been unbanned. Welcome back.");
                playerData.saveCustomConfig();
            } else {
                if (event.getPlayer().hasPermission("dls.bypass")) {
                    event.getPlayer().sendMessage("§6Depixa Lifesteal §8| §eYou have been eliminated, but you have bypass enabled.");
                } else {
                    if (plugin.getConfig().getInt("ban-length") > 0) {
                        Long millis =  (plugin.getConfig().getLong("ban-length") * 1000) - (System.currentTimeMillis() - config.getLong(event.getPlayer().getUniqueId() + ".banTime"));
                        String timeLeft = String.format("%02d:%02d:%02d",
                                TimeUnit.MILLISECONDS.toHours(millis),
                                TimeUnit.MILLISECONDS.toMinutes(millis) -
                                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                                TimeUnit.MILLISECONDS.toSeconds(millis) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                        event.getPlayer().kickPlayer("§eYou have been eliminated!\n§eYou will be unbanned in §c" + timeLeft);
                    } else {
                        event.getPlayer().kickPlayer("§eYou have been eliminated!");
                    }
                }
            }
        }
        AttributeInstance health = event.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if ((config.get(event.getPlayer().getUniqueId() + ".maxHealth") == null) || (config.getInt(event.getPlayer().getUniqueId() + ".maxHealth") < 1)) {
            config.set(event.getPlayer().getUniqueId() + ".maxHealth", health.getBaseValue());
        } else {
            health.setBaseValue(config.getInt(event.getPlayer().getUniqueId() + ".maxHealth"));
        }
        playerData.saveCustomConfig();
    }


    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        if (event.getReason().startsWith("§eYou have been eliminated!")) {
            event.setLeaveMessage("");
        }
    }

    @EventHandler
    public void onPlayerUse(PlayerInteractEvent event) {
        playerData.reloadCustomConfig();
        FileConfiguration config = playerData.getCustomConfig();
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
                        config.set(player.getUniqueId() + ".maxHealth", currentHealth + 2);
                        player.sendMessage("§6Depixa Lifesteal §8| §eYou consumed a heart.");
                        event.getItem().setAmount(event.getItem().getAmount() - 1);
                        playerData.saveCustomConfig();
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
