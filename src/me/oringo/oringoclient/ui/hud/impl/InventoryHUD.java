// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.ui.hud.impl;

import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.client.gui.GuiChat;
import me.oringo.oringoclient.qolfeatures.module.impl.render.InventoryDisplay;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import me.oringo.oringoclient.utils.shader.BlurUtils;
import me.oringo.oringoclient.utils.RenderUtils;
import java.awt.Color;
import me.oringo.oringoclient.utils.StencilUtils;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;
import me.oringo.oringoclient.ui.hud.HudVec;
import me.oringo.oringoclient.OringoClient;
import me.oringo.oringoclient.utils.font.Fonts;
import me.oringo.oringoclient.ui.hud.DraggableComponent;

public class InventoryHUD extends DraggableComponent
{
    public static final InventoryHUD inventoryHUD;
    
    public InventoryHUD() {
        this.setSize(182.0, 80 - (Fonts.robotoMedium.getHeight() - 4));
        this.setPosition(OringoClient.inventoryHUDModule.x.getValue(), OringoClient.inventoryHUDModule.y.getValue());
    }
    
    @Override
    public HudVec drawScreen() {
        GL11.glPushMatrix();
        super.drawScreen();
        final ScaledResolution scaledResolution = new ScaledResolution(InventoryHUD.mc);
        int blur = 0;
        final double x = this.x;
        double y = this.y;
        final String selected = OringoClient.inventoryHUDModule.blurStrength.getSelected();
        switch (selected) {
            case "Low": {
                blur = 7;
                break;
            }
            case "High": {
                blur = 15;
                break;
            }
        }
        final ScaledResolution resolution = new ScaledResolution(InventoryHUD.mc);
        StencilUtils.initStencil();
        StencilUtils.bindWriteStencilBuffer();
        RenderUtils.drawRoundedRect2(x, y + Fonts.robotoMedium.getHeight() - 4.0, 182.0, 80 - (Fonts.robotoMedium.getHeight() - 4), 5.0, Color.white.getRGB());
        StencilUtils.bindReadStencilBuffer(1);
        BlurUtils.renderBlurredBackground((float)blur, (float)resolution.func_78326_a(), (float)resolution.func_78328_b(), 0.0f, 0.0f, (float)scaledResolution.func_78326_a(), (float)scaledResolution.func_78328_b());
        StencilUtils.uninitStencil();
        this.drawBorderedRoundedRect((float)x, (float)y + Fonts.robotoMedium.getHeight() - 4.0f, 182.0f, (float)(80 - (Fonts.robotoMedium.getHeight() - 4)), 5.0f, 2.5f);
        Fonts.robotoMediumBold.drawSmoothCenteredString("Inventory", (float)x + 90.0f, (float)y + Fonts.robotoMedium.getHeight(), Color.white.getRGB());
        GlStateManager.func_179091_B();
        GlStateManager.func_179147_l();
        GlStateManager.func_179120_a(770, 771, 1, 0);
        RenderHelper.func_74520_c();
        for (int i = 9; i < 36; ++i) {
            if (i % 9 == 0) {
                y += 20.0;
            }
            final ItemStack stack = InventoryHUD.mc.field_71439_g.field_71071_by.func_70301_a(i);
            if (stack != null) {
                InventoryHUD.mc.func_175599_af().func_180450_b(stack, (int)x + 2 + 20 * (i % 9), (int)y);
                this.renderItemOverlayIntoGUI(stack, x + 2.0 + 20 * (i % 9), y);
            }
        }
        RenderHelper.func_74518_a();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.func_179101_C();
        GlStateManager.func_179084_k();
        GL11.glPopMatrix();
        final InventoryDisplay display = OringoClient.inventoryHUDModule;
        display.x.set(this.x);
        display.y.set(this.y);
        return new HudVec(x + this.width, y + this.height);
    }
    
