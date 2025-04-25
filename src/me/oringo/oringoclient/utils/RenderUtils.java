// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.utils;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.BlockPos;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.Vec3;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.Minecraft;
import org.lwjgl.util.vector.Vector2f;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.client.renderer.entity.RenderManager;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import java.awt.Color;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.ScaledResolution;
import me.oringo.oringoclient.OringoClient;

public class RenderUtils
{
    public static void scissor(double x, double y, double width, double height) {
        final ScaledResolution sr = new ScaledResolution(OringoClient.mc);
        final double scale = sr.func_78325_e();
        y = sr.func_78328_b() - y;
        x *= scale;
        y *= scale;
        width *= scale;
        height *= scale;
        GL11.glScissor((int)x, (int)(y - height), (int)width, (int)height);
    }
    
    public static void drawCircle(float x, float y, final int start, final int end, float radius, final int color) {
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glEnable(2881);
        GL11.glBlendFunc(770, 771);
        final float f = (color >> 24 & 0xFF) / 255.0f;
        final float f2 = (color >> 16 & 0xFF) / 255.0f;
        final float f3 = (color >> 8 & 0xFF) / 255.0f;
        final float f4 = (color & 0xFF) / 255.0f;
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glScaled(0.5, 0.5, 0.5);
        x *= 2.0f;
        y *= 2.0f;
        radius *= 2.0f;
        GL11.glBegin(9);
        for (int i = start; i <= end; ++i) {
            final double x2 = Math.sin(i * 3.141592653589793 / 45.0) * radius;
            final double y2 = Math.cos(i * 3.141592653589793 / 45.0) * radius;
            GL11.glVertex3d(x + x2, y + y2, 0.0);
        }
        GL11.glEnd();
        GL11.glScaled(2.0, 2.0, 2.0);
        GL11.glDisable(2881);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
    }
    
    public static void drawRect(float left, float top, float right, float bottom, final int color) {
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
        final Tessellator tessellator = Tessellator.func_178181_a();
        final WorldRenderer worldrenderer = tessellator.func_178180_c();
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179120_a(770, 771, 1, 0);
        GlStateManager.func_179131_c(f4, f5, f6, f3);
        worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldrenderer.func_181662_b((double)left, (double)bottom, 0.0).func_181675_d();
        worldrenderer.func_181662_b((double)right, (double)bottom, 0.0).func_181675_d();
        worldrenderer.func_181662_b((double)right, (double)top, 0.0).func_181675_d();
        worldrenderer.func_181662_b((double)left, (double)top, 0.0).func_181675_d();
        tessellator.func_78381_a();
        GlStateManager.func_179124_c(1.0f, 1.0f, 1.0f);
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }
    
    public static void drawBorderedRect(final float x, final float y, final float x1, final float y1, final float width, final int internalColor, final int borderColor) {
        enableGL2D();
        drawRect(x + width, y + width, x1 - width, y1 - width, internalColor);
        GL11.glPushMatrix();
        drawRect(x + width, y, x1 - width, y + width, borderColor);
        drawRect(x, y, x + width, y1, borderColor);
        drawRect(x1 - width, y, x1, y1, borderColor);
        drawRect(x + width, y1 - width, x1 - width, y1, borderColor);
        GL11.glPopMatrix();
        disableGL2D();
    }
    
