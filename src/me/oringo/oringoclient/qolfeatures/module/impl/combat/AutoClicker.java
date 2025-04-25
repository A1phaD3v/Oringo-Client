// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.combat;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.oringo.oringoclient.utils.SkyblockUtils;
import me.oringo.oringoclient.utils.MathUtil;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.utils.MilliTimer;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.ModeSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class AutoClicker extends Module
{
    public static final NumberSetting maxCps;
    public static final NumberSetting minCps;
    public static final ModeSetting mode;
    private MilliTimer timer;
    private double nextDelay;
    
    public AutoClicker() {
        super("AutoClicker", Category.COMBAT);
        this.timer = new MilliTimer();
        this.nextDelay = 10.0;
        this.addSettings(AutoClicker.minCps, AutoClicker.maxCps, AutoClicker.mode);
    }
    
    @SubscribeEvent
    public void onTick(final RenderWorldLastEvent event) {
        if (this.isToggled() && AutoClicker.mc.field_71439_g != null && this.isPressed() && !AutoClicker.mc.field_71439_g.func_71039_bw() && AutoClicker.mc.field_71462_r == null && this.timer.hasTimePassed((long)(1000.0 / this.nextDelay))) {
            this.timer.reset();
            this.nextDelay = MathUtil.getRandomInRange(AutoClicker.maxCps.getValue(), AutoClicker.minCps.getValue());
            SkyblockUtils.click();
        }
    }
    
    @Override
    public boolean isPressed() {
        final String selected = AutoClicker.mode.getSelected();
        switch (selected) {
            case "Key held": {
                return super.isPressed();
            }
            case "Toggle": {
                return this.isToggled();
            }
            default: {
                return AutoClicker.mc.field_71474_y.field_74312_F.func_151470_d();
            }
        }
    }
    
    @Override
    public boolean isKeybind() {
        return !AutoClicker.mode.is("Toggle");
    }
    
    static {
        maxCps = new NumberSetting("Max CPS", 12.0, 1.0, 20.0, 1.0) {
            @Override
            public void setValue(final double value) {
                super.setValue(value);
                AutoClicker.minCps.setMax(AutoClicker.maxCps.getValue());
                if (AutoClicker.minCps.getValue() > AutoClicker.minCps.getMax()) {
                    AutoClicker.minCps.setValue(AutoClicker.minCps.getMin());
                }
            }
        };
        minCps = new NumberSetting("Min CPS", 10.0, 1.0, 20.0, 1.0) {
            @Override
            public void setValue(final double value) {
                super.setValue(value);
                AutoClicker.maxCps.setMin(AutoClicker.minCps.getValue());
                if (AutoClicker.maxCps.getValue() < AutoClicker.maxCps.getMin()) {
                    AutoClicker.maxCps.setValue(AutoClicker.maxCps.getMin());
                }
            }
        };
        mode = new ModeSetting("Mode", "Attack held", new String[] { "Key held", "Toggle", "Attack held" });
    }
}
