// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.commands.impl;

import me.oringo.oringoclient.ui.notifications.Notifications;
import me.oringo.oringoclient.config.ConfigManager;
import me.oringo.oringoclient.commands.Command;

public class ConfigCommand extends Command
{
    public ConfigCommand() {
        super("config", new String[0]);
    }
    
    @Override
    public void execute(final String[] args) throws Exception {
        if (args.length > 0) {
            final String name = String.join(" ", (CharSequence[])args).replaceFirst(args[0] + " ", "");
            final String lowerCase = args[0].toLowerCase();
            switch (lowerCase) {
                case "save": {
                    if (ConfigManager.saveConfig(ConfigManager.configPath + String.format("%s.json", name), true)) {
                        Notifications.showNotification("Oringo Client", "Successfully saved config " + name, 3000);
                        break;
                    }
                    Notifications.showNotification("Saving " + name + " failed", 3000, Notifications.NotificationType.ERROR);
                    break;
                }
                case "load": {
                    if (ConfigManager.loadConfig(ConfigManager.configPath + String.format("%s.json", name))) {
                        Notifications.showNotification("Oringo Client", "Config " + name + " loaded", 3000);
                        break;
                    }
                    Notifications.showNotification("Loading config " + name + " failed", 3000, Notifications.NotificationType.ERROR);
                    break;
                }
            }
        }
        else {
            try {
                Notifications.showNotification("Oringo Client", ".config load/save name", 3000);
                ConfigManager.openExplorer();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public String getDescription() {
        return "Save or load a config. .config to open explorer";
    }
}
