// 
// Decompiled by Procyon v0.5.36
// 

package org.slf4j.spi;

import org.slf4j.Marker;
import org.slf4j.Logger;

public interface LocationAwareLogger extends Logger
{
    public static final int TRACE_INT = 0;
    public static final int DEBUG_INT = 10;
    public static final int INFO_INT = 20;
    public static final int WARN_INT = 30;
    public static final int ERROR_INT = 40;
    
    void log(final Marker p0, final String p1, final int p2, final String p3, final Object[] p4, final Throwable p5);
}
