// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.mixins.packet;

import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.network.play.client.C02PacketUseEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ C02PacketUseEntity.class })
public interface C02Accessor
{
    @Accessor
    void setEntityId(final int p0);
    
    @Accessor
    void setAction(final C02PacketUseEntity.Action p0);
}
