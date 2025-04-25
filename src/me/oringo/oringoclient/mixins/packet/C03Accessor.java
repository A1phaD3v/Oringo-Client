// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.mixins.packet;

import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ C03PacketPlayer.class })
public interface C03Accessor
{
    @Accessor("x")
    void setX(final double p0);
    
    @Accessor("y")
    void setY(final double p0);
    
    @Accessor("z")
    void setZ(final double p0);
    
    @Accessor
    void setYaw(final float p0);
    
    @Accessor
    void setPitch(final float p0);
    
    @Accessor
    void setOnGround(final boolean p0);
}
