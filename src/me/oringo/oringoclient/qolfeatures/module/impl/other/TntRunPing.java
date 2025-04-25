// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.other;

import net.minecraft.network.play.server.S25PacketBlockBreakAnim;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import io.netty.channel.ChannelHandlerContext;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.init.Blocks;
import net.minecraft.client.Minecraft;
import me.oringo.oringoclient.OringoClient;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.network.play.server.S22PacketMultiBlockChange;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.Arrays;
import me.oringo.oringoclient.events.PacketReceivedEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class TntRunPing extends Module
{
    NumberSetting ping;
    
    public TntRunPing() {
        super("TNT Run ping", 0, Category.OTHER);
        this.ping = new NumberSetting("Ping", 2000.0, 1.0, 2000.0, 1.0);
        this.addSettings(this.ping);
    }
    
    @SubscribeEvent
    public void onPacket(final PacketReceivedEvent event) {
        if (!this.isToggled()) {
            return;
        }
        try {
            final ScoreObjective objective = TntRunPing.mc.field_71439_g.func_96123_co().func_96539_a(1);
            if (!Arrays.asList("TNT RUN", "PVP RUN").contains(ChatFormatting.stripFormatting(objective.func_96678_d()))) {
                return;
            }
        }
        catch (Exception e) {
            return;
        }
        if (event.packet instanceof S22PacketMultiBlockChange && ((S22PacketMultiBlockChange)event.packet).func_179844_a().length <= 10) {
            event.setCanceled(true);
            for (final S22PacketMultiBlockChange.BlockUpdateData changedBlock : ((S22PacketMultiBlockChange)event.packet).func_179844_a()) {
                this.threadBreak(event.context, changedBlock.func_180090_a(), changedBlock.func_180088_c());
            }
        }
        if (event.packet instanceof S23PacketBlockChange) {
            if (OringoClient.stop.contains(((S23PacketBlockChange)event.packet).func_179827_b())) {
                event.setCanceled(true);
            }
            if (!Minecraft.func_71410_x().field_71441_e.func_180495_p(((S23PacketBlockChange)event.packet).func_179827_b()).func_177230_c().equals(Blocks.field_150325_L) && ((S23PacketBlockChange)event.packet).func_180728_a().func_177230_c().equals(Blocks.field_150350_a)) {
                event.setCanceled(true);
                this.threadBreak(event.context, ((S23PacketBlockChange)event.packet).func_179827_b(), ((S23PacketBlockChange)event.packet).func_180728_a());
            }
        }
    }
    
    private void threadBreak(final ChannelHandlerContext context, final BlockPos pos, final IBlockState state) {
        if (!this.isToggled()) {
            return;
        }
        Minecraft.func_71410_x().field_71441_e.func_175656_a(pos, Blocks.field_150325_L.func_176223_P());
        int i;
        new Thread(() -> {
            OringoClient.stop.add(pos);
            for (i = 0; i < 10; ++i) {
                try {
                    Thread.sleep((long)((long)this.ping.getValue() / 10.0));
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    TntRunPing.mc.func_147114_u().func_147294_a(new S25PacketBlockBreakAnim(pos.hashCode(), pos, i));
                }
                catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            OringoClient.stop.remove(pos);
            Minecraft.func_71410_x().field_71441_e.func_175656_a(pos, state);
        }).start();
    }
}
