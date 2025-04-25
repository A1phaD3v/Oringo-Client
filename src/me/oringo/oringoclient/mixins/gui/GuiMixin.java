// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.mixins.gui;

import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ Gui.class })
public abstract class GuiMixin
{
    @Shadow
    public static void func_73734_a(final int left, final int top, final int right, final int bottom, final int color) {
    }
    
    @Shadow
    protected abstract void func_73733_a(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5);
    
    @Shadow
    public abstract void func_73729_b(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5);
}
