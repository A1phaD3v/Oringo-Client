// 
// Decompiled by Procyon v0.5.36
// 

package org.slf4j.helpers;

import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Marker;
import java.util.concurrent.ConcurrentMap;
import org.slf4j.IMarkerFactory;

public class BasicMarkerFactory implements IMarkerFactory
{
    private final ConcurrentMap<String, Marker> markerMap;
    
    public BasicMarkerFactory() {
        this.markerMap = new ConcurrentHashMap<String, Marker>();
    }
    
    public Marker getMarker(final String name) {
        if (name == null) {
            throw new IllegalArgumentException("Marker name cannot be null");
        }
        Marker marker = this.markerMap.get(name);
        if (marker == null) {
            marker = new BasicMarker(name);
            final Marker oldMarker = this.markerMap.putIfAbsent(name, marker);
            if (oldMarker != null) {
                marker = oldMarker;
            }
        }
        return marker;
    }
    
    public boolean exists(final String name) {
        return name != null && this.markerMap.containsKey(name);
    }
    
    public boolean detachMarker(final String name) {
        return name != null && this.markerMap.remove(name) != null;
    }
    
    public Marker getDetachedMarker(final String name) {
        return new BasicMarker(name);
    }
}
