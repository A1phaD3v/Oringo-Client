// 
// Decompiled by Procyon v0.5.36
// 

package org.slf4j.event;

import org.slf4j.Marker;

public interface LoggingEvent
{
    Level getLevel();
    
    Marker getMarker();
    
    String getLoggerName();
    
    String getMessage();
    
    String getThreadName();
    
    Object[] getArgumentArray();
    
    long getTimeStamp();
    
    Throwable getThrowable();
}
