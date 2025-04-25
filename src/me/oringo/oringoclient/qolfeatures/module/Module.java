// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module;

import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentText;
import java.util.function.Predicate;
import java.util.Iterator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import me.oringo.oringoclient.OringoClient;
import org.lwjgl.input.Keyboard;
import java.util.ArrayList;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import java.util.List;
import me.oringo.oringoclient.utils.MilliTimer;
import net.minecraft.client.Minecraft;
import java.util.logging.Logger;
import me.oringo.oringoclient.config.ConfigManager;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;

public class Module
{
    @Expose
    @SerializedName("name")
    public String name;
    @Expose
    @SerializedName("toggled")
    private boolean toggled;
    @Expose
    @SerializedName("keyCode")
    private int keycode;
    private final Category category;
    public boolean extended;
    @Expose
    @SerializedName("settings")
    public ConfigManager.ConfigSetting[] cfgSettings;
    protected static final Logger LOGGER;
    private boolean devOnly;
    protected static final Minecraft mc;
    public final MilliTimer toggledTime;
    public final List<Setting> settings;
    
    public Module(final String name, final int keycode, final Category category) {
        this.toggledTime = new MilliTimer();
        this.settings = new ArrayList<Setting>();
        this.name = name;
        this.keycode = keycode;
        this.category = category;
    }
    
    public Module(final String name, final Category category) {
        this(name, 0, category);
    }
    
    public boolean isToggled() {
        return this.toggled;
    }
    
    public void toggle() {
        this.setToggled(!this.toggled);
    }
    
    public void onEnable() {
    }
    
    public void onSave() {
    }
    
    public boolean isKeybind() {
        return false;
    }
    
    public void addSetting(final Setting setting) {
        this.getSettings().add(setting);
    }
    
    public void addSettings(final Setting... settings) {
        for (final Setting setting : settings) {
            this.addSetting(setting);
        }
    }
    
    public Category getCategory() {
        return this.category;
    }
    
    public String getName() {
        return this.name;
    }
    
    public boolean isPressed() {
        return this.keycode != 0 && Keyboard.isKeyDown(this.keycode) && this.isKeybind();
    }
    
    public int getKeycode() {
        return this.keycode;
    }
    
    public void setKeycode(final int keycode) {
        this.keycode = keycode;
    }
    
    public List<Setting> getSettings() {
        return this.settings;
    }
    
    public static List<Module> getModulesByCategory(final Category c) {
        return OringoClient.modules.stream().filter(module -> module.category == c).collect((Collector<? super Object, ?, List<Module>>)Collectors.toList());
    }
    
    public static <T> T getModule(final Class<T> module) {
        for (final Module m : OringoClient.modules) {
            if (m.getClass().equals(module)) {
                return (T)m;
            }
        }
        return null;
    }
    
    public static Module getModule(final Predicate<Module> predicate) {
        for (final Module m : OringoClient.modules) {
            if (predicate.test(m)) {
                return m;
            }
        }
        return null;
    }
    
    public static Module getModule(final String string) {
        for (final Module m : OringoClient.modules) {
            if (m.getName().equalsIgnoreCase(string)) {
                return m;
            }
        }
        return null;
    }
    
    public void setToggled(final boolean toggled) {
        if (this.toggled != toggled) {
            this.toggled = toggled;
            this.toggledTime.reset();
            if (toggled) {
                this.onEnable();
            }
            else {
                this.onDisable();
            }
        }
    }
    
    public void onDisable() {
    }
    
    public void setDevOnly(final boolean devOnly) {
        this.devOnly = devOnly;
    }
    
    public boolean isDevOnly() {
        return this.devOnly;
    }
    
    protected static void sendMessage(final String message) {
        Minecraft.func_71410_x().field_71439_g.func_145747_a((IChatComponent)new ChatComponentText(message));
    }
    
    protected static void sendMessageWithPrefix(final String message) {
        Minecraft.func_71410_x().field_71439_g.func_145747_a((IChatComponent)new ChatComponentText("§bOringoClient §3» §7" + message));
    }
    
    static {
        LOGGER = OringoClient.LOGGER;
        mc = Minecraft.func_71410_x();
    }
    
    public enum Category
    {
        COMBAT("Combat"), 
        SKYBLOCK("Skyblock"), 
        RENDER("Render"), 
        MOVEMENT("Movement"), 
        PLAYER("Player"), 
        OTHER("Other"), 
        KEYBINDS("Keybinds");
        
        public String name;
        
        private Category(final String name) {
            this.name = name;
        }
    }
}
