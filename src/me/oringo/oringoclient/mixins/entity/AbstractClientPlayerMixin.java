// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.mixins.entity;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.apache.commons.codec.digest.DigestUtils;
import me.oringo.oringoclient.OringoClient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.entity.AbstractClientPlayer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ AbstractClientPlayer.class })
public abstract class AbstractClientPlayerMixin extends PlayerMixin
{
    private static ResourceLocation getCape(final String uuid) {
        return OringoClient.capes.get(DigestUtils.sha256Hex(uuid));
    }
    
    @Inject(method = { "getLocationCape" }, at = { @At("RETURN") }, cancellable = true)
    public void getLocationCape(final CallbackInfoReturnable<ResourceLocation> cir) {
        final ResourceLocation minecons = getCape(((AbstractClientPlayer)this).func_110124_au().toString());
        if (minecons != null) {
            cir.setReturnValue(minecons);
        }
    }
}
