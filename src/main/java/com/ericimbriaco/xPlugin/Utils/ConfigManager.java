package com.ericimbriaco.xPlugin.Utils;

import com.ericimbriaco.xPlugin.Main;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
    public static FileConfiguration getConfig() {
        return Main.getInstance().getConfig();
    }

    public static String getString(String path) {
        return getConfig().getString(path);
    }

    public static int getInt(String path) {
        return getConfig().getInt(path);
    }

    public static boolean getBoolean(String path) {
        return getConfig().getBoolean(path);
    }
}
