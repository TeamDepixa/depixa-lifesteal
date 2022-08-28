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
        getServer().getPluginManager().registerEvents(new EventListener(), this);

        this.getCommand("withdraw").setExecutor(new CommandWithdraw());
        this.getCommand("eliminate").setExecutor(new CommandEliminate());
        this.getCommand("giveheartitems").setExecutor(new CommandGiveHearts());
        this.getCommand("lifesteal").setExecutor(new CommandLifesteal());
        this.getCommand("resethealth").setExecutor(new CommandResetHealth());
        this.getCommand("revive").setExecutor(new CommandRevive());
        this.getCommand("transfer").setExecutor(new CommandTransfer());
        new HeartRecipe().heartRecipe();
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
