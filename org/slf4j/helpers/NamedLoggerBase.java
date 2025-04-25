// 
// Decompiled by Procyon v0.5.36
// 

package org.slf4j.helpers;

import java.io.ObjectStreamException;
import org.slf4j.LoggerFactory;
import java.io.Serializable;
import org.slf4j.Logger;

abstract class NamedLoggerBase implements Logger, Serializable
{
    private static final long serialVersionUID = 7535258609338176893L;
    protected String name;
    
    public String getName() {
        return this.name;
    }
    
    protected Object readResolve() throws ObjectStreamException {
        return LoggerFactory.getLogger(this.getName());
    }
}
