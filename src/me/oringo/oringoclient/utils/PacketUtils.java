// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.utils;

import java.lang.reflect.Field;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import me.oringo.oringoclient.OringoClient;
import net.minecraft.network.Packet;
import java.util.ArrayList;

public class PacketUtils
{
    public static ArrayList<Packet<?>> noEvent;
    
    public static void sendPacketNoEvent(final Packet<?> packet) {
        PacketUtils.noEvent.add(packet);
        OringoClient.mc.func_147114_u().func_147298_b().func_179290_a((Packet)packet);
    }
    
    public static C03PacketPlayer.C06PacketPlayerPosLook getResponse(final S08PacketPlayerPosLook packet) {
        double x = packet.func_148932_c();
        double y = packet.func_148928_d();
        double z = packet.func_148933_e();
        float yaw = packet.func_148931_f();
        float pitch = packet.func_148930_g();
        if (packet.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.X)) {
            x += OringoClient.mc.field_71439_g.field_70165_t;
        }
        if (packet.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.Y)) {
            y += OringoClient.mc.field_71439_g.field_70163_u;
        }
        if (packet.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.Z)) {
            z += OringoClient.mc.field_71439_g.field_70161_v;
        }
        if (packet.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.X_ROT)) {
            pitch += OringoClient.mc.field_71439_g.field_70125_A;
        }
        if (packet.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.Y_ROT)) {
            yaw += OringoClient.mc.field_71439_g.field_70177_z;
        }
        return new C03PacketPlayer.C06PacketPlayerPosLook(x, y, z, yaw % 360.0f, pitch % 360.0f, false);
    }
    
    public static String packetToString(final Packet<?> packet) {
        final StringBuilder postfix = new StringBuilder();
        boolean first = true;
        for (final Field field : packet.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                postfix.append(first ? "" : ", ").append(field.get(packet));
            }
            catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            first = false;
        }
        return packet.getClass().getSimpleName() + String.format("{%s}", postfix);
    }
    
    static {
        PacketUtils.noEvent = new ArrayList<Packet<?>>();
    }
}
