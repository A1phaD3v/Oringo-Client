// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.mixins.gui;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraftforge.fml.common.eventhandler.Event;
import me.oringo.oringoclient.events.GuiChatEvent;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.gui.GuiChat;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = { GuiChat.class }, priority = 1)
public abstract class GuiChatMixin extends GuiScreenMixin
{
    @Inject(method = { "drawScreen" }, at = { @At("RETURN") })
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks, final CallbackInfo ci) {
        if (MinecraftForge.EVENT_BUS.post((Event)new GuiChatEvent.DrawChatEvent(mouseX, mouseY))) {
            ci.cancel();
        }
    }
    
    @Inject(method = { "keyTyped" }, at = { @At("RETURN") })
    public void keyTyped(final char typedChar, final int keyCode, final CallbackInfo ci) {
        if (MinecraftForge.EVENT_BUS.post((Event)new GuiChatEvent.KeyTyped(keyCode, typedChar))) {
            ci.cancel();
        }
    }
    
    @Inject(method = { "mouseClicked" }, at = { @At("RETURN") })
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton, final CallbackInfo ci) {
        if (MinecraftForge.EVENT_BUS.post((Event)new GuiChatEvent.MouseClicked(mouseX, mouseY, mouseButton))) {
            ci.cancel();
        }
    }
    
    @Override
    protected void func_146286_b(final int mouseX, final int mouseY, final int state) {
        MinecraftForge.EVENT_BUS.post((Event)new GuiChatEvent.MouseReleased(mouseX, mouseY, state));
    }
    
    @Inject(method = { "onGuiClosed" }, at = { @At("RETURN") })
    public void onGuiClosed(final CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post((Event)new GuiChatEvent.Closed());
    }
}
