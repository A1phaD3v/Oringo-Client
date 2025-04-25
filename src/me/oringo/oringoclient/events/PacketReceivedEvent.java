// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.events;

import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class PacketReceivedEvent extends Event
{
    public Packet<?> packet;
    public ChannelHandlerContext context;
    
    public PacketReceivedEvent(final Packet<?> packet, final ChannelHandlerContext context) {
        this.packet = packet;
        this.context = context;
    }
    
    public static class Post extends Event
    {
        public Packet<?> packet;
        public ChannelHandlerContext context;
        
        public Post(final Packet<?> packet, final ChannelHandlerContext context) {
            this.packet = packet;
            this.context = context;
        }
    }
}
