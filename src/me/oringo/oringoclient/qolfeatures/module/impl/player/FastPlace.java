// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.player;

import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class FastPlace extends Module
{
    private static FastPlace instance;
    public NumberSetting placeDelay;
    
    public static FastPlace getInstance() {
        return FastPlace.instance;
    }
    
    public FastPlace() {
        super("Fast Place", Category.PLAYER);
        this.addSetting(this.placeDelay = new NumberSetting("Place delay", 2.0, 0.0, 4.0, 1.0));
    }
    
    static {
        FastPlace.instance = new FastPlace();
    }
}
