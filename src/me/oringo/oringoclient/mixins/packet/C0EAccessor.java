// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.mixins.packet;

import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ C0EPacketClickWindow.class })
public interface C0EAccessor
{
    @Accessor("windowID")
    void setWindowID(final int p0);
}
