// 
// Decompiled by Procyon v0.5.36
// 

package org.slf4j.spi;

import org.slf4j.IMarkerFactory;

public interface MarkerFactoryBinder
{
    IMarkerFactory getMarkerFactory();
    
    String getMarkerFactoryClassStr();
}
