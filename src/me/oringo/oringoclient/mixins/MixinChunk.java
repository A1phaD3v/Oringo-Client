// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.mixins;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraftforge.fml.common.eventhandler.Event;
import me.oringo.oringoclient.events.BlockChangeEvent;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ Chunk.class })
public class MixinChunk
{
    @Inject(method = { "setBlockState" }, at = { @At("HEAD") }, cancellable = true)
    private void onBlockChange(final BlockPos pos, final IBlockState state, final CallbackInfoReturnable<IBlockState> cir) {
        if (MinecraftForge.EVENT_BUS.post((Event)new BlockChangeEvent(pos, state))) {
            cir.setReturnValue(state);
        }
    }
}
