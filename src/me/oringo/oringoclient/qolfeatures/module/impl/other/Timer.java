// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.other;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.oringo.oringoclient.utils.TimerUtil;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class Timer extends Module
{
    public static final NumberSetting timer;
    
    public Timer() {
        super("Timer", Category.OTHER);
        this.addSettings(Timer.timer);
    }
    
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START && this.isToggled()) {
            TimerUtil.setSpeed((float)Timer.timer.getValue());
        }
    }
    
    @Override
    public void onDisable() {
        if (TimerUtil.getTimer() != null) {
            TimerUtil.resetSpeed();
        }
    }
    
    static {
        timer = new NumberSetting("Timer", 1.0, 0.1, 5.0, 0.1);
    }
}
