// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.mixins.entity;

import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.entity.projectile.EntityFishHook;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ EntityFishHook.class })
public interface EntityFishHookAccessor
{
    @Accessor("inGround")
    boolean inGround();
    
    @Accessor("ticksCatchable")
    int getTicksCatchable();
}
