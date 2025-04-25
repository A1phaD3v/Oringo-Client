// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.settings.impl;

import me.oringo.oringoclient.qolfeatures.module.settings.Setting;

public class RunnableSetting extends Setting
{
    private final Runnable runnable;
    
    public RunnableSetting(final String name, final Runnable runnable) {
        super(name);
        this.runnable = runnable;
    }
    
    public void execute() {
        this.runnable.run();
    }
}
