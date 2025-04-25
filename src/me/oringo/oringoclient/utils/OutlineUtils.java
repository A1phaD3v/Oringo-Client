// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.utils;

import org.lwjgl.opengl.EXTFramebufferObject;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.opengl.GL11;
import me.oringo.oringoclient.events.RenderLayersEvent;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import java.awt.Color;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLivingBase;

public class OutlineUtils
{
    public static void outlineESP(final EntityLivingBase entity, final float p_77036_2_, final float p_77036_3_, final float p_77036_4_, final float p_77036_5_, final float p_77036_6_, final float scaleFactor, final ModelBase modelBase, final Color color) {
        final Minecraft mc = Minecraft.func_71410_x();
        final boolean fancyGraphics = mc.field_71474_y.field_74347_j;
        final float gamma = mc.field_71474_y.field_74333_Y;
        mc.field_71474_y.field_74347_j = false;
        mc.field_71474_y.field_74333_Y = 100000.0f;
        GlStateManager.func_179117_G();
        setColor(color);
        renderOne(2.0f);
        modelBase.func_78088_a((Entity)entity, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
        setColor(color);
        renderTwo();
        modelBase.func_78088_a((Entity)entity, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
        setColor(color);
        renderThree();
        modelBase.func_78088_a((Entity)entity, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
        setColor(color);
        renderFour(color);
        modelBase.func_78088_a((Entity)entity, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
        setColor(color);
        renderFive();
        setColor(Color.WHITE);
        mc.field_71474_y.field_74347_j = fancyGraphics;
        mc.field_71474_y.field_74333_Y = gamma;
    }
    
    public static void outlineESP(final RenderLayersEvent event, final Color color) {
        final Minecraft mc = Minecraft.func_71410_x();
        final boolean fancyGraphics = mc.field_71474_y.field_74347_j;
        final float gamma = mc.field_71474_y.field_74333_Y;
        mc.field_71474_y.field_74347_j = false;
        mc.field_71474_y.field_74333_Y = 100000.0f;
        GlStateManager.func_179117_G();
        setColor(color);
        renderOne(3.0f);
        event.modelBase.func_78088_a((Entity)event.entity, event.p_77036_2_, event.p_77036_3_, event.p_77036_4_, event.p_77036_5_, event.p_77036_6_, event.scaleFactor);
        setColor(color);
        renderTwo();
        event.modelBase.func_78088_a((Entity)event.entity, event.p_77036_2_, event.p_77036_3_, event.p_77036_4_, event.p_77036_5_, event.p_77036_6_, event.scaleFactor);
        setColor(color);
        renderThree();
        event.modelBase.func_78088_a((Entity)event.entity, event.p_77036_2_, event.p_77036_3_, event.p_77036_4_, event.p_77036_5_, event.p_77036_6_, event.scaleFactor);
        setColor(color);
        renderFour(color);
        event.modelBase.func_78088_a((Entity)event.entity, event.p_77036_2_, event.p_77036_3_, event.p_77036_4_, event.p_77036_5_, event.p_77036_6_, event.scaleFactor);
        setColor(color);
        renderFive();
        setColor(Color.WHITE);
        mc.field_71474_y.field_74347_j = fancyGraphics;
        mc.field_71474_y.field_74333_Y = gamma;
    }
    
    public static void renderOne(final float lineWidth) {
        checkSetupFBO();
        GL11.glPushAttrib(1048575);
        GL11.glDisable(3008);
        GL11.glDisable(3553);
        GL11.glDisable(2896);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(lineWidth);
        GL11.glEnable(2848);
        GL11.glEnable(2960);
        GL11.glClear(1024);
        GL11.glClearStencil(15);
        GL11.glStencilFunc(512, 1, 15);
        GL11.glStencilOp(7681, 7681, 7681);
        GL11.glPolygonMode(1032, 6913);
    }
    
    public static void renderTwo() {
        GL11.glStencilFunc(512, 0, 15);
        GL11.glStencilOp(7681, 7681, 7681);
        GL11.glPolygonMode(1032, 6914);
    }
    
    public static void renderThree() {
        GL11.glStencilFunc(514, 1, 15);
        GL11.glStencilOp(7680, 7680, 7680);
        GL11.glPolygonMode(1032, 6913);
    }
    
    public static void renderFour(final Color color) {
        setColor(color);
        GL11.glDepthMask(false);
        GL11.glDisable(2929);
        GL11.glEnable(10754);
        GL11.glPolygonOffset(1.0f, -2000000.0f);
        OpenGlHelper.func_77475_a(OpenGlHelper.field_77476_b, 240.0f, 240.0f);
    }
    
    public static void renderFive() {
        GL11.glPolygonOffset(1.0f, 2000000.0f);
        GL11.glDisable(10754);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(2960);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glEnable(3042);
        GL11.glEnable(2896);
        GL11.glEnable(3553);
        GL11.glEnable(3008);
        GL11.glPopAttrib();
    }
    
    public static void setColor(final Color color) {
        GL11.glColor4d((double)(color.getRed() / 255.0f), (double)(color.getGreen() / 255.0f), (double)(color.getBlue() / 255.0f), (double)(color.getAlpha() / 255.0f));
    }
    
    public static void checkSetupFBO() {
        final Framebuffer fbo = Minecraft.func_71410_x().func_147110_a();
        if (fbo != null && fbo.field_147624_h > -1) {
            setupFBO(fbo);
            fbo.field_147624_h = -1;
        }
    }
    
    private static void setupFBO(final Framebuffer fbo) {
        EXTFramebufferObject.glDeleteRenderbuffersEXT(fbo.field_147624_h);
        final int stencil_depth_buffer_ID = EXTFramebufferObject.glGenRenderbuffersEXT();
        EXTFramebufferObject.glBindRenderbufferEXT(36161, stencil_depth_buffer_ID);
        EXTFramebufferObject.glRenderbufferStorageEXT(36161, 34041, Minecraft.func_71410_x().field_71443_c, Minecraft.func_71410_x().field_71440_d);
        EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36128, 36161, stencil_depth_buffer_ID);
        EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36096, 36161, stencil_depth_buffer_ID);
    }
}
