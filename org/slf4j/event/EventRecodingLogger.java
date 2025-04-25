// 
// Decompiled by Procyon v0.5.36
// 

package org.slf4j.event;

import org.slf4j.Marker;
import java.util.Queue;
import org.slf4j.helpers.SubstituteLogger;
import org.slf4j.Logger;

public class EventRecodingLogger implements Logger
{
    String name;
    SubstituteLogger logger;
    Queue<SubstituteLoggingEvent> eventQueue;
    
    public EventRecodingLogger(final SubstituteLogger logger, final Queue<SubstituteLoggingEvent> eventQueue) {
        this.logger = logger;
        this.name = logger.getName();
        this.eventQueue = eventQueue;
    }
    
    public String getName() {
        return this.name;
    }
    
    private void recordEvent(final Level level, final String msg, final Object[] args, final Throwable throwable) {
        this.recordEvent(level, null, msg, args, throwable);
    }
    
    private void recordEvent(final Level level, final Marker marker, final String msg, final Object[] args, final Throwable throwable) {
        final SubstituteLoggingEvent loggingEvent = new SubstituteLoggingEvent();
        loggingEvent.setTimeStamp(System.currentTimeMillis());
        loggingEvent.setLevel(level);
        loggingEvent.setLogger(this.logger);
        loggingEvent.setLoggerName(this.name);
        loggingEvent.setMarker(marker);
        loggingEvent.setMessage(msg);
        loggingEvent.setArgumentArray(args);
        loggingEvent.setThrowable(throwable);
        loggingEvent.setThreadName(Thread.currentThread().getName());
        this.eventQueue.add(loggingEvent);
    }
    
    public boolean isTraceEnabled() {
        return true;
    }
    
    public void trace(final String msg) {
        this.recordEvent(Level.TRACE, msg, null, null);
    }
    
    public void trace(final String format, final Object arg) {
        this.recordEvent(Level.TRACE, format, new Object[] { arg }, null);
    }
    
    public void trace(final String format, final Object arg1, final Object arg2) {
        this.recordEvent(Level.TRACE, format, new Object[] { arg1, arg2 }, null);
    }
    
    public void trace(final String format, final Object... arguments) {
        this.recordEvent(Level.TRACE, format, arguments, null);
    }
    
    public void trace(final String msg, final Throwable t) {
        this.recordEvent(Level.TRACE, msg, null, t);
    }
    
    public boolean isTraceEnabled(final Marker marker) {
        return true;
    }
    
    public void trace(final Marker marker, final String msg) {
        this.recordEvent(Level.TRACE, marker, msg, null, null);
    }
    
    public void trace(final Marker marker, final String format, final Object arg) {
        this.recordEvent(Level.TRACE, marker, format, new Object[] { arg }, null);
    }
    
    public void trace(final Marker marker, final String format, final Object arg1, final Object arg2) {
        this.recordEvent(Level.TRACE, marker, format, new Object[] { arg1, arg2 }, null);
    }
    
    public void trace(final Marker marker, final String format, final Object... argArray) {
        this.recordEvent(Level.TRACE, marker, format, argArray, null);
    }
    
    public void trace(final Marker marker, final String msg, final Throwable t) {
        this.recordEvent(Level.TRACE, marker, msg, null, t);
    }
    
    public boolean isDebugEnabled() {
        return true;
    }
    
    public void debug(final String msg) {
        this.recordEvent(Level.TRACE, msg, null, null);
    }
    
    public void debug(final String format, final Object arg) {
        this.recordEvent(Level.DEBUG, format, new Object[] { arg }, null);
    }
    
    public void debug(final String format, final Object arg1, final Object arg2) {
        this.recordEvent(Level.DEBUG, format, new Object[] { arg1, arg2 }, null);
    }
    
    public void debug(final String format, final Object... arguments) {
        this.recordEvent(Level.DEBUG, format, arguments, null);
    }
    
    public void debug(final String msg, final Throwable t) {
        this.recordEvent(Level.DEBUG, msg, null, t);
    }
    
    public boolean isDebugEnabled(final Marker marker) {
        return true;
    }
    
    public void debug(final Marker marker, final String msg) {
        this.recordEvent(Level.DEBUG, marker, msg, null, null);
    }
    
    public void debug(final Marker marker, final String format, final Object arg) {
        this.recordEvent(Level.DEBUG, marker, format, new Object[] { arg }, null);
    }
    
    public void debug(final Marker marker, final String format, final Object arg1, final Object arg2) {
        this.recordEvent(Level.DEBUG, marker, format, new Object[] { arg1, arg2 }, null);
    }
    
    public void debug(final Marker marker, final String format, final Object... arguments) {
        this.recordEvent(Level.DEBUG, marker, format, arguments, null);
    }
    
