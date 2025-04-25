// 
// Decompiled by Procyon v0.5.36
// 

package org.slf4j;

import org.slf4j.helpers.Util;
import org.slf4j.helpers.BasicMarkerFactory;
import org.slf4j.impl.StaticMarkerBinder;

public class MarkerFactory
{
    static IMarkerFactory MARKER_FACTORY;
    
    private MarkerFactory() {
    }
    
    private static IMarkerFactory bwCompatibleGetMarkerFactoryFromBinder() throws NoClassDefFoundError {
        try {
            return StaticMarkerBinder.getSingleton().getMarkerFactory();
        }
        catch (NoSuchMethodError nsme) {
            return StaticMarkerBinder.SINGLETON.getMarkerFactory();
        }
    }
    
    public static Marker getMarker(final String name) {
        return MarkerFactory.MARKER_FACTORY.getMarker(name);
    }
    
    public static Marker getDetachedMarker(final String name) {
        return MarkerFactory.MARKER_FACTORY.getDetachedMarker(name);
    }
    
    public static IMarkerFactory getIMarkerFactory() {
        return MarkerFactory.MARKER_FACTORY;
    }
    
    static {
        try {
            MarkerFactory.MARKER_FACTORY = bwCompatibleGetMarkerFactoryFromBinder();
        }
        catch (NoClassDefFoundError e2) {
            MarkerFactory.MARKER_FACTORY = new BasicMarkerFactory();
        }
        catch (Exception e) {
            Util.report("Unexpected failure while binding MarkerFactory", e);
        }
    }
}
