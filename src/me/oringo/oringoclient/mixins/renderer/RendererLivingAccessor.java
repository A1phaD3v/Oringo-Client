// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.mixins.renderer;

import org.spongepowered.asm.mixin.gen.Invoker;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ RendererLivingEntity.class })
public interface RendererLivingAccessor
{
    @Invoker("renderModel")
     <T extends EntityLivingBase> void renderModel(final T p0, final float p1, final float p2, final float p3, final float p4, final float p5, final float p6);
    
    @Invoker("renderLayers")
    void renderLayers(final EntityLivingBase p0, final float p1, final float p2, final float p3, final float p4, final float p5, final float p6, final float p7);
}
