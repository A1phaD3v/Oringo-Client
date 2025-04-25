// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.mixins.renderer;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraftforge.fml.common.eventhandler.Event;
import me.oringo.oringoclient.events.RenderChestEvent;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.client.renderer.tileentity.TileEntityChestRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ TileEntityChestRenderer.class })
public class RendererChestMixin
{
    @Inject(method = { "renderTileEntityAt(Lnet/minecraft/tileentity/TileEntityChest;DDDFI)V" }, at = { @At("HEAD") }, cancellable = true)
    public void onDrawChest(final TileEntityChest te, final double x, final double y, final double z, final float partialTicks, final int destroyStage, final CallbackInfo ci) {
        if (MinecraftForge.EVENT_BUS.post((Event)new RenderChestEvent.Pre(te, x, y, z, partialTicks, destroyStage))) {
            ci.cancel();
        }
    }
    
    @Inject(method = { "renderTileEntityAt(Lnet/minecraft/tileentity/TileEntityChest;DDDFI)V" }, at = { @At("RETURN") }, cancellable = true)
    public void onDrawChestPost(final TileEntityChest te, final double x, final double y, final double z, final float partialTicks, final int destroyStage, final CallbackInfo ci) {
        if (MinecraftForge.EVENT_BUS.post((Event)new RenderChestEvent.Post(te, x, y, z, partialTicks, destroyStage))) {
            ci.cancel();
        }
    }
}
