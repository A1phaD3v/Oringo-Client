// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.render;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.oringo.oringoclient.events.PostGuiOpenEvent;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import java.util.concurrent.Callable;
import net.minecraft.crash.CrashReport;
import net.minecraftforge.client.ForgeHooksClient;
import me.oringo.oringoclient.utils.TimerUtil;
import me.oringo.oringoclient.utils.RenderUtils;
import java.awt.Color;
import org.lwjgl.input.Mouse;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class NoRender extends Module
{
    public NoRender() {
        super("NoRender", Category.RENDER);
    }
    
    @Override
    public void onEnable() {
        NoRender.mc.field_71454_w = true;
    }
    
    public static void drawGui() {
        if (NoRender.mc.field_71454_w && NoRender.mc.field_71462_r != null) {
            NoRender.mc.func_71364_i();
            if (NoRender.mc.field_71441_e == null) {
                GlStateManager.func_179083_b(0, 0, NoRender.mc.field_71443_c, NoRender.mc.field_71440_d);
                GlStateManager.func_179128_n(5889);
                GlStateManager.func_179096_D();
                GlStateManager.func_179128_n(5888);
                GlStateManager.func_179096_D();
                NoRender.mc.field_71460_t.func_78478_c();
            }
            else {
                GlStateManager.func_179092_a(516, 0.1f);
                NoRender.mc.field_71460_t.func_78478_c();
            }
            final ScaledResolution scaledresolution = new ScaledResolution(NoRender.mc);
            final int i1 = scaledresolution.func_78326_a();
            final int j1 = scaledresolution.func_78328_b();
            final int k1 = Mouse.getX() * i1 / NoRender.mc.field_71443_c;
            final int l1 = j1 - Mouse.getY() * j1 / NoRender.mc.field_71440_d - 1;
            GlStateManager.func_179086_m(256);
            RenderUtils.drawRect(0.0f, 0.0f, (float)i1, (float)j1, Color.black.getRGB());
            try {
                ForgeHooksClient.drawScreen(NoRender.mc.field_71462_r, k1, l1, TimerUtil.getTimer().field_74281_c);
            }
            catch (Throwable throwable) {
                final CrashReport crashreport = CrashReport.func_85055_a(throwable, "Rendering screen");
                final CrashReportCategory crashreportcategory = crashreport.func_85058_a("Screen render details");
                crashreportcategory.func_71500_a("Screen name", (Callable)new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        return NoRender.mc.field_71462_r.getClass().getCanonicalName();
                    }
                });
                crashreportcategory.func_71500_a("Mouse location", (Callable)new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        return String.format("Scaled: (%d, %d). Absolute: (%d, %d)", k1, l1, Mouse.getX(), Mouse.getY());
                    }
                });
                crashreportcategory.func_71500_a("Screen size", (Callable)new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        return String.format("Scaled: (%d, %d). Absolute: (%d, %d). Scale factor of %d", scaledresolution.func_78326_a(), scaledresolution.func_78328_b(), NoRender.mc.field_71443_c, NoRender.mc.field_71440_d, scaledresolution.func_78325_e());
                    }
                });
                throw new ReportedException(crashreport);
            }
        }
    }
    
    @SubscribeEvent
    public void onPostGui(final PostGuiOpenEvent event) {
        if (this.isToggled()) {
            NoRender.mc.field_71454_w = true;
        }
    }
    
    @Override
    public void onDisable() {
        NoRender.mc.field_71454_w = false;
    }
}
