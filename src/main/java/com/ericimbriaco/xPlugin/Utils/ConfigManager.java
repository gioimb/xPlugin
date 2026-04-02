package com.ericimbriaco.xPlugin.Utils;

import com.ericimbriaco.xPlugin.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

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

    public static List<String> getStringList(String path) {
        return getConfig().getStringList(path);
    }

    private static File file;
    private static FileConfiguration cfg;

    public static void setupData(){
        file = new File(Main.getInstance().getDataFolder(), "data.yml");

        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        cfg = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration get() {
        return cfg;
    }

    public static void saveDataFile() {
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void reloadDataFile() {
        cfg = YamlConfiguration.loadConfiguration(file);
    }
}
