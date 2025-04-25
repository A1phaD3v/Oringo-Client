// 
// Decompiled by Procyon v0.5.36
// 

package org.slf4j;

public interface IMarkerFactory
{
    Marker getMarker(final String p0);
    
    boolean exists(final String p0);
    
    boolean detachMarker(final String p0);
    
    Marker getDetachedMarker(final String p0);
}
