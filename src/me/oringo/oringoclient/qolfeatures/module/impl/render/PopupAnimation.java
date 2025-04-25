// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.render;

import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiInventory;
import me.oringo.oringoclient.ui.gui.ClickGUI;
import me.oringo.oringoclient.OringoClient;
import net.minecraft.client.gui.GuiScreen;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import me.oringo.oringoclient.utils.MilliTimer;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class PopupAnimation extends Module
{
    public static MilliTimer animationTimer;
    public static MilliTimer lastGuiTimer;
    public static BooleanSetting clickGui;
    public static BooleanSetting inventory;
    public static BooleanSetting chests;
    public static NumberSetting startSize;
    public static NumberSetting time;
    
    public PopupAnimation() {
        super("Popup Animation", Category.RENDER);
        this.setToggled(true);
        this.addSettings(PopupAnimation.clickGui, PopupAnimation.inventory, PopupAnimation.chests, PopupAnimation.startSize, PopupAnimation.time);
    }
    
    public static float getScaling() {
        if (!PopupAnimation.animationTimer.hasTimePassed((long)PopupAnimation.time.getValue())) {
            return (float)(PopupAnimation.animationTimer.getTimePassed() / PopupAnimation.time.getValue() * (1.0 - PopupAnimation.startSize.getValue()) + PopupAnimation.startSize.getValue());
        }
        return 1.0f;
    }
    
    public static boolean shouldScale(final GuiScreen gui) {
        return OringoClient.popupAnimation.isToggled() && ((gui instanceof ClickGUI && PopupAnimation.clickGui.isEnabled()) || (gui instanceof GuiInventory && PopupAnimation.inventory.isEnabled()) || (gui instanceof GuiChest && PopupAnimation.chests.isEnabled()));
    }
    
    @SubscribeEvent
    public void onTick(final RenderWorldLastEvent event) {
        if (PopupAnimation.mc.field_71462_r != null) {
            PopupAnimation.lastGuiTimer.reset();
        }
    }
    
    @SubscribeEvent
    public void onGuiOpen(final GuiOpenEvent event) {
        if (PopupAnimation.mc.field_71462_r == null && PopupAnimation.lastGuiTimer.hasTimePassed(150L)) {
            PopupAnimation.animationTimer.reset();
        }
    }
    
    public static void doScaling() {
        final float scaling = getScaling();
        final ScaledResolution res = new ScaledResolution(PopupAnimation.mc);
        GL11.glTranslated(res.func_78326_a() / 2.0, res.func_78328_b() / 2.0, 0.0);
        GL11.glScaled((double)scaling, (double)scaling, 1.0);
        GL11.glTranslated(-res.func_78326_a() / 2.0, -res.func_78328_b() / 2.0, 0.0);
    }
    
    static {
        PopupAnimation.animationTimer = new MilliTimer();
        PopupAnimation.lastGuiTimer = new MilliTimer();
        PopupAnimation.clickGui = new BooleanSetting("Click Gui", true);
        PopupAnimation.inventory = new BooleanSetting("Inventory", false);
        PopupAnimation.chests = new BooleanSetting("Chests", false);
        PopupAnimation.startSize = new NumberSetting("Starting size", 0.75, 0.01, 1.0, 0.01);
        PopupAnimation.time = new NumberSetting("Time", 200.0, 0.0, 1000.0, 10.0);
    }
}
