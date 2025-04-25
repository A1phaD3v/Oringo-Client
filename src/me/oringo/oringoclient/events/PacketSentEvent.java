// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.events;

import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class PacketSentEvent extends Event
{
    public Packet<?> packet;
    
    public PacketSentEvent(final Packet<?> packet) {
        this.packet = packet;
    }
    
    public Packet<?> getPacket() {
        return this.packet;
    }
    
    public static class Post extends Event
    {
        public Packet<?> packet;
        
        public Post(final Packet<?> packet) {
            this.packet = packet;
        }
    }
}
