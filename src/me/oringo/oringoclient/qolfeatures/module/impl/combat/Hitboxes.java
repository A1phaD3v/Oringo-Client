// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.combat;

import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class Hitboxes extends Module
{
    public BooleanSetting onlyPlayers;
    public NumberSetting expand;
    
    public Hitboxes() {
        super("Hitboxes", Category.COMBAT);
        this.onlyPlayers = new BooleanSetting("Only players", false);
        this.expand = new NumberSetting("Expand", 0.5, 0.1, 1.0, 0.1);
        this.addSettings(this.expand);
    }
}
