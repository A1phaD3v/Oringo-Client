// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.player;

import me.oringo.oringoclient.events.MoveEvent;
import me.oringo.oringoclient.ui.notifications.Notifications;
import me.oringo.oringoclient.OringoClient;
import me.oringo.oringoclient.mixins.packet.C03Accessor;
import me.oringo.oringoclient.events.PacketSentEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import me.oringo.oringoclient.qolfeatures.module.impl.other.Disabler;
import me.oringo.oringoclient.events.MotionUpdateEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.ModeSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class NoFall extends Module
{
    public ModeSetting mode;
    public ModeSetting hypixelSpoofMode;
    
    public NoFall() {
        super("NoFall", Category.PLAYER);
        this.mode = new ModeSetting("Mode", "Hypixel", new String[] { "Hypixel", "Packet", "NoGround" });
        this.hypixelSpoofMode = new ModeSetting("Spoof mode", "Fall", new String[] { "Always", "Fall" }) {
            @Override
            public boolean isHidden() {
                return !NoFall.this.mode.is("Hypixel");
            }
        };
        this.addSettings(this.mode, this.hypixelSpoofMode);
    }
    
    @Override
    public boolean isToggled() {
        return super.isToggled();
    }
    
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onUpdate(final MotionUpdateEvent event) {
        if (this.isToggled()) {
            final String selected = this.mode.getSelected();
            switch (selected) {
                case "Hypixel": {
                    if ((NoFall.mc.field_71439_g.field_70143_R > 1.5 || this.hypixelSpoofMode.is("Always")) && Disabler.wasEnabled) {
                        event.setOnGround(true);
                        break;
                    }
                    break;
                }
                case "Packet": {
                    if (NoFall.mc.field_71439_g.field_70143_R > 1.5f) {
                        NoFall.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C03PacketPlayer(true));
                        NoFall.mc.field_71439_g.field_70143_R = 0.0f;
                        break;
                    }
                    break;
                }
            }
        }
    }
    
    @SubscribeEvent
    public void onPacket(final PacketSentEvent event) {
        if (event.packet instanceof C03PacketPlayer && this.isToggled()) {
            final String selected = this.mode.getSelected();
            switch (selected) {
                case "NoGround": {
                    ((C03Accessor)event.packet).setOnGround(false);
                    break;
                }
            }
        }
    }
    
    @Override
    public void onEnable() {
        if (!OringoClient.disabler.isToggled()) {
            Notifications.showNotification("Disabler not enabled", 3000, Notifications.NotificationType.WARNING);
        }
    }
    
    @SubscribeEvent
    public void onMove(final MoveEvent event) {
    }
}
