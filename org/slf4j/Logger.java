// 
// Decompiled by Procyon v0.5.36
// 

package org.slf4j;

public interface Logger
{
    public static final String ROOT_LOGGER_NAME = "ROOT";
    
    String getName();
    
    boolean isTraceEnabled();
    
    void trace(final String p0);
    
    void trace(final String p0, final Object p1);
    
    void trace(final String p0, final Object p1, final Object p2);
    
    void trace(final String p0, final Object... p1);
    
    void trace(final String p0, final Throwable p1);
    
    boolean isTraceEnabled(final Marker p0);
    
    void trace(final Marker p0, final String p1);
    
    void trace(final Marker p0, final String p1, final Object p2);
    
    void trace(final Marker p0, final String p1, final Object p2, final Object p3);
    
    void trace(final Marker p0, final String p1, final Object... p2);
    
    void trace(final Marker p0, final String p1, final Throwable p2);
    
    boolean isDebugEnabled();
    
    void debug(final String p0);
    
    void debug(final String p0, final Object p1);
    
    void debug(final String p0, final Object p1, final Object p2);
    
    void debug(final String p0, final Object... p1);
    
    void debug(final String p0, final Throwable p1);
    
    boolean isDebugEnabled(final Marker p0);
    
    void debug(final Marker p0, final String p1);
    
    void debug(final Marker p0, final String p1, final Object p2);
    
    void debug(final Marker p0, final String p1, final Object p2, final Object p3);
    
    void debug(final Marker p0, final String p1, final Object... p2);
    
    void debug(final Marker p0, final String p1, final Throwable p2);
    
    boolean isInfoEnabled();
    
    void info(final String p0);
    
    void info(final String p0, final Object p1);
    
    void info(final String p0, final Object p1, final Object p2);
    
    void info(final String p0, final Object... p1);
    
    void info(final String p0, final Throwable p1);
    
    boolean isInfoEnabled(final Marker p0);
    
    void info(final Marker p0, final String p1);
    
    void info(final Marker p0, final String p1, final Object p2);
    
    void info(final Marker p0, final String p1, final Object p2, final Object p3);
    
    void info(final Marker p0, final String p1, final Object... p2);
    
    void info(final Marker p0, final String p1, final Throwable p2);
    
    boolean isWarnEnabled();
    
    void warn(final String p0);
    
    void warn(final String p0, final Object p1);
    
    void warn(final String p0, final Object... p1);
    
    void warn(final String p0, final Object p1, final Object p2);
    
    void warn(final String p0, final Throwable p1);
    
    boolean isWarnEnabled(final Marker p0);
    
    void warn(final Marker p0, final String p1);
    
    void warn(final Marker p0, final String p1, final Object p2);
    
    void warn(final Marker p0, final String p1, final Object p2, final Object p3);
    
    void warn(final Marker p0, final String p1, final Object... p2);
    
    void warn(final Marker p0, final String p1, final Throwable p2);
    
    boolean isErrorEnabled();
    
    void error(final String p0);
    
    void error(final String p0, final Object p1);
    
    void error(final String p0, final Object p1, final Object p2);
    
    void error(final String p0, final Object... p1);
    
    void error(final String p0, final Throwable p1);
    
    boolean isErrorEnabled(final Marker p0);
    
    void error(final Marker p0, final String p1);
    
    void error(final Marker p0, final String p1, final Object p2);
    
    void error(final Marker p0, final String p1, final Object p2, final Object p3);
    
    void error(final Marker p0, final String p1, final Object... p2);
    
    void error(final Marker p0, final String p1, final Throwable p2);
}
