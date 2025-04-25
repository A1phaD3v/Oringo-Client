// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.ui.gui;

import net.minecraftforge.common.MinecraftForge;
import me.oringo.oringoclient.qolfeatures.module.impl.keybinds.Keybind;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.RunnableSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.ModeSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.utils.RenderUtils;
import java.awt.Color;
import java.util.Collections;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Comparator;
import me.oringo.oringoclient.utils.font.Fonts;
import java.util.List;
import me.oringo.oringoclient.mixins.MinecraftAccessor;
import me.oringo.oringoclient.qolfeatures.module.impl.render.PopupAnimation;
import me.oringo.oringoclient.utils.shader.BlurUtils;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;
import me.oringo.oringoclient.OringoClient;
import me.oringo.oringoclient.config.ConfigManager;
import org.lwjgl.input.Mouse;
import java.io.IOException;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import me.oringo.oringoclient.utils.MilliTimer;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.StringSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;
import java.util.ArrayList;
import net.minecraft.client.gui.GuiScreen;

public class ClickGUI extends GuiScreen
{
    public static ArrayList<Panel> panels;
    private Module binding;
    private Panel draggingPanel;
    private NumberSetting draggingSlider;
    private StringSetting settingString;
    private float startX;
    private float startY;
    public int guiScale;
    private MilliTimer animationTimer;
    private static int background;
    
    public ClickGUI() {
        this.binding = null;
        this.animationTimer = new MilliTimer();
        ClickGUI.panels = new ArrayList<Panel>();
        final int pwidth = 100;
        final int pheight = 20;
        int px = 100;
        final int py = 10;
        for (final Module.Category c : Module.Category.values()) {
            ClickGUI.panels.add(new Panel(c, (float)px, (float)py, (float)pwidth, (float)pheight));
            px += pwidth + 10;
        }
    }
    
    public void func_146280_a(final Minecraft mc, final int width, final int height) {
        this.animationTimer.reset();
        super.func_146280_a(mc, width, height);
    }
    
    public void func_146269_k() throws IOException {
        for (final Panel panel : ClickGUI.panels) {
            panel.prevScrolling = panel.scrolling;
        }
        super.func_146269_k();
    }
    
    public void func_146274_d() throws IOException {
        super.func_146274_d();
        int scroll = Mouse.getEventDWheel();
        if (scroll != 0) {
            if (scroll > 1) {
                scroll = 1;
            }
            if (scroll < -1) {
                scroll = -1;
            }
            final int mouseX = Mouse.getX() * this.field_146294_l / this.field_146297_k.field_71443_c;
            final int mouseZ = this.field_146295_m - Mouse.getY() * this.field_146295_m / this.field_146297_k.field_71440_d - 1;
            this.handle(mouseX, mouseZ, scroll, 0.0f, Handle.SCROLL);
        }
    }
    
    protected void func_73864_a(final int mouseX, final int mouseY, final int mouseButton) {
        this.draggingSlider = null;
        this.draggingPanel = null;
        this.settingString = null;
        this.binding = null;
        this.handle(mouseX, mouseY, mouseButton, 0.0f, Handle.CLICK);
    }
    
    protected void func_146286_b(final int mouseX, final int mouseY, final int state) {
        ConfigManager.saveConfig();
        this.handle(mouseX, mouseY, state, 0.0f, Handle.RELEASE);
        super.func_146286_b(mouseX, mouseY, state);
    }
    
    public void func_73863_a(final int mouseX, final int mouseY, final float partialTicks) {
        this.handle(mouseX, mouseY, -1, partialTicks, Handle.DRAW);
        super.func_73863_a(mouseX, mouseY, partialTicks);
    }
    
    protected void func_73869_a(final char typedChar, final int keyCode) throws IOException {
        ConfigManager.saveConfig();
        if (keyCode == 1 || keyCode == OringoClient.clickGui.getKeycode()) {
            if (this.binding != null) {
                this.binding.setKeycode(0);
                this.binding = null;
            }
            else if (this.settingString != null) {
                this.settingString = null;
            }
            else {
                this.draggingPanel = null;
                this.draggingSlider = null;
                super.func_73869_a(typedChar, keyCode);
            }
        }
        else if (this.binding != null) {
            this.binding.setKeycode(keyCode);
            this.binding = null;
        }
        else if (this.settingString != null) {
            if (keyCode == 28) {
                this.settingString = null;
            }
            else if (keyCode == 47 && (Keyboard.isKeyDown(157) || Keyboard.isKeyDown(29))) {
                this.settingString.setValue(this.settingString.getValue() + func_146277_j());
            }
            else if (keyCode != 14) {
                this.settingString.setValue(ChatAllowedCharacters.func_71565_a(this.settingString.getValue() + typedChar));
            }
            else {
                this.settingString.setValue(this.settingString.getValue().substring(0, Math.max(0, this.settingString.getValue().length() - 1)));
            }
        }
    }
    
