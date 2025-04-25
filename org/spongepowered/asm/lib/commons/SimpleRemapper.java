// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.lib.commons;

import java.util.Collections;
import java.util.Map;

public class SimpleRemapper extends Remapper
{
    private final Map<String, String> mapping;
    
    public SimpleRemapper(final Map<String, String> mapping) {
        this.mapping = mapping;
    }
    
    public SimpleRemapper(final String oldName, final String newName) {
        this.mapping = Collections.singletonMap(oldName, newName);
    }
    
    @Override
    public String mapMethodName(final String owner, final String name, final String desc) {
        final String s = this.map(owner + '.' + name + desc);
        return (s == null) ? name : s;
    }
    
    @Override
    public String mapInvokeDynamicMethodName(final String name, final String desc) {
        final String s = this.map('.' + name + desc);
        return (s == null) ? name : s;
    }
    
    @Override
    public String mapFieldName(final String owner, final String name, final String desc) {
        final String s = this.map(owner + '.' + name);
        return (s == null) ? name : s;
    }
    
    @Override
    public String map(final String key) {
        return this.mapping.get(key);
    }
}
