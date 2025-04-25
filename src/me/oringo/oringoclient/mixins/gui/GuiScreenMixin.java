// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.mixins.gui;

import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.client.gui.GuiYesNoCallback;

@Mixin({ GuiScreen.class })
public abstract class GuiScreenMixin extends GuiMixin implements GuiYesNoCallback
{
    @Shadow
    public Minecraft field_146297_k;
    @Shadow
    public int field_146295_m;
    @Shadow
    public int field_146294_l;
    
    @Shadow
    protected void func_146286_b(final int mouseX, final int mouseY, final int state) {
    }
    
    @Shadow
    public abstract void func_73863_a(final int p0, final int p1, final float p2);
}
