// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.settings.impl;

import java.util.function.Predicate;
import java.util.Arrays;
import java.util.List;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;

public class ModeSetting extends Setting
{
    @Expose
    @SerializedName("value")
    private String selected;
    private int index;
    private List<String> modes;
    private String defaultSelected;
    
    public ModeSetting(final String name, final String defaultSelected, final String... options) {
        super(name);
        this.defaultSelected = defaultSelected;
        this.modes = Arrays.asList(options);
        this.index = this.modes.indexOf(defaultSelected);
        this.selected = this.modes.get(this.index);
    }
    
    public ModeSetting(final String name, final Predicate<Boolean> isHidden, final String defaultSelected, final String... options) {
        super(name, isHidden);
        this.defaultSelected = defaultSelected;
        this.modes = Arrays.asList(options);
        this.index = this.modes.indexOf(defaultSelected);
        this.selected = this.modes.get(this.index);
    }
    
    public String getSelected() {
        return this.selected;
    }
    
    public void setSelected(final String selected) {
        this.selected = selected;
        this.index = this.modes.indexOf(selected);
    }
    
    public boolean is(final String mode) {
        return mode.equals(this.selected);
    }
    
    public int getIndex() {
        return this.index;
    }
    
    public void setIndex(final int index) {
        this.index = index;
        this.selected = this.modes.get(index);
    }
    
    public List<String> getModes() {
        return this.modes;
    }
    
    public void setModes(final List<String> modes) {
        this.modes = modes;
    }
    
    public void cycle(final int key) {
        switch (key) {
            case 0: {
                if (this.index < this.modes.size() - 1) {
                    ++this.index;
                    this.selected = this.modes.get(this.index);
                    break;
                }
                if (this.index >= this.modes.size() - 1) {
                    this.index = 0;
                    this.selected = this.modes.get(0);
                    break;
                }
                break;
            }
            case 1: {
                if (this.index > 0) {
                    --this.index;
                    this.selected = this.modes.get(this.index);
                    break;
                }
                this.index = this.modes.size() - 1;
                this.selected = this.modes.get(this.index);
                break;
            }
            default: {
                this.index = this.modes.indexOf(this.defaultSelected);
                this.selected = this.modes.get(this.index);
                break;
            }
        }
    }
}
