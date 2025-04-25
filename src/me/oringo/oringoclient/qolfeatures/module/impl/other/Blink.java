// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.other;

import me.oringo.oringoclient.utils.PacketUtils;
import me.oringo.oringoclient.events.WorldJoinEvent;
import net.minecraft.network.play.client.C03PacketPlayer;
import me.oringo.oringoclient.events.PacketSentEvent;
import me.oringo.oringoclient.events.PlayerUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import java.util.concurrent.ConcurrentLinkedQueue;
import me.oringo.oringoclient.utils.TickTimer;
import net.minecraft.network.Packet;
import java.util.Queue;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class Blink extends Module
{
    public BooleanSetting onlyPos;
    public BooleanSetting pulse;
    public NumberSetting pulseTicks;
    private Queue<Packet<?>> packets;
    private TickTimer timer;
    
    public Blink() {
        super("Blink", Category.OTHER);
        this.onlyPos = new BooleanSetting("Only pos packets", false);
        this.pulse = new BooleanSetting("Pulse", false);
        this.pulseTicks = new NumberSetting("Pulse ticks", 10.0, 1.0, 100.0, 1.0) {
            @Override
            public boolean isHidden() {
                return !Blink.this.pulse.isEnabled();
            }
        };
        this.packets = new ConcurrentLinkedQueue<Packet<?>>();
        this.timer = new TickTimer();
        this.addSettings(this.onlyPos, this.pulse, this.pulseTicks);
    }
    
    @Override
    public void onEnable() {
        this.timer.reset();
    }
    
    @SubscribeEvent
    public void onDisconnect(final FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        this.packets.clear();
        if (this.isToggled()) {
            this.setToggled(false);
        }
    }
    
    @SubscribeEvent
    public void onUpdate(final PlayerUpdateEvent event) {
        this.timer.updateTicks();
        if (this.timer.passed((int)this.pulseTicks.getValue()) && this.pulse.isEnabled()) {
            this.sendPackets();
            this.timer.reset();
        }
    }
    
    @SubscribeEvent
    public void onPacket(final PacketSentEvent event) {
        if (this.isToggled() && Blink.mc.field_71439_g == null) {
            this.packets.clear();
            this.setToggled(false);
            return;
        }
        if (this.isToggled() && (event.packet instanceof C03PacketPlayer || !this.onlyPos.isEnabled())) {
            event.setCanceled(true);
            this.packets.offer(event.packet);
        }
    }
    
    @SubscribeEvent
    public void onWorld(final WorldJoinEvent event) {
        this.packets.clear();
        if (this.isToggled()) {
            this.toggle();
        }
    }
    
    private void sendPackets() {
        if (Blink.mc.func_147114_u() != null) {
            while (!this.packets.isEmpty()) {
                PacketUtils.sendPacketNoEvent(this.packets.poll());
            }
        }
    }
    
    @Override
    public void onDisable() {
        this.sendPackets();
    }
}