    private void drawBorderedRoundedRect(final float x, final float y, final float width, final float height, final float radius, final float linewidth) {
        RenderUtils.drawRoundedRect(x, y, x + width, y + height, radius, new Color(21, 21, 21, 50).getRGB());
        if (this.isHovered() && InventoryHUD.mc.field_71462_r instanceof GuiChat) {
            RenderUtils.drawOutlinedRoundedRect(x, y, width, height, radius, linewidth, Color.white.getRGB());
        }
        else {
            RenderUtils.drawGradientOutlinedRoundedRect(x, y, width, height, radius, linewidth, OringoClient.clickGui.getColor(0).getRGB(), OringoClient.clickGui.getColor(3).getRGB(), OringoClient.clickGui.getColor(6).getRGB(), OringoClient.clickGui.getColor(9).getRGB());
        }
    }
    
    public void renderItemOverlayIntoGUI(final ItemStack itemStack, final double x, final double y) {
        if (itemStack != null) {
            if (itemStack.field_77994_a != 1) {
                String s = String.valueOf(itemStack.field_77994_a);
                if (itemStack.field_77994_a < 1) {
                    s = EnumChatFormatting.RED + String.valueOf(itemStack.field_77994_a);
                }
                GlStateManager.func_179140_f();
                GlStateManager.func_179097_i();
                GlStateManager.func_179084_k();
                Fonts.robotoMediumBold.drawSmoothStringWithShadow(s, x + 19.0 - 2.0 - Fonts.robotoMediumBold.getStringWidth(s), y + 6.0 + 3.0, Color.white.getRGB());
                GlStateManager.func_179145_e();
                GlStateManager.func_179126_j();
            }
            if (itemStack.func_77973_b().showDurabilityBar(itemStack)) {
                final double health = itemStack.func_77973_b().getDurabilityForDisplay(itemStack);
                final int j = (int)Math.round(13.0 - health * 13.0);
                final int i = (int)Math.round(255.0 - health * 255.0);
                GlStateManager.func_179140_f();
                GlStateManager.func_179097_i();
                GlStateManager.func_179090_x();
                GlStateManager.func_179118_c();
                GlStateManager.func_179084_k();
                final Tessellator tessellator = Tessellator.func_178181_a();
                final WorldRenderer worldrenderer = tessellator.func_178180_c();
                this.draw(worldrenderer, x + 2.0, y + 13.0, 13, 2, 0, 0, 0, 255);
                this.draw(worldrenderer, x + 2.0, y + 13.0, 12, 1, (255 - i) / 4, 64, 0, 255);
                this.draw(worldrenderer, x + 2.0, y + 13.0, j, 1, 255 - i, i, 0, 255);
                GlStateManager.func_179141_d();
                GlStateManager.func_179098_w();
                GlStateManager.func_179145_e();
                GlStateManager.func_179126_j();
            }
        }
    }
    
    private void draw(final WorldRenderer p_draw_1_, final double p_draw_2_, final double p_draw_3_, final int p_draw_4_, final int p_draw_5_, final int p_draw_6_, final int p_draw_7_, final int p_draw_8_, final int p_draw_9_) {
        p_draw_1_.func_181668_a(7, DefaultVertexFormats.field_181706_f);
        p_draw_1_.func_181662_b(p_draw_2_ + 0.0, p_draw_3_ + 0.0, 0.0).func_181669_b(p_draw_6_, p_draw_7_, p_draw_8_, p_draw_9_).func_181675_d();
        p_draw_1_.func_181662_b(p_draw_2_ + 0.0, p_draw_3_ + p_draw_5_, 0.0).func_181669_b(p_draw_6_, p_draw_7_, p_draw_8_, p_draw_9_).func_181675_d();
        p_draw_1_.func_181662_b(p_draw_2_ + p_draw_4_, p_draw_3_ + p_draw_5_, 0.0).func_181669_b(p_draw_6_, p_draw_7_, p_draw_8_, p_draw_9_).func_181675_d();
        p_draw_1_.func_181662_b(p_draw_2_ + p_draw_4_, p_draw_3_ + 0.0, 0.0).func_181669_b(p_draw_6_, p_draw_7_, p_draw_8_, p_draw_9_).func_181675_d();
        Tessellator.func_178181_a().func_78381_a();
    }
    
    static {
        inventoryHUD = new InventoryHUD();
    }
}
