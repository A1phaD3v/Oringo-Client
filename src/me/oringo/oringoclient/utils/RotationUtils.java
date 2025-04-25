// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.utils;

import net.minecraft.entity.Entity;
import me.oringo.oringoclient.mixins.entity.PlayerSPAccessor;
import net.minecraft.util.MathHelper;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import java.util.Random;
import net.minecraft.entity.EntityLivingBase;
import me.oringo.oringoclient.OringoClient;
import net.minecraft.util.AxisAlignedBB;

public class RotationUtils
{
    public static float lastLastReportedPitch;
    
    private RotationUtils() {
    }
    
    public static Rotation getClosestRotation(final AxisAlignedBB aabb) {
        return getRotations(getClosestPointInAABB(OringoClient.mc.field_71439_g.func_174824_e(1.0f), aabb));
    }
    
    public static Rotation getClosestRotation(final AxisAlignedBB aabb, final float offset) {
        return getClosestRotation(aabb.func_72314_b((double)(-offset), (double)(-offset), (double)(-offset)));
    }
    
    public static Rotation getRotations(final EntityLivingBase target) {
        return getRotations(target.field_70165_t, target.field_70163_u + target.func_70047_e() / 2.0, target.field_70161_v);
    }
    
    public static Rotation getRotations(final EntityLivingBase target, final float random) {
        return getRotations(target.field_70165_t + (new Random().nextInt(3) - 1) * random * new Random().nextFloat(), target.field_70163_u + target.func_70047_e() / 2.0 + (new Random().nextInt(3) - 1) * random * new Random().nextFloat(), target.field_70161_v + (new Random().nextInt(3) - 1) * random * new Random().nextFloat());
    }
    
    public static double getRotationDifference(final Rotation a, final Rotation b) {
        return Math.hypot(getAngleDifference(a.getYaw(), b.getYaw()), getAngleDifference(a.getPitch(), b.getPitch()));
    }
    
    public static Rotation getRotations(final Vec3 vec3) {
        return getRotations(vec3.field_72450_a, vec3.field_72448_b, vec3.field_72449_c);
    }
    
    public static Rotation getScaffoldRotations(final BlockPos position) {
        final double direction = MovementUtils.getDirection();
        final double posX = -Math.sin(direction) * 0.5;
        final double posZ = Math.cos(direction) * 0.5;
        final double x = position.func_177958_n() - OringoClient.mc.field_71439_g.field_70165_t - posX;
        final double y = position.func_177956_o() - OringoClient.mc.field_71439_g.field_70167_r - OringoClient.mc.field_71439_g.func_70047_e();
        final double z = position.func_177952_p() - OringoClient.mc.field_71439_g.field_70161_v - posZ;
        final double distance = Math.hypot(x, z);
        final float yaw = (float)(Math.atan2(z, x) * 180.0 / 3.141592653589793 - 90.0);
        final float pitch = (float)(-(Math.atan2(y, distance) * 180.0 / 3.141592653589793));
        return new Rotation(yaw, pitch);
    }
    
    public static Rotation getRotations(final double posX, final double posY, final double posZ) {
        final double x = posX - OringoClient.mc.field_71439_g.field_70165_t;
        final double y = posY - (OringoClient.mc.field_71439_g.field_70163_u + OringoClient.mc.field_71439_g.func_70047_e());
        final double z = posZ - OringoClient.mc.field_71439_g.field_70161_v;
        final double dist = MathHelper.func_76133_a(x * x + z * z);
        final float yaw = (float)(Math.atan2(z, x) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(y, dist) * 180.0 / 3.141592653589793));
        return new Rotation(yaw, pitch);
    }
    
    public static Rotation getSmoothRotation(final Rotation current, final Rotation target, final float smooth) {
        return new Rotation(current.getYaw() + (target.getYaw() - current.getYaw()) / smooth, current.getPitch() + (target.getPitch() - current.getPitch()) / smooth);
    }
    
    public static Rotation getLastReportedRotation() {
        return new Rotation(((PlayerSPAccessor)OringoClient.mc.field_71439_g).getLastReportedYaw(), ((PlayerSPAccessor)OringoClient.mc.field_71439_g).getLastReportedPitch());
    }
    
    public static Rotation getPlayerRotation() {
        return new Rotation(OringoClient.mc.field_71439_g.field_70177_z, OringoClient.mc.field_71439_g.field_70125_A);
    }
    
    public static Rotation getLimitedRotation(final Rotation currentRotation, final Rotation targetRotation, final float turnSpeed) {
        return new Rotation(currentRotation.getYaw() + MathHelper.func_76131_a(getAngleDifference(targetRotation.getYaw(), currentRotation.getYaw()), -turnSpeed, turnSpeed), currentRotation.getPitch() + MathHelper.func_76131_a(getAngleDifference(targetRotation.getPitch(), currentRotation.getPitch()), -turnSpeed, turnSpeed));
    }
    
    public static float getAngleDifference(final float a, final float b) {
        return ((a - b) % 360.0f + 540.0f) % 360.0f - 180.0f;
    }
    
    public static Rotation getBowRotation(final Entity entity) {
        final double xDelta = (entity.field_70165_t - entity.field_70142_S) * 0.4;
        final double zDelta = (entity.field_70161_v - entity.field_70136_U) * 0.4;
        double d = OringoClient.mc.field_71439_g.func_70032_d(entity);
        d -= d % 0.8;
        final double xMulti = d / 0.8 * xDelta;
        final double zMulti = d / 0.8 * zDelta;
        final double x = entity.field_70165_t + xMulti - OringoClient.mc.field_71439_g.field_70165_t;
        final double z = entity.field_70161_v + zMulti - OringoClient.mc.field_71439_g.field_70161_v;
        final double y = OringoClient.mc.field_71439_g.field_70163_u + OringoClient.mc.field_71439_g.func_70047_e() - (entity.field_70163_u + entity.func_70047_e());
        final double dist = OringoClient.mc.field_71439_g.func_70032_d(entity);
        final float yaw = (float)Math.toDegrees(Math.atan2(z, x)) - 90.0f;
        final double d2 = MathHelper.func_76133_a(x * x + z * z);
        final float pitch = (float)(-(Math.atan2(y, d2) * 180.0 / 3.141592653589793)) + (float)dist * 0.11f;
        return new Rotation(yaw, -pitch);
    }
    
    public static Vec3 getClosestPointInAABB(final Vec3 vec3, final AxisAlignedBB aabb) {
        return new Vec3(clamp(aabb.field_72340_a, aabb.field_72336_d, vec3.field_72450_a), clamp(aabb.field_72338_b, aabb.field_72337_e, vec3.field_72448_b), clamp(aabb.field_72339_c, aabb.field_72334_f, vec3.field_72449_c));
    }
    
    private static double clamp(final double min, final double max, final double value) {
        return Math.max(min, Math.min(max, value));
    }
}
