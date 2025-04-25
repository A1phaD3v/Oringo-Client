// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.movement;

import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class Sprint extends Module
{
    public BooleanSetting omni;
    public BooleanSetting keep;
    
    public Sprint() {
        super("Sprint", 0, Category.MOVEMENT);
        this.omni = new BooleanSetting("OmniSprint", false);
        this.keep = new BooleanSetting("KeepSprint", true);
        this.addSettings(this.keep, this.omni);
    }
}
