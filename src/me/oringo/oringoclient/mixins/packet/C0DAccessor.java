// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.mixins.packet;

import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ C0DPacketCloseWindow.class })
public interface C0DAccessor
{
    @Accessor
    int getWindowId();
}
