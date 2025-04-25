// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.other;

import net.minecraft.network.Packet;
import net.minecraft.util.EnumFacing;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import java.util.Random;
import me.oringo.oringoclient.events.MotionUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import me.oringo.oringoclient.events.PacketSentEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.ModeSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class ServerBeamer extends Module
{
    public NumberSetting beamer;
    public NumberSetting randomSend;
    public BooleanSetting start;
    public ModeSetting mode;
    private int i;
    
    public ServerBeamer() {
        super("Server Beamer", Category.OTHER);
        this.beamer = new NumberSetting("Packets", 10.0, 1.0, 50.0, 1.0);
        this.randomSend = new NumberSetting("Send ticks", 0.0, 0.0, 100.0, 1.0);
        this.start = new BooleanSetting("Start Breaking", true);
        this.mode = new ModeSetting("Mode", "Sync", new String[] { "Sync", "Async" });
        this.i = 0;
        this.addSettings(this.beamer, this.randomSend, this.mode, this.start);
    }
    
    @SubscribeEvent
    public void onUpdate(final PacketSentEvent event) {
        if (!this.isToggled() || !(event.packet instanceof C0FPacketConfirmTransaction) || !this.mode.is("Sync")) {
            return;
        }
        this.beam();
    }
    
    @SubscribeEvent
    public void onMotion(final MotionUpdateEvent.Pre event) {
        if (!this.isToggled()) {
            return;
        }
        if (this.randomSend.getValue() != 0.0 && this.i++ % this.randomSend.getValue() == 0.0) {
            this.beam();
        }
        if (!this.mode.is("Async")) {
            return;
        }
        this.beam();
    }
    
    private void beam() {
        for (int i = 0; i < this.beamer.getValue(); ++i) {
            final BlockPos pos = new BlockPos(new Random().nextInt(10000) * 16, 255, new Random().nextInt(10000) * 16);
            ServerBeamer.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C07PacketPlayerDigging(this.start.isEnabled() ? C07PacketPlayerDigging.Action.START_DESTROY_BLOCK : C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, EnumFacing.func_176733_a((double)ServerBeamer.mc.field_71439_g.field_70177_z)));
        }
    }
}
