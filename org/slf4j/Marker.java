// 
// Decompiled by Procyon v0.5.36
// 

package org.slf4j;

import java.util.Iterator;
import java.io.Serializable;

public interface Marker extends Serializable
{
    public static final String ANY_MARKER = "*";
    public static final String ANY_NON_NULL_MARKER = "+";
    
    String getName();
    
    void add(final Marker p0);
    
    boolean remove(final Marker p0);
    
    @Deprecated
    boolean hasChildren();
    
    boolean hasReferences();
    
    Iterator<Marker> iterator();
    
    boolean contains(final Marker p0);
    
    boolean contains(final String p0);
    
    boolean equals(final Object p0);
    
    int hashCode();
}
