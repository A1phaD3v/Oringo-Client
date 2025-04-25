// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.config;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;
import org.jetbrains.annotations.NotNull;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.IOException;
import java.awt.Desktop;
import java.util.List;
import java.nio.file.OpenOption;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import com.google.gson.Gson;
import net.minecraftforge.common.MinecraftForge;
import me.oringo.oringoclient.qolfeatures.module.impl.keybinds.Keybind;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.StringSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.ModeSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.OringoClient;
import me.oringo.oringoclient.qolfeatures.module.Module;
import com.google.gson.GsonBuilder;
import java.nio.file.Files;
import java.io.File;

public class ConfigManager
{
    public static String configPath;
    
    public static boolean loadConfig(final String configPath) {
        try {
            final String configString = new String(Files.readAllBytes(new File(configPath).toPath()));
            final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
            final Module[] modules = (Module[])gson.fromJson(configString, (Class)Module[].class);
            for (final Module module : OringoClient.modules) {
                for (final Module configModule : modules) {
                    if (module.getName().equals(configModule.getName())) {
                        try {
                            try {
                                module.setToggled(configModule.isToggled());
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                            module.setKeycode(configModule.getKeycode());
                            for (final Setting setting : module.settings) {
                                for (final ConfigSetting cfgSetting : configModule.cfgSettings) {
                                    if (setting != null) {
                                        if (setting.name.equals(cfgSetting.name)) {
                                            if (setting instanceof BooleanSetting) {
                                                ((BooleanSetting)setting).setEnabled((boolean)cfgSetting.value);
                                            }
                                            else if (setting instanceof ModeSetting) {
                                                ((ModeSetting)setting).setSelected((String)cfgSetting.value);
                                            }
                                            else if (setting instanceof NumberSetting) {
                                                ((NumberSetting)setting).setValue((double)cfgSetting.value);
                                            }
                                            else if (setting instanceof StringSetting) {
                                                ((StringSetting)setting).setValue((String)cfgSetting.value);
                                            }
                                        }
                                    }
                                    else {
                                        System.out.println("[OringoClient] Setting in " + module.getName() + " is null!");
                                    }
                                }
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("Config Issue");
                        }
                    }
                }
            }
            for (final Module module2 : modules) {
                if (module2.getName().startsWith("Keybind ") && Module.getModule(module2.getName()) == null) {
                    final Keybind keybind = new Keybind(module2.getName());
                    keybind.setKeycode(module2.getKeycode());
                    keybind.setToggled(module2.isToggled());
                    for (final Setting setting2 : keybind.settings) {
                        for (final ConfigSetting cfgSetting2 : module2.cfgSettings) {
                            if (setting2.name.equals(cfgSetting2.name)) {
                                if (setting2 instanceof BooleanSetting) {
                                    ((BooleanSetting)setting2).setEnabled((boolean)cfgSetting2.value);
                                }
                                else if (setting2 instanceof ModeSetting) {
                                    ((ModeSetting)setting2).setSelected((String)cfgSetting2.value);
                                }
                                else if (setting2 instanceof NumberSetting) {
                                    ((NumberSetting)setting2).setValue((double)cfgSetting2.value);
                                }
                                else if (setting2 instanceof StringSetting) {
                                    ((StringSetting)setting2).setValue((String)cfgSetting2.value);
                                }
                            }
                        }
                    }
                    MinecraftForge.EVENT_BUS.register((Object)keybind);
                    OringoClient.modules.add(keybind);
                    System.out.println("Loaded Keybind: " + keybind.getName());
                }
            }
        }
        catch (Exception e2) {
            e2.printStackTrace();
            return false;
        }
        return true;
    }
    
    public static void loadConfig() {
        loadConfig(OringoClient.mc.field_71412_D + "/config/OringoClient/OringoClient.json");
    }
    
    public static void saveConfig() {
        saveConfig(OringoClient.mc.field_71412_D + "/config/OringoClient/OringoClient.json", false);
    }
    
    public static boolean saveConfig(final String configPath, final boolean openExplorer) {
        final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
        for (final Module module : OringoClient.modules) {
            module.onSave();
            final List<ConfigSetting> settings = new ArrayList<ConfigSetting>();
            for (final Setting setting : module.settings) {
                final ConfigSetting cfgSetting = new ConfigSetting(null, null);
                cfgSetting.name = setting.name;
                if (setting instanceof BooleanSetting) {
                    cfgSetting.value = ((BooleanSetting)setting).isEnabled();
                }
                else if (setting instanceof ModeSetting) {
                    cfgSetting.value = ((ModeSetting)setting).getSelected();
                }
                else if (setting instanceof NumberSetting) {
                    cfgSetting.value = ((NumberSetting)setting).getValue();
                }
                else if (setting instanceof StringSetting) {
                    cfgSetting.value = ((StringSetting)setting).getValue();
                }
                settings.add(cfgSetting);
            }
            module.cfgSettings = settings.toArray(new ConfigSetting[0]);
        }
        try {
            final File file = new File(configPath);
            Files.write(file.toPath(), gson.toJson((Object)OringoClient.modules).getBytes(StandardCharsets.UTF_8), new OpenOption[0]);
            if (openExplorer) {
                try {
                    openExplorer();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e2) {
            e2.printStackTrace();
            return false;
        }
        return true;
    }
    
    public static void openExplorer() throws IOException {
        Desktop.getDesktop().open(new File(ConfigManager.configPath));
    }
    
    static {
        ConfigManager.configPath = OringoClient.mc.field_71412_D.getPath() + "/config/OringoClient/configs/";
    }
    
    public static class FileSelection implements Transferable
    {
        private List<File> listOfFiles;
        
        public FileSelection(final List<File> listOfFiles) {
            this.listOfFiles = listOfFiles;
        }
        
        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[] { DataFlavor.javaFileListFlavor };
        }
        
        @Override
        public boolean isDataFlavorSupported(final DataFlavor flavor) {
            return flavor == DataFlavor.javaFileListFlavor;
        }
        
        @NotNull
        @Override
        public Object getTransferData(final DataFlavor flavor) {
            return this.listOfFiles;
        }
    }
    
    public static class ConfigSetting
    {
        @Expose
        @SerializedName("name")
        public String name;
        @Expose
        @SerializedName("value")
        public Object value;
        
        public ConfigSetting(final String name, final Object value) {
            this.name = name;
            this.value = value;
        }
    }
}
