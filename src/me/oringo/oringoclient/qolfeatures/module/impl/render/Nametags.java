// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.render;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.oringo.oringoclient.OringoClient;
import me.oringo.oringoclient.utils.MathUtil;
import me.oringo.oringoclient.utils.RenderUtils;
import java.awt.Color;
import me.oringo.oringoclient.utils.font.Fonts;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import me.oringo.oringoclient.qolfeatures.module.impl.combat.AntiBot;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderLivingEvent;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class Nametags extends Module
{
    public Nametags() {
        super("Nametags", Category.RENDER);
    }
    
    @SubscribeEvent
    public void onRender(final RenderLivingEvent.Specials.Pre<EntityLivingBase> event) {
        if (this.isToggled() && AntiBot.isValidEntity((Entity)event.entity) && event.entity instanceof EntityPlayer && event.entity != Nametags.mc.field_71439_g && event.entity.func_70032_d((Entity)Nametags.mc.field_71439_g) < 100.0f) {
            event.setCanceled(true);
            GlStateManager.func_179092_a(516, 0.1f);
            final String name = event.entity.func_70005_c_();
            final double x = event.x;
            final double y = event.y;
            final double z = event.z;
            final float f = Math.max(1.4f, event.entity.func_70032_d((Entity)Nametags.mc.field_71439_g) / 10.0f);
            final float scale = 0.016666668f * f;
            GlStateManager.func_179094_E();
            GlStateManager.func_179109_b((float)x + 0.0f, (float)y + event.entity.field_70131_O + 0.5f, (float)z);
            GL11.glNormal3f(0.0f, 1.0f, 0.0f);
            GlStateManager.func_179114_b(-Nametags.mc.func_175598_ae().field_78735_i, 0.0f, 1.0f, 0.0f);
            GlStateManager.func_179114_b(Nametags.mc.func_175598_ae().field_78732_j, 1.0f, 0.0f, 0.0f);
            GlStateManager.func_179152_a(-scale, -scale, scale);
            GlStateManager.func_179140_f();
            GlStateManager.func_179132_a(false);
            GlStateManager.func_179097_i();
            GlStateManager.func_179147_l();
            GlStateManager.func_179120_a(770, 771, 1, 0);
            final float textWidth = (float)Math.max(Fonts.robotoMediumBold.getStringWidth(name) / 2.0, 30.0);
            GlStateManager.func_179090_x();
            RenderUtils.drawRect(-textWidth - 3.0f, (float)(Fonts.robotoMediumBold.getHeight() + 3), textWidth + 3.0f, -3.0f, new Color(20, 20, 20, 80).getRGB());
            RenderUtils.drawRect(-textWidth - 3.0f, (float)(Fonts.robotoMediumBold.getHeight() + 3), (float)((textWidth + 3.0f) * ((MathUtil.clamp(event.entity.func_110143_aJ() / event.entity.func_110138_aP(), 1.0, 0.0) - 0.5) * 2.0)), (float)(Fonts.robotoMediumBold.getHeight() + 2), OringoClient.clickGui.getColor(event.entity.func_145782_y()).getRGB());
            GlStateManager.func_179098_w();
            Fonts.robotoMediumBold.drawSmoothString(name, -Fonts.robotoMediumBold.getStringWidth(name) / 2.0, 0.0f, Color.WHITE.getRGB());
            GlStateManager.func_179126_j();
            GlStateManager.func_179132_a(true);
            Fonts.robotoMediumBold.drawSmoothString(name, -Fonts.robotoMediumBold.getStringWidth(name) / 2.0, 0.0f, Color.WHITE.getRGB());
            GlStateManager.func_179145_e();
            GlStateManager.func_179084_k();
            GlStateManager.func_179131_c(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.func_179121_F();
        }
    }
}
