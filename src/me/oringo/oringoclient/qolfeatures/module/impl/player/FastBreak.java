// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.player;

import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class FastBreak extends Module
{
    public NumberSetting mineSpeed;
    public NumberSetting maxBlocks;
    
    public FastBreak() {
        super("Fast break", 0, Category.PLAYER);
        this.mineSpeed = new NumberSetting("Mining speed", 1.4, 1.0, 1.6, 0.1);
        this.maxBlocks = new NumberSetting("Additional blocks", 0.0, 0.0, 4.0, 1.0);
        this.addSettings(this.maxBlocks, this.mineSpeed);
    }
}
