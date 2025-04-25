// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.other;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.oringo.oringoclient.ui.notifications.Notifications;
import me.oringo.oringoclient.utils.api.PlanckeScraper;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.utils.MilliTimer;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class StaffAnalyser extends Module
{
    private NumberSetting delay;
    private MilliTimer timer;
    private int lastBans;
    
    public StaffAnalyser() {
        super("Staff Analyser", Category.OTHER);
        this.delay = new NumberSetting("Delay", 5.0, 5.0, 60.0, 1.0);
        this.timer = new MilliTimer();
        this.lastBans = -1;
        this.addSettings(this.delay);
    }
    
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (this.isToggled() && this.timer.hasTimePassed((long)(this.delay.getValue() * 1000.0))) {
            this.timer.reset();
            final int bans;
            new Thread(() -> {
                bans = PlanckeScraper.getBans();
                if (bans != this.lastBans && this.lastBans != -1 && bans > this.lastBans) {
                    Notifications.showNotification(String.format("Staff has banned %s %s in last %s seconds", bans - this.lastBans, (bans - this.lastBans > 1) ? "people" : "person", (int)this.delay.getValue()), 2500, (bans - this.lastBans > 2) ? Notifications.NotificationType.WARNING : Notifications.NotificationType.INFO);
                }
                this.lastBans = bans;
            }).start();
        }
    }
}
