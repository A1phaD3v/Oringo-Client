// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.utils;

import net.minecraft.util.MovingObjectPosition;
import net.minecraft.entity.Entity;
import net.minecraft.potion.Potion;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import me.oringo.oringoclient.mixins.PlayerControllerAccessor;
import net.minecraft.util.Vec3;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.block.BlockAir;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;
import java.util.Collection;
import net.minecraft.inventory.Slot;
import java.util.ArrayList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import java.util.function.Predicate;
import net.minecraft.entity.player.EntityPlayer;
import me.oringo.oringoclient.OringoClient;

public class PlayerUtils
{
    public static boolean lastGround;
    
    private PlayerUtils() {
    }
    
    public static void swapToSlot(final int slot) {
        OringoClient.mc.field_71439_g.field_71071_by.field_70461_c = slot;
        syncHeldItem();
    }
    
    public static void numberClick(final int slot, final int button) {
        OringoClient.mc.field_71442_b.func_78753_a(OringoClient.mc.field_71439_g.field_71069_bz.field_75152_c, slot, button, 2, (EntityPlayer)OringoClient.mc.field_71439_g);
    }
    
    public static void shiftClick(final int slot) {
        OringoClient.mc.field_71442_b.func_78753_a(OringoClient.mc.field_71439_g.field_71069_bz.field_75152_c, slot, 0, 1, (EntityPlayer)OringoClient.mc.field_71439_g);
    }
    
    public static void drop(final int slot) {
        OringoClient.mc.field_71442_b.func_78753_a(OringoClient.mc.field_71439_g.field_71069_bz.field_75152_c, slot, 1, 4, (EntityPlayer)OringoClient.mc.field_71439_g);
    }
    
    public static int getHotbar(final Predicate<ItemStack> predicate) {
        for (int i = 0; i < 9; ++i) {
            if (OringoClient.mc.field_71439_g.field_71071_by.func_70301_a(i) != null && predicate.test(OringoClient.mc.field_71439_g.field_71071_by.func_70301_a(i))) {
                return i;
            }
        }
        return -1;
    }
    
    public static <T extends Item> int getHotbar(final Class<T> clazz) {
        return getHotbar(stack -> clazz.isAssignableFrom(stack.func_77973_b().getClass()));
    }
    
    public static int getItem(final String name) {
        final List<Slot> slots = new ArrayList<Slot>(OringoClient.mc.field_71439_g.field_71069_bz.field_75151_b);
        Collections.reverse(slots);
        for (final Slot slot : slots) {
            if (slot.func_75216_d() && slot.func_75211_c().func_82833_r().toLowerCase().contains(name.toLowerCase())) {
                return slot.field_75222_d;
            }
        }
        return -1;
    }
    
    public static int getItem(final Predicate<ItemStack> predicate) {
        final List<Slot> slots = new ArrayList<Slot>(OringoClient.mc.field_71439_g.field_71069_bz.field_75151_b);
        Collections.reverse(slots);
        for (final Slot slot : slots) {
            if (slot.func_75216_d() && predicate.test(slot.func_75211_c())) {
                return slot.field_75222_d;
            }
        }
        return -1;
    }
    
    public static <T extends Item> int getItem(final Class<T> clazz) {
        final List<Slot> slots = new ArrayList<Slot>(OringoClient.mc.field_71439_g.field_71069_bz.field_75151_b);
        Collections.reverse(slots);
        for (final Slot slot : slots) {
            if (slot.func_75216_d() && clazz.isAssignableFrom(slot.func_75211_c().func_77973_b().getClass())) {
                return slot.field_75222_d;
            }
        }
        return -1;
    }
    
