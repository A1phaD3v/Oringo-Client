// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.utils;

public class TickTimer
{
    private int ticks;
    
    public TickTimer() {
        this(0);
    }
    
    public TickTimer(final int ticks) {
        this.ticks = ticks;
    }
    
    public int updateTicks() {
        return ++this.ticks;
    }
    
    public boolean passed(final int ticks) {
        return ticks >= this.ticks;
    }
    
    public int getTicks() {
        return this.ticks;
    }
    
    public void setTicks(final int ticks) {
        this.ticks = ticks;
    }
    
    public void reset() {
        this.setTicks(0);
    }
}