    private void handle(int mouseX, int mouseY, final int key, final float partialTicks, final Handle handle) {
        final int toggled = OringoClient.clickGui.getColor().getRGB();
        final float scale = 2.0f / this.field_146297_k.field_71474_y.field_74335_Z;
        final int prevScale = this.field_146297_k.field_71474_y.field_74335_Z;
        GL11.glPushMatrix();
        if (this.field_146297_k.field_71474_y.field_74335_Z > 2 && !OringoClient.clickGui.scaleGui.isEnabled()) {
            this.field_146297_k.field_71474_y.field_74335_Z = 2;
            mouseX /= (int)scale;
            mouseY /= (int)scale;
            GL11.glScaled((double)scale, (double)scale, (double)scale);
        }
        if (handle == Handle.DRAW) {
            int blur = 0;
            final String selected = OringoClient.clickGui.blur.getSelected();
            switch (selected) {
                case "Low": {
                    blur = 4;
                    break;
                }
                case "High": {
                    blur = 7;
                    break;
                }
            }
            final ScaledResolution resolution = new ScaledResolution(this.field_146297_k);
            BlurUtils.renderBlurredBackground((float)blur, (float)resolution.func_78326_a(), (float)resolution.func_78328_b(), 0.0f, 0.0f, (float)resolution.func_78326_a(), (float)resolution.func_78328_b());
            if (PopupAnimation.shouldScale(this)) {
                PopupAnimation.doScaling();
            }
        }
        for (final Panel p : ClickGUI.panels) {
            switch (handle) {
                case DRAW: {
                    if (this.draggingPanel == p) {
                        p.x = this.startX + mouseX;
                        p.y = this.startY + mouseY;
                    }
                }
                case CLICK: {
                    if (!this.isHovered(mouseX, mouseY, p.x, p.y, p.height, p.width)) {
                        break;
                    }
                    if (key == 1) {
                        this.startX = p.x - mouseX;
                        this.startY = p.y - mouseY;
                        this.draggingPanel = p;
                        this.draggingSlider = null;
                        break;
                    }
                    if (key == 0) {
                        if (p.extended) {
                            p.scrolling = -this.getTotalSettingsCount(p);
                        }
                        else {
                            p.scrolling = 0;
                        }
                        p.extended = !p.extended;
                        break;
                    }
                    break;
                }
                case RELEASE: {
                    this.draggingPanel = null;
                    this.draggingSlider = null;
                    break;
                }
            }
            float y = p.y + p.height + 3.0f;
            final int moduleHeight = 15;
            y += moduleHeight * (p.prevScrolling + (p.scrolling - p.prevScrolling) * ((MinecraftAccessor)this.field_146297_k).getTimer().field_74281_c);
            final List<Module> list = Module.getModulesByCategory(p.category).stream().sorted(Comparator.comparing(module -> Fonts.robotoMediumBold.getStringWidth(module.getName()))).collect((Collector<? super Object, ?, List<Module>>)Collectors.toList());
            Collections.reverse(list);
            for (final Module module2 : list) {
                if (module2.isDevOnly() && !OringoClient.devMode) {
                    continue;
                }
                Label_0942: {
                    switch (handle) {
                        case DRAW: {
                            if (y < p.y) {
                                break;
                            }
                            RenderUtils.drawRect(p.x, y, p.x + p.width, y + moduleHeight, module2.isToggled() ? new Color(toggled).getRGB() : ClickGUI.background);
                            Fonts.robotoMediumBold.drawSmoothCenteredString(module2.getName(), p.x + p.width / 2.0f, y + moduleHeight / 2.0f - Fonts.robotoMediumBold.getHeight() / 2.0f, Color.white.getRGB());
                            break;
                        }
                        case CLICK: {
                            if (!this.isHovered(mouseX, mouseY, p.x, y, moduleHeight, p.width)) {
                                break;
                            }
                            if (y < p.y + p.height + 3.0f) {
                                break;
                            }
                            switch (key) {
                                case 1: {
                                    module2.extended = !module2.extended;
                                    break Label_0942;
                                }
                                case 0: {
                                    module2.toggle();
                                    break Label_0942;
                                }
                            }
                            break;
                        }
                    }
                }
                y += moduleHeight;
                if (!module2.extended) {
                    continue;
                }
                for (final Setting setting : module2.settings) {
                    if (setting.isHidden()) {
                        continue;
                    }
                    if ((handle == Handle.DRAW && y >= p.y) || (handle == Handle.CLICK && y >= p.y + p.height + 3.0f)) {
                        if (setting instanceof ModeSetting) {
                            switch (handle) {
                                case DRAW: {
                                    RenderUtils.drawRect(p.x, y, p.x + p.width, y + moduleHeight, new Color(ClickGUI.background).brighter().getRGB());
                                    Fonts.robotoMediumBold.drawSmoothString(setting.name, p.x + 2.0f, y + moduleHeight / 2.0f - Fonts.robotoMediumBold.getHeight() / 2.0f, Color.white.getRGB());
                                    Fonts.robotoMediumBold.drawSmoothString(((ModeSetting)setting).getSelected(), p.x + p.width - Fonts.robotoMediumBold.getStringWidth(((ModeSetting)setting).getSelected()) - 2.0, y + moduleHeight / 2.0f - Fonts.robotoMediumBold.getHeight() / 2.0f, new Color(143, 143, 143, 255).getRGB());
                                    break;
                                }
                                case CLICK: {
                                    if (!this.isHovered(mouseX, mouseY, p.x + p.width - Fonts.robotoMediumBold.getStringWidth(((ModeSetting)setting).getSelected()) - 2.0, y, moduleHeight, Fonts.robotoMediumBold.getStringWidth(((ModeSetting)setting).getSelected()))) {
                                        break;
                                    }
                                    ((ModeSetting)setting).cycle(key);
                                    break;
                                }
                            }
                        }
                        else if (setting instanceof NumberSetting) {
                            final float percent = (float)((((NumberSetting)setting).getValue() - ((NumberSetting)setting).getMin()) / (((NumberSetting)setting).getMax() - ((NumberSetting)setting).getMin()));
                            final float numberWidth = percent * p.width;
                            if (this.draggingSlider != null && this.draggingSlider == setting) {
                                final double mousePercent = Math.min(1.0f, Math.max(0.0f, (mouseX - p.x) / p.width));
                                final double newValue = mousePercent * (((NumberSetting)setting).getMax() - ((NumberSetting)setting).getMin()) + ((NumberSetting)setting).getMin();
                                ((NumberSetting)setting).setValue(newValue);
                            }
                            switch (handle) {
                                case DRAW: {
                                    RenderUtils.drawRect(p.x, y, p.x + p.width, y + moduleHeight, new Color(ClickGUI.background).brighter().brighter().getRGB());
                                    RenderUtils.drawRect(p.x, y, p.x + numberWidth, y + moduleHeight, new Color(toggled).brighter().getRGB());
                                    Fonts.robotoMediumBold.drawSmoothString(setting.name, p.x + 2.0f, y + moduleHeight / 2.0f - Fonts.robotoMediumBold.getHeight() / 2.0f, Color.white.getRGB());
                                    Fonts.robotoMediumBold.drawSmoothString(String.valueOf(((NumberSetting)setting).getValue()), p.x + p.width - 2.0f - Fonts.robotoMediumBold.getStringWidth(String.valueOf(((NumberSetting)setting).getValue())), y + moduleHeight / 2.0f - Fonts.robotoBig.getHeight() / 2.0f, Color.white.getRGB());
                                    break;
                                }
                                case CLICK: {
                                    if (!this.isHovered(mouseX, mouseY, p.x, y, moduleHeight, p.width)) {
                                        break;
                                    }
                                    this.draggingSlider = (NumberSetting)setting;
                                    this.draggingPanel = null;
                                    break;
                                }
                            }
                        }
                        else if (setting instanceof BooleanSetting) {
                            switch (handle) {
                                case DRAW: {
                                    RenderUtils.drawRect(p.x, y, p.x + p.width, y + moduleHeight, new Color(ClickGUI.background).brighter().getRGB());
                                    RenderUtils.drawBorderedRoundedRect(p.x + p.width - 12.0f, y + moduleHeight / 2.0f - 4.0f, 8.0f, 8.0f, 3.0f, 2.0f, ((BooleanSetting)setting).isEnabled() ? new Color(toggled).brighter().getRGB() : new Color(ClickGUI.background).brighter().getRGB(), new Color(toggled).getRGB());
                                    Fonts.robotoMediumBold.drawSmoothString(setting.name, p.x + 2.0f, y + moduleHeight / 2.0f - Fonts.robotoMediumBold.getHeight() / 2.0f, Color.white.getRGB());
                                    break;
                                }
                                case CLICK: {
                                    if (!this.isHovered(mouseX, mouseY, p.x + p.width - 12.0f, y + moduleHeight / 2.0f - 4.0f, 8.0, 8.0)) {
                                        break;
                                    }
                                    ((BooleanSetting)setting).toggle();
                                    break;
                                }
                            }
                        }
                        else if (setting instanceof StringSetting) {
                            final String value = (this.settingString == setting) ? (((StringSetting)setting).getValue() + "_") : ((((StringSetting)setting).getValue() == null || ((StringSetting)setting).getValue().equals("")) ? (setting.name + "...") : Fonts.robotoMediumBold.trimStringToWidth(((StringSetting)setting).getValue(), (int)p.width));
                            Label_2455: {
                                switch (handle) {
                                    case DRAW: {
                                        RenderUtils.drawRect(p.x, y, p.x + p.width, y + moduleHeight, new Color(ClickGUI.background).brighter().getRGB());
                                        Fonts.robotoMediumBold.drawCenteredString(value, p.x + p.width / 2.0f, y + moduleHeight / 2.0f - Fonts.robotoMediumBold.getHeight() / 2.0f, (((StringSetting)setting).getValue() == null || (((StringSetting)setting).getValue().equals("") && this.settingString != setting)) ? new Color(143, 143, 143, 255).getRGB() : Color.white.getRGB());
                                        break;
                                    }
                                    case CLICK: {
                                        if (!this.isHovered(mouseX, mouseY, p.x, y, moduleHeight, p.width)) {
                                            break;
                                        }
                                        switch (key) {
                                            case 0: {
                                                this.settingString = (StringSetting)setting;
                                                break Label_2455;
                                            }
                                            case 1: {
                                                ((StringSetting)setting).setValue("");
                                                break Label_2455;
                                            }
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                        else if (setting instanceof RunnableSetting) {
                            switch (handle) {
                                case CLICK: {
                                    if (!this.isHovered(mouseX, mouseY, p.x, y, moduleHeight, p.width)) {
                                        break;
                                    }
                                    ((RunnableSetting)setting).execute();
                                    break;
                                }
                                case DRAW: {
                                    RenderUtils.drawRect(p.x, y, p.x + p.width, y + moduleHeight, new Color(ClickGUI.background).brighter().getRGB());
                                    Fonts.robotoMediumBold.drawCenteredString(setting.name, p.x + p.width / 2.0f, y + moduleHeight / 2.0f - Fonts.robotoMediumBold.getHeight() / 2.0f, Color.white.getRGB());
                                    break;
                                }
                            }
                        }
                    }
                    y += moduleHeight;
                }
                Label_3059: {
                    if ((handle == Handle.DRAW && y >= p.y) || (handle == Handle.CLICK && y >= p.y + p.height + 3.0f)) {
                        final String keyText = (this.binding == module2) ? "[...]" : ("[" + ((module2.getKeycode() >= 256) ? "  " : Keyboard.getKeyName(module2.getKeycode()).replaceAll("NONE", "  ")) + "]");
                        switch (handle) {
                            case DRAW: {
                                RenderUtils.drawRect(p.x, y, p.x + p.width, y + moduleHeight, new Color(ClickGUI.background).brighter().getRGB());
                                Fonts.robotoMediumBold.drawSmoothString("Key", p.x + 2.0f, y + moduleHeight / 2.0f - Fonts.robotoMediumBold.getHeight() / 2.0f, Color.white.getRGB());
                                Fonts.robotoMediumBold.drawSmoothString(keyText, p.x + p.width - Fonts.robotoMediumBold.getStringWidth(keyText) - 2.0, y + moduleHeight / 2.0f - Fonts.robotoMediumBold.getHeight() / 2.0f, new Color(143, 143, 143, 255).getRGB());
                                break;
                            }
                            case CLICK: {
                                if (!this.isHovered(mouseX, mouseY, p.x + p.width - Fonts.robotoMediumBold.getStringWidth(keyText) - 2.0, y, moduleHeight, Fonts.robotoMediumBold.getStringWidth(keyText))) {
                                    break;
                                }
                                switch (key) {
                                    case 0: {
                                        this.binding = module2;
                                        break Label_3059;
                                    }
                                    case 1: {
                                        module2.setKeycode(0);
                                        break Label_3059;
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
                y += moduleHeight;
            }
            if (p.category == Module.Category.KEYBINDS) {
                switch (handle) {
                    case CLICK: {
                        if (!this.isHovered(mouseX, mouseY, p.x, y, 15.0, p.width)) {
                            break;
                        }
                        final Keybind keybind = new Keybind("Keybind " + (Module.getModulesByCategory(Module.Category.KEYBINDS).size() + 1));
                        OringoClient.modules.add(keybind);
                        MinecraftForge.EVENT_BUS.register((Object)keybind);
                        keybind.setToggled(true);
                        break;
                    }
                    case DRAW: {
                        RenderUtils.drawRect(p.x, y, p.x + p.width, y + 15.0f, toggled);
                        Fonts.robotoMediumBold.drawSmoothCenteredString("Add new keybind", p.x + p.width / 2.0f, y + 7.5f - Fonts.robotoMediumBold.getHeight() / 2.0f, Color.white.getRGB());
                        break;
                    }
                }
                y += 15.0f;
            }
            if (handle == Handle.DRAW) {
                RenderUtils.drawRect(p.x, p.y + 3.0f, p.x + p.width, p.y + p.height + 3.0f, new Color(toggled).getRGB());
                for (int i = 1; i < 4; ++i) {
                    RenderUtils.drawRect(p.x, p.y + i, p.x + p.width, p.y + p.height + i, new Color(0, 0, 0, 40).getRGB());
                }
                RenderUtils.drawRect(p.x - 1.0f, p.y, p.x + p.width + 1.0f, p.y + p.height, new Color(toggled).darker().getRGB());
                Fonts.robotoBig.drawSmoothCenteredString(p.category.name, p.x + p.width / 2.0f, p.y + p.height / 2.0f - Fonts.robotoBig.getHeight() / 2.0f, Color.white.getRGB());
                RenderUtils.drawRect(p.x - 1.0f, y, p.x + p.width + 1.0f, y + 5.0f, new Color(toggled).darker().getRGB());
            }
            else {
                if (handle != Handle.SCROLL || !this.isHovered(mouseX, mouseY, p.x, p.y, y - p.y, p.width)) {
                    continue;
                }
                if (key == -1) {
                    final Panel panel = p;
                    --panel.scrolling;
                }
                else if (key == 1) {
                    final Panel panel2 = p;
                    ++panel2.scrolling;
                }
                p.scrolling = Math.min(0, Math.max(p.scrolling, -this.getTotalSettingsCount(p)));
            }
        }
        GL11.glPopMatrix();
        this.field_146297_k.field_71474_y.field_74335_Z = prevScale;
    }
    
    public void func_146281_b() {
        this.draggingPanel = null;
        this.draggingSlider = null;
        this.binding = null;
        this.settingString = null;
        super.func_146281_b();
    }
    
    public boolean isHovered(final int mouseX, final int mouseY, final double x, final double y, final double height, final double width) {
        return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
    }
    
    private int getTotalSettingsCount(final Panel panel) {
        int count = 0;
        for (final Module module : Module.getModulesByCategory(panel.category)) {
            if (module.isDevOnly() && !OringoClient.devMode) {
                continue;
            }
            ++count;
            if (!module.extended) {
                continue;
            }
            for (final Setting setting : module.settings) {
                if (!setting.isHidden()) {
                    ++count;
                }
            }
            ++count;
        }
        return count;
    }
    
    public boolean func_73868_f() {
        return false;
    }
    
    static {
        ClickGUI.background = Color.getHSBColor(0.0f, 0.0f, 0.1f).getRGB();
    }
    
    enum Handle
    {
        DRAW, 
        CLICK, 
        RELEASE, 
        SCROLL;
    }
    
    public static class Panel
    {
        public Module.Category category;
        public float x;
        public float y;
        public float width;
        public float height;
        public boolean dragging;
        public boolean extended;
        public int scrolling;
        public int prevScrolling;
        
        public Panel(final Module.Category category, final float x, final float y, final float width, final float height) {
            this.category = category;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.extended = true;
            this.dragging = false;
        }
    }
}