    public static boolean isInsideBlock() {
        for (int x = MathHelper.func_76128_c(OringoClient.mc.field_71439_g.func_174813_aQ().field_72340_a); x < MathHelper.func_76128_c(OringoClient.mc.field_71439_g.func_174813_aQ().field_72336_d) + 1; ++x) {
            for (int y = MathHelper.func_76128_c(OringoClient.mc.field_71439_g.func_174813_aQ().field_72338_b); y < MathHelper.func_76128_c(OringoClient.mc.field_71439_g.func_174813_aQ().field_72337_e) + 1; ++y) {
                for (int z = MathHelper.func_76128_c(OringoClient.mc.field_71439_g.func_174813_aQ().field_72339_c); z < MathHelper.func_76128_c(OringoClient.mc.field_71439_g.func_174813_aQ().field_72334_f) + 1; ++z) {
                    final Block block = OringoClient.mc.field_71441_e.func_180495_p(new BlockPos(x, y, z)).func_177230_c();
                    if (block != null && !(block instanceof BlockAir)) {
                        final AxisAlignedBB boundingBox = block.func_180640_a((World)OringoClient.mc.field_71441_e, new BlockPos(x, y, z), OringoClient.mc.field_71441_e.func_180495_p(new BlockPos(x, y, z)));
                        if (boundingBox != null && OringoClient.mc.field_71439_g.func_174813_aQ().func_72326_a(boundingBox)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    public static Vec3 getVectorForRotation(final float yaw, final float pitch) {
        final float f = MathHelper.func_76134_b(-yaw * 0.017453292f - 3.1415927f);
        final float f2 = MathHelper.func_76126_a(-yaw * 0.017453292f - 3.1415927f);
        final float f3 = -MathHelper.func_76134_b(-pitch * 0.017453292f);
        final float f4 = MathHelper.func_76126_a(-pitch * 0.017453292f);
        return new Vec3((double)(f2 * f3), (double)f4, (double)(f * f3));
    }
    
    public static void syncHeldItem() {
        final int slot = OringoClient.mc.field_71439_g.field_71071_by.field_70461_c;
        if (slot != ((PlayerControllerAccessor)OringoClient.mc.field_71442_b).getCurrentPlayerItem()) {
            ((PlayerControllerAccessor)OringoClient.mc.field_71442_b).setCurrentPlayerItem(slot);
            PacketUtils.sendPacketNoEvent((Packet<?>)new C09PacketHeldItemChange(slot));
        }
    }
    
    public static float getJumpMotion() {
        float motionY = 0.42f;
        if (OringoClient.mc.field_71439_g.func_70644_a(Potion.field_76430_j)) {
            motionY += (OringoClient.mc.field_71439_g.func_70660_b(Potion.field_76430_j).func_76458_c() + 1) * 0.1f;
        }
        return motionY;
    }
    
    public static float getFriction(final boolean onGround) {
        float f4 = 0.91f;
        if (onGround) {
            f4 = OringoClient.mc.field_71439_g.field_70170_p.func_180495_p(new BlockPos(MathHelper.func_76128_c(OringoClient.mc.field_71439_g.field_70165_t), MathHelper.func_76128_c(OringoClient.mc.field_71439_g.func_174813_aQ().field_72338_b) - 1, MathHelper.func_76128_c(OringoClient.mc.field_71439_g.field_70161_v))).func_177230_c().field_149765_K * 0.91f;
        }
        final float f5 = 0.16277136f / (f4 * f4 * f4);
        float f6;
        if (onGround) {
            f6 = OringoClient.mc.field_71439_g.func_70689_ay() * f5;
        }
        else {
            f6 = OringoClient.mc.field_71439_g.field_70747_aH;
        }
        return f6;
    }
    
    public static boolean isOnGround(final double height) {
        return !OringoClient.mc.field_71441_e.func_72945_a((Entity)OringoClient.mc.field_71439_g, OringoClient.mc.field_71439_g.func_174813_aQ().func_72317_d(0.0, -height, 0.0)).isEmpty();
    }
    
    public static Vec3 getInterpolatedPos(final float partialTicks) {
        return new Vec3(interpolate(OringoClient.mc.field_71439_g.field_70169_q, OringoClient.mc.field_71439_g.field_70165_t, partialTicks), interpolate(OringoClient.mc.field_71439_g.field_70167_r, OringoClient.mc.field_71439_g.field_70163_u, partialTicks) + 0.1, interpolate(OringoClient.mc.field_71439_g.field_70166_s, OringoClient.mc.field_71439_g.field_70161_v, partialTicks));
    }
    
    public static double interpolate(final double prev, final double newPos, final float partialTicks) {
        return prev + (newPos - prev) * partialTicks;
    }
    
    public static boolean isFall(final float distance) {
        return isFall(distance, 0.0, 0.0);
    }
    
    public static boolean isFall(final float distance, final double xOffset, final double zOffset) {
        final BlockPos block = new BlockPos(OringoClient.mc.field_71439_g.field_70165_t, OringoClient.mc.field_71439_g.field_70163_u, OringoClient.mc.field_71439_g.field_70161_v);
        if (!OringoClient.mc.field_71441_e.func_175667_e(block)) {
            return false;
        }
        final AxisAlignedBB player = OringoClient.mc.field_71439_g.func_174813_aQ().func_72317_d(xOffset, 0.0, zOffset);
        return OringoClient.mc.field_71441_e.func_72945_a((Entity)OringoClient.mc.field_71439_g, new AxisAlignedBB(player.field_72340_a, player.field_72338_b - distance, player.field_72339_c, player.field_72336_d, player.field_72337_e, player.field_72334_f)).isEmpty();
    }
    
    public static boolean isLiquid(final float distance) {
        return isLiquid(distance, 0.0, 0.0);
    }
    
    public static boolean isLiquid(final float distance, final double xOffset, final double zOffset) {
        final BlockPos block = new BlockPos(OringoClient.mc.field_71439_g.field_70165_t, OringoClient.mc.field_71439_g.field_70163_u, OringoClient.mc.field_71439_g.field_70161_v);
        if (!OringoClient.mc.field_71441_e.func_175667_e(block)) {
            return false;
        }
        final AxisAlignedBB player = OringoClient.mc.field_71439_g.func_174813_aQ().func_72317_d(xOffset, 0.0, zOffset);
        return OringoClient.mc.field_71441_e.func_72953_d(new AxisAlignedBB(player.field_72340_a, player.field_72338_b - distance, player.field_72339_c, player.field_72336_d, player.field_72337_e, player.field_72334_f));
    }
    
    public static boolean isOverVoid() {
        return isOverVoid(0.0, 0.0);
    }
    
    public static boolean isOverVoid(final double xOffset, final double zOffset) {
        final BlockPos block = new BlockPos(OringoClient.mc.field_71439_g.field_70165_t, OringoClient.mc.field_71439_g.field_70163_u, OringoClient.mc.field_71439_g.field_70161_v);
        if (!OringoClient.mc.field_71441_e.func_175667_e(block)) {
            return false;
        }
        final AxisAlignedBB player = OringoClient.mc.field_71439_g.func_174813_aQ().func_72317_d(xOffset, 0.0, zOffset);
        return OringoClient.mc.field_71441_e.func_72945_a((Entity)OringoClient.mc.field_71439_g, new AxisAlignedBB(player.field_72340_a, 0.0, player.field_72339_c, player.field_72336_d, player.field_72337_e, player.field_72334_f)).isEmpty();
    }
    
    public static MovingObjectPosition rayTrace(final float yaw, final float pitch, final float distance) {
        final Vec3 vec3 = OringoClient.mc.field_71439_g.func_174824_e(1.0f);
        final Vec3 vec4 = getVectorForRotation(yaw, pitch);
        final Vec3 vec5 = vec3.func_72441_c(vec4.field_72450_a * distance, vec4.field_72448_b * distance, vec4.field_72449_c * distance);
        return OringoClient.mc.field_71441_e.func_147447_a(vec3, vec5, false, true, true);
    }
}
