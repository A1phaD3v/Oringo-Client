// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.render;

import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class Camera extends Module
{
    public BooleanSetting cameraClip;
    public BooleanSetting noHurtCam;
    public BooleanSetting smoothF5;
    public NumberSetting cameraDistance;
    public NumberSetting speed;
    public NumberSetting startSize;
    
    public Camera() {
        super("Camera", Category.RENDER);
        this.cameraClip = new BooleanSetting("Camera Clip", true);
        this.noHurtCam = new BooleanSetting("No hurt cam", false);
        this.smoothF5 = new BooleanSetting("Smooth f5", true);
        this.cameraDistance = new NumberSetting("Camera Distance", 4.0, 2.0, 10.0, 0.1);
        this.speed = new NumberSetting("Smooth speed", 1.0, 0.1, 5.0, 0.1, aBoolean -> !this.smoothF5.isEnabled());
        this.startSize = new NumberSetting("Start distance", 3.0, 1.0, 10.0, 0.1, aBoolean -> !this.smoothF5.isEnabled());
        this.addSettings(this.cameraDistance, this.cameraClip, this.noHurtCam, this.smoothF5, this.speed, this.startSize);
    }
}
