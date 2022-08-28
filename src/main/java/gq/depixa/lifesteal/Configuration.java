package gq.depixa.lifesteal;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.logging.Level;

public class Configuration {
    private FileConfiguration customConfig = null;
    private File customConfigFile = null;

    private static Main plugin = Main.getPlugin();
    private static String fileName;

    public Configuration(String string) {
        this.fileName = string;
    }

    public void reloadCustomConfig() {
        if (customConfigFile == null) {
            customConfigFile = new File(plugin.getDataFolder(), fileName + ".yml");
            if (!customConfigFile.exists()) {
                customConfigFile.getParentFile().mkdirs();
                plugin.saveResource(fileName + ".yml", false);
            }
        }
        customConfig = YamlConfiguration.loadConfiguration(customConfigFile);

        // Look for defaults in the jar
        Reader defConfigStream = null;
        defConfigStream = new InputStreamReader(plugin.getResource(fileName + ".yml"));
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            customConfig.setDefaults(defConfig);
        }
    }

    public FileConfiguration getCustomConfig() {
        if (customConfig == null) {
            reloadCustomConfig();
        }
        return customConfig;
    }

    public void saveCustomConfig() {
        if (customConfig == null || customConfigFile == null) {
            return;
        }
        try {
            getCustomConfig().save(customConfigFile);
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config to " + customConfigFile, ex);
        }
    }
}
