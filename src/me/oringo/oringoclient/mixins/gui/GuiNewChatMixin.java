// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.mixins.gui;

import com.google.common.collect.Lists;
import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Overwrite;
import java.util.Iterator;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import me.oringo.oringoclient.utils.shader.BlurUtils;
import me.oringo.oringoclient.utils.StencilUtils;
import me.oringo.oringoclient.utils.RenderUtils;
import java.awt.Color;
import me.oringo.oringoclient.utils.MathUtil;
import me.oringo.oringoclient.utils.TimerUtil;
import net.minecraft.util.MathHelper;
import me.oringo.oringoclient.utils.font.Fonts;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import me.oringo.oringoclient.OringoClient;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.gui.ChatLine;
import java.util.List;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = { GuiNewChat.class }, priority = 1)
public abstract class GuiNewChatMixin extends GuiMixin
{
    @Shadow
    @Final
    private Minecraft field_146247_f;
    @Shadow
    @Final
    private List<ChatLine> field_146253_i;
    @Shadow
    private int field_146250_j;
    @Shadow
    private boolean field_146251_k;
    
    @Shadow
    public abstract int func_146232_i();
    
    @Shadow
    public abstract boolean func_146241_e();
    
    @Shadow
    public abstract float func_146244_h();
    
    @Shadow
    public abstract int func_146228_f();
    
    @Inject(method = { "drawChat" }, at = { @At("HEAD") }, cancellable = true)
    private void drawChat(final int updateCounter, final CallbackInfo ci) {
        if (OringoClient.interfaces.customChat.isEnabled() && OringoClient.interfaces.isToggled()) {
            if (this.field_146247_f.field_71474_y.field_74343_n != EntityPlayer.EnumChatVisibility.HIDDEN) {
                final ScaledResolution scaledresolution = new ScaledResolution(this.field_146247_f);
                GL11.glPopMatrix();
                GL11.glPushMatrix();
                GlStateManager.func_179109_b(0.0f, (float)(scaledresolution.func_78328_b() - 60), 0.0f);
                final int maxLineCount = this.func_146232_i();
                boolean isChatOpen = false;
                int j = 0;
                final int lineCount = this.field_146253_i.size();
                int fontHeight = OringoClient.interfaces.customChatFont.isEnabled() ? (Fonts.robotoBig.getHeight() + 3) : this.field_146247_f.field_71466_p.field_78288_b;
                if (lineCount > 0) {
                    if (this.func_146241_e()) {
                        isChatOpen = true;
                    }
                    final float scale = this.func_146244_h();
                    GlStateManager.func_179094_E();
                    GlStateManager.func_179109_b(2.0f, 20.0f, 0.0f);
                    GlStateManager.func_179152_a(scale, scale, 1.0f);
                    final int scaledWidth = MathHelper.func_76123_f(this.func_146228_f() / scale);
                    final float x = 0.0f;
                    float y = 0.0f;
                    boolean render = false;
                    for (int i = 0; i + this.field_146250_j < this.field_146253_i.size() && i < maxLineCount; ++i) {
                        final ChatLine chatline = this.field_146253_i.get(i + this.field_146250_j);
                        if (chatline != null && (updateCounter - chatline.func_74540_b() < 200 || isChatOpen)) {
                            render = true;
                            if (!isChatOpen && updateCounter - chatline.func_74540_b() > 195) {
                                float percent = 1.0f - (updateCounter - chatline.func_74540_b() + TimerUtil.getTimer().field_74281_c - 195.0f) / 5.0f;
                                percent = MathUtil.clamp(percent, 0.0f, 1.0f);
                                y -= fontHeight * percent;
                            }
                            else {
                                y -= fontHeight;
                            }
                        }
                    }
                    if (render) {
                        int blur = 0;
                        final String selected = OringoClient.interfaces.blurStrength.getSelected();
                        switch (selected) {
                            case "Low": {
                                blur = 7;
                                break;
                            }
                            case "High": {
                                blur = 25;
                                break;
                            }
                        }
                        if (blur > 0) {
                            for (float k = 0.5f; k < 3.0f; k += 0.5f) {
                                RenderUtils.drawRoundedRect(x + k - 2.0f, y + k, x + scaledWidth + 4.0f + k, 1.0f + k, 5.0, new Color(20, 20, 20, 40).getRGB());
                            }
                        }
                        StencilUtils.initStencil();
                        StencilUtils.bindWriteStencilBuffer();
                        RenderUtils.drawRoundedRect(x - 2.0f, y, x + scaledWidth + 4.0f, 1.0, 5.0, Color.white.getRGB());
                        GL11.glPopMatrix();
                        GL11.glPopMatrix();
                        StencilUtils.bindReadStencilBuffer(1);
                        BlurUtils.renderBlurredBackground((float)blur, (float)scaledresolution.func_78326_a(), (float)scaledresolution.func_78328_b(), 0.0f, 0.0f, (float)scaledresolution.func_78326_a(), (float)scaledresolution.func_78328_b());
                        GL11.glPushMatrix();
                        GlStateManager.func_179109_b(0.0f, (float)(scaledresolution.func_78328_b() - 60), 0.0f);
                        GL11.glPushMatrix();
                        GlStateManager.func_179109_b(2.0f, 20.0f, 0.0f);
                        GlStateManager.func_179152_a(scale, scale, 1.0f);
                        RenderUtils.drawRoundedRect(x - 2.0f, y, x + scaledWidth + 4.0f, 1.0, 5.0, new Color(20, 20, 20, 60).getRGB());
                    }
                    for (int i = 0; i + this.field_146250_j < this.field_146253_i.size() && i < maxLineCount; ++i) {
                        final ChatLine chatline = this.field_146253_i.get(i + this.field_146250_j);
                        if (chatline != null) {
                            final int j2 = updateCounter - chatline.func_74540_b();
                            if (j2 < 200 || isChatOpen) {
                                ++j;
                                final int left = 0;
                                final int top = -i * fontHeight;
                                final String text = chatline.func_151461_a().func_150254_d();
                                GlStateManager.func_179147_l();
                                if (OringoClient.interfaces.customChatFont.isEnabled()) {
                                    Fonts.robotoBig.drawSmoothStringWithShadow(text, (float)left, (float)(top - (fontHeight - 2.3)), Color.white.getRGB());
                                }
                                else {
                                    this.field_146247_f.field_71466_p.func_175063_a(text, (float)left, (float)(top - (fontHeight - 1)), 16777215);
                                }
                                GlStateManager.func_179118_c();
                                GlStateManager.func_179084_k();
                            }
                        }
                    }
                    if (render) {
                        StencilUtils.uninitStencil();
                    }
                    if (isChatOpen) {
                        GlStateManager.func_179109_b(-3.0f, 0.0f, 0.0f);
                        fontHeight = this.field_146247_f.field_71466_p.field_78288_b;
                        final int l2 = lineCount * fontHeight + lineCount;
                        final int i2 = j * fontHeight + j;
                        final int j3 = this.field_146250_j * i2 / lineCount;
                        final int k2 = i2 * i2 / l2;
                        if (l2 != i2) {
                            final int opacity = (j3 > 0) ? 170 : 96;
                            final int l3 = this.field_146251_k ? 13382451 : 3355562;
                            GuiMixin.func_73734_a(0, -j3, 2, -j3 - k2, l3 + (opacity << 24));
                            GuiMixin.func_73734_a(2, -j3, 1, -j3 - k2, 13421772 + (opacity << 24));
                        }
                    }
                    GlStateManager.func_179121_F();
                }
            }
            ci.cancel();
        }
    }
    
