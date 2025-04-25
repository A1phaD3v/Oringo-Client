// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.combat;

import me.oringo.oringoclient.utils.RotationUtils;
import me.oringo.oringoclient.events.MoveFlyingEvent;
import java.awt.Color;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.opengl.GL11;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.Entity;
import me.oringo.oringoclient.utils.PlayerUtils;
import me.oringo.oringoclient.qolfeatures.module.impl.movement.GuiMove;
import me.oringo.oringoclient.events.MoveStateUpdateEvent;
import me.oringo.oringoclient.qolfeatures.module.impl.movement.Speed;
import me.oringo.oringoclient.OringoClient;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.utils.MilliTimer;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.ModeSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class TargetStrafe extends Module
{
    public ModeSetting mode;
    public NumberSetting distance;
    public BooleanSetting controllable;
    public BooleanSetting jumpOnly;
    public BooleanSetting thirdPerson;
    public BooleanSetting smart;
    public BooleanSetting liquidCheck;
    public BooleanSetting voidCheck;
    private MilliTimer strafeDelay;
    private int prev;
    double lastx;
    double lasty;
    private float strafe;
    
    public TargetStrafe() {
        super("Target Strafe", Category.COMBAT);
        this.mode = new ModeSetting("Mode", "Normal", new String[] { "Normal", "Back" });
        this.distance = new NumberSetting("Distance", 2.0, 1.0, 4.0, 0.1);
        this.controllable = new BooleanSetting("Controllable", true);
        this.jumpOnly = new BooleanSetting("Space only", true);
        this.thirdPerson = new BooleanSetting("Third person", false);
        this.smart = new BooleanSetting("Smart", true);
        this.liquidCheck = new BooleanSetting("Liquid check", false, aBoolean -> !this.smart.isEnabled());
        this.voidCheck = new BooleanSetting("Void check", true, aBoolean -> !this.smart.isEnabled());
        this.strafeDelay = new MilliTimer();
        this.prev = -1;
        this.strafe = 1.0f;
        this.addSettings(this.mode, this.distance, this.thirdPerson, this.smart, this.voidCheck, this.liquidCheck, this.controllable, this.jumpOnly);
    }
    
    public boolean isUsing() {
        return KillAura.target != null && this.isToggled() && (TargetStrafe.mc.field_71474_y.field_74314_A.func_151470_d() || !this.jumpOnly.isEnabled()) && ((OringoClient.speed.isToggled() && !Speed.isDisabled()) || OringoClient.fly.isFlying());
    }
    
    @SubscribeEvent
    public void onMove(final MoveStateUpdateEvent event) {
        if (this.isUsing() && (TargetStrafe.mc.field_71462_r == null || Module.getModule(GuiMove.class).isToggled())) {
            if (this.thirdPerson.isEnabled()) {
                if (this.prev == -1) {
                    this.prev = TargetStrafe.mc.field_71474_y.field_74320_O;
                }
                TargetStrafe.mc.field_71474_y.field_74320_O = 1;
            }
            if (this.controllable.isEnabled() && (TargetStrafe.mc.field_71474_y.field_74366_z.func_151470_d() || TargetStrafe.mc.field_71474_y.field_74370_x.func_151470_d())) {
                if (TargetStrafe.mc.field_71474_y.field_74370_x.func_151470_d()) {
                    this.strafe = 1.0f;
                }
                if (TargetStrafe.mc.field_71474_y.field_74366_z.func_151470_d()) {
                    this.strafe = -1.0f;
                }
            }
            else if (this.strafeDelay.hasTimePassed(200L)) {
                if (TargetStrafe.mc.field_71439_g.field_70123_F || (this.smart.isEnabled() && !OringoClient.fly.isFlying() && ((this.voidCheck.isEnabled() && TargetStrafe.mc.field_71439_g.field_70143_R < 2.5 && PlayerUtils.isFall(6.0f, (TargetStrafe.mc.field_71439_g.field_70165_t - TargetStrafe.mc.field_71439_g.field_70169_q) * 2.5, (TargetStrafe.mc.field_71439_g.field_70161_v - TargetStrafe.mc.field_71439_g.field_70166_s) * 2.5)) || (this.liquidCheck.isEnabled() && !TargetStrafe.mc.field_71439_g.func_180799_ab() && !TargetStrafe.mc.field_71439_g.func_70090_H() && PlayerUtils.isLiquid(3.0f, (TargetStrafe.mc.field_71439_g.field_70165_t - TargetStrafe.mc.field_71439_g.field_70169_q) * 2.5, (TargetStrafe.mc.field_71439_g.field_70161_v - TargetStrafe.mc.field_71439_g.field_70166_s) * 2.5))))) {
                    this.strafe = -this.strafe;
                    this.strafeDelay.reset();
                }
                else if (this.mode.is("Back")) {
                    final Entity entity = (Entity)KillAura.target;
                    final float yaw = (entity.field_70177_z - 90.0f) % 360.0f;
                    final double x = Math.cos(yaw * 3.141592653589793 / 180.0) * this.distance.getValue() + entity.field_70165_t;
                    final double z = Math.sin(yaw * 3.141592653589793 / 180.0) * this.distance.getValue() + entity.field_70161_v;
                    if (this.getDistance(x, z, TargetStrafe.mc.field_71439_g.field_70165_t, TargetStrafe.mc.field_71439_g.field_70161_v) > 0.2 && this.getDistance(x, z, TargetStrafe.mc.field_71439_g.field_70165_t, TargetStrafe.mc.field_71439_g.field_70161_v) > this.getDistance(this.lastx, this.lasty, x, z)) {
                        this.strafe = -this.strafe;
                        this.strafeDelay.reset();
                    }
                    this.lastx = TargetStrafe.mc.field_71439_g.field_70165_t;
                    this.lasty = TargetStrafe.mc.field_71439_g.field_70161_v;
                }
            }
            if (this.getDistance((Entity)KillAura.target) <= this.distance.getValue() + 2.0 || (this.controllable.isEnabled() && (TargetStrafe.mc.field_71474_y.field_74366_z.func_151470_d() || TargetStrafe.mc.field_71474_y.field_74370_x.func_151470_d()))) {
                event.setStrafe(this.strafe);
            }
            event.setForward(TargetStrafe.mc.field_71474_y.field_74368_y.func_151470_d() ? -1.0f : ((float)((this.getDistance((Entity)KillAura.target) > this.distance.getValue()) ? 1 : 0)));
        }
        else if (this.thirdPerson.isEnabled() && this.prev != -1) {
            TargetStrafe.mc.field_71474_y.field_74320_O = this.prev;
            this.prev = -1;
        }
    }
    
    @SubscribeEvent
    public void onRender(final RenderWorldLastEvent event) {
        if (this.isUsing()) {
            final Entity entity = (Entity)KillAura.target;
            final float partialTicks = event.partialTicks;
            GL11.glPushMatrix();
            GL11.glEnable(3042);
            OpenGlHelper.func_148821_a(770, 771, 1, 0);
            GL11.glEnable(2848);
            GL11.glHint(3154, 4354);
            GL11.glDisable(3553);
            GL11.glDisable(2884);
            GL11.glDisable(2929);
            GL11.glShadeModel(7425);
            GlStateManager.func_179140_f();
            GL11.glTranslated(entity.field_70142_S + (entity.field_70165_t - entity.field_70142_S) * partialTicks - TargetStrafe.mc.func_175598_ae().field_78730_l, entity.field_70137_T + (entity.field_70163_u - entity.field_70137_T) * partialTicks - TargetStrafe.mc.func_175598_ae().field_78731_m + 0.1, entity.field_70136_U + (entity.field_70161_v - entity.field_70136_U) * partialTicks - TargetStrafe.mc.func_175598_ae().field_78728_n);
            final double radius = this.distance.getValue();
            GL11.glLineWidth(4.0f);
            GL11.glBegin(2);
            for (int angles = 90, i = 0; i <= angles; ++i) {
                final Color color = Color.white;
                GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 1.0f);
                GL11.glVertex3d(Math.cos(i * 3.141592653589793 / (angles / 2.0)) * radius, 0.0, Math.sin(i * 3.141592653589793 / (angles / 2.0)) * radius);
            }
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glShadeModel(7424);
            GL11.glEnable(2929);
            GL11.glEnable(2884);
            GlStateManager.func_179117_G();
            GL11.glEnable(3553);
            GL11.glDisable(3042);
            GL11.glDisable(2848);
        }
    }
    
    double getDistance(final double x, final double z, final double x1, final double z1) {
        final double x2 = x1 - x;
        final double z2 = z1 - z;
        return Math.sqrt(x2 * x2 + z2 * z2);
    }
    
    double getDistance(final Entity entity) {
        return Math.hypot(entity.field_70165_t - TargetStrafe.mc.field_71439_g.field_70165_t, entity.field_70161_v - TargetStrafe.mc.field_71439_g.field_70161_v);
    }
    
    @SubscribeEvent
    public void onMoveFly(final MoveFlyingEvent event) {
        if (this.isUsing()) {
            event.setYaw(RotationUtils.getRotations(KillAura.target).getYaw());
        }
    }
}
