// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.settings.impl;

import java.util.function.Predicate;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;

public class BooleanSetting extends Setting
{
    @Expose
    @SerializedName("value")
    private boolean enabled;
    
    public BooleanSetting(final String name, final boolean enabled) {
        super(name);
        this.enabled = enabled;
    }
    
    public BooleanSetting(final String name, final boolean enabled, final Predicate<Boolean> isHidden) {
        super(name, isHidden);
        this.enabled = enabled;
    }
    
    public boolean isEnabled() {
        return this.enabled;
    }
    
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }
    
    public void toggle() {
        this.setEnabled(!this.isEnabled());
    }
}
