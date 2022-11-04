package gq.depixa.lifesteal;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    static Main plugin;
    FileConfiguration config;
    static Configuration playerData;
    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.plugin = this;
        this.config = this.getConfig();
        this.playerData = new Configuration("playerdata");
        if (!config.get("version").equals("0.3")) {
            config.options().copyDefaults(true);
            config.set("version", "0.3");
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
    public static Configuration getPlayerConfig() {
        return playerData;
    }
}
