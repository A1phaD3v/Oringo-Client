// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.mixins.gui;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import me.oringo.oringoclient.utils.font.Fonts;
import me.oringo.oringoclient.utils.RenderUtils;
import java.awt.Color;
import net.minecraft.client.renderer.GlStateManager;
import me.oringo.oringoclient.OringoClient;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Final;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.gui.GuiButton;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ GuiButton.class })
public abstract class MixinGuiButton extends GuiMixin
{
    @Shadow
    public boolean field_146125_m;
    @Shadow
    @Final
    protected static ResourceLocation field_146122_a;
    @Shadow
    public int field_146121_g;
    @Shadow
    protected boolean field_146123_n;
    @Shadow
    public int field_146128_h;
    @Shadow
    public int field_146129_i;
    @Shadow
    public int field_146120_f;
    @Shadow
    public int packedFGColour;
    @Shadow
    public boolean field_146124_l;
    @Shadow
    public String field_146126_j;
    
    @Shadow
    public abstract int func_146117_b();
    
    @Shadow
    protected abstract int func_146114_a(final boolean p0);
    
    @Shadow
    protected abstract void func_146119_b(final Minecraft p0, final int p1, final int p2);
    
    @Inject(method = { "drawButton" }, at = { @At("HEAD") }, cancellable = true)
    public void drawButton(final Minecraft mc, final int mouseX, final int mouseY, final CallbackInfo callbackInfo) {
        if (this.field_146125_m && OringoClient.interfaces.customButtons.isEnabled()) {
            GlStateManager.func_179131_c(1.0f, 1.0f, 1.0f, 1.0f);
            this.field_146123_n = (mouseX >= this.field_146128_h && mouseY >= this.field_146129_i && mouseX < this.field_146128_h + this.field_146120_f && mouseY < this.field_146129_i + this.field_146121_g);
            GlStateManager.func_179147_l();
            GlStateManager.func_179120_a(770, 771, 1, 0);
            GlStateManager.func_179112_b(770, 771);
            for (float i = 0.0f; i < 2.0f; i += 0.5) {
                RenderUtils.drawRoundedRect(this.field_146128_h - i, this.field_146129_i + i, this.field_146128_h + this.field_146120_f - i, this.field_146129_i + this.field_146121_g + i, 2.0, new Color(21, 21, 21, 30).getRGB());
            }
            RenderUtils.drawRoundedRect(this.field_146128_h, this.field_146129_i, this.field_146128_h + this.field_146120_f, this.field_146129_i + this.field_146121_g, 2.0, new Color(21, 21, 21, 180).getRGB());
            this.drawGradientRect(0.0f, 255);
            this.func_146119_b(mc, mouseX, mouseY);
            Fonts.robotoMediumBold.drawSmoothCenteredString(this.field_146126_j, this.field_146128_h + this.field_146120_f / 2.0f, this.field_146129_i + (this.field_146121_g - Fonts.robotoMediumBold.getHeight()) / 2.0f, this.field_146123_n ? Color.white.getRGB() : new Color(200, 200, 200).getRGB());
            GlStateManager.func_179131_c(1.0f, 1.0f, 1.0f, 1.0f);
            callbackInfo.cancel();
        }
    }
    
    public void drawGradientRect(final float expand, final int opacity) {
        if (OringoClient.interfaces.buttonLine.is("Wave")) {
            this.start2ColorDraw();
            float prevPos = (float)this.field_146128_h;
            for (int i = 1; i < 11; ++i) {
                final float pos = this.field_146128_h + i * 0.1f * this.field_146120_f;
                if (OringoClient.interfaces.lineLocation.is("Top")) {
                    this.addVertexes(prevPos - expand, this.field_146129_i - expand, pos + expand, this.field_146129_i + 1.5f + expand, RenderUtils.applyOpacity(OringoClient.clickGui.getColor(i), opacity).getRGB(), RenderUtils.applyOpacity(OringoClient.clickGui.getColor(i + 1), opacity).getRGB());
                }
                else {
                    this.addVertexes(prevPos - expand, this.field_146129_i - expand + this.field_146121_g - 1.5f, pos + expand, this.field_146129_i + this.field_146121_g + expand, RenderUtils.applyOpacity(OringoClient.clickGui.getColor(i), opacity).getRGB(), RenderUtils.applyOpacity(OringoClient.clickGui.getColor(i + 1), opacity).getRGB());
                }
                prevPos = pos;
            }
            this.end2ColorDraw();
        }
        else if (OringoClient.interfaces.buttonLine.is("Single")) {
            if (OringoClient.interfaces.lineLocation.is("Top")) {
                RenderUtils.drawRect(this.field_146128_h - expand, this.field_146129_i - expand, this.field_146128_h + this.field_146120_f + expand, this.field_146129_i + 1.5f + expand, RenderUtils.applyOpacity(OringoClient.clickGui.getColor(), opacity).getRGB());
            }
            else {
                RenderUtils.drawRect(this.field_146128_h - expand, this.field_146129_i - expand + this.field_146121_g - 1.5f, this.field_146128_h + this.field_146120_f + expand, this.field_146129_i + this.field_146121_g + expand, RenderUtils.applyOpacity(OringoClient.clickGui.getColor(), opacity).getRGB());
            }
        }
    }
    
    public void start2ColorDraw() {
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179120_a(770, 771, 1, 0);
        GlStateManager.func_179103_j(7425);
        final WorldRenderer worldrenderer = Tessellator.func_178181_a().func_178180_c();
        worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181706_f);
    }
    
    public void end2ColorDraw() {
        Tessellator.func_178181_a().func_78381_a();
        GlStateManager.func_179103_j(7424);
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }
    
    public void addVertexes(float left, float top, float right, float bottom, final int color, final int color2) {
        if (left < right) {
            final float i = left;
            left = right;
            right = i;
        }
        if (top < bottom) {
            final float j = top;
            top = bottom;
            bottom = j;
        }
        final float f3 = (color >> 24 & 0xFF) / 255.0f;
        final float f4 = (color >> 16 & 0xFF) / 255.0f;
        final float f5 = (color >> 8 & 0xFF) / 255.0f;
        final float f6 = (color & 0xFF) / 255.0f;
        final float ff3 = (color2 >> 24 & 0xFF) / 255.0f;
        final float ff4 = (color2 >> 16 & 0xFF) / 255.0f;
        final float ff5 = (color2 >> 8 & 0xFF) / 255.0f;
        final float ff6 = (color2 & 0xFF) / 255.0f;
        final Tessellator tessellator = Tessellator.func_178181_a();
        final WorldRenderer worldrenderer = tessellator.func_178180_c();
        worldrenderer.func_181662_b((double)left, (double)bottom, 0.0).func_181666_a(ff4, ff5, ff6, ff3).func_181675_d();
        worldrenderer.func_181662_b((double)right, (double)bottom, 0.0).func_181666_a(f4, f5, f6, f3).func_181675_d();
        worldrenderer.func_181662_b((double)right, (double)top, 0.0).func_181666_a(f4, f5, f6, f3).func_181675_d();
        worldrenderer.func_181662_b((double)left, (double)top, 0.0).func_181666_a(ff4, ff5, ff6, ff3).func_181675_d();
    }
}
