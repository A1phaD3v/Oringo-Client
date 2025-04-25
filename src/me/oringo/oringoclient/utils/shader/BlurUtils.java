// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.utils.shader;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL14;
import net.minecraft.client.gui.ScaledResolution;
import me.oringo.oringoclient.utils.RenderUtils;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import java.util.Set;
import java.util.Iterator;
import java.util.Collection;
import java.util.Map;
import org.lwjgl.util.vector.Matrix4f;
import net.minecraft.client.shader.Shader;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraft.client.shader.Framebuffer;
import java.util.HashSet;
import java.util.HashMap;

public class BlurUtils
{
    private static HashMap<Float, OutputStuff> blurOutput;
    private static HashMap<Float, Long> lastBlurUse;
    private static HashSet<Float> requestedBlurs;
    private static int fogColour;
    private static boolean registered;
    private static Framebuffer blurOutputHorz;
    
    public static void registerListener() {
        if (!BlurUtils.registered) {
            BlurUtils.registered = true;
            MinecraftForge.EVENT_BUS.register((Object)new BlurUtils());
        }
    }
    
    public static void processBlurs() {
        final long currentTime = System.currentTimeMillis();
        for (final float blur : BlurUtils.requestedBlurs) {
            BlurUtils.lastBlurUse.put(blur, currentTime);
            final int width = Minecraft.func_71410_x().field_71443_c;
            final int height = Minecraft.func_71410_x().field_71440_d;
            final Framebuffer fb;
            final OutputStuff output = BlurUtils.blurOutput.computeIfAbsent(blur, k -> {
                fb = new Framebuffer(width, height, false);
                fb.func_147607_a(9728);
                return new OutputStuff(fb, null, null);
            });
            if (output.framebuffer.field_147621_c != width || output.framebuffer.field_147618_d != height) {
                output.framebuffer.func_147613_a(width, height);
                if (output.blurShaderHorz != null) {
                    output.blurShaderHorz.func_148045_a((Matrix4f)createProjectionMatrix(width, height));
                }
                if (output.blurShaderVert != null) {
                    output.blurShaderVert.func_148045_a((Matrix4f)createProjectionMatrix(width, height));
                }
            }
            blurBackground(output, blur);
        }
        final Set<Float> remove = new HashSet<Float>();
        for (final Map.Entry<Float, Long> entry : BlurUtils.lastBlurUse.entrySet()) {
            if (currentTime - entry.getValue() > 30000L) {
                remove.add(entry.getKey());
            }
        }
        for (final Map.Entry<Float, OutputStuff> entry2 : BlurUtils.blurOutput.entrySet()) {
            if (remove.contains(entry2.getKey())) {
                entry2.getValue().framebuffer.func_147608_a();
                entry2.getValue().blurShaderHorz.func_148044_b();
                entry2.getValue().blurShaderVert.func_148044_b();
            }
        }
        BlurUtils.lastBlurUse.keySet().removeAll(remove);
        BlurUtils.blurOutput.keySet().removeAll(remove);
        BlurUtils.requestedBlurs.clear();
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onScreenRender(final RenderGameOverlayEvent.Pre event) {
        if (event.type == RenderGameOverlayEvent.ElementType.ALL) {
            processBlurs();
        }
        Minecraft.func_71410_x().func_147110_a().func_147610_a(false);
    }
    
    private static net.minecraft.util.Matrix4f createProjectionMatrix(final int width, final int height) {
        final net.minecraft.util.Matrix4f projMatrix = new net.minecraft.util.Matrix4f();
        projMatrix.setIdentity();
        projMatrix.m00 = 2.0f / width;
        projMatrix.m11 = 2.0f / -height;
        projMatrix.m22 = -0.0020001999f;
        projMatrix.m33 = 1.0f;
        projMatrix.m03 = -1.0f;
        projMatrix.m13 = 1.0f;
        projMatrix.m23 = -1.0001999f;
        return projMatrix;
    }
    
    private static void blurBackground(final OutputStuff output, final float blurFactor) {
        if (!OpenGlHelper.func_148822_b() || !OpenGlHelper.func_153193_b()) {
            return;
        }
        final int width = Minecraft.func_71410_x().field_71443_c;
        final int height = Minecraft.func_71410_x().field_71440_d;
        GlStateManager.func_179128_n(5889);
        GlStateManager.func_179096_D();
        GlStateManager.func_179130_a(0.0, (double)width, (double)height, 0.0, 1000.0, 3000.0);
        GlStateManager.func_179128_n(5888);
        GlStateManager.func_179096_D();
        GlStateManager.func_179109_b(0.0f, 0.0f, -2000.0f);
        if (BlurUtils.blurOutputHorz == null) {
            (BlurUtils.blurOutputHorz = new Framebuffer(width, height, false)).func_147607_a(9728);
        }
        if (BlurUtils.blurOutputHorz == null || output == null) {
            return;
        }
        if (BlurUtils.blurOutputHorz.field_147621_c != width || BlurUtils.blurOutputHorz.field_147618_d != height) {
            BlurUtils.blurOutputHorz.func_147613_a(width, height);
            Minecraft.func_71410_x().func_147110_a().func_147610_a(false);
        }
        if (output.blurShaderHorz == null) {
            try {
                output.blurShaderHorz = new Shader(Minecraft.func_71410_x().func_110442_L(), "blur", output.framebuffer, BlurUtils.blurOutputHorz);
                output.blurShaderHorz.func_148043_c().func_147991_a("BlurDir").func_148087_a(1.0f, 0.0f);
                output.blurShaderHorz.func_148045_a((Matrix4f)createProjectionMatrix(width, height));
            }
            catch (Exception ex) {}
        }
        if (output.blurShaderVert == null) {
            try {
                output.blurShaderVert = new Shader(Minecraft.func_71410_x().func_110442_L(), "blur", BlurUtils.blurOutputHorz, output.framebuffer);
                output.blurShaderVert.func_148043_c().func_147991_a("BlurDir").func_148087_a(0.0f, 1.0f);
                output.blurShaderVert.func_148045_a((Matrix4f)createProjectionMatrix(width, height));
            }
            catch (Exception ex2) {}
        }
        if (output.blurShaderHorz != null && output.blurShaderVert != null) {
            if (output.blurShaderHorz.func_148043_c().func_147991_a("Radius") == null) {
                return;
            }
            output.blurShaderHorz.func_148043_c().func_147991_a("Radius").func_148090_a(blurFactor);
            output.blurShaderVert.func_148043_c().func_147991_a("Radius").func_148090_a(blurFactor);
            GL11.glPushMatrix();
            GL30.glBindFramebuffer(36008, Minecraft.func_71410_x().func_147110_a().field_147616_f);
            GL30.glBindFramebuffer(36009, output.framebuffer.field_147616_f);
            GL30.glBlitFramebuffer(0, 0, width, height, 0, 0, output.framebuffer.field_147621_c, output.framebuffer.field_147618_d, 16384, 9728);
            output.blurShaderHorz.func_148042_a(0.0f);
            output.blurShaderVert.func_148042_a(0.0f);
            GlStateManager.func_179126_j();
            GL11.glPopMatrix();
        }
        Minecraft.func_71410_x().func_147110_a().func_147610_a(false);
    }
    
    public static void renderBlurredBackground(final float blurStrength, final float screenWidth, final float screenHeight, final float x, final float y, final float blurWidth, final float blurHeight) {
        if (!OpenGlHelper.func_148822_b() || !OpenGlHelper.func_153193_b()) {
            return;
        }
        if (blurStrength < 0.5) {
            return;
        }
        BlurUtils.requestedBlurs.add(blurStrength);
        if (BlurUtils.blurOutput.isEmpty()) {
            return;
        }
        OutputStuff out = BlurUtils.blurOutput.get(blurStrength);
        if (out == null) {
            out = BlurUtils.blurOutput.values().iterator().next();
        }
        final float uMin = x / screenWidth;
        final float uMax = (x + blurWidth) / screenWidth;
        final float vMin = (screenHeight - y) / screenHeight;
        final float vMax = (screenHeight - y - blurHeight) / screenHeight;
        GlStateManager.func_179132_a(false);
        RenderUtils.drawRect(x, y, x + blurWidth, y + blurHeight, BlurUtils.fogColour);
        out.framebuffer.func_147612_c();
        GlStateManager.func_179131_c(1.0f, 1.0f, 1.0f, 1.0f);
        drawTexturedRect(x, y, blurWidth, blurHeight, uMin, uMax, vMin, vMax, 9728);
        out.framebuffer.func_147606_d();
        GlStateManager.func_179132_a(true);
        GlStateManager.func_179117_G();
    }
    
    public static void renderBlurredBackground(final float blurStrength) {
        final ScaledResolution res = new ScaledResolution(Minecraft.func_71410_x());
        renderBlurredBackground(blurStrength, (float)res.func_78326_a(), (float)res.func_78328_b(), 0.0f, 0.0f, (float)res.func_78326_a(), (float)res.func_78328_b());
    }
    
    public static void drawTexturedRect(final float x, final float y, final float width, final float height, final float uMin, final float uMax, final float vMin, final float vMax, final int filter) {
        GlStateManager.func_179147_l();
        GL14.glBlendFuncSeparate(770, 771, 1, 771);
        drawTexturedRectNoBlend(x, y, width, height, uMin, uMax, vMin, vMax, filter);
        GlStateManager.func_179084_k();
    }
    
    public static void drawTexturedRectNoBlend(final float x, final float y, final float width, final float height, final float uMin, final float uMax, final float vMin, final float vMax, final int filter) {
        GlStateManager.func_179098_w();
        GL11.glTexParameteri(3553, 10241, filter);
        GL11.glTexParameteri(3553, 10240, filter);
        final Tessellator tessellator = Tessellator.func_178181_a();
        final WorldRenderer worldrenderer = tessellator.func_178180_c();
        worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181707_g);
        worldrenderer.func_181662_b((double)x, (double)(y + height), 0.0).func_181673_a((double)uMin, (double)vMax).func_181675_d();
        worldrenderer.func_181662_b((double)(x + width), (double)(y + height), 0.0).func_181673_a((double)uMax, (double)vMax).func_181675_d();
        worldrenderer.func_181662_b((double)(x + width), (double)y, 0.0).func_181673_a((double)uMax, (double)vMin).func_181675_d();
        worldrenderer.func_181662_b((double)x, (double)y, 0.0).func_181673_a((double)uMin, (double)vMin).func_181675_d();
        tessellator.func_78381_a();
        GL11.glTexParameteri(3553, 10241, 9728);
        GL11.glTexParameteri(3553, 10240, 9728);
    }
    
    static {
        BlurUtils.blurOutput = new HashMap<Float, OutputStuff>();
        BlurUtils.lastBlurUse = new HashMap<Float, Long>();
        BlurUtils.requestedBlurs = new HashSet<Float>();
        BlurUtils.fogColour = 0;
        BlurUtils.registered = false;
        BlurUtils.blurOutputHorz = null;
    }
    
    private static class OutputStuff
    {
        public Framebuffer framebuffer;
        public Shader blurShaderHorz;
        public Shader blurShaderVert;
        
        public OutputStuff(final Framebuffer framebuffer, final Shader blurShaderHorz, final Shader blurShaderVert) {
            this.blurShaderHorz = null;
            this.blurShaderVert = null;
            this.framebuffer = framebuffer;
            this.blurShaderHorz = blurShaderHorz;
            this.blurShaderVert = blurShaderVert;
        }
    }
}
