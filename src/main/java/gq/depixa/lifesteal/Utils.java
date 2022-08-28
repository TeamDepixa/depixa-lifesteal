package gq.depixa.lifesteal;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.UUID;

public class Utils {
    Main plugin = Main.getPlugin();
    private Configuration playerData = new Configuration("playerdata");
    FileConfiguration config = playerData.getCustomConfig();
    public UUID getUniqueId(String name) {
        UUID uniqueId = null;
        for (String key : config.getKeys(false)) {
            if (config.getString(key + ".username") == null) continue;
            if (config.getString(key + ".username").equalsIgnoreCase(name)) {
                uniqueId = UUID.fromString(key);
            }
        }
        return uniqueId;
    }
}
