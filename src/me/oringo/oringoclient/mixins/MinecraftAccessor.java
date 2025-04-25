// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.mixins;

import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.util.Timer;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ Minecraft.class })
public interface MinecraftAccessor
{
    @Accessor
    Timer getTimer();
}
