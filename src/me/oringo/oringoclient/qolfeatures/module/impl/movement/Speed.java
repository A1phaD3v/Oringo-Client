// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.movement;

import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import me.oringo.oringoclient.events.PacketReceivedEvent;
import net.minecraft.stats.StatList;
import net.minecraft.util.MathHelper;
import me.oringo.oringoclient.ui.notifications.Notifications;
import me.oringo.oringoclient.utils.font.Fonts;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import me.oringo.oringoclient.utils.TimerUtil;
import me.oringo.oringoclient.OringoClient;
import me.oringo.oringoclient.events.MoveHeadingEvent;
import me.oringo.oringoclient.events.MoveStateUpdateEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.potion.Potion;
import me.oringo.oringoclient.events.MoveEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.oringo.oringoclient.utils.MovementUtils;
import me.oringo.oringoclient.mixins.MinecraftAccessor;
import me.oringo.oringoclient.events.MotionUpdateEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.utils.MilliTimer;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class Speed extends Module
{
    public BooleanSetting stopOnDisable;
    public BooleanSetting disableOnFlag;
    public BooleanSetting sneak;
    public NumberSetting timer;
    public NumberSetting sneakTimer;
    private static MilliTimer disable;
    int airTicks;
    boolean canApplySpeed;
    
    public Speed() {
        super("Speed", Category.MOVEMENT);
        this.stopOnDisable = new BooleanSetting("Stop on disable", true);
        this.disableOnFlag = new BooleanSetting("Disable on flag", true);
        this.sneak = new BooleanSetting("Sneak timer", true);
        this.timer = new NumberSetting("Timer", 1.0, 0.1, 3.0, 0.05);
        this.sneakTimer = new NumberSetting("SneakTimer", 1.0, 0.1, 3.0, 0.05, aBoolean -> !this.sneak.isEnabled());
        this.addSettings(this.stopOnDisable, this.disableOnFlag, this.sneak, this.sneakTimer, this.timer);
    }
    
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onUpdate(final MotionUpdateEvent.Pre event) {
        if (this.isToggled() && !isDisabled()) {
            ((MinecraftAccessor)Speed.mc).getTimer().field_74278_d = (float)((this.sneak.isEnabled() && Speed.mc.field_71474_y.field_74311_E.func_151470_d()) ? this.sneakTimer.getValue() : this.timer.getValue());
            if (MovementUtils.isMoving()) {
                event.setYaw(MovementUtils.getYaw());
            }
        }
    }
    
    @SubscribeEvent
    public void onMove(final MoveEvent event) {
        if (this.isToggled() && !isDisabled()) {
            if (MovementUtils.isMoving()) {
                double multi = 1.0;
                if (Speed.mc.field_71439_g.func_70644_a(Potion.field_76424_c) && this.canApplySpeed) {
                    multi += 0.015f * (Speed.mc.field_71439_g.func_70660_b(Potion.field_76424_c).func_76458_c() + 1);
                }
                if (Speed.mc.field_71439_g.field_71075_bZ.func_75094_b() > 0.2f) {
                    multi = 0.8999999761581421;
                }
                final EntityPlayerSP field_71439_g = Speed.mc.field_71439_g;
                field_71439_g.field_70159_w *= multi;
                final EntityPlayerSP field_71439_g2 = Speed.mc.field_71439_g;
                field_71439_g2.field_70179_y *= multi;
            }
            else {
                Speed.mc.field_71439_g.field_70159_w = 0.0;
                Speed.mc.field_71439_g.field_70179_y = 0.0;
            }
            event.setX(Speed.mc.field_71439_g.field_70159_w).setZ(Speed.mc.field_71439_g.field_70179_y);
        }
    }
    
    @SubscribeEvent
    public void onUpdateMove(final MoveStateUpdateEvent event) {
        if (this.isToggled() && !isDisabled()) {
            event.setSneak(false);
        }
    }
    
    @SubscribeEvent
    public void onMoveFlying(final MoveHeadingEvent event) {
        if (this.isToggled() && MovementUtils.isMoving() && !isDisabled()) {
            if (Speed.mc.field_71439_g.field_70122_E) {
                this.jump();
                this.canApplySpeed = Speed.mc.field_71439_g.func_70644_a(Potion.field_76424_c);
                this.airTicks = 0;
            }
            else {
                ++this.airTicks;
                event.setOnGround(true);
                if (!Speed.mc.field_71439_g.func_70644_a(Potion.field_76424_c)) {
                    if (!this.canApplySpeed) {
                        if (Speed.mc.field_71439_g.field_70143_R < 0.4 && Speed.mc.field_71439_g.field_71075_bZ.func_75094_b() < 0.2f) {
                            event.setFriction2Multi(0.95f);
                        }
                    }
                    else {
                        event.setFriction2Multi(0.87f);
                    }
                }
            }
        }
    }
    
    public static boolean isDisabled() {
        return (OringoClient.scaffold.isToggled() && Scaffold.disableSpeed.isEnabled()) || !Speed.disable.hasTimePassed(2000L);
    }
    
    private String getBPS() {
        final double bps = Math.hypot(Speed.mc.field_71439_g.field_70165_t - Speed.mc.field_71439_g.field_70169_q, Speed.mc.field_71439_g.field_70161_v - Speed.mc.field_71439_g.field_70166_s) * TimerUtil.getTimer().field_74278_d * 20.0;
        return String.format("%.2f", bps);
    }
    
    @SubscribeEvent
    public void onRender(final RenderGameOverlayEvent.Post event) {
        if (Speed.mc.field_71441_e == null || Speed.mc.field_71439_g == null || !this.isToggled()) {
            return;
        }
        if (event.type == RenderGameOverlayEvent.ElementType.ALL) {
            final ScaledResolution resolution = new ScaledResolution(Speed.mc);
            Fonts.robotoMediumBold.drawSmoothCenteredStringWithShadow(this.getBPS(), 20.0, resolution.func_78328_b() - 20, OringoClient.clickGui.getColor().getRGB());
        }
    }
    
    @Override
    public void onDisable() {
        if (TimerUtil.getTimer() != null) {
            ((MinecraftAccessor)Speed.mc).getTimer().field_74278_d = 1.0f;
            this.canApplySpeed = false;
        }
        if (Speed.mc.field_71439_g != null && this.stopOnDisable.isEnabled()) {
            Speed.mc.field_71439_g.field_70159_w = 0.0;
            Speed.mc.field_71439_g.field_70179_y = 0.0;
        }
    }
    
    @Override
    public void onEnable() {
        this.airTicks = 0;
        if (!OringoClient.disabler.isToggled()) {
            Notifications.showNotification("Disabler not enabled", 3000, Notifications.NotificationType.WARNING);
        }
    }
    
    private void jump() {
        Speed.mc.field_71439_g.field_70181_x = 0.41999998688697815;
        if (Speed.mc.field_71439_g.func_70051_ag()) {
            final float f = MovementUtils.getYaw() * 0.017453292f;
            final EntityPlayerSP field_71439_g = Speed.mc.field_71439_g;
            field_71439_g.field_70159_w -= MathHelper.func_76126_a(f) * 0.2f;
            final EntityPlayerSP field_71439_g2 = Speed.mc.field_71439_g;
            field_71439_g2.field_70179_y += MathHelper.func_76134_b(f) * 0.2f;
        }
        Speed.mc.field_71439_g.field_70160_al = true;
        Speed.mc.field_71439_g.func_71029_a(StatList.field_75953_u);
        if (Speed.mc.field_71439_g.func_70051_ag()) {
            Speed.mc.field_71439_g.func_71020_j(0.8f);
        }
        else {
            Speed.mc.field_71439_g.func_71020_j(0.2f);
        }
    }
    
    @SubscribeEvent(receiveCanceled = true)
    public void onPacket(final PacketReceivedEvent event) {
        if (event.packet instanceof S08PacketPlayerPosLook && this.disableOnFlag.isEnabled()) {
            if (!isDisabled() && this.isToggled()) {
                Notifications.showNotification("Oringo Client", "Disabled speed due to a flag", 1500);
                ((MinecraftAccessor)Speed.mc).getTimer().field_74278_d = 1.0f;
                this.canApplySpeed = false;
                if (Speed.mc.field_71439_g != null) {
                    Speed.mc.field_71439_g.field_70159_w = 0.0;
                    Speed.mc.field_71439_g.field_70179_y = 0.0;
                }
            }
            Speed.disable.reset();
        }
    }
    
    static {
        Speed.disable = new MilliTimer();
    }
}
