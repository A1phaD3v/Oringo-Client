// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.combat;

import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class Reach extends Module
{
    public NumberSetting reach;
    public NumberSetting blockReach;
    
    public Reach() {
        super("Reach", 0, Category.COMBAT);
        this.reach = new NumberSetting("Range", 3.0, 2.0, 4.5, 0.1);
        this.blockReach = new NumberSetting("Block Range", 4.5, 2.0, 6.0, 0.01);
        this.addSettings(this.reach, this.blockReach);
    }
}
