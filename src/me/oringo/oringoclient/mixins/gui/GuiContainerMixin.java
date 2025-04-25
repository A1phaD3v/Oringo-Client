// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.mixins.gui;

import org.lwjgl.opengl.GL11;
import me.oringo.oringoclient.qolfeatures.module.impl.render.PopupAnimation;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraft.client.renderer.GlStateManager;
import me.oringo.oringoclient.utils.font.Fonts;
import net.minecraft.client.gui.ScaledResolution;
import me.oringo.oringoclient.OringoClient;
import net.minecraft.inventory.ContainerChest;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.inventory.Container;
import net.minecraft.client.gui.inventory.GuiContainer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ GuiContainer.class })
public abstract class GuiContainerMixin extends GuiScreenMixin
{
    @Shadow
    public Container field_147002_h;
    
    @Inject(method = { "drawScreen" }, at = { @At("HEAD") }, cancellable = true)
    public void onDrawScreen(final int k1, final int slot, final float i1, final CallbackInfo ci) {
        if (this.field_147002_h instanceof ContainerChest && OringoClient.guiMove.shouldHideGui((ContainerChest)this.field_147002_h)) {
            this.field_146297_k.field_71415_G = true;
            this.field_146297_k.field_71417_B.func_74372_a();
            final ScaledResolution res = new ScaledResolution(this.field_146297_k);
            Fonts.robotoBig.drawSmoothCenteredStringWithShadow("In terminal!", res.func_78326_a() / 2, res.func_78328_b() / 2, OringoClient.clickGui.getColor().getRGB());
            GlStateManager.func_179131_c(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.func_179131_c(1.0f, 1.0f, 1.0f, 1.0f);
            MinecraftForge.EVENT_BUS.post((Event)new GuiScreenEvent.BackgroundDrawnEvent((GuiScreen)this));
            ci.cancel();
        }
    }
    
    @Inject(method = { "drawScreen" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/inventory/GuiContainer;drawGuiContainerBackgroundLayer(FII)V") }, cancellable = true)
    public void onDrawBackground(final int k1, final int slot, final float i1, final CallbackInfo ci) {
        if (PopupAnimation.shouldScale((GuiScreen)this)) {
            GL11.glPushMatrix();
            PopupAnimation.doScaling();
        }
    }
    
    @Inject(method = { "drawScreen" }, at = { @At("RETURN") }, cancellable = true)
    public void onDrawScreenPost(final int k1, final int slot, final float i1, final CallbackInfo ci) {
        if (PopupAnimation.shouldScale((GuiScreen)this)) {
            GL11.glPopMatrix();
        }
    }
}