    public static void draw2D(final Entity entityLiving, final float partialTicks, final float lineWidth, final Color color) {
        final Matrix4f mvMatrix = WorldToScreen.getMatrix(2982);
        final Matrix4f projectionMatrix = WorldToScreen.getMatrix(2983);
        GL11.glPushAttrib(8192);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glMatrixMode(5889);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0, (double)OringoClient.mc.field_71443_c, (double)OringoClient.mc.field_71440_d, 0.0, -1.0, 1.0);
        GL11.glMatrixMode(5888);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glDisable(2929);
        GL11.glBlendFunc(770, 771);
        GlStateManager.func_179098_w();
        GlStateManager.func_179132_a(true);
        GL11.glLineWidth(lineWidth);
        final RenderManager renderManager = OringoClient.mc.func_175598_ae();
        final AxisAlignedBB bb = entityLiving.func_174813_aQ().func_72317_d(-entityLiving.field_70165_t, -entityLiving.field_70163_u, -entityLiving.field_70161_v).func_72317_d(entityLiving.field_70142_S + (entityLiving.field_70165_t - entityLiving.field_70142_S) * partialTicks, entityLiving.field_70137_T + (entityLiving.field_70163_u - entityLiving.field_70137_T) * partialTicks, entityLiving.field_70136_U + (entityLiving.field_70161_v - entityLiving.field_70136_U) * partialTicks).func_72317_d(-renderManager.field_78730_l, -renderManager.field_78731_m, -renderManager.field_78728_n);
        GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 1.0f);
        final double[][] boxVertices = { { bb.field_72340_a, bb.field_72338_b, bb.field_72339_c }, { bb.field_72340_a, bb.field_72337_e, bb.field_72339_c }, { bb.field_72336_d, bb.field_72337_e, bb.field_72339_c }, { bb.field_72336_d, bb.field_72338_b, bb.field_72339_c }, { bb.field_72340_a, bb.field_72338_b, bb.field_72334_f }, { bb.field_72340_a, bb.field_72337_e, bb.field_72334_f }, { bb.field_72336_d, bb.field_72337_e, bb.field_72334_f }, { bb.field_72336_d, bb.field_72338_b, bb.field_72334_f } };
        float minX = Float.MAX_VALUE;
        float minY = Float.MAX_VALUE;
        float maxX = -1.0f;
        float maxY = -1.0f;
        for (final double[] boxVertex : boxVertices) {
            final Vector2f screenPos = WorldToScreen.worldToScreen(new Vector3f((float)boxVertex[0], (float)boxVertex[1], (float)boxVertex[2]), mvMatrix, projectionMatrix, OringoClient.mc.field_71443_c, OringoClient.mc.field_71440_d);
            if (screenPos != null) {
                minX = Math.min(screenPos.x, minX);
                minY = Math.min(screenPos.y, minY);
                maxX = Math.max(screenPos.x, maxX);
                maxY = Math.max(screenPos.y, maxY);
            }
        }
        if (minX > 0.0f || minY > 0.0f || maxX <= OringoClient.mc.field_71443_c || maxY <= OringoClient.mc.field_71443_c) {
            GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 1.0f);
            GL11.glBegin(2);
            GL11.glVertex2f(minX, minY);
            GL11.glVertex2f(minX, maxY);
            GL11.glVertex2f(maxX, maxY);
            GL11.glVertex2f(maxX, minY);
            GL11.glEnd();
        }
        GL11.glEnable(2929);
        GL11.glMatrixMode(5889);
        GL11.glPopMatrix();
        GL11.glMatrixMode(5888);
        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }
    
    public static void tracerLine(final Entity entity, final float partialTicks, final float lineWidth, final Color color) {
        final double x = entity.field_70169_q + (entity.field_70165_t - entity.field_70169_q) * partialTicks - Minecraft.func_71410_x().func_175598_ae().field_78730_l;
        final double y = entity.field_70167_r + (entity.field_70163_u - entity.field_70167_r) * partialTicks + entity.field_70131_O / 2.0f - Minecraft.func_71410_x().func_175598_ae().field_78731_m;
        final double z = entity.field_70166_s + (entity.field_70161_v - entity.field_70166_s) * partialTicks - Minecraft.func_71410_x().func_175598_ae().field_78728_n;
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glLineWidth(lineWidth);
        GL11.glDepthMask(false);
        setColor(color);
        GL11.glBegin(2);
        GL11.glVertex3d(0.0, (double)Minecraft.func_71410_x().field_71439_g.func_70047_e(), 0.0);
        GL11.glVertex3d(x, y, z);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
    }
    
    public static void drawTargetESP(final EntityLivingBase target, final Color color, final float partialTicks) {
        GL11.glPushMatrix();
        final float location = (float)((Math.sin(System.currentTimeMillis() * 0.005) + 1.0) * 0.5);
        GlStateManager.func_179137_b(target.field_70142_S + (target.field_70165_t - target.field_70142_S) * partialTicks - OringoClient.mc.func_175598_ae().field_78730_l, target.field_70137_T + (target.field_70163_u - target.field_70137_T) * partialTicks - OringoClient.mc.func_175598_ae().field_78731_m + target.field_70131_O * location, target.field_70136_U + (target.field_70161_v - target.field_70136_U) * partialTicks - OringoClient.mc.func_175598_ae().field_78728_n);
        enableGL2D();
        GL11.glShadeModel(7425);
        GL11.glDisable(2884);
        GL11.glLineWidth(3.0f);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glBegin(3);
        final double cos = Math.cos(System.currentTimeMillis() * 0.005);
        for (int i = 0; i <= 120; ++i) {
            GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 1.0f);
            final double x = Math.cos(i * 3.141592653589793 / 60.0) * target.field_70130_N;
            final double z = Math.sin(i * 3.141592653589793 / 60.0) * target.field_70130_N;
            GL11.glVertex3d(x, 0.15000000596046448 * cos, z);
        }
        GL11.glEnd();
        GL11.glDisable(2848);
        GL11.glBegin(5);
        for (int i = 0; i <= 120; ++i) {
            GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 0.5f);
            final double x = Math.cos(i * 3.141592653589793 / 60.0) * target.field_70130_N;
            final double z = Math.sin(i * 3.141592653589793 / 60.0) * target.field_70130_N;
            GL11.glVertex3d(x, 0.15000000596046448 * cos, z);
            GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 0.2f);
            GL11.glVertex3d(x, -0.15000000596046448 * cos, z);
        }
        GL11.glEnd();
        GL11.glShadeModel(7424);
        GL11.glEnable(2884);
        disableGL2D();
        GL11.glPopMatrix();
    }
    
    public static void drawFace(final int x, final int y, final float u, final float v, final int uWidth, final int vHeight, final int width, final int height, final float tileWidth, final float tileHeight, final AbstractClientPlayer target) {
        final ResourceLocation skin = target.func_110306_p();
        if (skin != null) {
            Minecraft.func_71410_x().func_110434_K().func_110577_a(skin);
            GL11.glEnable(3042);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            Gui.func_152125_a(x, y, u, v, uWidth, vHeight, width, height, tileWidth, tileHeight);
            GL11.glDisable(3042);
        }
    }
    
    public static void drawRectWith2Colors(float left, float top, float right, float bottom, final int color, final int color2) {
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
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179120_a(770, 771, 1, 0);
        GlStateManager.func_179103_j(7425);
        GlStateManager.func_179131_c(f4, f5, f6, f3);
        worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181706_f);
        worldrenderer.func_181662_b((double)left, (double)bottom, 0.0).func_181666_a(ff4, ff5, ff6, ff3).func_181675_d();
        worldrenderer.func_181662_b((double)right, (double)bottom, 0.0).func_181666_a(f4, f5, f6, f3).func_181675_d();
        worldrenderer.func_181662_b((double)right, (double)top, 0.0).func_181666_a(f4, f5, f6, f3).func_181675_d();
        worldrenderer.func_181662_b((double)left, (double)top, 0.0).func_181666_a(ff4, ff5, ff6, ff3).func_181675_d();
        tessellator.func_78381_a();
        GlStateManager.func_179103_j(7424);
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }
    
    public static Color interpolateColor(final Color color1, final Color color2, final float value) {
        return new Color((int)(color1.getRed() + (color2.getRed() - color1.getRed()) * value), (int)(color1.getGreen() + (color2.getGreen() - color1.getGreen()) * value), (int)(color1.getBlue() + (color2.getBlue() - color1.getBlue()) * value));
    }
    
    public static Color applyOpacity(final Color color, final int opacity) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), opacity);
    }
    
    public static void drawRoundedRect2(double x, double y, final double width, final double height, final double radius, final int color) {
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179120_a(770, 771, 1, 0);
        double x2 = x + width;
        double y2 = y + height;
        final float f = (color >> 24 & 0xFF) / 255.0f;
        final float f2 = (color >> 16 & 0xFF) / 255.0f;
        final float f3 = (color >> 8 & 0xFF) / 255.0f;
        final float f4 = (color & 0xFF) / 255.0f;
        GL11.glPushAttrib(0);
        GL11.glScaled(0.5, 0.5, 0.5);
        x *= 2.0;
        y *= 2.0;
        x2 *= 2.0;
        y2 *= 2.0;
        GL11.glDisable(3553);
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glEnable(2848);
        GL11.glBegin(9);
        for (int i = 0; i <= 90; i += 3) {
            GL11.glVertex2d(x + radius + Math.sin(i * 3.141592653589793 / 180.0) * (radius * -1.0), y + radius + Math.cos(i * 3.141592653589793 / 180.0) * (radius * -1.0));
        }
        for (int i = 90; i <= 180; i += 3) {
            GL11.glVertex2d(x + radius + Math.sin(i * 3.141592653589793 / 180.0) * (radius * -1.0), y2 - radius + Math.cos(i * 3.141592653589793 / 180.0) * (radius * -1.0));
        }
        for (int i = 0; i <= 90; i += 3) {
            GL11.glVertex2d(x2 - radius + Math.sin(i * 3.141592653589793 / 180.0) * radius, y2 - radius + Math.cos(i * 3.141592653589793 / 180.0) * radius);
        }
        for (int i = 90; i <= 180; i += 3) {
            GL11.glVertex2d(x2 - radius + Math.sin(i * 3.141592653589793 / 180.0) * radius, y + radius + Math.cos(i * 3.141592653589793 / 180.0) * radius);
        }
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glScaled(2.0, 2.0, 2.0);
        GL11.glPopAttrib();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }
    
    public static void drawGradientRoundedRect(double x, double y, double x2, double y2, final double radius, final int color1, final int color2) {
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179120_a(770, 771, 1, 0);
        final float f = (color1 >> 24 & 0xFF) / 255.0f;
        final float f2 = (color1 >> 16 & 0xFF) / 255.0f;
        final float f3 = (color1 >> 8 & 0xFF) / 255.0f;
        final float f4 = (color1 & 0xFF) / 255.0f;
        final float f5 = (color2 >> 24 & 0xFF) / 255.0f;
        final float f6 = (color2 >> 16 & 0xFF) / 255.0f;
        final float f7 = (color2 >> 8 & 0xFF) / 255.0f;
        final float f8 = (color2 & 0xFF) / 255.0f;
        GL11.glPushAttrib(0);
        GL11.glScaled(0.5, 0.5, 0.5);
        x *= 2.0;
        y *= 2.0;
        x2 *= 2.0;
        y2 *= 2.0;
        GL11.glShadeModel(7425);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glBegin(9);
        GL11.glColor4f(f2, f3, f4, f);
        for (int i = 0; i <= 90; i += 3) {
            GL11.glVertex2d(x + radius + Math.sin(i * 3.141592653589793 / 180.0) * (radius * -1.0), y + radius + Math.cos(i * 3.141592653589793 / 180.0) * (radius * -1.0));
        }
        for (int i = 90; i <= 180; i += 3) {
            GL11.glVertex2d(x + radius + Math.sin(i * 3.141592653589793 / 180.0) * (radius * -1.0), y2 - radius + Math.cos(i * 3.141592653589793 / 180.0) * (radius * -1.0));
        }
        GL11.glColor4f(f6, f7, f8, f5);
        for (int i = 0; i <= 90; i += 3) {
            GL11.glVertex2d(x2 - radius + Math.sin(i * 3.141592653589793 / 180.0) * radius, y2 - radius + Math.cos(i * 3.141592653589793 / 180.0) * radius);
        }
        for (int i = 90; i <= 180; i += 3) {
            GL11.glVertex2d(x2 - radius + Math.sin(i * 3.141592653589793 / 180.0) * radius, y + radius + Math.cos(i * 3.141592653589793 / 180.0) * radius);
        }
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glShadeModel(7424);
        GL11.glScaled(2.0, 2.0, 2.0);
        GL11.glPopAttrib();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }
    
    public static void drawRoundedRect(double x, double y, double x2, double y2, final double radius, final int color) {
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179120_a(770, 771, 1, 0);
        final float f = (color >> 24 & 0xFF) / 255.0f;
        final float f2 = (color >> 16 & 0xFF) / 255.0f;
        final float f3 = (color >> 8 & 0xFF) / 255.0f;
        final float f4 = (color & 0xFF) / 255.0f;
        GL11.glPushAttrib(0);
        GL11.glScaled(0.5, 0.5, 0.5);
        x *= 2.0;
        y *= 2.0;
        x2 *= 2.0;
        y2 *= 2.0;
        GL11.glDisable(3553);
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glEnable(2848);
        GL11.glBegin(9);
        for (int i = 0; i <= 90; i += 3) {
            GL11.glVertex2d(x + radius + Math.sin(i * 3.141592653589793 / 180.0) * (radius * -1.0), y + radius + Math.cos(i * 3.141592653589793 / 180.0) * (radius * -1.0));
        }
        for (int i = 90; i <= 180; i += 3) {
            GL11.glVertex2d(x + radius + Math.sin(i * 3.141592653589793 / 180.0) * (radius * -1.0), y2 - radius + Math.cos(i * 3.141592653589793 / 180.0) * (radius * -1.0));
        }
        for (int i = 0; i <= 90; i += 3) {
            GL11.glVertex2d(x2 - radius + Math.sin(i * 3.141592653589793 / 180.0) * radius, y2 - radius + Math.cos(i * 3.141592653589793 / 180.0) * radius);
        }
        for (int i = 90; i <= 180; i += 3) {
            GL11.glVertex2d(x2 - radius + Math.sin(i * 3.141592653589793 / 180.0) * radius, y + radius + Math.cos(i * 3.141592653589793 / 180.0) * radius);
        }
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glScaled(2.0, 2.0, 2.0);
        GL11.glPopAttrib();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }
    
    public static void drawImage(final ResourceLocation image, final float x, final float y, final float width, final float height, final float alpha) {
        GL11.glPushMatrix();
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDepthMask(false);
        OpenGlHelper.func_148821_a(770, 771, 1, 0);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, alpha);
        OringoClient.mc.func_110434_K().func_110577_a(image);
        GL11.glTexParameteri(3553, 10241, 9729);
        GL11.glTexParameteri(3553, 10240, 9729);
        drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, width, height);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    public static void drawModalRectWithCustomSizedTexture(final float x, final float y, final float u, final float v, final float width, final float height, final float textureWidth, final float textureHeight) {
        final float f = 1.0f / textureWidth;
        final float f2 = 1.0f / textureHeight;
        final Tessellator tessellator = Tessellator.func_178181_a();
        final WorldRenderer worldrenderer = tessellator.func_178180_c();
        worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181707_g);
        worldrenderer.func_181662_b((double)x, (double)(y + height), 0.0).func_181673_a((double)(u * f), (double)((v + height) * f2)).func_181675_d();
        worldrenderer.func_181662_b((double)(x + width), (double)(y + height), 0.0).func_181673_a((double)((u + width) * f), (double)((v + height) * f2)).func_181675_d();
        worldrenderer.func_181662_b((double)(x + width), (double)y, 0.0).func_181673_a((double)((u + width) * f), (double)(v * f2)).func_181675_d();
        worldrenderer.func_181662_b((double)x, (double)y, 0.0).func_181673_a((double)(u * f), (double)(v * f2)).func_181675_d();
        tessellator.func_78381_a();
    }
    
    public static void enableGL2D() {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
    }
    
    public static void disableGL2D() {
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }
    
    public static void drawArc(final float x, final float y, final float radius, final int angleStart) {
        final Tessellator tessellator = Tessellator.func_178181_a();
        final WorldRenderer worldrenderer = tessellator.func_178180_c();
        worldrenderer.func_181668_a(6, DefaultVertexFormats.field_181705_e);
        GlStateManager.func_179137_b((double)x, (double)y, 0.0);
        worldrenderer.func_181662_b(0.0, 0.0, 0.0).func_181675_d();
        final int points = 20;
        for (double i = 0.0; i < points; ++i) {
            final double radians = Math.toRadians(i / points * 90.0 + angleStart);
            worldrenderer.func_181662_b(radius * Math.sin(radians), radius * Math.cos(radians), 0.0).func_181675_d();
        }
        tessellator.func_78381_a();
        GlStateManager.func_179137_b((double)(-x), (double)(-y), 0.0);
    }
    
    public static void tracerLine(double x, double y, double z, final Color color) {
        x -= Minecraft.func_71410_x().func_175598_ae().field_78730_l;
        y -= Minecraft.func_71410_x().func_175598_ae().field_78731_m;
        z -= Minecraft.func_71410_x().func_175598_ae().field_78728_n;
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(2.5f);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        setColor(color);
        GL11.glBegin(1);
        GL11.glVertex3d(0.0, (double)Minecraft.func_71410_x().field_71439_g.func_70047_e(), 0.0);
        GL11.glVertex3d(x, y, z);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(2848);
        GL11.glDisable(3042);
    }
    
    public static void drawLine(final Vec3 pos1, final Vec3 pos2, final Color color) {
        GL11.glPushMatrix();
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(2.5f);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        setColor(color);
        GL11.glTranslated(-Minecraft.func_71410_x().func_175598_ae().field_78730_l, -Minecraft.func_71410_x().func_175598_ae().field_78731_m, -Minecraft.func_71410_x().func_175598_ae().field_78728_n);
        GL11.glBegin(1);
        GL11.glVertex3d(pos1.field_72450_a, pos1.field_72448_b, pos1.field_72449_c);
        GL11.glVertex3d(pos2.field_72450_a, pos2.field_72448_b, pos2.field_72449_c);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
    
    public static void setColor(final Color c) {
        GL11.glColor4f(c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f, c.getAlpha() / 255.0f);
    }
    
    public static void entityESPBox(final Entity entity, final float partialTicks, final Color color) {
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(1.5f);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        setColor(color);
        RenderGlobal.func_181561_a(new AxisAlignedBB(entity.func_174813_aQ().field_72340_a - entity.field_70165_t + (entity.field_70169_q + (entity.field_70165_t - entity.field_70169_q) * partialTicks - Minecraft.func_71410_x().func_175598_ae().field_78730_l), entity.func_174813_aQ().field_72338_b - entity.field_70163_u + (entity.field_70167_r + (entity.field_70163_u - entity.field_70167_r) * partialTicks - Minecraft.func_71410_x().func_175598_ae().field_78731_m), entity.func_174813_aQ().field_72339_c - entity.field_70161_v + (entity.field_70166_s + (entity.field_70161_v - entity.field_70166_s) * partialTicks - Minecraft.func_71410_x().func_175598_ae().field_78728_n), entity.func_174813_aQ().field_72336_d - entity.field_70165_t + (entity.field_70169_q + (entity.field_70165_t - entity.field_70169_q) * partialTicks - Minecraft.func_71410_x().func_175598_ae().field_78730_l), entity.func_174813_aQ().field_72337_e - entity.field_70163_u + (entity.field_70167_r + (entity.field_70163_u - entity.field_70167_r) * partialTicks - Minecraft.func_71410_x().func_175598_ae().field_78731_m), entity.func_174813_aQ().field_72334_f - entity.field_70161_v + (entity.field_70166_s + (entity.field_70161_v - entity.field_70166_s) * partialTicks - Minecraft.func_71410_x().func_175598_ae().field_78728_n)));
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
    }
    
    public static void blockBox(final TileEntity block, final Color color) {
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(2.0f);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        setColor(color);
        RenderGlobal.func_181561_a(block.getRenderBoundingBox().func_72317_d(-OringoClient.mc.func_175598_ae().field_78730_l, -OringoClient.mc.func_175598_ae().field_78731_m, -OringoClient.mc.func_175598_ae().field_78728_n));
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
    }
    
    public static void blockBox(final BlockPos block, final Color color) {
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(2.0f);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        setColor(color);
        RenderGlobal.func_181561_a(new AxisAlignedBB(block.func_177958_n() - Minecraft.func_71410_x().func_175598_ae().field_78730_l, block.func_177956_o() - Minecraft.func_71410_x().func_175598_ae().field_78731_m, block.func_177952_p() - Minecraft.func_71410_x().func_175598_ae().field_78728_n, block.func_177958_n() + 1 - Minecraft.func_71410_x().func_175598_ae().field_78730_l, block.func_177956_o() + 1 - Minecraft.func_71410_x().func_175598_ae().field_78731_m, block.func_177952_p() + 1 - Minecraft.func_71410_x().func_175598_ae().field_78728_n));
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
    }
    
    public static void doScale(final double x, final double y, final double scale) {
        GL11.glTranslated(x, y, 0.0);
        GL11.glScaled(scale, scale, scale);
        GL11.glTranslated(-x, -y, 0.0);
    }
    
    public static void miniBlockBox(final Vec3 block, final Color color) {
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(2.0f);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        setColor(color);
        Minecraft.func_71410_x().func_175598_ae();
        RenderGlobal.func_181561_a(new AxisAlignedBB(block.field_72450_a - 0.05 - Minecraft.func_71410_x().func_175598_ae().field_78730_l, block.field_72448_b - 0.05 - Minecraft.func_71410_x().func_175598_ae().field_78731_m, block.field_72449_c - 0.05 - Minecraft.func_71410_x().func_175598_ae().field_78728_n, block.field_72450_a + 0.05 - Minecraft.func_71410_x().func_175598_ae().field_78730_l, block.field_72448_b + 0.05 - Minecraft.func_71410_x().func_175598_ae().field_78731_m, block.field_72449_c + 0.05 - Minecraft.func_71410_x().func_175598_ae().field_78728_n));
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
    }
    
    public static void drawBorderedRoundedRect(final float x, final float y, final float width, final float height, final float radius, final float linewidth, final int insideC, final int borderC) {
        drawRoundedRect(x, y, x + width, y + height, radius, insideC);
        drawOutlinedRoundedRect(x, y, width, height, radius, linewidth, borderC);
    }
    
    public static void drawGradientOutlinedRoundedRect(float x, float y, final float width, final float height, final float radius, final float linewidth, final int color1, final int color2, final int color3, final int color4) {
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179120_a(770, 771, 1, 0);
        double x2 = x + width;
        double y2 = y + height;
        final float opacity1 = (color1 >> 24 & 0xFF) / 255.0f;
        final float red1 = (color1 >> 16 & 0xFF) / 255.0f;
        final float green1 = (color1 >> 8 & 0xFF) / 255.0f;
        final float blue1 = (color1 & 0xFF) / 255.0f;
        final float opacity2 = (color2 >> 24 & 0xFF) / 255.0f;
        final float red2 = (color2 >> 16 & 0xFF) / 255.0f;
        final float green2 = (color2 >> 8 & 0xFF) / 255.0f;
        final float blue2 = (color2 & 0xFF) / 255.0f;
        final float opacity3 = (color3 >> 24 & 0xFF) / 255.0f;
        final float red3 = (color3 >> 16 & 0xFF) / 255.0f;
        final float green3 = (color3 >> 8 & 0xFF) / 255.0f;
        final float blue3 = (color3 & 0xFF) / 255.0f;
        final float opacity4 = (color4 >> 24 & 0xFF) / 255.0f;
        final float red4 = (color4 >> 16 & 0xFF) / 255.0f;
        final float green4 = (color4 >> 8 & 0xFF) / 255.0f;
        final float blue4 = (color4 & 0xFF) / 255.0f;
        GL11.glPushAttrib(0);
        GL11.glScaled(0.5, 0.5, 0.5);
        x *= 2.0f;
        y *= 2.0f;
        x2 *= 2.0;
        y2 *= 2.0;
        GL11.glLineWidth(linewidth);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);
        GL11.glBegin(2);
        GL11.glColor4f(red1, green1, blue1, opacity1);
        for (int i = 0; i <= 90; i += 3) {
            GL11.glVertex2d(x + radius + Math.sin(i * 3.141592653589793 / 180.0) * (radius * -1.0f), y + radius + Math.cos(i * 3.141592653589793 / 180.0) * (radius * -1.0f));
        }
        GL11.glColor4f(red2, green2, blue2, opacity2);
        for (int i = 90; i <= 180; i += 3) {
            GL11.glVertex2d(x + radius + Math.sin(i * 3.141592653589793 / 180.0) * (radius * -1.0f), y2 - radius + Math.cos(i * 3.141592653589793 / 180.0) * (radius * -1.0f));
        }
        GL11.glColor4f(red3, green3, blue3, opacity3);
        for (int i = 0; i <= 90; i += 3) {
            GL11.glVertex2d(x2 - radius + Math.sin(i * 3.141592653589793 / 180.0) * radius, y2 - radius + Math.cos(i * 3.141592653589793 / 180.0) * radius);
        }
        GL11.glColor4f(red4, green4, blue4, opacity4);
        for (int i = 90; i <= 180; i += 3) {
            GL11.glVertex2d(x2 - radius + Math.sin(i * 3.141592653589793 / 180.0) * radius, y + radius + Math.cos(i * 3.141592653589793 / 180.0) * radius);
        }
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glShadeModel(7424);
        GL11.glScaled(2.0, 2.0, 2.0);
        GL11.glPopAttrib();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }
    
    public static Vec3 getInterpolatedPos(final float partialTicks) {
        return new Vec3(interpolate(OringoClient.mc.field_71439_g.field_70169_q, OringoClient.mc.field_71439_g.field_70165_t, partialTicks), interpolate(OringoClient.mc.field_71439_g.field_70167_r, OringoClient.mc.field_71439_g.field_70163_u, partialTicks) + 0.1, interpolate(OringoClient.mc.field_71439_g.field_70166_s, OringoClient.mc.field_71439_g.field_70161_v, partialTicks));
    }
    
    public static double interpolate(final double prev, final double newPos, final float partialTicks) {
        return prev + (newPos - prev) * partialTicks;
    }
    
    public static void drawOutlinedRoundedRect(float x, float y, final float width, final float height, final float radius, final float linewidth, final int color) {
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179120_a(770, 771, 1, 0);
        double x2 = x + width;
        double y2 = y + height;
        final float f = (color >> 24 & 0xFF) / 255.0f;
        final float f2 = (color >> 16 & 0xFF) / 255.0f;
        final float f3 = (color >> 8 & 0xFF) / 255.0f;
        final float f4 = (color & 0xFF) / 255.0f;
        GL11.glPushAttrib(0);
        GL11.glScaled(0.5, 0.5, 0.5);
        x *= 2.0f;
        y *= 2.0f;
        x2 *= 2.0;
        y2 *= 2.0;
        GL11.glLineWidth(linewidth);
        GL11.glDisable(3553);
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glEnable(2848);
        GL11.glBegin(2);
        for (int i = 0; i <= 90; i += 3) {
            GL11.glVertex2d(x + radius + Math.sin(i * 3.141592653589793 / 180.0) * (radius * -1.0f), y + radius + Math.cos(i * 3.141592653589793 / 180.0) * (radius * -1.0f));
        }
        for (int i = 90; i <= 180; i += 3) {
            GL11.glVertex2d(x + radius + Math.sin(i * 3.141592653589793 / 180.0) * (radius * -1.0f), y2 - radius + Math.cos(i * 3.141592653589793 / 180.0) * (radius * -1.0f));
        }
        for (int i = 0; i <= 90; i += 3) {
            GL11.glVertex2d(x2 - radius + Math.sin(i * 3.141592653589793 / 180.0) * radius, y2 - radius + Math.cos(i * 3.141592653589793 / 180.0) * radius);
        }
        for (int i = 90; i <= 180; i += 3) {
            GL11.glVertex2d(x2 - radius + Math.sin(i * 3.141592653589793 / 180.0) * radius, y + radius + Math.cos(i * 3.141592653589793 / 180.0) * radius);
        }
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glScaled(2.0, 2.0, 2.0);
        GL11.glPopAttrib();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }
    
    public static void enableChams() {
        GL11.glEnable(32823);
        GlStateManager.func_179088_q();
        GlStateManager.func_179136_a(1.0f, -1000000.0f);
    }
    
    public static void disableChams() {
        GL11.glDisable(32823);
        GlStateManager.func_179136_a(1.0f, 1000000.0f);
        GlStateManager.func_179113_r();
    }
    
    public static void unForceColor() {
        MobRenderUtils.unsetColor();
    }
    
    public static void renderStarredNametag(final Entity entityIn, final String str, final double x, final double y, final double z, final int maxDistance) {
        final double d0 = entityIn.func_70068_e(OringoClient.mc.func_175598_ae().field_78734_h);
        if (d0 <= maxDistance * maxDistance) {
            final FontRenderer fontrenderer = OringoClient.mc.func_175598_ae().func_78716_a();
            final float f = 1.6f;
            final float f2 = 0.016666668f * f;
            GlStateManager.func_179094_E();
            GlStateManager.func_179109_b((float)x + 0.0f, (float)y + entityIn.field_70131_O + 0.5f, (float)z);
            GL11.glNormal3f(0.0f, 1.0f, 0.0f);
            GlStateManager.func_179114_b(-OringoClient.mc.func_175598_ae().field_78735_i, 0.0f, 1.0f, 0.0f);
            GlStateManager.func_179114_b(OringoClient.mc.func_175598_ae().field_78732_j, 1.0f, 0.0f, 0.0f);
            GlStateManager.func_179152_a(-f2, -f2, f2);
            GlStateManager.func_179140_f();
            GlStateManager.func_179132_a(false);
            GlStateManager.func_179097_i();
            GlStateManager.func_179147_l();
            GlStateManager.func_179120_a(770, 771, 1, 0);
            final int i = 0;
            fontrenderer.func_78276_b(str, -fontrenderer.func_78256_a(str) / 2, i, -1);
            GlStateManager.func_179126_j();
            GlStateManager.func_179132_a(true);
            fontrenderer.func_78276_b(str, -fontrenderer.func_78256_a(str) / 2, i, -1);
            GlStateManager.func_179145_e();
            GlStateManager.func_179084_k();
            GlStateManager.func_179131_c(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.func_179121_F();
        }
    }
    
    public static void renderLivingLabel(final Entity entityIn, final String str, final double x, final double y, final double z, final int maxDistance) {
        final double d0 = entityIn.func_70068_e(OringoClient.mc.func_175598_ae().field_78734_h);
        if (d0 <= maxDistance * maxDistance) {
            final FontRenderer fontrenderer = OringoClient.mc.func_175598_ae().func_78716_a();
            final float f = 1.6f;
            final float f2 = 0.016666668f * f;
            GlStateManager.func_179094_E();
            GlStateManager.func_179109_b((float)x + 0.0f, (float)y + entityIn.field_70131_O + 0.5f, (float)z);
            GL11.glNormal3f(0.0f, 1.0f, 0.0f);
            GlStateManager.func_179114_b(-OringoClient.mc.func_175598_ae().field_78735_i, 0.0f, 1.0f, 0.0f);
            GlStateManager.func_179114_b(OringoClient.mc.func_175598_ae().field_78732_j, 1.0f, 0.0f, 0.0f);
            GlStateManager.func_179152_a(-f2, -f2, f2);
            GlStateManager.func_179140_f();
            GlStateManager.func_179132_a(false);
            GlStateManager.func_179097_i();
            GlStateManager.func_179147_l();
            GlStateManager.func_179120_a(770, 771, 1, 0);
            final Tessellator tessellator = Tessellator.func_178181_a();
            final WorldRenderer worldrenderer = tessellator.func_178180_c();
            int i = 0;
            if (str.equals("deadmau5")) {
                i = -10;
            }
            final int j = fontrenderer.func_78256_a(str) / 2;
            GlStateManager.func_179090_x();
            worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181706_f);
            worldrenderer.func_181662_b((double)(-j - 1), (double)(-1 + i), 0.0).func_181666_a(0.0f, 0.0f, 0.0f, 0.25f).func_181675_d();
            worldrenderer.func_181662_b((double)(-j - 1), (double)(8 + i), 0.0).func_181666_a(0.0f, 0.0f, 0.0f, 0.25f).func_181675_d();
            worldrenderer.func_181662_b((double)(j + 1), (double)(8 + i), 0.0).func_181666_a(0.0f, 0.0f, 0.0f, 0.25f).func_181675_d();
            worldrenderer.func_181662_b((double)(j + 1), (double)(-1 + i), 0.0).func_181666_a(0.0f, 0.0f, 0.0f, 0.25f).func_181675_d();
            tessellator.func_78381_a();
            GlStateManager.func_179098_w();
            fontrenderer.func_78276_b(str, -fontrenderer.func_78256_a(str) / 2, i, 553648127);
            GlStateManager.func_179126_j();
            GlStateManager.func_179132_a(true);
            fontrenderer.func_78276_b(str, -fontrenderer.func_78256_a(str) / 2, i, -1);
            GlStateManager.func_179145_e();
            GlStateManager.func_179084_k();
            GlStateManager.func_179131_c(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.func_179121_F();
        }
    }
    
    public static Color glColor(final int hex) {
        final float alpha = (hex >> 24 & 0xFF) / 256.0f;
        final float red = (hex >> 16 & 0xFF) / 255.0f;
        final float green = (hex >> 8 & 0xFF) / 255.0f;
        final float blue = (hex & 0xFF) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
        return new Color(red, green, blue, alpha);
    }
}
