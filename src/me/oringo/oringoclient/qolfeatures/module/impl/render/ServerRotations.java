// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.render;

import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class ServerRotations extends Module
{
    private static ServerRotations instance;
    public BooleanSetting onlyKillAura;
    
    public static ServerRotations getInstance() {
        return ServerRotations.instance;
    }
    
    public ServerRotations() {
        super("Server Rotations", Category.RENDER);
        this.onlyKillAura = new BooleanSetting("Only aura rotations", false);
        this.setToggled(true);
        this.addSettings(this.onlyKillAura);
    }
    
    static {
        ServerRotations.instance = new ServerRotations();
    }
}
