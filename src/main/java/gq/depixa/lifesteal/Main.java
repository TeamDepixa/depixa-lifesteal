package gq.depixa.lifesteal;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;

public class Main extends JavaPlugin {
    static Main plugin;
    static Configuration playerData;
    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.plugin = this;
        this.playerData = new Configuration("playerdata");
        if (!this.getConfig().get("version").equals(0.6)) {
            if (this.getConfig().getKeys(true).size() == 0) {
                this.saveDefaultConfig();
            } else {
                HashMap<String, Object> oldConfig = new HashMap<String, Object>();
                for (String key : this.getConfig().getKeys(true)) {
                    oldConfig.put(key, this.getConfig().get(key));
                }
                File configFile = new File(plugin.getDataFolder(), "config.yml");
                File oldConfigFile = new File(plugin.getDataFolder(), "config_old.yml");
                if (oldConfigFile.exists()) {
                    oldConfigFile.delete();
                }
                configFile.renameTo(oldConfigFile);
                this.reloadConfig();
                File newConfigFile = new File(plugin.getDataFolder(), "config.yml");
                if (newConfigFile.exists()) {
                    newConfigFile.delete();
                }
                this.saveDefaultConfig();
                this.reloadConfig();
                for (String key : oldConfig.keySet()) {
                    this.getConfig().set(key, oldConfig.get(key));
                }
                this.getConfig().set("version", 0.6);
                this.saveConfig();
                this.reloadConfig();
                getLogger().info("Updated your config.");
            }
            this.saveConfig();
        }
        getServer().getPluginManager().registerEvents(new EventListener(), this);

        this.getCommand("withdraw").setExecutor(new CommandWithdraw());
        this.getCommand("eliminate").setExecutor(new CommandEliminate());
        this.getCommand("giveheartitems").setExecutor(new CommandGiveHearts());
        this.getCommand("lifesteal").setExecutor(new CommandLifesteal());
        this.getCommand("resethealth").setExecutor(new CommandResetHealth());
        this.getCommand("revive").setExecutor(new CommandRevive());
        this.getCommand("transfer").setExecutor(new CommandTransfer());
        this.getCommand("viewhearts").setExecutor(new CommandViewHearts());
        new HeartRecipe().heartRecipe();

        new UpdateChecker(this, 104937).getVersion(version -> {
            if (this.getDescription().getVersion().equals(version)) {
                getLogger().info("You are running the latest version. (" + version + ")");
            } else {
                getLogger().warning("There is a new version available: " + version + ". You are on: " + this.getDescription().getVersion() + ". Get the latest version at https://www.spigotmc.org/resources/depixa-lifesteal.104937/");
            }
        });
    }

    @Override
    public void onDisable() {

    }

    public static Main getPlugin() {
        return plugin;
    }
}
