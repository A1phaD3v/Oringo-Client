// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.skyblock;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.event.world.WorldEvent;
import me.oringo.oringoclient.events.BlockChangeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.oringo.oringoclient.OringoClient;
import net.minecraft.init.Blocks;
import net.minecraft.util.Vec3;
import me.oringo.oringoclient.utils.SkyblockUtils;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraft.util.BlockPos;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class AutoS1 extends Module
{
    private boolean clicked;
    private boolean clickedButton;
    private static BlockPos clickPos;
    
    public AutoS1() {
        super("Auto SS", 0, Category.SKYBLOCK);
    }
    
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (AutoS1.mc.field_71439_g == null || !SkyblockUtils.inDungeon || !this.isToggled() || !SkyblockUtils.inP3) {
            return;
        }
        if (AutoS1.mc.field_71439_g.func_174824_e(0.0f).func_72438_d(new Vec3(309.0, 121.0, 290.0)) < 5.5 && !this.clicked && AutoS1.mc.field_71441_e.func_180495_p(new BlockPos(309, 121, 290)).func_177230_c() == Blocks.field_150430_aB) {
            this.clickBlock(new BlockPos(309, 121, 290));
            this.clicked = true;
            this.clickedButton = false;
        }
        if (AutoS1.clickPos != null && AutoS1.mc.field_71439_g.func_70011_f((double)AutoS1.clickPos.func_177958_n(), (double)(AutoS1.clickPos.func_177956_o() - AutoS1.mc.field_71439_g.func_70047_e()), (double)AutoS1.clickPos.func_177952_p()) < 5.5 && !this.clickedButton && AutoS1.mc.field_71441_e.func_180495_p(AutoS1.clickPos).func_177230_c() == Blocks.field_150430_aB) {
            for (int i = 0; i < 20; ++i) {
                this.clickBlock(AutoS1.clickPos);
            }
            AutoS1.clickPos = null;
            this.clickedButton = true;
            OringoClient.sendMessageWithPrefix("Clicked!");
        }
    }
    
    @SubscribeEvent
    public void onPacket(final BlockChangeEvent event) {
        if (this.clicked && !this.clickedButton && SkyblockUtils.inP3 && event.state.func_177230_c() == Blocks.field_180398_cJ && event.pos.func_177958_n() == 310 && event.pos.func_177956_o() >= 120 && event.pos.func_177956_o() <= 123 && event.pos.func_177952_p() >= 291 && event.pos.func_177952_p() <= 294) {
            AutoS1.clickPos = new BlockPos(event.pos.func_177958_n() - 1, event.pos.func_177956_o(), event.pos.func_177952_p());
        }
    }
    
    @SubscribeEvent
    public void onWorldChange(final WorldEvent.Load event) {
        this.clicked = false;
        AutoS1.clickPos = null;
        this.clickedButton = false;
    }
    
    private void clickBlock(final BlockPos hitPos) {
        final Vec3 hitVec = new Vec3(0.0, 0.0, 0.0);
        final float f = (float)(hitVec.field_72450_a - hitPos.func_177958_n());
        final float f2 = (float)(hitVec.field_72448_b - hitPos.func_177956_o());
        final float f3 = (float)(hitVec.field_72449_c - hitPos.func_177952_p());
        AutoS1.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C08PacketPlayerBlockPlacement(hitPos, EnumFacing.func_176733_a((double)AutoS1.mc.field_71439_g.field_70177_z).func_176745_a(), AutoS1.mc.field_71439_g.field_71071_by.func_70448_g(), f, f2, f3));
    }
}
