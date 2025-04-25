// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.events;

import net.minecraftforge.fml.common.eventhandler.Event;

public class UpdateRenderEvent extends Event
{
    public float partialTicks;
    
    public UpdateRenderEvent(final float partialTicks) {
        this.partialTicks = partialTicks;
    }
    
    public static class Pre extends UpdateRenderEvent
    {
        public Pre(final float partialTicks) {
            super(partialTicks);
        }
    }
    
    public static class Post extends UpdateRenderEvent
    {
        public Post(final float partialTicks) {
            super(partialTicks);
        }
    }
}
