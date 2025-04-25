// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.mixins;

import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.client.network.NetHandlerPlayClient;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ NetHandlerPlayClient.class })
public interface NetPlayHandlerAccessor
{
    @Accessor("doneLoadingTerrain")
    void setDoneLoadingTerrain(final boolean p0);
    
    @Accessor("doneLoadingTerrain")
    boolean isDoneLoadingTerrain();
}
