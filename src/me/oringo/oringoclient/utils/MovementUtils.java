// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.utils;

import me.oringo.oringoclient.qolfeatures.module.impl.combat.KillAura;
import me.oringo.oringoclient.events.MoveEvent;
import net.minecraft.entity.Entity;
import me.oringo.oringoclient.OringoClient;

public class MovementUtils
{
    public static MilliTimer strafeTimer;
    
    public static float getSpeed() {
        return (float)Math.sqrt(OringoClient.mc.field_71439_g.field_70159_w * OringoClient.mc.field_71439_g.field_70159_w + OringoClient.mc.field_71439_g.field_70179_y * OringoClient.mc.field_71439_g.field_70179_y);
    }
    
    public static float getSpeed(final double x, final double z) {
        return (float)Math.sqrt(x * x + z * z);
    }
    
    public static void strafe() {
        strafe(getSpeed());
    }
    
    public static boolean isMoving() {
        return OringoClient.mc.field_71439_g.field_70701_bs != 0.0f || OringoClient.mc.field_71439_g.field_70702_br != 0.0f;
    }
    
    public static boolean hasMotion() {
        return OringoClient.mc.field_71439_g.field_70159_w != 0.0 && OringoClient.mc.field_71439_g.field_70179_y != 0.0 && OringoClient.mc.field_71439_g.field_70181_x != 0.0;
    }
    
    public static boolean isOnGround(final double height) {
        return !OringoClient.mc.field_71441_e.func_72945_a((Entity)OringoClient.mc.field_71439_g, OringoClient.mc.field_71439_g.func_174813_aQ().func_72317_d(0.0, -height, 0.0)).isEmpty();
    }
    
    public static void strafe(final double speed) {
        if (!isMoving()) {
            return;
        }
        final double yaw = getDirection();
        OringoClient.mc.field_71439_g.field_70159_w = -Math.sin(yaw) * speed;
        OringoClient.mc.field_71439_g.field_70179_y = Math.cos(yaw) * speed;
        MovementUtils.strafeTimer.reset();
    }
    
    public static void strafe(final float speed, final float yaw) {
        if (!isMoving() || !MovementUtils.strafeTimer.hasTimePassed(150L)) {
            return;
        }
        OringoClient.mc.field_71439_g.field_70159_w = -Math.sin(Math.toRadians(yaw)) * speed;
        OringoClient.mc.field_71439_g.field_70179_y = Math.cos(Math.toRadians(yaw)) * speed;
        MovementUtils.strafeTimer.reset();
    }
    
    public static void forward(final double length) {
        final double yaw = Math.toRadians(OringoClient.mc.field_71439_g.field_70177_z);
        OringoClient.mc.field_71439_g.func_70107_b(OringoClient.mc.field_71439_g.field_70165_t + -Math.sin(yaw) * length, OringoClient.mc.field_71439_g.field_70163_u, OringoClient.mc.field_71439_g.field_70161_v + Math.cos(yaw) * length);
    }
    
    public static double getDirection() {
        return Math.toRadians(getYaw());
    }
    
    public static void setMotion(final MoveEvent em, final double speed) {
        double forward = OringoClient.mc.field_71439_g.field_71158_b.field_78900_b;
        double strafe = OringoClient.mc.field_71439_g.field_71158_b.field_78902_a;
        float yaw = ((KillAura.target != null && OringoClient.killAura.movementFix.isEnabled()) || OringoClient.targetStrafe.isUsing()) ? RotationUtils.getRotations(KillAura.target).getYaw() : OringoClient.mc.field_71439_g.field_70177_z;
        if (forward == 0.0 && strafe == 0.0) {
            OringoClient.mc.field_71439_g.field_70159_w = 0.0;
            OringoClient.mc.field_71439_g.field_70179_y = 0.0;
            if (em != null) {
                em.setX(0.0);
                em.setZ(0.0);
            }
        }
        else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += ((forward > 0.0) ? -45 : 45);
                }
                else if (strafe < 0.0) {
                    yaw += ((forward > 0.0) ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                }
                else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            final double cos = Math.cos(Math.toRadians(yaw + 90.0f));
            final double sin = Math.sin(Math.toRadians(yaw + 90.0f));
            OringoClient.mc.field_71439_g.field_70159_w = forward * speed * cos + strafe * speed * sin;
            OringoClient.mc.field_71439_g.field_70179_y = forward * speed * sin - strafe * speed * cos;
            if (em != null) {
                em.setX(OringoClient.mc.field_71439_g.field_70159_w);
                em.setZ(OringoClient.mc.field_71439_g.field_70179_y);
            }
        }
    }
    
    public static float getYaw() {
        float yaw = ((KillAura.target != null && OringoClient.killAura.movementFix.isEnabled()) || OringoClient.targetStrafe.isUsing()) ? RotationUtils.getRotations(KillAura.target).getYaw() : OringoClient.mc.field_71439_g.field_70177_z;
        if (OringoClient.mc.field_71439_g.field_70701_bs < 0.0f) {
            yaw += 180.0f;
        }
        float forward = 1.0f;
        if (OringoClient.mc.field_71439_g.field_70701_bs < 0.0f) {
            forward = -0.5f;
        }
        else if (OringoClient.mc.field_71439_g.field_70701_bs > 0.0f) {
            forward = 0.5f;
        }
        if (OringoClient.mc.field_71439_g.field_70702_br > 0.0f) {
            yaw -= 90.0f * forward;
        }
        if (OringoClient.mc.field_71439_g.field_70702_br < 0.0f) {
            yaw += 90.0f * forward;
        }
        return yaw;
    }
    
    static {
        MovementUtils.strafeTimer = new MilliTimer();
    }
}
