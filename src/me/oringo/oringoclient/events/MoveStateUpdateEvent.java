// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.events;

import net.minecraftforge.fml.common.eventhandler.Event;

public class MoveStateUpdateEvent extends Event
{
    private float forward;
    private float strafe;
    private boolean jump;
    private boolean sneak;
    
    public MoveStateUpdateEvent(final float forward, final float strafe, final boolean jump, final boolean sneak) {
        this.forward = forward;
        this.jump = jump;
        this.strafe = strafe;
        this.sneak = sneak;
    }
    
    public float getStrafe() {
        return this.strafe;
    }
    
    public float getForward() {
        return this.forward;
    }
    
    public boolean isJump() {
        return this.jump;
    }
    
    public boolean isSneak() {
        return this.sneak;
    }
    
    public MoveStateUpdateEvent setStrafe(final float strafe) {
        this.strafe = strafe;
        return this;
    }
    
    public MoveStateUpdateEvent setForward(final float forward) {
        this.forward = forward;
        return this;
    }
    
    public MoveStateUpdateEvent setJump(final boolean jump) {
        this.jump = jump;
        return this;
    }
    
    public MoveStateUpdateEvent setSneak(final boolean sneak) {
        this.sneak = sneak;
        return this;
    }
}
