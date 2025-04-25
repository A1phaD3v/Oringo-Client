// 
// Decompiled by Procyon v0.5.36
// 

package org.slf4j.helpers;

import java.lang.reflect.InvocationTargetException;
import org.slf4j.event.LoggingEvent;
import org.slf4j.Marker;
import org.slf4j.event.SubstituteLoggingEvent;
import java.util.Queue;
import org.slf4j.event.EventRecodingLogger;
import java.lang.reflect.Method;
import org.slf4j.Logger;

public class SubstituteLogger implements Logger
{
    private final String name;
    private volatile Logger _delegate;
    private Boolean delegateEventAware;
    private Method logMethodCache;
    private EventRecodingLogger eventRecodingLogger;
    private Queue<SubstituteLoggingEvent> eventQueue;
    private final boolean createdPostInitialization;
    
    public SubstituteLogger(final String name, final Queue<SubstituteLoggingEvent> eventQueue, final boolean createdPostInitialization) {
        this.name = name;
        this.eventQueue = eventQueue;
        this.createdPostInitialization = createdPostInitialization;
    }
    
    public String getName() {
        return this.name;
    }
    
    public boolean isTraceEnabled() {
        return this.delegate().isTraceEnabled();
    }
    
    public void trace(final String msg) {
        this.delegate().trace(msg);
    }
    
    public void trace(final String format, final Object arg) {
        this.delegate().trace(format, arg);
    }
    
    public void trace(final String format, final Object arg1, final Object arg2) {
        this.delegate().trace(format, arg1, arg2);
    }
    
    public void trace(final String format, final Object... arguments) {
        this.delegate().trace(format, arguments);
    }
    
    public void trace(final String msg, final Throwable t) {
        this.delegate().trace(msg, t);
    }
    
    public boolean isTraceEnabled(final Marker marker) {
        return this.delegate().isTraceEnabled(marker);
    }
    
    public void trace(final Marker marker, final String msg) {
        this.delegate().trace(marker, msg);
    }
    
    public void trace(final Marker marker, final String format, final Object arg) {
        this.delegate().trace(marker, format, arg);
    }
    
    public void trace(final Marker marker, final String format, final Object arg1, final Object arg2) {
        this.delegate().trace(marker, format, arg1, arg2);
    }
    
    public void trace(final Marker marker, final String format, final Object... arguments) {
        this.delegate().trace(marker, format, arguments);
    }
    
    public void trace(final Marker marker, final String msg, final Throwable t) {
        this.delegate().trace(marker, msg, t);
    }
    
    public boolean isDebugEnabled() {
        return this.delegate().isDebugEnabled();
    }
    
    public void debug(final String msg) {
        this.delegate().debug(msg);
    }
    
    public void debug(final String format, final Object arg) {
        this.delegate().debug(format, arg);
    }
    
    public void debug(final String format, final Object arg1, final Object arg2) {
        this.delegate().debug(format, arg1, arg2);
    }
    
    public void debug(final String format, final Object... arguments) {
        this.delegate().debug(format, arguments);
    }
    
    public void debug(final String msg, final Throwable t) {
        this.delegate().debug(msg, t);
    }
    
    public boolean isDebugEnabled(final Marker marker) {
        return this.delegate().isDebugEnabled(marker);
    }
    
    public void debug(final Marker marker, final String msg) {
        this.delegate().debug(marker, msg);
    }
    
    public void debug(final Marker marker, final String format, final Object arg) {
        this.delegate().debug(marker, format, arg);
    }
    
    public void debug(final Marker marker, final String format, final Object arg1, final Object arg2) {
        this.delegate().debug(marker, format, arg1, arg2);
    }
    
    public void debug(final Marker marker, final String format, final Object... arguments) {
        this.delegate().debug(marker, format, arguments);
    }
    
    public void debug(final Marker marker, final String msg, final Throwable t) {
        this.delegate().debug(marker, msg, t);
    }
    
    public boolean isInfoEnabled() {
        return this.delegate().isInfoEnabled();
    }
    
    public void info(final String msg) {
        this.delegate().info(msg);
    }
    
    public void info(final String format, final Object arg) {
        this.delegate().info(format, arg);
    }
    
    public void info(final String format, final Object arg1, final Object arg2) {
        this.delegate().info(format, arg1, arg2);
    }
    
    public void info(final String format, final Object... arguments) {
        this.delegate().info(format, arguments);
    }
    
    public void info(final String msg, final Throwable t) {
        this.delegate().info(msg, t);
    }
    
    public boolean isInfoEnabled(final Marker marker) {
        return this.delegate().isInfoEnabled(marker);
    }
    
    public void info(final Marker marker, final String msg) {
        this.delegate().info(marker, msg);
    }
    
    public void info(final Marker marker, final String format, final Object arg) {
        this.delegate().info(marker, format, arg);
    }
    
