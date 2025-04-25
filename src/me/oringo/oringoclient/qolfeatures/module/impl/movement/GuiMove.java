// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.movement;

import me.oringo.oringoclient.utils.SkyblockUtils;
import net.minecraft.inventory.ContainerChest;
import org.lwjgl.opengl.Display;
import org.lwjgl.input.Mouse;
import org.lwjgl.input.Keyboard;
import net.minecraft.inventory.ICrafting;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.settings.GameSettings;
import me.oringo.oringoclient.qolfeatures.module.impl.other.Disabler;
import net.minecraft.client.gui.GuiChat;
import me.oringo.oringoclient.events.PostGuiOpenEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import net.minecraft.client.settings.KeyBinding;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class GuiMove extends Module
{
    private BooleanSetting rotate;
    private BooleanSetting drag;
    private BooleanSetting hideTerminalGui;
    private NumberSetting sensivity;
    public static final KeyBinding[] binds;
    
    public GuiMove() {
        super("InvMove", Category.MOVEMENT);
        this.rotate = new BooleanSetting("Rotate", true);
        this.drag = new BooleanSetting("Alt drag", true) {
            @Override
            public boolean isHidden() {
                return !GuiMove.this.rotate.isEnabled();
            }
        };
        this.hideTerminalGui = new BooleanSetting("Hide terminals", false);
        this.sensivity = new NumberSetting("Sensivity", 1.5, 0.1, 3.0, 0.01, aBoolean -> !this.rotate.isEnabled());
        this.addSettings(this.hideTerminalGui, this.rotate, this.sensivity, this.drag);
    }
    
    @Override
    public boolean isToggled() {
        return super.isToggled();
    }
    
    @Override
    public void onDisable() {
        if (GuiMove.mc.field_71462_r != null) {
            for (final KeyBinding bind : GuiMove.binds) {
                KeyBinding.func_74510_a(bind.func_151463_i(), false);
            }
        }
    }
    
    @SubscribeEvent
    public void onGUi(final PostGuiOpenEvent event) {
        if (!(event.gui instanceof GuiChat) && this.isToggled() && Disabler.wasEnabled) {
            for (final KeyBinding bind : GuiMove.binds) {
                KeyBinding.func_74510_a(bind.func_151463_i(), GameSettings.func_100015_a(bind));
            }
        }
    }
    
    @SubscribeEvent
    public void onRender(final RenderWorldLastEvent event) {
        if (GuiMove.mc.field_71462_r != null && !(GuiMove.mc.field_71462_r instanceof GuiChat) && this.isToggled() && Disabler.wasEnabled) {
            for (final KeyBinding bind : GuiMove.binds) {
                KeyBinding.func_74510_a(bind.func_151463_i(), GameSettings.func_100015_a(bind));
            }
            if ((GuiMove.mc.field_71462_r instanceof GuiContainer || GuiMove.mc.field_71462_r instanceof ICrafting) && this.rotate.isEnabled()) {
                GuiMove.mc.field_71417_B.func_74374_c();
                float f = GuiMove.mc.field_71474_y.field_74341_c * 0.6f + 0.2f;
                f *= (float)this.sensivity.getValue();
                final float f2 = f * f * f * 8.0f;
                final float f3 = GuiMove.mc.field_71417_B.field_74377_a * f2;
                final float f4 = GuiMove.mc.field_71417_B.field_74375_b * f2;
                int i = 1;
                if (GuiMove.mc.field_71474_y.field_74338_d) {
                    i = -1;
                }
                if (Keyboard.isKeyDown(56) && Mouse.isButtonDown(2) && this.drag.isEnabled()) {
                    Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 6);
                    GuiMove.mc.func_71364_i();
                    Mouse.setGrabbed(false);
                }
                GuiMove.mc.field_71439_g.func_70082_c(f3, f4 * i);
            }
        }
    }
    
    public boolean shouldHideGui(final ContainerChest chest) {
        return SkyblockUtils.isTerminal(chest.func_85151_d().func_70005_c_()) && this.isToggled() && this.hideTerminalGui.isEnabled();
    }
    
    static {
        binds = new KeyBinding[] { GuiMove.mc.field_71474_y.field_74311_E, GuiMove.mc.field_71474_y.field_74314_A, GuiMove.mc.field_71474_y.field_151444_V, GuiMove.mc.field_71474_y.field_74351_w, GuiMove.mc.field_71474_y.field_74368_y, GuiMove.mc.field_71474_y.field_74370_x, GuiMove.mc.field_71474_y.field_74366_z };
    }
}
