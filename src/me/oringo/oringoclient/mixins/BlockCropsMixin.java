// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.mixins;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.block.BlockCrops;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ BlockCrops.class })
public abstract class BlockCropsMixin extends BlockMixin
{
    @Inject(method = { "<init>" }, at = { @At("RETURN") })
    private void init(final CallbackInfo ci) {
        this.func_149676_a(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
    }
}
