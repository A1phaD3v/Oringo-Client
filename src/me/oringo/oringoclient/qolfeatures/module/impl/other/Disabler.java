// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.other;

import me.oringo.oringoclient.ui.notifications.Notifications;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import me.oringo.oringoclient.OringoClient;
import me.oringo.oringoclient.events.PlayerUpdateEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class Disabler extends Module
{
    public static boolean wasEnabled;
    public static final BooleanSetting first;
    public static final BooleanSetting timerSemi;
    
    public Disabler() {
        super("Disabler", Category.OTHER);
        this.addSettings(Disabler.timerSemi, Disabler.first);
    }
    
    @SubscribeEvent
    public void onUpdate(final PlayerUpdateEvent e) {
        if (!Disabler.mc.field_71439_g.func_70093_af() && !Disabler.mc.field_71439_g.func_70115_ae() && this.isToggled() && ((OringoClient.sprint.omni.isEnabled() && OringoClient.sprint.isToggled()) || OringoClient.derp.isToggled() || OringoClient.killAura.isToggled() || OringoClient.speed.isToggled())) {
            Disabler.mc.func_147114_u().func_147297_a((Packet)new C0BPacketEntityAction((Entity)Disabler.mc.field_71439_g, C0BPacketEntityAction.Action.START_SNEAKING));
            Disabler.mc.func_147114_u().func_147297_a((Packet)new C0BPacketEntityAction((Entity)Disabler.mc.field_71439_g, C0BPacketEntityAction.Action.STOP_SNEAKING));
        }
    }
    
    @Override
    public void onEnable() {
        Notifications.showNotification("Disabler will work after lobby change", 2000, Notifications.NotificationType.WARNING);
    }
    
    static {
        Disabler.wasEnabled = false;
        first = new BooleanSetting("First2", true, aBoolean -> true);
        timerSemi = new BooleanSetting("Timer semi", false);
    }
}
