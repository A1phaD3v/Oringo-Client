// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.mixins.packet;

import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.network.handshake.client.C00Handshake;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ C00Handshake.class })
public interface HandshakeAccessor
{
    @Accessor
    String getIp();
    
    @Accessor
    void setProtocolVersion(final int p0);
}
