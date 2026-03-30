package com.ericimbriaco.xPlugin.Utils;

import com.ericimbriaco.xPlugin.Main;
import com.ericimbriaco.xPlugin.Commands.*;

public class commandManager {
    public commandManager(Main plugin){
        plugin.getCommand("xplugin").setExecutor(new xPluginCommand());
        plugin.getCommand("heal").setExecutor(new healCommand());
        plugin.getCommand("crash").setExecutor(new crashKickCommand());
    }
}
