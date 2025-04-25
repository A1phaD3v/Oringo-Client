// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.settings;

import java.util.function.Predicate;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;

public class Setting
{
    @Expose
    @SerializedName("name")
    public String name;
    private Predicate<Boolean> predicate;
    
    protected Setting(final String name, final Predicate<Boolean> predicate) {
        this.name = name;
        this.predicate = predicate;
    }
    
    protected Setting(final String name) {
        this(name, null);
    }
    
    public boolean isHidden() {
        return this.predicate != null && this.predicate.test(true);
    }
}
