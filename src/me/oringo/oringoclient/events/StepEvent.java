// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.events;

import net.minecraftforge.fml.common.eventhandler.Event;

public class StepEvent extends Event
{
    private double height;
    
    public StepEvent(final double height) {
        this.height = height;
    }
    
    public double getHeight() {
        return this.height;
    }
    
    public void setHeight(final double height) {
        this.height = height;
    }
    
    public static class Pre extends StepEvent
    {
        public Pre(final double height) {
            super(height);
        }
    }
    
    public static class Post extends StepEvent
    {
        public Post(final double height) {
            super(height);
        }
    }
}
