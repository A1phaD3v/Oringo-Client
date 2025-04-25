// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.render;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.renderer.WorldRenderer;
import java.util.Iterator;
import me.oringo.oringoclient.utils.font.MinecraftFontRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import me.oringo.oringoclient.utils.shader.BlurUtils;
import java.util.Collections;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Comparator;
import me.oringo.oringoclient.OringoClient;
import java.util.List;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;
import me.oringo.oringoclient.utils.font.Fonts;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import me.oringo.oringoclient.utils.RenderUtils;
import java.awt.Color;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.StringSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.ModeSetting;
import me.oringo.oringoclient.ui.gui.ClickGUI;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class Gui extends Module
{
    public ClickGUI clickGUI;
    public ModeSetting colorMode;
    public NumberSetting redCustom;
    public NumberSetting greenCustom;
    public NumberSetting blueCustom;
    public NumberSetting redShift1;
    public NumberSetting greenShift1;
    public NumberSetting blueShift1;
    public NumberSetting redShift2;
    public NumberSetting greenShift2;
    public NumberSetting blueShift2;
    public NumberSetting shiftSpeed;
    public NumberSetting rgbSpeed;
    public ModeSetting blur;
    public BooleanSetting scaleGui;
    public BooleanSetting arrayList;
    public BooleanSetting disableNotifs;
    public BooleanSetting arrayBlur;
    public BooleanSetting arrayOutline;
    public BooleanSetting waterMark;
    public BooleanSetting hsb;
    public static final StringSetting commandPrefix;
    public static final StringSetting clientName;
    
    public Gui() {
        super("Click Gui", 54, Category.RENDER);
        this.clickGUI = new ClickGUI();
        this.colorMode = new ModeSetting("Mode", "Color shift", new String[] { "Rainbow", "Color shift", "Astolfo", "Pulse", "Custom" });
        this.redCustom = new NumberSetting("Red", 0.0, 0.0, 255.0, 1.0, aBoolean -> !this.colorMode.is("Custom") && !this.colorMode.is("Pulse"));
        this.greenCustom = new NumberSetting("Green", 80.0, 0.0, 255.0, 1.0, aBoolean -> !this.colorMode.is("Custom") && !this.colorMode.is("Pulse"));
        this.blueCustom = new NumberSetting("Blue", 255.0, 0.0, 255.0, 1.0, aBoolean -> !this.colorMode.is("Custom") && !this.colorMode.is("Pulse"));
        this.redShift1 = new NumberSetting("Red 1 ", 0.0, 0.0, 255.0, 1.0, aBoolean -> !this.colorMode.is("Color shift"));
        this.greenShift1 = new NumberSetting("Green 1 ", 255.0, 0.0, 255.0, 1.0, aBoolean -> !this.colorMode.is("Color shift"));
        this.blueShift1 = new NumberSetting("Blue 1 ", 110.0, 0.0, 255.0, 1.0, aBoolean -> !this.colorMode.is("Color shift"));
        this.redShift2 = new NumberSetting("Red 2 ", 255.0, 0.0, 255.0, 1.0, aBoolean -> !this.colorMode.is("Color shift"));
        this.greenShift2 = new NumberSetting("Green 2 ", 175.0, 0.0, 255.0, 1.0, aBoolean -> !this.colorMode.is("Color shift"));
        this.blueShift2 = new NumberSetting("Blue 2 ", 255.0, 0.0, 255.0, 1.0, aBoolean -> !this.colorMode.is("Color shift"));
        this.shiftSpeed = new NumberSetting("Shift Speed ", 1.0, 0.1, 5.0, 0.1, aBoolean -> !this.colorMode.is("Color shift") && !this.colorMode.is("Pulse") && !this.colorMode.is("Astolfo"));
        this.rgbSpeed = new NumberSetting("Rainbow Speed", 2.5, 0.1, 5.0, 0.1, aBoolean -> !this.colorMode.is("Rainbow"));
        this.blur = new ModeSetting("Blur strength", "Low", new String[] { "None", "Low", "High" });
        this.scaleGui = new BooleanSetting("Scale gui", false);
        this.arrayList = new BooleanSetting("ArrayList", true);
        this.disableNotifs = new BooleanSetting("Disable notifications", false);
        this.arrayBlur = new BooleanSetting("Array background", true);
        this.arrayOutline = new BooleanSetting("Array line", true);
        this.waterMark = new BooleanSetting("Watermark", true);
        this.hsb = new BooleanSetting("HSB ", true, aBoolean -> !this.colorMode.is("Color shift"));
        this.addSettings(this.colorMode, this.hsb, this.rgbSpeed, this.shiftSpeed, this.redCustom, this.greenCustom, this.blueCustom, this.redShift1, this.greenShift1, this.blueShift1, this.redShift2, this.greenShift2, this.blueShift2, Gui.commandPrefix, this.blur, this.waterMark, Gui.clientName, this.arrayList, this.arrayOutline, this.arrayBlur, this.disableNotifs, this.scaleGui);
    }
    
    public Color getColor() {
        return this.getColor(0);
    }
    
    public Color getColor(final int index) {
        final String selected = this.colorMode.getSelected();
        switch (selected) {
            case "Color shift": {
                final float location = (float)((Math.cos((index * 450.0 + System.currentTimeMillis() * this.shiftSpeed.getValue()) / 1000.0) + 1.0) * 0.5);
                if (!this.hsb.isEnabled()) {
                    return new Color((int)(this.redShift1.getValue() + (this.redShift2.getValue() - this.redShift1.getValue()) * location), (int)(this.greenShift1.getValue() + (this.greenShift2.getValue() - this.greenShift1.getValue()) * location), (int)(this.blueShift1.getValue() + (this.blueShift2.getValue() - this.blueShift1.getValue()) * location));
                }
                final float[] c1 = Color.RGBtoHSB((int)this.redShift1.getValue(), (int)this.greenShift1.getValue(), (int)this.blueShift1.getValue(), null);
                final float[] c2 = Color.RGBtoHSB((int)this.redShift2.getValue(), (int)this.greenShift2.getValue(), (int)this.blueShift2.getValue(), null);
                return Color.getHSBColor(c1[0] + (c2[0] - c1[0]) * location, c1[1] + (c2[1] - c1[1]) * location, c1[2] + (c2[2] - c1[2]) * location);
            }
            case "Rainbow": {
                return Color.getHSBColor((float)((index * 100.0 + System.currentTimeMillis() * this.rgbSpeed.getValue()) / 5000.0 % 1.0), 0.8f, 1.0f);
            }
            case "Pulse": {
                final Color baseColor = new Color((int)this.redCustom.getValue(), (int)this.greenCustom.getValue(), (int)this.blueCustom.getValue(), 255);
                return RenderUtils.interpolateColor(baseColor, baseColor.darker().darker(), (float)((Math.sin((index * 450.0 + System.currentTimeMillis() * this.shiftSpeed.getValue()) / 1000.0) + 1.0) * 0.5));
            }
            case "Astolfo": {
                final float pos = (float)((Math.cos((index * 450.0 + System.currentTimeMillis() * this.shiftSpeed.getValue()) / 1000.0) + 1.0) * 0.5);
                return Color.getHSBColor(0.5f + 0.4f * pos, 0.6f, 1.0f);
            }
            default: {
                return new Color((int)this.redCustom.getValue(), (int)this.greenCustom.getValue(), (int)this.blueCustom.getValue(), 255);
            }
        }
    }
    
    @SubscribeEvent
    public void onRender(final RenderGameOverlayEvent.Post event) {
        if (event.type == RenderGameOverlayEvent.ElementType.CROSSHAIRS) {
            if (this.waterMark.isEnabled()) {
                if (Gui.clientName.getValue().length() == 0) {
                    Fonts.tahomaBold.drawSmoothString("ringo", Fonts.tahomaBold.drawSmoothString("O", 5.0, 5.0f, this.getColor().getRGB()) + 1.0f, 5.0f, Color.white.getRGB());
                }
                else {
                    final float f = Fonts.tahomaBold.drawSmoothString(String.valueOf(Gui.clientName.getValue().charAt(0)), 5.0, 5.0f, this.getColor().getRGB()) + 1.0f;
                    if (Gui.clientName.getValue().length() > 1) {
                        Fonts.tahomaBold.drawSmoothString(Gui.clientName.getValue().substring(1), f, 5.0f, Color.white.getRGB());
                    }
                }
            }
            if (this.arrayList.isEnabled()) {
                final MinecraftFontRenderer font = Fonts.tahomaSmall;
                GL11.glPushMatrix();
                final ScaledResolution resolution = new ScaledResolution(Gui.mc);
                final List<Module> list = OringoClient.modules.stream().filter(module -> module.getCategory() != Category.RENDER && module.getCategory() != Category.KEYBINDS && (module.isToggled() || module.toggledTime.getTimePassed() <= 250L)).sorted(Comparator.comparingDouble(module -> font.getStringWidth(module.getName()))).collect((Collector<? super Object, ?, List<Module>>)Collectors.toList());
                Collections.reverse(list);
                float y = 2.0f;
                int x = list.size();
                for (final Module module2 : list) {
                    --x;
                    GL11.glPushMatrix();
                    final float width = (float)(font.getStringWidth(module2.getName()) + 5.0);
                    final float translatedWidth = width * Math.max(Math.min(module2.isToggled() ? ((250.0f - module2.toggledTime.getTimePassed()) / 250.0f) : (module2.toggledTime.getTimePassed() / 250.0f), 1.0f), 0.0f);
                    GL11.glTranslated((double)translatedWidth, 0.0, 0.0);
                    final float height = (float)(font.getHeight() + 5);
                    if (this.arrayBlur.isEnabled()) {
                        for (float i = 0.0f; i < 3.0f; i += 0.5f) {
                            RenderUtils.drawRect(resolution.func_78326_a() - 1 - width - i, y + i, (float)resolution.func_78326_a(), y + (font.getHeight() + 5.0f) * Math.max(Math.min(module2.isToggled() ? (module2.toggledTime.getTimePassed() / 250.0f) : ((250.0f - module2.toggledTime.getTimePassed()) / 250.0f), 1.0f), 0.0f) + i, new Color(20, 20, 20, 40).getRGB());
                        }
                        BlurUtils.renderBlurredBackground(10.0f, resolution.func_78326_a() - translatedWidth, (float)resolution.func_78328_b(), resolution.func_78326_a() - 1 - width, y, width, height);
                        RenderUtils.drawRect(resolution.func_78326_a() - 1 - width, y, (float)(resolution.func_78326_a() - 1), y + height, new Color(19, 19, 19, 70).getRGB());
                    }
                    font.drawSmoothCenteredString(module2.getName(), resolution.func_78326_a() - 1 - width / 2.0f + 0.4f, y + height / 2.0f - font.getHeight() / 2.0f + 0.5f, new Color(20, 20, 20).getRGB());
                    font.drawSmoothCenteredString(module2.getName(), resolution.func_78326_a() - 1 - width / 2.0f, y + height / 2.0f - font.getHeight() / 2.0f, this.getColor(x).getRGB(), this.getColor(x - 1).getRGB());
                    y += (font.getHeight() + 5) * Math.max(Math.min(module2.isToggled() ? (module2.toggledTime.getTimePassed() / 250.0f) : ((250.0f - module2.toggledTime.getTimePassed()) / 250.0f), 1.0f), 0.0f);
                    GL11.glPopMatrix();
                }
                x = list.size();
                y = 2.0f;
                if (this.arrayOutline.isEnabled()) {
                    final Tessellator tessellator = Tessellator.func_178181_a();
                    final WorldRenderer worldrenderer = tessellator.func_178180_c();
                    GlStateManager.func_179147_l();
                    GlStateManager.func_179090_x();
                    GlStateManager.func_179120_a(770, 771, 1, 0);
                    GlStateManager.func_179103_j(7425);
                    worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181706_f);
                    for (final Module module3 : list) {
                        --x;
                        final float height = (font.getHeight() + 5.0f) * Math.max(Math.min(module3.isToggled() ? (module3.toggledTime.getTimePassed() / 250.0f) : ((250.0f - module3.toggledTime.getTimePassed()) / 250.0f), 1.0f), 0.0f);
                        addVertex((float)(resolution.func_78326_a() - 1), y, (float)resolution.func_78326_a(), y + height, this.getColor(x - 1).getRGB(), this.getColor(x).getRGB());
                        y += height;
                    }
                    tessellator.func_78381_a();
                    GlStateManager.func_179103_j(7424);
                }
                GlStateManager.func_179098_w();
                GlStateManager.func_179084_k();
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                GL11.glPopMatrix();
            }
        }
    }
    
    public static void addVertex(float left, float top, float right, float bottom, final int color, final int color2) {
        if (left < right) {
            final float i = left;
            left = right;
            right = i;
        }
        if (top < bottom) {
            final float j = top;
            top = bottom;
            bottom = j;
        }
        final float f3 = (color >> 24 & 0xFF) / 255.0f;
        final float f4 = (color >> 16 & 0xFF) / 255.0f;
        final float f5 = (color >> 8 & 0xFF) / 255.0f;
        final float f6 = (color & 0xFF) / 255.0f;
        final float ff3 = (color2 >> 24 & 0xFF) / 255.0f;
        final float ff4 = (color2 >> 16 & 0xFF) / 255.0f;
        final float ff5 = (color2 >> 8 & 0xFF) / 255.0f;
        final float ff6 = (color2 & 0xFF) / 255.0f;
        final Tessellator tessellator = Tessellator.func_178181_a();
        final WorldRenderer worldrenderer = tessellator.func_178180_c();
        worldrenderer.func_181662_b((double)left, (double)bottom, 0.0).func_181666_a(ff4, ff5, ff6, ff3).func_181675_d();
        worldrenderer.func_181662_b((double)right, (double)bottom, 0.0).func_181666_a(ff4, ff5, ff6, ff3).func_181675_d();
        worldrenderer.func_181662_b((double)right, (double)top, 0.0).func_181666_a(f4, f5, f6, f3).func_181675_d();
        worldrenderer.func_181662_b((double)left, (double)top, 0.0).func_181666_a(f4, f5, f6, f3).func_181675_d();
    }
    
    public float getHeight() {
        if (!this.arrayList.isEnabled()) {
            return 0.0f;
        }
        final List<Module> list = OringoClient.modules.stream().filter(module -> module.getCategory() != Category.RENDER && module.getCategory() != Category.KEYBINDS && (module.isToggled() || module.toggledTime.getTimePassed() <= 250L)).sorted(Comparator.comparingDouble(module -> Fonts.tahomaSmall.getStringWidth(module.getName()))).collect((Collector<? super Object, ?, List<Module>>)Collectors.toList());
        float y = 3.0f;
        for (final Module module2 : list) {
            y += (Fonts.tahomaSmall.getHeight() + 5.0f) * Math.max(Math.min(module2.isToggled() ? (module2.toggledTime.getTimePassed() / 250.0f) : ((250.0f - module2.toggledTime.getTimePassed()) / 250.0f), 1.0f), 0.0f);
        }
        return y;
    }
    
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (this.isToggled()) {
            Gui.mc.func_147108_a((GuiScreen)this.clickGUI);
            this.toggle();
        }
        if (Gui.mc.field_71462_r instanceof ClickGUI) {
            KeyBinding.func_74510_a(Gui.mc.field_71474_y.field_74351_w.func_151463_i(), GameSettings.func_100015_a(Gui.mc.field_71474_y.field_74351_w));
            KeyBinding.func_74510_a(Gui.mc.field_71474_y.field_74368_y.func_151463_i(), GameSettings.func_100015_a(Gui.mc.field_71474_y.field_74368_y));
            KeyBinding.func_74510_a(Gui.mc.field_71474_y.field_74370_x.func_151463_i(), GameSettings.func_100015_a(Gui.mc.field_71474_y.field_74370_x));
            KeyBinding.func_74510_a(Gui.mc.field_71474_y.field_74366_z.func_151463_i(), GameSettings.func_100015_a(Gui.mc.field_71474_y.field_74366_z));
            KeyBinding.func_74510_a(Gui.mc.field_71474_y.field_74314_A.func_151463_i(), GameSettings.func_100015_a(Gui.mc.field_71474_y.field_74314_A));
            KeyBinding.func_74510_a(Gui.mc.field_71474_y.field_151444_V.func_151463_i(), GameSettings.func_100015_a(Gui.mc.field_71474_y.field_151444_V));
            KeyBinding.func_74510_a(Gui.mc.field_71474_y.field_74311_E.func_151463_i(), GameSettings.func_100015_a(Gui.mc.field_71474_y.field_74311_E));
        }
    }
    
    static {
        commandPrefix = new StringSetting("Prefix", ".", 1);
        clientName = new StringSetting("Client Name", "", 20);
    }
}
