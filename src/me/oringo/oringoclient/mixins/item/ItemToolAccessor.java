// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.mixins.item;

import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.item.ItemTool;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ ItemTool.class })
public interface ItemToolAccessor
{
    @Accessor("toolClass")
    String getToolClass();
}