    @Overwrite
    public IChatComponent func_146236_a(final int p_146236_1_, final int p_146236_2_) {
        if (!this.func_146241_e()) {
            return null;
        }
        final ScaledResolution scaledresolution = new ScaledResolution(this.field_146247_f);
        final int i = scaledresolution.func_78325_e();
        final float f = this.func_146244_h();
        int j = p_146236_1_ / i - 3;
        int k = p_146236_2_ / i - 27;
        if (OringoClient.interfaces.isToggled() && OringoClient.interfaces.customChat.isEnabled()) {
            k -= 12;
        }
        j = MathHelper.func_76141_d(j / f);
        k = MathHelper.func_76141_d(k / f);
        if (j < 0 || k < 0) {
            return null;
        }
        final int l = Math.min(this.func_146232_i(), this.field_146253_i.size());
        if (j <= MathHelper.func_76141_d(this.func_146228_f() / this.func_146244_h()) && k < this.getHeight() * l + l) {
            final int i2 = k / this.getHeight() + this.field_146250_j;
            if (i2 >= 0 && i2 < this.field_146253_i.size()) {
                final ChatLine chatline = this.field_146253_i.get(i2);
                int j2 = 0;
                for (final IChatComponent ichatcomponent : chatline.func_151461_a()) {
                    if (ichatcomponent instanceof ChatComponentText) {
                        j2 += (int)((OringoClient.interfaces.customChatFont.isEnabled() && OringoClient.interfaces.isToggled() && OringoClient.interfaces.customChat.isEnabled()) ? Fonts.robotoBig.getStringWidth(GuiUtilRenderComponents.func_178909_a(((ChatComponentText)ichatcomponent).func_150265_g(), false)) : this.field_146247_f.field_71466_p.func_78256_a(GuiUtilRenderComponents.func_178909_a(((ChatComponentText)ichatcomponent).func_150265_g(), false)));
                        if (j2 > j) {
                            return ichatcomponent;
                        }
                        continue;
                    }
                }
            }
            return null;
        }
        return null;
    }
    
