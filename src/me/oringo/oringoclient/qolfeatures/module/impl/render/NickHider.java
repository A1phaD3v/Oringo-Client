// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.render;

import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.StringSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class NickHider extends Module
{
    public StringSetting name;
    
    public NickHider() {
        super("Nick Hider", 0, Category.RENDER);
        this.name = new StringSetting("Name");
        this.addSettings(this.name);
    }
}
