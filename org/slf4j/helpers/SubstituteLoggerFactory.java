// 
// Decompiled by Procyon v0.5.36
// 

package org.slf4j.helpers;

import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import org.slf4j.Logger;
import java.util.HashMap;
import org.slf4j.event.SubstituteLoggingEvent;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.Map;
import org.slf4j.ILoggerFactory;

public class SubstituteLoggerFactory implements ILoggerFactory
{
    boolean postInitialization;
    final Map<String, SubstituteLogger> loggers;
    final LinkedBlockingQueue<SubstituteLoggingEvent> eventQueue;
    
    public SubstituteLoggerFactory() {
        this.postInitialization = false;
        this.loggers = new HashMap<String, SubstituteLogger>();
        this.eventQueue = new LinkedBlockingQueue<SubstituteLoggingEvent>();
    }
    
    public synchronized Logger getLogger(final String name) {
        SubstituteLogger logger = this.loggers.get(name);
        if (logger == null) {
            logger = new SubstituteLogger(name, this.eventQueue, this.postInitialization);
            this.loggers.put(name, logger);
        }
        return logger;
    }
    
    public List<String> getLoggerNames() {
        return new ArrayList<String>(this.loggers.keySet());
    }
    
    public List<SubstituteLogger> getLoggers() {
        return new ArrayList<SubstituteLogger>(this.loggers.values());
    }
    
    public LinkedBlockingQueue<SubstituteLoggingEvent> getEventQueue() {
        return this.eventQueue;
    }
    
    public void postInitialization() {
        this.postInitialization = true;
    }
    
    public void clear() {
        this.loggers.clear();
        this.eventQueue.clear();
    }
}
