// 
// Decompiled by Procyon v0.5.36
// 

package org.slf4j.event;

public enum Level
{
    ERROR(40, "ERROR"), 
    WARN(30, "WARN"), 
    INFO(20, "INFO"), 
    DEBUG(10, "DEBUG"), 
    TRACE(0, "TRACE");
    
    private int levelInt;
    private String levelStr;
    
    private Level(final int i, final String s) {
        this.levelInt = i;
        this.levelStr = s;
    }
    
    public int toInt() {
        return this.levelInt;
    }
    
    @Override
    public String toString() {
        return this.levelStr;
    }
}
