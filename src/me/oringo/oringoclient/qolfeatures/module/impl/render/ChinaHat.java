// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.render;

import me.oringo.oringoclient.OringoClient;
import java.awt.Color;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.opengl.GL11;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.ModeSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class ChinaHat extends Module
{
    public NumberSetting radius;
    public NumberSetting height;
    public NumberSetting pos;
    public NumberSetting rotation;
    public NumberSetting angles;
    public BooleanSetting firstPerson;
    public BooleanSetting shade;
    public ModeSetting colorMode;
    public NumberSetting red;
    public NumberSetting green;
    public NumberSetting blue;
    public NumberSetting redGradient1;
    public NumberSetting greenGradient1;
    public NumberSetting blueGradient1;
    public NumberSetting redGradient2;
    public NumberSetting greenGradient2;
    public NumberSetting blueGradient2;
    
    public ChinaHat() {
        super("China hat", 0, Category.RENDER);
        this.radius = new NumberSetting("Radius", 0.7, 0.5, 1.0, 0.01);
        this.height = new NumberSetting("Height", 0.3, 0.10000000149011612, 0.699999988079071, 0.01);
        this.pos = new NumberSetting("Position", 0.1, -0.5, 0.5, 0.01);
        this.rotation = new NumberSetting("Rotation", 5.0, 0.0, 5.0, 0.1);
        this.angles = new NumberSetting("Angles", 8.0, 4.0, 90.0, 1.0);
        this.firstPerson = new BooleanSetting("Show in first person", false);
        this.shade = new BooleanSetting("Shade", true);
        this.colorMode = new ModeSetting("Color mode", "Rainbow", new String[] { "Rainbow", "Gradient", "Single", "Theme" });
        this.red = new NumberSetting("Red", 0.0, 0.0, 255.0, 1.0) {
            @Override
            public boolean isHidden() {
                return !ChinaHat.this.colorMode.is("Single");
            }
        };
        this.green = new NumberSetting("Green", 80.0, 0.0, 255.0, 1.0) {
            @Override
            public boolean isHidden() {
                return !ChinaHat.this.colorMode.is("Single");
            }
        };
        this.blue = new NumberSetting("Blue", 255.0, 0.0, 255.0, 1.0) {
            @Override
            public boolean isHidden() {
                return !ChinaHat.this.colorMode.is("Single");
            }
        };
        this.redGradient1 = new NumberSetting("Red 1", 255.0, 0.0, 255.0, 1.0) {
            @Override
            public boolean isHidden() {
                return !ChinaHat.this.colorMode.is("Gradient");
            }
        };
        this.greenGradient1 = new NumberSetting("Green 1", 0.0, 0.0, 255.0, 1.0) {
            @Override
            public boolean isHidden() {
                return !ChinaHat.this.colorMode.is("Gradient");
            }
        };
        this.blueGradient1 = new NumberSetting("Blue 1", 255.0, 0.0, 255.0, 1.0) {
            @Override
            public boolean isHidden() {
                return !ChinaHat.this.colorMode.is("Gradient");
            }
        };
        this.redGradient2 = new NumberSetting("Red 2", 90.0, 0.0, 255.0, 1.0) {
            @Override
            public boolean isHidden() {
                return !ChinaHat.this.colorMode.is("Gradient");
            }
        };
        this.greenGradient2 = new NumberSetting("Green 2", 10.0, 0.0, 255.0, 1.0) {
            @Override
            public boolean isHidden() {
                return !ChinaHat.this.colorMode.is("Gradient");
            }
        };
        this.blueGradient2 = new NumberSetting("Blue 2", 255.0, 0.0, 255.0, 1.0) {
            @Override
            public boolean isHidden() {
                return !ChinaHat.this.colorMode.is("Gradient");
            }
        };
        this.addSettings(this.radius, this.height, this.firstPerson, this.rotation, this.pos, this.angles, this.shade, this.colorMode, this.red, this.green, this.blue, this.redGradient1, this.greenGradient1, this.blueGradient1, this.redGradient2, this.greenGradient2, this.blueGradient2);
    }
    
    @SubscribeEvent
    public void onRender(final RenderWorldLastEvent event) {
        if (this.isToggled() && (ChinaHat.mc.field_71474_y.field_74320_O != 0 || this.firstPerson.isEnabled())) {
            this.drawChinaHat((EntityLivingBase)ChinaHat.mc.field_71439_g, event.partialTicks);
        }
    }
    
    private void drawChinaHat(final EntityLivingBase entity, final float partialTicks) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        OpenGlHelper.func_148821_a(770, 771, 1, 0);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        if (this.shade.isEnabled()) {
            GL11.glShadeModel(7425);
        }
        GL11.glDisable(3553);
        GL11.glDisable(2884);
        GlStateManager.func_179140_f();
        GL11.glTranslated(entity.field_70142_S + (entity.field_70165_t - entity.field_70142_S) * partialTicks - ChinaHat.mc.func_175598_ae().field_78730_l, entity.field_70137_T + (entity.field_70163_u - entity.field_70137_T) * partialTicks - ChinaHat.mc.func_175598_ae().field_78731_m + entity.field_70131_O + (entity.func_70093_af() ? (this.pos.getValue() - 0.23000000417232513) : this.pos.getValue()), entity.field_70136_U + (entity.field_70161_v - entity.field_70136_U) * partialTicks - ChinaHat.mc.func_175598_ae().field_78728_n);
        GL11.glRotatef((float)((entity.field_70173_aa + partialTicks) * this.rotation.getValue()) - 90.0f, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(-(ChinaHat.mc.field_71439_g.field_70758_at + (ChinaHat.mc.field_71439_g.field_70759_as - ChinaHat.mc.field_71439_g.field_70758_at) * partialTicks), 0.0f, 1.0f, 0.0f);
        final double radius = this.radius.getValue();
        GL11.glLineWidth(2.0f);
        GL11.glBegin(2);
        for (int i = 0; i <= this.angles.getValue(); ++i) {
            final Color color = this.getColor(i, (int)this.angles.getValue(), false);
            GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 0.5f);
            GL11.glVertex3d(Math.cos(i * 3.141592653589793 / (this.angles.getValue() / 2.0)) * radius, 0.0, Math.sin(i * 3.141592653589793 / (this.angles.getValue() / 2.0)) * radius);
        }
        GL11.glEnd();
        GL11.glBegin(6);
        final Color c1 = this.getColor(0.0, this.angles.getValue(), true);
        GL11.glColor4f(c1.getRed() / 255.0f, c1.getGreen() / 255.0f, c1.getBlue() / 255.0f, 0.8f);
        GL11.glVertex3d(0.0, this.height.getValue(), 0.0);
        for (int j = 0; j <= this.angles.getValue(); ++j) {
            final Color color2 = this.getColor(j, (int)this.angles.getValue(), false);
            GL11.glColor4f(color2.getRed() / 255.0f, color2.getGreen() / 255.0f, color2.getBlue() / 255.0f, 0.3f);
            GL11.glVertex3d(Math.cos(j * 3.141592653589793 / (this.angles.getValue() / 2.0)) * radius, 0.0, Math.sin(j * 3.141592653589793 / (this.angles.getValue() / 2.0)) * radius);
        }
        GL11.glVertex3d(0.0, this.height.getValue(), 0.0);
        GL11.glEnd();
        GL11.glShadeModel(7424);
        GL11.glEnable(2884);
        GlStateManager.func_179117_G();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glPopMatrix();
    }
    
    public Color getColor(final double i, final double max, final boolean first) {
        final String selected = this.colorMode.getSelected();
        switch (selected) {
            case "Rainbow": {
                return Color.getHSBColor((float)(i / max), 0.65f, 1.0f);
            }
            case "Gradient": {
                if (first) {
                    return new Color((int)this.redGradient1.getValue(), (int)this.greenGradient1.getValue(), (int)this.blueGradient1.getValue());
                }
                return new Color((int)this.redGradient2.getValue(), (int)this.greenGradient2.getValue(), (int)this.blueGradient2.getValue());
            }
            case "Theme": {
                double c = i / max * 10.0;
                if (i > max / 2.0) {
                    c = 10.0 - c;
                }
                return OringoClient.clickGui.getColor((int)c);
            }
            default: {
                if (first) {
                    return new Color((int)this.red.getValue(), (int)this.green.getValue(), (int)this.blue.getValue()).darker().darker();
                }
                return new Color((int)this.red.getValue(), (int)this.green.getValue(), (int)this.blue.getValue());
            }
        }
    }
}
