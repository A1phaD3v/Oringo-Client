// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.mixins;

import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraftforge.fml.common.eventhandler.Event;
import me.oringo.oringoclient.events.PreAttackEvent;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import me.oringo.oringoclient.OringoClient;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.Block;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ PlayerControllerMP.class })
public class PlayerControllerMixin
{
    @Redirect(method = { "onPlayerDamageBlock" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getPlayerRelativeBlockHardness(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/world/World;Lnet/minecraft/util/BlockPos;)F"))
    public float onPlayerDamageBlock(final Block instance, final EntityPlayer playerIn, final World worldIn, final BlockPos pos) {
        float hardness = instance.func_180647_a(playerIn, worldIn, pos);
        if (OringoClient.fastBreak.isToggled()) {
            hardness *= (float)OringoClient.fastBreak.mineSpeed.getValue();
        }
        return hardness;
    }
    
    @Inject(method = { "attackEntity" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/network/NetHandlerPlayClient;addToSendQueue(Lnet/minecraft/network/Packet;)V") })
    public void attackEntity(final EntityPlayer playerIn, final Entity targetEntity, final CallbackInfo ci) {
        if (MinecraftForge.EVENT_BUS.post((Event)new PreAttackEvent(targetEntity))) {
            ci.cancel();
        }
    }
}
