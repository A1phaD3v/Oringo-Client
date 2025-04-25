// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.render;

import java.util.ArrayList;
import me.oringo.oringoclient.events.WorldJoinEvent;
import java.awt.Color;
import java.util.Iterator;
import me.oringo.oringoclient.OringoClient;
import org.lwjgl.opengl.GL11;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.oringo.oringoclient.events.PlayerUpdateEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import net.minecraft.util.Vec3;
import java.util.List;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class Trial extends Module
{
    public static final NumberSetting count;
    private static final List<Vec3> vecs;
    
    public Trial() {
        super("Trail", Category.RENDER);
        this.addSettings(Trial.count);
    }
    
    @SubscribeEvent
    public void onUpdate(final PlayerUpdateEvent event) {
        if (this.isToggled()) {
            Trial.vecs.add(new Vec3(Trial.mc.field_71439_g.field_70169_q, Trial.mc.field_71439_g.field_70167_r + 0.1, Trial.mc.field_71439_g.field_70166_s));
            while (Trial.vecs.size() > Trial.count.getValue()) {
                Trial.vecs.remove(0);
            }
        }
    }
    
    @SubscribeEvent
    public void onRenderWorld(final RenderWorldLastEvent event) {
        if (this.isToggled() && !Trial.vecs.isEmpty() && Trial.mc.field_71439_g != null && Trial.mc.func_175598_ae() != null) {
            GL11.glBlendFunc(770, 771);
            GL11.glEnable(3042);
            GL11.glLineWidth(2.5f);
            GL11.glDisable(3553);
            GL11.glDisable(2884);
            GL11.glShadeModel(7425);
            GL11.glEnable(2848);
            GL11.glHint(3154, 4354);
            GL11.glBegin(3);
            int index = 0;
            for (final Vec3 vec : Trial.vecs) {
                final boolean isFirst = index == 0;
                ++index;
                final Color color = OringoClient.clickGui.getColor(index);
                GL11.glColor3f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f);
                if (isFirst && Trial.vecs.size() > 2) {
                    final Vec3 newVec = Trial.vecs.get(1);
                    GL11.glVertex3d(this.interpolate(vec.field_72450_a, newVec.field_72450_a, event.partialTicks) - Trial.mc.func_175598_ae().field_78730_l, this.interpolate(vec.field_72448_b, newVec.field_72448_b, event.partialTicks) - Trial.mc.func_175598_ae().field_78731_m, this.interpolate(vec.field_72449_c, newVec.field_72449_c, event.partialTicks) - Trial.mc.func_175598_ae().field_78728_n);
                }
                else {
                    GL11.glVertex3d(vec.field_72450_a - Trial.mc.func_175598_ae().field_78730_l, vec.field_72448_b - Trial.mc.func_175598_ae().field_78731_m, vec.field_72449_c - Trial.mc.func_175598_ae().field_78728_n);
                }
            }
            final Color color = OringoClient.clickGui.getColor(index);
            GL11.glColor3f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f);
            GL11.glVertex3d(Trial.mc.field_71439_g.field_70169_q + (Trial.mc.field_71439_g.field_70165_t - Trial.mc.field_71439_g.field_70169_q) * event.partialTicks - Trial.mc.func_175598_ae().field_78730_l, Trial.mc.field_71439_g.field_70167_r + (Trial.mc.field_71439_g.field_70163_u - Trial.mc.field_71439_g.field_70167_r) * event.partialTicks - Trial.mc.func_175598_ae().field_78731_m + 0.1, Trial.mc.field_71439_g.field_70166_s + (Trial.mc.field_71439_g.field_70161_v - Trial.mc.field_71439_g.field_70166_s) * event.partialTicks - Trial.mc.func_175598_ae().field_78728_n);
            GL11.glEnd();
            GL11.glEnable(3553);
            GL11.glShadeModel(7424);
            GL11.glEnable(2884);
            GL11.glDisable(2848);
            GL11.glDisable(2881);
            GL11.glDisable(3042);
        }
    }
    
    @SubscribeEvent
    public void onWorldJoin(final WorldJoinEvent event) {
        Trial.vecs.clear();
    }
    
    private double interpolate(final double prev, final double newPos, final float partialTicks) {
        return prev + (newPos - prev) * partialTicks;
    }
    
    private boolean hasMoved() {
        return Trial.mc.field_71439_g.field_70161_v - Trial.mc.field_71439_g.field_70166_s != 0.0 || Trial.mc.field_71439_g.field_70163_u - Trial.mc.field_71439_g.field_70167_r != 0.0 || Trial.mc.field_71439_g.field_70165_t - Trial.mc.field_71439_g.field_70169_q != 0.0;
    }
    
    static {
        count = new NumberSetting("Points", 20.0, 5.0, 100.0, 1.0);
        vecs = new ArrayList<Vec3>();
    }
}
