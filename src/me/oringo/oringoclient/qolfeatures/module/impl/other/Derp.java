// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.other;

import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C02PacketUseEntity;
import me.oringo.oringoclient.events.PacketSentEvent;
import java.util.Iterator;
import me.oringo.oringoclient.utils.PacketUtils;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Random;
import me.oringo.oringoclient.events.MotionUpdateEvent;
import net.minecraft.network.Packet;
import java.util.ArrayList;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class Derp extends Module
{
    private ArrayList<Packet<?>> packets;
    
    public Derp() {
        super("Derp", Category.OTHER);
        this.packets = new ArrayList<Packet<?>>();
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onUpdate(final MotionUpdateEvent.Pre event) {
        if (!this.isToggled() || !this.packets.isEmpty()) {
            return;
        }
        event.yaw = (float)(new Random().nextInt(181) * ((Derp.mc.field_71439_g.field_70173_aa % 2 == 0) ? -1 : 1));
        event.pitch = (float)(new Random().nextInt(181) - 90);
    }
    
    @SubscribeEvent
    public void onUpdatePost(final MotionUpdateEvent.Post e) {
        if (this.packets.isEmpty()) {
            return;
        }
        for (final Packet<?> packet : this.packets) {
            PacketUtils.sendPacketNoEvent(packet);
        }
        this.packets.clear();
    }
    
    @SubscribeEvent
    public void onPacket(final PacketSentEvent event) {
        if (!this.isToggled()) {
            return;
        }
        if (event.packet instanceof C02PacketUseEntity || event.packet instanceof C08PacketPlayerBlockPlacement || event.packet instanceof C07PacketPlayerDigging || event.packet instanceof C0APacketAnimation || event.packet instanceof C01PacketChatMessage || event.packet instanceof C09PacketHeldItemChange) {
            this.packets.add(event.packet);
            event.setCanceled(true);
        }
    }
}
