// 
// Decompiled by Procyon v0.5.36
// 

package org.slf4j.event;

import org.slf4j.helpers.SubstituteLogger;
import org.slf4j.Marker;

public class SubstituteLoggingEvent implements LoggingEvent
{
    Level level;
    Marker marker;
    String loggerName;
    SubstituteLogger logger;
    String threadName;
    String message;
    Object[] argArray;
    long timeStamp;
    Throwable throwable;
    
    public Level getLevel() {
        return this.level;
    }
    
    public void setLevel(final Level level) {
        this.level = level;
    }
    
    public Marker getMarker() {
        return this.marker;
    }
    
    public void setMarker(final Marker marker) {
        this.marker = marker;
    }
    
    public String getLoggerName() {
        return this.loggerName;
    }
    
    public void setLoggerName(final String loggerName) {
        this.loggerName = loggerName;
    }
    
    public SubstituteLogger getLogger() {
        return this.logger;
    }
    
    public void setLogger(final SubstituteLogger logger) {
        this.logger = logger;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    public void setMessage(final String message) {
        this.message = message;
    }
    
    public Object[] getArgumentArray() {
        return this.argArray;
    }
    
    public void setArgumentArray(final Object[] argArray) {
        this.argArray = argArray;
    }
    
    public long getTimeStamp() {
        return this.timeStamp;
    }
    
    public void setTimeStamp(final long timeStamp) {
        this.timeStamp = timeStamp;
    }
    
    public String getThreadName() {
        return this.threadName;
    }
    
    public void setThreadName(final String threadName) {
        this.threadName = threadName;
    }
    
    public Throwable getThrowable() {
        return this.throwable;
    }
    
    public void setThrowable(final Throwable throwable) {
        this.throwable = throwable;
    }
}
