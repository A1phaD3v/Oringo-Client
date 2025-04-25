// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.events;

import net.minecraftforge.fml.common.eventhandler.Event;

public class MoveHeadingEvent extends Event
{
    private boolean onGround;
    private float friction2Multi;
    
    public MoveHeadingEvent(final boolean onGround) {
        this.friction2Multi = 0.91f;
        this.onGround = onGround;
    }
    
    public MoveHeadingEvent setOnGround(final boolean onGround) {
        this.onGround = onGround;
        return this;
    }
    
    public boolean isOnGround() {
        return this.onGround;
    }
    
    public void setFriction2Multi(final float friction2Multi) {
        this.friction2Multi = friction2Multi;
    }
    
    public float getFriction2Multi() {
        return this.friction2Multi;
    }
}
