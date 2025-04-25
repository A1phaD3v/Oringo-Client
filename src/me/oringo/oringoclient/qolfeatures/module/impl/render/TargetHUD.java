// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.render;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import me.oringo.oringoclient.ui.hud.impl.TargetComponent;
import me.oringo.oringoclient.events.GuiChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.oringo.oringoclient.utils.RenderUtils;
import me.oringo.oringoclient.OringoClient;
import me.oringo.oringoclient.qolfeatures.module.impl.combat.KillAura;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.ModeSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class TargetHUD extends Module
{
    public static TargetHUD instance;
    public ModeSetting blurStrength;
    public BooleanSetting targetESP;
    public NumberSetting x;
    public NumberSetting y;
    
    public static TargetHUD getInstance() {
        return TargetHUD.instance;
    }
    
    public TargetHUD() {
        super("Target HUD", Category.RENDER);
        this.blurStrength = new ModeSetting("Blur Strength", "Low", new String[] { "None", "Low", "High" });
        this.targetESP = new BooleanSetting("Target ESP", true);
        this.x = new NumberSetting("X123", 0.0, -100000.0, 100000.0, 1.0E-5, a -> true);
        this.y = new NumberSetting("Y123", 0.0, -100000.0, 100000.0, 1.0E-5, a -> true);
        this.setToggled(true);
        this.addSettings(this.targetESP, this.blurStrength, this.x, this.y);
    }
    
    @SubscribeEvent
    public void onRender(final RenderWorldLastEvent event) {
        if (KillAura.target != null && KillAura.target.func_110143_aJ() > 0.0f && !KillAura.target.field_70128_L && this.targetESP.isEnabled() && this.isToggled()) {
            RenderUtils.drawTargetESP(KillAura.target, OringoClient.clickGui.getColor(), event.partialTicks);
        }
    }
    
    @SubscribeEvent
    public void onChatEvent(final GuiChatEvent event) {
        if (!this.isToggled()) {
            return;
        }
        final TargetComponent component = TargetComponent.INSTANCE;
        if (event instanceof GuiChatEvent.MouseClicked) {
            if (component.isHovered(event.mouseX, event.mouseY)) {
                component.startDragging();
            }
        }
        else if (event instanceof GuiChatEvent.MouseReleased || event instanceof GuiChatEvent.Closed) {
            component.stopDragging();
        }
    }
    
    @SubscribeEvent
    public void onRender(final RenderGameOverlayEvent.Pre event) {
        if (this.isToggled() && event.type == RenderGameOverlayEvent.ElementType.CROSSHAIRS) {
            TargetComponent.INSTANCE.drawScreen();
        }
    }
    
    static {
        TargetHUD.instance = new TargetHUD();
    }
}
