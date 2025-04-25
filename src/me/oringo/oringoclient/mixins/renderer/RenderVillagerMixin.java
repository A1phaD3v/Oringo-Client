// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.mixins.renderer;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.client.renderer.GlStateManager;
import me.oringo.oringoclient.OringoClient;
import net.minecraft.entity.passive.EntityVillager;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.renderer.entity.RenderVillager;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ RenderVillager.class })
public class RenderVillagerMixin
{
    @Inject(method = { "preRenderCallback(Lnet/minecraft/entity/passive/EntityVillager;F)V" }, at = { @At("HEAD") }, cancellable = true)
    private <T extends EntityVillager> void onPreRenderCallback(final T entitylivingbaseIn, final float partialTickTime, final CallbackInfo ci) {
        if (OringoClient.giants.isToggled() && OringoClient.giants.mobs.isEnabled()) {
            GlStateManager.func_179139_a(OringoClient.giants.scale.getValue(), OringoClient.giants.scale.getValue(), OringoClient.giants.scale.getValue());
        }
    }
}