    public void info(final Marker marker, final String format, final Object arg1, final Object arg2) {
        this.delegate().info(marker, format, arg1, arg2);
    }
    
    public void info(final Marker marker, final String format, final Object... arguments) {
        this.delegate().info(marker, format, arguments);
    }
    
    public void info(final Marker marker, final String msg, final Throwable t) {
        this.delegate().info(marker, msg, t);
    }
    
    public boolean isWarnEnabled() {
        return this.delegate().isWarnEnabled();
    }
    
    public void warn(final String msg) {
        this.delegate().warn(msg);
    }
    
    public void warn(final String format, final Object arg) {
        this.delegate().warn(format, arg);
    }
    
    public void warn(final String format, final Object arg1, final Object arg2) {
        this.delegate().warn(format, arg1, arg2);
    }
    
    public void warn(final String format, final Object... arguments) {
        this.delegate().warn(format, arguments);
    }
    
    public void warn(final String msg, final Throwable t) {
        this.delegate().warn(msg, t);
    }
    
    public boolean isWarnEnabled(final Marker marker) {
        return this.delegate().isWarnEnabled(marker);
    }
    
    public void warn(final Marker marker, final String msg) {
        this.delegate().warn(marker, msg);
    }
    
    public void warn(final Marker marker, final String format, final Object arg) {
        this.delegate().warn(marker, format, arg);
    }
    
    public void warn(final Marker marker, final String format, final Object arg1, final Object arg2) {
        this.delegate().warn(marker, format, arg1, arg2);
    }
    
    public void warn(final Marker marker, final String format, final Object... arguments) {
        this.delegate().warn(marker, format, arguments);
    }
    
    public void warn(final Marker marker, final String msg, final Throwable t) {
        this.delegate().warn(marker, msg, t);
    }
    
    public boolean isErrorEnabled() {
        return this.delegate().isErrorEnabled();
    }
    
    public void error(final String msg) {
        this.delegate().error(msg);
    }
    
    public void error(final String format, final Object arg) {
        this.delegate().error(format, arg);
    }
    
    public void error(final String format, final Object arg1, final Object arg2) {
        this.delegate().error(format, arg1, arg2);
    }
    
    public void error(final String format, final Object... arguments) {
        this.delegate().error(format, arguments);
    }
    
    public void error(final String msg, final Throwable t) {
        this.delegate().error(msg, t);
    }
    
    public boolean isErrorEnabled(final Marker marker) {
        return this.delegate().isErrorEnabled(marker);
    }
    
    public void error(final Marker marker, final String msg) {
        this.delegate().error(marker, msg);
    }
    
    public void error(final Marker marker, final String format, final Object arg) {
        this.delegate().error(marker, format, arg);
    }
    
    public void error(final Marker marker, final String format, final Object arg1, final Object arg2) {
        this.delegate().error(marker, format, arg1, arg2);
    }
    
    public void error(final Marker marker, final String format, final Object... arguments) {
        this.delegate().error(marker, format, arguments);
    }
    
    public void error(final Marker marker, final String msg, final Throwable t) {
        this.delegate().error(marker, msg, t);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final SubstituteLogger that = (SubstituteLogger)o;
        return this.name.equals(that.name);
    }
    
    @Override
    public int hashCode() {
        return this.name.hashCode();
    }
    
    Logger delegate() {
        if (this._delegate != null) {
            return this._delegate;
        }
        if (this.createdPostInitialization) {
            return NOPLogger.NOP_LOGGER;
        }
        return this.getEventRecordingLogger();
    }
    
    private Logger getEventRecordingLogger() {
        if (this.eventRecodingLogger == null) {
            this.eventRecodingLogger = new EventRecodingLogger(this, this.eventQueue);
        }
        return this.eventRecodingLogger;
    }
    
    public void setDelegate(final Logger delegate) {
        this._delegate = delegate;
    }
    
    public boolean isDelegateEventAware() {
        if (this.delegateEventAware != null) {
            return this.delegateEventAware;
        }
        try {
            this.logMethodCache = this._delegate.getClass().getMethod("log", LoggingEvent.class);
            this.delegateEventAware = Boolean.TRUE;
        }
        catch (NoSuchMethodException e) {
            this.delegateEventAware = Boolean.FALSE;
        }
        return this.delegateEventAware;
    }
    
    public void log(final LoggingEvent event) {
        if (this.isDelegateEventAware()) {
            try {
                this.logMethodCache.invoke(this._delegate, event);
            }
            catch (IllegalAccessException e) {}
            catch (IllegalArgumentException e2) {}
            catch (InvocationTargetException ex) {}
        }
    }
    
    public boolean isDelegateNull() {
        return this._delegate == null;
    }
    
    public boolean isDelegateNOP() {
        return this._delegate instanceof NOPLogger;
    }
}
