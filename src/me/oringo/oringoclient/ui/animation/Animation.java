// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.ui.animation;

import me.oringo.oringoclient.utils.MilliTimer;

public abstract class Animation
{
    private final MilliTimer animationTimer;
    protected long time;
    
    public abstract void doAnimation(final double p0, final double p1);
    
    public Animation(final long time) {
        this.animationTimer = new MilliTimer();
        this.time = time;
    }
    
    public boolean isFinished() {
        return this.animationTimer.hasTimePassed(this.time);
    }
    
    public void start() {
        this.animationTimer.reset();
    }
}
