// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.ui.hud.impl;

import me.oringo.oringoclient.utils.MathUtil;
import net.minecraft.entity.Entity;
import net.minecraft.client.gui.inventory.GuiInventory;
import me.oringo.oringoclient.OringoClient;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.oringo.oringoclient.utils.font.Fonts;
import net.minecraft.client.Minecraft;
import me.oringo.oringoclient.utils.shader.BlurUtils;
import me.oringo.oringoclient.utils.RenderUtils;
import java.awt.Color;
import me.oringo.oringoclient.utils.StencilUtils;
import net.minecraft.client.gui.ScaledResolution;
import me.oringo.oringoclient.qolfeatures.module.impl.render.TargetHUD;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.GuiChat;
import me.oringo.oringoclient.qolfeatures.module.impl.combat.KillAura;
import me.oringo.oringoclient.ui.hud.HudVec;
import net.minecraftforge.common.MinecraftForge;
import me.oringo.oringoclient.utils.MilliTimer;
import net.minecraft.entity.EntityLivingBase;
import me.oringo.oringoclient.ui.hud.DraggableComponent;

public class TargetComponent extends DraggableComponent
{
    public static final TargetComponent INSTANCE;
    private float lastHp;
    private static EntityLivingBase lastEntity;
    private static final MilliTimer resetTimer;
    private static final MilliTimer startTimer;
    
    public TargetComponent() {
        this.lastHp = 0.8f;
        this.setSize(150.0, 50.0);
        MinecraftForge.EVENT_BUS.register((Object)this);
    }
    
    @Override
    public HudVec drawScreen() {
        return this.drawScreen((EntityLivingBase)((KillAura.target == null && TargetComponent.mc.field_71462_r instanceof GuiChat) ? TargetComponent.mc.field_71439_g : KillAura.target));
    }
    
    public HudVec drawScreen(final EntityLivingBase entity) {
        if (entity != null) {
            if (TargetComponent.lastEntity == null) {
                TargetComponent.startTimer.reset();
            }
            TargetComponent.resetTimer.reset();
        }
        if (TargetComponent.resetTimer.hasTimePassed(750L) || entity != null) {
            TargetComponent.lastEntity = entity;
        }
        if (TargetComponent.lastEntity != null) {
            super.drawScreen();
            final double x = this.getX();
            final double y = this.getY();
            GL11.glPushMatrix();
            int blur = 0;
            final String selected = TargetHUD.getInstance().blurStrength.getSelected();
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
            final ScaledResolution resolution = new ScaledResolution(TargetComponent.mc);
            GL11.glPushMatrix();
            scale(x + 75.0, y + 25.0, 0.0);
            StencilUtils.initStencil();
            StencilUtils.bindWriteStencilBuffer();
            RenderUtils.drawRoundedRect2(x, y, 150.0, 50.0, 5.0, Color.black.getRGB());
            StencilUtils.bindReadStencilBuffer(1);
            GL11.glPopMatrix();
            BlurUtils.renderBlurredBackground((float)blur, (float)resolution.func_78326_a(), (float)resolution.func_78328_b(), 0.0f, 0.0f, (float)resolution.func_78326_a(), (float)resolution.func_78328_b());
            StencilUtils.uninitStencil();
            scale(x + 75.0, y + 25.0, 0.0);
            final float hp = this.lastHp + (getHp() - this.lastHp) / (7.0f * (Minecraft.func_175610_ah() / 20.0f));
            if (Math.abs(hp - this.lastHp) < 0.001f) {
                this.lastHp = hp;
            }
            if (TargetComponent.mc.field_71462_r instanceof GuiChat && this.isHovered()) {
                RenderUtils.drawBorderedRoundedRect((float)x, (float)y, 150.0f, 50.0f, 5.0f, 2.0f, new Color(21, 21, 21, 52).getRGB(), Color.white.getRGB());
            }
            else {
                RenderUtils.drawRoundedRect2(x, y, 150.0, 50.0, 5.0, new Color(21, 21, 21, 52).getRGB());
            }
            Fonts.robotoBig.drawSmoothStringWithShadow(ChatFormatting.stripFormatting(TargetComponent.lastEntity.func_70005_c_()), x + 5.0, y + 6.0, OringoClient.clickGui.getColor(0).brighter().getRGB());
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            final int i = 0;
            try {
                final EntityLivingBase lastEntity = TargetComponent.lastEntity;
                lastEntity.field_70163_u += 1000.0;
                GuiInventory.func_147046_a((int)(x + 130.0), (int)(y + 40.0), (int)(35.0 / Math.max(TargetComponent.lastEntity.field_70131_O, 1.5)), 20.0f, 10.0f, TargetComponent.lastEntity);
                final EntityLivingBase lastEntity2 = TargetComponent.lastEntity;
                lastEntity2.field_70163_u -= 1000.0;
            }
            catch (Exception ex) {}
            Fonts.robotoMediumBold.drawSmoothStringWithShadow((int)(TargetComponent.lastEntity.func_70032_d((Entity)TargetComponent.mc.field_71439_g) * 10.0f) / 10.0 + "m", x + 5.0, y + 11.0 + Fonts.robotoMediumBold.getHeight(), new Color(231, 231, 231).getRGB());
            final String text = String.format("%.1f", getHp() * 100.0f) + "%";
            RenderUtils.drawRoundedRect(x + 10.0, y + 42.0, x + 140.0, y + 46.0, 2.0, Color.HSBtoRGB(0.0f, 0.0f, 0.1f));
            if (hp > 0.05) {
                RenderUtils.drawRoundedRect(x + 10.0, y + 42.0, x + 140.0f * hp, y + 46.0, 2.0, OringoClient.clickGui.getColor(0).getRGB());
            }
            Fonts.robotoMediumBold.drawSmoothStringWithShadow(text, x + 75.0 - Fonts.robotoMediumBold.getStringWidth(text) / 2.0, y + 33.0, new Color(231, 231, 231).getRGB());
            this.lastHp = hp;
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glPopMatrix();
        }
        TargetHUD.getInstance().x.set(this.x);
        TargetHUD.getInstance().y.set(this.y);
        return new HudVec(this.x + this.getWidth(), this.y + this.getHeight());
    }
    
    private static float getHp() {
        if (TargetComponent.lastEntity == null) {
            return 0.0f;
        }
        return MathUtil.clamp(TargetComponent.lastEntity.func_110143_aJ() / TargetComponent.lastEntity.func_110138_aP(), 1.0f, 0.0f);
    }
    
    private static void scale(final double x, final double y, final double startingSize) {
        final ScaledResolution resolution = new ScaledResolution(TargetComponent.mc);
        if (TargetComponent.resetTimer.hasTimePassed(550L)) {
            RenderUtils.doScale(x, y, (750L - TargetComponent.resetTimer.getTimePassed()) / 200.0);
        }
        else if (!TargetComponent.startTimer.hasTimePassed(200L)) {
            RenderUtils.doScale(x, y, TargetComponent.startTimer.getTimePassed() / 200.0 * (1.0 - startingSize + startingSize));
        }
    }
    
    static {
        INSTANCE = new TargetComponent();
        resetTimer = new MilliTimer();
        startTimer = new MilliTimer();
    }
}
