// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.render;

import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class AnimationCreator extends Module
{
    public BooleanSetting swingProgress;
    public BooleanSetting blockProgress;
    public NumberSetting angle1;
    public NumberSetting angle2;
    public NumberSetting angle3;
    public NumberSetting translateX;
    public NumberSetting translateY;
    public NumberSetting translateZ;
    public NumberSetting rotation1x;
    public NumberSetting rotation1y;
    public NumberSetting rotation1z;
    public NumberSetting rotation2x;
    public NumberSetting rotation2y;
    public NumberSetting rotation2z;
    
    public AnimationCreator() {
        super("Animation helper", Category.RENDER);
        this.swingProgress = new BooleanSetting("Swing Progress", false);
        this.blockProgress = new BooleanSetting("Block Progress", true);
        this.angle1 = new NumberSetting("angle1", 30.0, -180.0, 180.0, 1.0);
        this.angle2 = new NumberSetting("angle2", -80.0, -180.0, 180.0, 1.0);
        this.angle3 = new NumberSetting("angle3", 60.0, -180.0, 180.0, 1.0);
        this.translateX = new NumberSetting("x1", -0.5, -5.0, 5.0, 0.1);
        this.translateY = new NumberSetting("y1", 0.2, -5.0, 5.0, 0.1);
        this.translateZ = new NumberSetting("z1", 0.0, -5.0, 5.0, 0.1);
        this.rotation1x = new NumberSetting("x1", 0.0, -5.0, 5.0, 0.1);
        this.rotation1y = new NumberSetting("y1", 1.0, -5.0, 5.0, 0.1);
        this.rotation1z = new NumberSetting("z1", 0.0, -5.0, 5.0, 0.1);
        this.rotation2x = new NumberSetting("x2", 1.0, -5.0, 5.0, 0.1);
        this.rotation2y = new NumberSetting("y2", 0.0, -5.0, 5.0, 0.1);
        this.rotation2z = new NumberSetting("z2", 0.0, -5.0, 5.0, 0.1);
        this.addSettings(this.swingProgress, this.blockProgress, this.translateX, this.translateY, this.translateZ, this.angle1, this.rotation1x, this.rotation1y, this.rotation1z, this.angle2, this.rotation2x, this.rotation2y, this.rotation2z, this.angle3);
    }
}