    public void debug(final Marker marker, final String msg, final Throwable t) {
        this.recordEvent(Level.DEBUG, marker, msg, null, t);
    }
    
    public boolean isInfoEnabled() {
        return true;
    }
    
    public void info(final String msg) {
        this.recordEvent(Level.INFO, msg, null, null);
    }
    
    public void info(final String format, final Object arg) {
        this.recordEvent(Level.INFO, format, new Object[] { arg }, null);
    }
    
    public void info(final String format, final Object arg1, final Object arg2) {
        this.recordEvent(Level.INFO, format, new Object[] { arg1, arg2 }, null);
    }
    
    public void info(final String format, final Object... arguments) {
        this.recordEvent(Level.INFO, format, arguments, null);
    }
    
    public void info(final String msg, final Throwable t) {
        this.recordEvent(Level.INFO, msg, null, t);
    }
    
    public boolean isInfoEnabled(final Marker marker) {
        return true;
    }
    
    public void info(final Marker marker, final String msg) {
        this.recordEvent(Level.INFO, marker, msg, null, null);
    }
    
    public void info(final Marker marker, final String format, final Object arg) {
        this.recordEvent(Level.INFO, marker, format, new Object[] { arg }, null);
    }
    
    public void info(final Marker marker, final String format, final Object arg1, final Object arg2) {
        this.recordEvent(Level.INFO, marker, format, new Object[] { arg1, arg2 }, null);
    }
    
    public void info(final Marker marker, final String format, final Object... arguments) {
        this.recordEvent(Level.INFO, marker, format, arguments, null);
    }
    
    public void info(final Marker marker, final String msg, final Throwable t) {
        this.recordEvent(Level.INFO, marker, msg, null, t);
    }
    
    public boolean isWarnEnabled() {
        return true;
    }
    
    public void warn(final String msg) {
        this.recordEvent(Level.WARN, msg, null, null);
    }
    
    public void warn(final String format, final Object arg) {
        this.recordEvent(Level.WARN, format, new Object[] { arg }, null);
    }
    
    public void warn(final String format, final Object arg1, final Object arg2) {
        this.recordEvent(Level.WARN, format, new Object[] { arg1, arg2 }, null);
    }
    
    public void warn(final String format, final Object... arguments) {
        this.recordEvent(Level.WARN, format, arguments, null);
    }
    
    public void warn(final String msg, final Throwable t) {
        this.recordEvent(Level.WARN, msg, null, t);
    }
    
    public boolean isWarnEnabled(final Marker marker) {
        return true;
    }
    
    public void warn(final Marker marker, final String msg) {
        this.recordEvent(Level.WARN, msg, null, null);
    }
    
    public void warn(final Marker marker, final String format, final Object arg) {
        this.recordEvent(Level.WARN, format, new Object[] { arg }, null);
    }
    
    public void warn(final Marker marker, final String format, final Object arg1, final Object arg2) {
        this.recordEvent(Level.WARN, marker, format, new Object[] { arg1, arg2 }, null);
    }
    
    public void warn(final Marker marker, final String format, final Object... arguments) {
        this.recordEvent(Level.WARN, marker, format, arguments, null);
    }
    
    public void warn(final Marker marker, final String msg, final Throwable t) {
        this.recordEvent(Level.WARN, marker, msg, null, t);
    }
    
    public boolean isErrorEnabled() {
        return true;
    }
    
    public void error(final String msg) {
        this.recordEvent(Level.ERROR, msg, null, null);
    }
    
    public void error(final String format, final Object arg) {
        this.recordEvent(Level.ERROR, format, new Object[] { arg }, null);
    }
    
    public void error(final String format, final Object arg1, final Object arg2) {
        this.recordEvent(Level.ERROR, format, new Object[] { arg1, arg2 }, null);
    }
    
    public void error(final String format, final Object... arguments) {
        this.recordEvent(Level.ERROR, format, arguments, null);
    }
    
    public void error(final String msg, final Throwable t) {
        this.recordEvent(Level.ERROR, msg, null, t);
    }
    
    public boolean isErrorEnabled(final Marker marker) {
        return true;
    }
    
    public void error(final Marker marker, final String msg) {
        this.recordEvent(Level.ERROR, marker, msg, null, null);
    }
    
    public void error(final Marker marker, final String format, final Object arg) {
        this.recordEvent(Level.ERROR, marker, format, new Object[] { arg }, null);
    }
    
    public void error(final Marker marker, final String format, final Object arg1, final Object arg2) {
        this.recordEvent(Level.ERROR, marker, format, new Object[] { arg1, arg2 }, null);
    }
    
    public void error(final Marker marker, final String format, final Object... arguments) {
        this.recordEvent(Level.ERROR, marker, format, arguments, null);
    }
    
    public void error(final Marker marker, final String msg, final Throwable t) {
        this.recordEvent(Level.ERROR, marker, msg, null, t);
    }
}