    private int getHeight() {
        return (OringoClient.interfaces.customChatFont.isEnabled() && OringoClient.interfaces.customChat.isEnabled() && OringoClient.interfaces.isToggled()) ? (Fonts.robotoBig.getHeight() + 3) : this.field_146247_f.field_71466_p.field_78288_b;
    }
    
    @Redirect(method = { "setChatLine" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiUtilRenderComponents;func_178908_a(Lnet/minecraft/util/IChatComponent;ILnet/minecraft/client/gui/FontRenderer;ZZ)Ljava/util/List;"))
    private List<IChatComponent> onFunc(final IChatComponent k, final int s1, final FontRenderer chatcomponenttext, final boolean l, final boolean chatcomponenttext2) {
        return (OringoClient.interfaces.customChatFont.isEnabled() && OringoClient.interfaces.isToggled() && OringoClient.interfaces.customChat.isEnabled()) ? this.wrapToLen(k, s1, chatcomponenttext) : GuiUtilRenderComponents.func_178908_a(k, s1, chatcomponenttext, l, chatcomponenttext2);
    }
    
    private List<IChatComponent> wrapToLen(final IChatComponent p_178908_0_, final int p_178908_1_, final FontRenderer p_178908_2_) {
        int i = 0;
        IChatComponent ichatcomponent = (IChatComponent)new ChatComponentText("");
        final List<IChatComponent> list = (List<IChatComponent>)Lists.newArrayList();
        final List<IChatComponent> list2 = (List<IChatComponent>)Lists.newArrayList((Iterable)p_178908_0_);
        for (int j = 0; j < list2.size(); ++j) {
            final IChatComponent ichatcomponent2 = list2.get(j);
            String s = ichatcomponent2.func_150261_e();
            boolean flag = false;
            if (s.contains("\n")) {
                final int k = s.indexOf(10);
                final String s2 = s.substring(k + 1);
                s = s.substring(0, k + 1);
                final ChatComponentText chatcomponenttext = new ChatComponentText(s2);
                chatcomponenttext.func_150255_a(ichatcomponent2.func_150256_b().func_150232_l());
                list2.add(j + 1, (IChatComponent)chatcomponenttext);
                flag = true;
            }
            final String s3 = GuiUtilRenderComponents.func_178909_a(ichatcomponent2.func_150256_b().func_150218_j() + s, false);
            final String s4 = s3.endsWith("\n") ? s3.substring(0, s3.length() - 1) : s3;
            double i2 = Fonts.robotoBig.getStringWidth(s4);
            ChatComponentText chatcomponenttext2 = new ChatComponentText(s4);
            chatcomponenttext2.func_150255_a(ichatcomponent2.func_150256_b().func_150232_l());
            if (i + i2 > p_178908_1_) {
                String s5 = Fonts.robotoBig.trimStringToWidth(s3, p_178908_1_ - i, false);
                String s6 = (s5.length() < s3.length()) ? s3.substring(s5.length()) : null;
                if (s6 != null && s6.length() > 0) {
                    final int l = s5.lastIndexOf(" ");
                    if (l >= 0 && Fonts.robotoBig.getStringWidth(s3.substring(0, l)) > 0.0) {
                        s5 = s3.substring(0, l);
                        s6 = s3.substring(l);
                    }
                    else if (i > 0 && !s3.contains(" ")) {
                        s5 = "";
                        s6 = s3;
                    }
                    s6 = FontRenderer.func_78282_e(s5) + s6;
                    final ChatComponentText chatcomponenttext3 = new ChatComponentText(s6);
                    chatcomponenttext3.func_150255_a(ichatcomponent2.func_150256_b().func_150232_l());
                    list2.add(j + 1, (IChatComponent)chatcomponenttext3);
                }
                i2 = Fonts.robotoBig.getStringWidth(s5);
                chatcomponenttext2 = new ChatComponentText(s5);
                chatcomponenttext2.func_150255_a(ichatcomponent2.func_150256_b().func_150232_l());
                flag = true;
            }
            if (i + i2 <= p_178908_1_) {
                i += (int)i2;
                ichatcomponent.func_150257_a((IChatComponent)chatcomponenttext2);
            }
            else {
                flag = true;
            }
            if (flag) {
                list.add(ichatcomponent);
                i = 0;
                ichatcomponent = (IChatComponent)new ChatComponentText("");
            }
        }
        list.add(ichatcomponent);
        return list;
    }
}
