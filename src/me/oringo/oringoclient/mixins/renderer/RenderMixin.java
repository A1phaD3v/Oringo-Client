// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.mixins.renderer;

import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.entity.Render;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ Render.class })
public abstract class RenderMixin
{
    @Shadow
    public <T extends Entity> void func_76986_a(final T entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
    }
}
