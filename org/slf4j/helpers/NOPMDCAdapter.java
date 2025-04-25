// 
// Decompiled by Procyon v0.5.36
// 

package org.slf4j.helpers;

import java.util.Map;
import org.slf4j.spi.MDCAdapter;

public class NOPMDCAdapter implements MDCAdapter
{
    public void clear() {
    }
    
    public String get(final String key) {
        return null;
    }
    
    public void put(final String key, final String val) {
    }
    
    public void remove(final String key) {
    }
    
    public Map<String, String> getCopyOfContextMap() {
        return null;
    }
    
    public void setContextMap(final Map<String, String> contextMap) {
    }
}
