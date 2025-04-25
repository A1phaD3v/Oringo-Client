// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.player;

import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class Velocity extends Module
{
    public NumberSetting vModifier;
    public NumberSetting hModifier;
    public BooleanSetting skyblockKB;
    
    public Velocity() {
        super("Velocity", 0, Category.PLAYER);
        this.vModifier = new NumberSetting("Vertical", 0.0, -2.0, 2.0, 0.05);
        this.hModifier = new NumberSetting("Horizontal", 0.0, -2.0, 2.0, 0.05);
        this.skyblockKB = new BooleanSetting("Skyblock kb", true);
        this.addSettings(this.hModifier, this.vModifier, this.skyblockKB);
    }
}
