// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.mixins;

import net.minecraft.network.play.server.S12PacketEntityVelocity;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.client.entity.EntityPlayerSP;
import me.oringo.oringoclient.utils.SkyblockUtils;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.world.Explosion;
import net.minecraft.util.IThreadListener;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketThreadUtil;
import me.oringo.oringoclient.OringoClient;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.network.play.server.S27PacketExplosion;
import org.spongepowered.asm.mixin.Final;
import net.minecraft.network.NetworkManager;
import net.minecraft.client.multiplayer.WorldClient;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = { NetHandlerPlayClient.class }, priority = 1)
public abstract class PlayHandlerMixin
{
    @Shadow
    private Minecraft field_147299_f;
    @Shadow
    private WorldClient field_147300_g;
    @Shadow
    private boolean field_147309_h;
    @Shadow
    @Final
    private NetworkManager field_147302_e;
    
    @Inject(method = { "handleExplosion" }, at = { @At("HEAD") }, cancellable = true)
    private void handleExplosion(final S27PacketExplosion packetIn, final CallbackInfo ci) {
        if (OringoClient.velocity.isToggled() || OringoClient.speed.isToggled()) {
            PacketThreadUtil.func_180031_a((Packet)packetIn, (INetHandler)OringoClient.mc.func_147114_u(), (IThreadListener)this.field_147299_f);
            final Explosion explosion = new Explosion((World)this.field_147299_f.field_71441_e, (Entity)null, packetIn.func_149148_f(), packetIn.func_149143_g(), packetIn.func_149145_h(), packetIn.func_149146_i(), packetIn.func_149150_j());
            explosion.func_77279_a(true);
            final boolean shouldTakeKB = OringoClient.velocity.skyblockKB.isEnabled() && (Minecraft.func_71410_x().field_71439_g.func_180799_ab() || SkyblockUtils.getDisplayName(Minecraft.func_71410_x().field_71439_g.func_70694_bm()).contains("Bonzo's Staff") || SkyblockUtils.getDisplayName(Minecraft.func_71410_x().field_71439_g.func_70694_bm()).contains("Jerry-chine Gun"));
            if ((shouldTakeKB || OringoClient.velocity.hModifier.getValue() != 0.0 || OringoClient.velocity.vModifier.getValue() != 0.0) && !OringoClient.speed.isToggled()) {
                final EntityPlayerSP field_71439_g = this.field_147299_f.field_71439_g;
                field_71439_g.field_70159_w += packetIn.func_149149_c() * (shouldTakeKB ? 1.0 : OringoClient.velocity.hModifier.getValue());
                final EntityPlayerSP field_71439_g2 = this.field_147299_f.field_71439_g;
                field_71439_g2.field_70181_x += packetIn.func_149144_d() * (shouldTakeKB ? 1.0 : OringoClient.velocity.vModifier.getValue());
                final EntityPlayerSP field_71439_g3 = this.field_147299_f.field_71439_g;
                field_71439_g3.field_70179_y += packetIn.func_149147_e() * (shouldTakeKB ? 1.0 : OringoClient.velocity.hModifier.getValue());
            }
            ci.cancel();
        }
    }
    
    @Inject(method = { "handleEntityVelocity" }, at = { @At("HEAD") }, cancellable = true)
    public void handleEntityVelocity(final S12PacketEntityVelocity packetIn, final CallbackInfo ci) {
        if (OringoClient.velocity.isToggled() || OringoClient.speed.isToggled()) {
            PacketThreadUtil.func_180031_a((Packet)packetIn, (INetHandler)OringoClient.mc.func_147114_u(), (IThreadListener)this.field_147299_f);
            final Entity entity = this.field_147300_g.func_73045_a(packetIn.func_149412_c());
            if (entity != null) {
                if (entity.equals((Object)OringoClient.mc.field_71439_g)) {
                    final boolean shouldTakeKB = OringoClient.velocity.skyblockKB.isEnabled() && (Minecraft.func_71410_x().field_71439_g.func_180799_ab() || SkyblockUtils.getDisplayName(Minecraft.func_71410_x().field_71439_g.func_70694_bm()).contains("Bonzo's Staff") || SkyblockUtils.getDisplayName(Minecraft.func_71410_x().field_71439_g.func_70694_bm()).contains("Jerry-chine Gun"));
                    if ((shouldTakeKB || OringoClient.velocity.hModifier.getValue() != 0.0 || OringoClient.velocity.vModifier.getValue() != 0.0) && !OringoClient.speed.isToggled()) {
                        entity.func_70016_h(packetIn.func_149411_d() * (shouldTakeKB ? 1.0 : OringoClient.velocity.hModifier.getValue()) / 8000.0, packetIn.func_149410_e() * (shouldTakeKB ? 1.0 : OringoClient.velocity.vModifier.getValue()) / 8000.0, packetIn.func_149409_f() * (shouldTakeKB ? 1.0 : OringoClient.velocity.hModifier.getValue()) / 8000.0);
                    }
                }
                else {
                    entity.func_70016_h(packetIn.func_149411_d() / 8000.0, packetIn.func_149410_e() / 8000.0, packetIn.func_149409_f() / 8000.0);
                }
            }
            ci.cancel();
        }
    }
}
