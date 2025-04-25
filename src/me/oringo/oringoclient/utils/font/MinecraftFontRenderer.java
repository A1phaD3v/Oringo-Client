// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.utils.font;

import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import me.oringo.oringoclient.utils.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import java.awt.Color;
import java.awt.Font;
import net.minecraft.client.renderer.texture.DynamicTexture;

public class MinecraftFontRenderer extends CFont
{
    CharData[] boldChars;
    CharData[] italicChars;
    CharData[] boldItalicChars;
    int[] colorCode;
    String colorcodeIdentifiers;
    DynamicTexture texBold;
    DynamicTexture texItalic;
    DynamicTexture texItalicBold;
    
    public MinecraftFontRenderer(final Font font) {
        this(font, true, false);
    }
    
    public MinecraftFontRenderer(final Font font, final boolean antiAlias, final boolean fractionalMetrics) {
        super(font, antiAlias, fractionalMetrics);
        this.boldChars = new CharData[256];
        this.italicChars = new CharData[256];
        this.boldItalicChars = new CharData[256];
        this.colorCode = new int[32];
        this.colorcodeIdentifiers = "0123456789abcdefklmnor";
        this.setupMinecraftColorcodes();
        this.setupBoldItalicIDs();
    }
    
    public int drawStringWithShadow(final String text, final double x2, final double y2, final int color) {
        final float shadowWidth = this.drawString(text, x2 + 0.5, y2 + 0.8999999761581421, new Color(20, 20, 20).getRGB(), true, 8.3f);
        return (int)Math.max(shadowWidth, this.drawString(text, x2, y2, color, false, 8.3f));
    }
    
    public int drawSmoothStringWithShadow(final String text, final double x2, final double y2, final int color) {
        final float shadowWidth = this.drawSmoothString(text, x2 + 0.5, y2 + 0.8999999761581421, new Color(20, 20, 20).getRGB(), true);
        return (int)Math.max(shadowWidth, this.drawSmoothString(text, x2, y2, color, false));
    }
    
    public int drawSmoothCenteredStringWithShadow(final String text, final double x2, final double y2, final int color) {
        final float shadowWidth = this.drawSmoothString(text, x2 + 0.5 - (float)(this.getStringWidth(text) / 2.0), y2 + 0.8999999761581421, new Color(20, 20, 20).getRGB(), true);
        return (int)Math.max(shadowWidth, this.drawSmoothString(text, x2 - (float)(this.getStringWidth(text) / 2.0), y2, color, false));
    }
    
    public int drawString(final String text, final double x2, final double y2, final int color) {
        return (int)this.drawString(text, x2, y2, color, false, 8.3f);
    }
    
    public int drawNoBSString(final String text, final double x2, final float y2, final int color) {
        return (int)this.drawNoBSString(text, x2, y2, color, false);
    }
    
    public float drawSmoothString(final String text, final double x2, final float y2, final int color) {
        return this.drawSmoothString(text, x2, y2, color, false);
    }
    
    public float drawSmoothCenteredString(final String text, final float x2, final float y2, final int color) {
        return this.drawSmoothString(text, x2 - (float)(this.getStringWidth(text) / 2.0), y2, color);
    }
    
    public float drawCenteredString(final String text, final float x2, final float y2, final int color) {
        return (float)this.drawString(text, x2 - (float)(this.getStringWidth(text) / 2.0), y2, color);
    }
    
    public float drawNoBSCenteredString(final String text, final float x2, final float y2, final int color) {
        return (float)this.drawNoBSString(text, x2 - (float)(this.getStringWidth(text) / 2.0), y2, color);
    }
    
    public float drawCenteredStringWithShadow(final String text, final float x2, final float y2, final int color) {
        return (float)this.drawStringWithShadow(text, x2 - (float)(this.getStringWidth(text) / 2.0), y2, color);
    }
    
    public float drawString(final String text, double x, double y, int color, final boolean shadow, final float kerning) {
        --x;
        if (text == null) {
            return 0.0f;
        }
        if (color == 553648127) {
            color = 16777215;
        }
        if ((color & 0xFC000000) == 0x0) {
            color |= 0xFF000000;
        }
        if (shadow) {
            color = ((color & 0xFCFCFC) >> 2 | (color & 0xFF000000));
        }
        CharData[] currentData = this.charData;
        final float alpha = (color >> 24 & 0xFF) / 255.0f;
        final boolean randomCase = false;
        boolean bold = false;
        boolean italic = false;
        boolean strikethrough = false;
        boolean underline = false;
        final boolean render = true;
        x *= 2.0;
        y = (y - 3.0) * 2.0;
        GL11.glPushMatrix();
        GlStateManager.func_179139_a(0.5, 0.5, 0.5);
        GlStateManager.func_179147_l();
        GlStateManager.func_179112_b(770, 771);
        GlStateManager.func_179131_c((color >> 16 & 0xFF) / 255.0f, (color >> 8 & 0xFF) / 255.0f, (color & 0xFF) / 255.0f, alpha);
        GlStateManager.func_179098_w();
        GlStateManager.func_179144_i(this.tex.func_110552_b());
        GL11.glBindTexture(3553, this.tex.func_110552_b());
        for (int index = 0; index < text.length(); ++index) {
            final char character = text.charAt(index);
            if (character == '§') {
                int colorIndex = 21;
                try {
                    colorIndex = "0123456789abcdefklmnor".indexOf(text.charAt(index + 1));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                if (colorIndex < 16) {
                    bold = false;
                    italic = false;
                    underline = false;
                    strikethrough = false;
                    GlStateManager.func_179144_i(this.tex.func_110552_b());
                    currentData = this.charData;
                    if (colorIndex < 0) {
                        colorIndex = 15;
                    }
                    if (shadow) {
                        colorIndex += 16;
                    }
                    final int colorcode = this.colorCode[colorIndex];
                    GlStateManager.func_179131_c((colorcode >> 16 & 0xFF) / 255.0f, (colorcode >> 8 & 0xFF) / 255.0f, (colorcode & 0xFF) / 255.0f, alpha);
                }
                else if (colorIndex == 17) {
                    bold = true;
                    if (italic) {
                        GlStateManager.func_179144_i(this.texItalicBold.func_110552_b());
                        currentData = this.boldItalicChars;
                    }
                    else {
                        GlStateManager.func_179144_i(this.texBold.func_110552_b());
                        currentData = this.boldChars;
                    }
                }
                else if (colorIndex == 18) {
                    strikethrough = true;
                }
                else if (colorIndex == 19) {
                    underline = true;
                }
                else if (colorIndex == 20) {
                    italic = true;
                    if (bold) {
                        GlStateManager.func_179144_i(this.texItalicBold.func_110552_b());
                        currentData = this.boldItalicChars;
                    }
                    else {
                        GlStateManager.func_179144_i(this.texItalic.func_110552_b());
                        currentData = this.italicChars;
                    }
                }
                else {
                    bold = false;
                    italic = false;
                    underline = false;
                    strikethrough = false;
                    GlStateManager.func_179131_c((color >> 16 & 0xFF) / 255.0f, (color >> 8 & 0xFF) / 255.0f, (color & 0xFF) / 255.0f, alpha);
                    GlStateManager.func_179144_i(this.tex.func_110552_b());
                    currentData = this.charData;
                }
                ++index;
            }
            else if (character < currentData.length) {
                GL11.glBegin(4);
                this.drawChar(currentData, character, (float)x, (float)y);
                GL11.glEnd();
                if (strikethrough) {
                    this.drawLine(x, y + currentData[character].height / 2, x + currentData[character].width - 8.0, y + currentData[character].height / 2, 1.0f);
                }
                if (underline) {
                    this.drawLine(x, y + currentData[character].height - 2.0, x + currentData[character].width - 8.0, y + currentData[character].height - 2.0, 1.0f);
                }
                x += currentData[character].width - kerning + this.charOffset;
            }
        }
        GL11.glHint(3155, 4352);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        return (float)x / 2.0f;
    }
    
    public float drawSmoothString(final String text, double x, double y, final int color, final boolean shadow) {
        --x;
        if (text == null) {
            return 0.0f;
        }
        CharData[] currentData = this.charData;
        final float alpha = (color >> 24 & 0xFF) / 255.0f;
        final boolean randomCase = false;
        boolean bold = false;
        boolean italic = false;
        boolean strikethrough = false;
        boolean underline = false;
        final boolean render = true;
        x *= 2.0;
        y = (y - 3.0) * 2.0;
        GL11.glPushMatrix();
        GlStateManager.func_179139_a(0.5, 0.5, 0.5);
        GlStateManager.func_179147_l();
        GlStateManager.func_179112_b(770, 771);
        GlStateManager.func_179131_c((color >> 16 & 0xFF) / 255.0f, (color >> 8 & 0xFF) / 255.0f, (color & 0xFF) / 255.0f, alpha);
        GlStateManager.func_179098_w();
        GlStateManager.func_179144_i(this.tex.func_110552_b());
        GL11.glBindTexture(3553, this.tex.func_110552_b());
        GL11.glTexParameteri(3553, 10241, 9729);
        GL11.glTexParameteri(3553, 10240, 9729);
        for (int index = 0; index < text.length(); ++index) {
            final char character = text.charAt(index);
            if (character == '§') {
                if (index < text.length() - 1) {
                    int colorIndex = 21;
                    try {
                        colorIndex = "0123456789abcdefklmnor".indexOf(text.charAt(index + 1));
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (colorIndex < 16) {
                        bold = false;
                        italic = false;
                        underline = false;
                        strikethrough = false;
                        GlStateManager.func_179144_i(this.tex.func_110552_b());
                        currentData = this.charData;
                        if (colorIndex < 0) {
                            colorIndex = 15;
                        }
                        final int colorcode = this.colorCode[colorIndex];
                        if (!shadow) {
                            GlStateManager.func_179131_c((colorcode >> 16 & 0xFF) / 255.0f, (colorcode >> 8 & 0xFF) / 255.0f, (colorcode & 0xFF) / 255.0f, alpha);
                        }
                    }
                    else if (colorIndex != 16) {
                        if (colorIndex == 17) {
                            bold = true;
                            if (italic) {
                                GlStateManager.func_179144_i(this.texItalicBold.func_110552_b());
                                GL11.glBindTexture(3553, this.texItalicBold.func_110552_b());
                                GL11.glTexParameteri(3553, 10241, 9729);
                                GL11.glTexParameteri(3553, 10240, 9729);
                                currentData = this.boldItalicChars;
                            }
                            else {
                                GlStateManager.func_179144_i(this.texBold.func_110552_b());
                                GL11.glBindTexture(3553, this.texBold.func_110552_b());
                                GL11.glTexParameteri(3553, 10241, 9729);
                                GL11.glTexParameteri(3553, 10240, 9729);
                                currentData = this.boldChars;
                            }
                        }
                        else if (colorIndex == 18) {
                            strikethrough = true;
                        }
                        else if (colorIndex == 19) {
                            underline = true;
                        }
                        else if (colorIndex == 20) {
                            italic = true;
                            if (bold) {
                                GlStateManager.func_179144_i(this.texItalicBold.func_110552_b());
                                GL11.glBindTexture(3553, this.texItalicBold.func_110552_b());
                                GL11.glTexParameteri(3553, 10241, 9729);
                                GL11.glTexParameteri(3553, 10240, 9729);
                                currentData = this.boldItalicChars;
                            }
                            else {
                                GlStateManager.func_179144_i(this.texItalic.func_110552_b());
                                GL11.glBindTexture(3553, this.texItalic.func_110552_b());
                                GL11.glTexParameteri(3553, 10241, 9729);
                                GL11.glTexParameteri(3553, 10240, 9729);
                                currentData = this.italicChars;
                            }
                        }
                        else {
                            bold = false;
                            italic = false;
                            underline = false;
                            strikethrough = false;
                            GlStateManager.func_179131_c((color >> 16 & 0xFF) / 255.0f, (color >> 8 & 0xFF) / 255.0f, (color & 0xFF) / 255.0f, alpha);
                            GlStateManager.func_179144_i(this.tex.func_110552_b());
                            currentData = this.charData;
                        }
                    }
                }
                ++index;
            }
            else if (character < currentData.length) {
                GL11.glBegin(4);
                this.drawChar(currentData, character, (float)x, (float)y);
                GL11.glEnd();
                if (strikethrough) {
                    this.drawLine(x, y + currentData[character].height / 2, x + currentData[character].width - 8.0, y + currentData[character].height / 2, 1.0f);
                }
                if (underline) {
                    this.drawLine(x, y + currentData[character].height - 2.0, x + currentData[character].width - 8.0, y + currentData[character].height - 2.0, 1.0f);
                }
                x += currentData[character].width - 8.3f + this.charOffset;
            }
        }
        GL11.glHint(3155, 4352);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        return (float)x / 2.0f;
    }
    
    public float drawSmoothCenteredString(final String text, final float x2, final float y2, final int topColor, final int bottomColor) {
        return this.drawSmoothMultiColoredStringTB(text, x2 - (float)(this.getStringWidth(text) / 2.0), y2, topColor, bottomColor, false);
    }
    
    public float drawSmoothMultiColoredStringTB(final String text, double x, double y, final int topColor, final int bottomColor, final boolean shadow) {
        --x;
        if (text == null) {
            return 0.0f;
        }
        final CharData[] currentData = this.charData;
        x *= 2.0;
        y = (y - 3.0) * 2.0;
        GL11.glPushMatrix();
        GlStateManager.func_179139_a(0.5, 0.5, 0.5);
        GlStateManager.func_179147_l();
        GlStateManager.func_179112_b(770, 771);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.func_179098_w();
        GlStateManager.func_179144_i(this.tex.func_110552_b());
        GL11.glBindTexture(3553, this.tex.func_110552_b());
        GL11.glTexParameteri(3553, 10241, 9729);
        GL11.glTexParameteri(3553, 10240, 9729);
        GL11.glShadeModel(7425);
        for (int index = 0; index < text.length(); ++index) {
            final char character = text.charAt(index);
            if (character < currentData.length) {
                GL11.glShadeModel(7425);
                GL11.glBegin(4);
                this.drawMultiColoredCharTB(currentData, character, (float)x, (float)y, topColor, bottomColor);
                GL11.glEnd();
                GL11.glShadeModel(7424);
                x += currentData[character].width - 8.3f + this.charOffset;
            }
        }
        GL11.glHint(3155, 4352);
        GL11.glShadeModel(7424);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        return (float)x / 2.0f;
    }
    
    public float drawSmoothMultiColoredStringLR(final String text, double x, double y, final int leftColor, final int rightColor) {
        --x;
        if (text == null) {
            return 0.0f;
        }
        final CharData[] currentData = this.charData;
        x *= 2.0;
        y = (y - 3.0) * 2.0;
        GL11.glPushMatrix();
        GlStateManager.func_179139_a(0.5, 0.5, 0.5);
        GlStateManager.func_179147_l();
        GlStateManager.func_179112_b(770, 771);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.func_179098_w();
        GlStateManager.func_179144_i(this.tex.func_110552_b());
        GL11.glBindTexture(3553, this.tex.func_110552_b());
        GL11.glTexParameteri(3553, 10241, 9729);
        GL11.glTexParameteri(3553, 10240, 9729);
        GL11.glShadeModel(7425);
        final Color color1 = new Color(leftColor);
        final Color color2 = new Color(rightColor);
        for (int index = 0; index < text.length(); ++index) {
            final char character = text.charAt(index);
            if (character < currentData.length) {
                final float point = index / (float)text.length();
                final float point2 = (index + 1) / (float)text.length();
                GL11.glShadeModel(7425);
                GL11.glBegin(4);
                this.drawMulticoloredCharLR(currentData, character, (float)x, (float)y, RenderUtils.interpolateColor(color1, color2, point).getRGB(), RenderUtils.interpolateColor(color1, color2, point2).getRGB());
                GL11.glEnd();
                GL11.glShadeModel(7424);
                x += currentData[character].width - 8.3f + this.charOffset;
            }
        }
        GL11.glHint(3155, 4352);
        GL11.glShadeModel(7424);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        return (float)x / 2.0f;
    }
    
    public float drawNoBSString(final String text, double x, double y, final int color, final boolean shadow) {
        --x;
        if (text == null) {
            return 0.0f;
        }
        CharData[] currentData = this.charData;
        final float alpha = (color >> 24 & 0xFF) / 255.0f;
        final boolean randomCase = false;
        boolean bold = false;
        boolean italic = false;
        boolean strikethrough = false;
        boolean underline = false;
        final boolean render = true;
        x *= 2.0;
        y = (y - 3.0) * 2.0;
        GL11.glPushMatrix();
        GlStateManager.func_179139_a(0.5, 0.5, 0.5);
        GlStateManager.func_179147_l();
        GlStateManager.func_179112_b(770, 771);
        GlStateManager.func_179131_c((color >> 16 & 0xFF) / 255.0f, (color >> 8 & 0xFF) / 255.0f, (color & 0xFF) / 255.0f, alpha);
        GlStateManager.func_179098_w();
        GlStateManager.func_179144_i(this.tex.func_110552_b());
        GL11.glBindTexture(3553, this.tex.func_110552_b());
        GL11.glTexParameteri(3553, 10241, 9728);
        GL11.glTexParameteri(3553, 10240, 9728);
        for (int index = 0; index < text.length(); ++index) {
            final char character = text.charAt(index);
            if (character == '§') {
                int colorIndex = 21;
                try {
                    colorIndex = "0123456789abcdefklmnor".indexOf(text.charAt(index + 1));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                if (colorIndex < 16) {
                    bold = false;
                    italic = false;
                    underline = false;
                    strikethrough = false;
                    GlStateManager.func_179144_i(this.tex.func_110552_b());
                    currentData = this.charData;
                    if (colorIndex < 0) {
                        colorIndex = 15;
                    }
                    if (shadow) {
                        colorIndex += 16;
                    }
                    final int colorcode = this.colorCode[colorIndex];
                    GlStateManager.func_179131_c((colorcode >> 16 & 0xFF) / 255.0f, (colorcode >> 8 & 0xFF) / 255.0f, (colorcode & 0xFF) / 255.0f, alpha);
                }
                else if (colorIndex != 16) {
                    if (colorIndex == 17) {
                        bold = true;
                        if (italic) {
                            GlStateManager.func_179144_i(this.texItalicBold.func_110552_b());
                            currentData = this.boldItalicChars;
                        }
                        else {
                            GlStateManager.func_179144_i(this.texBold.func_110552_b());
                            currentData = this.boldChars;
                        }
                    }
                    else if (colorIndex == 18) {
                        strikethrough = true;
                    }
                    else if (colorIndex == 19) {
                        underline = true;
                    }
                    else if (colorIndex == 20) {
                        italic = true;
                        if (bold) {
                            GlStateManager.func_179144_i(this.texItalicBold.func_110552_b());
                            currentData = this.boldItalicChars;
                        }
                        else {
                            GlStateManager.func_179144_i(this.texItalic.func_110552_b());
                            currentData = this.italicChars;
                        }
                    }
                    else {
                        bold = false;
                        italic = false;
                        underline = false;
                        strikethrough = false;
                        GlStateManager.func_179131_c((color >> 16 & 0xFF) / 255.0f, (color >> 8 & 0xFF) / 255.0f, (color & 0xFF) / 255.0f, alpha);
                        GlStateManager.func_179144_i(this.tex.func_110552_b());
                        currentData = this.charData;
                    }
                }
                ++index;
            }
            else if (character < currentData.length) {
                GL11.glBegin(4);
                this.drawChar(currentData, character, (float)x, (float)y);
                GL11.glEnd();
                if (strikethrough) {
                    this.drawLine(x, y + currentData[character].height / 2, x + currentData[character].width - 8.0, y + currentData[character].height / 2, 1.0f);
                }
                if (underline) {
                    this.drawLine(x, y + currentData[character].height - 2.0, x + currentData[character].width - 8.0, y + currentData[character].height - 2.0, 1.0f);
                }
                x += currentData[character].width - 8.3f + this.charOffset;
            }
        }
        GL11.glHint(3155, 4352);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        return (float)x / 2.0f;
    }
    
    public double getStringWidth(final String text) {
        if (text == null) {
            return 0.0;
        }
        float width = 0.0f;
        final CharData[] currentData = this.charData;
        final boolean bold = false;
        final boolean italic = false;
        for (int index = 0; index < text.length(); ++index) {
            final char character = text.charAt(index);
            if (character == '§') {
                final int colorIndex = "0123456789abcdefklmnor".indexOf(character);
                ++index;
            }
            else if (character < currentData.length) {
                width += currentData[character].width - 8.3f + this.charOffset;
            }
        }
        return width / 2.0f;
    }
    
    public double getStringWidth(final String text, final float kerning) {
        if (text == null) {
            return 0.0;
        }
        float width = 0.0f;
        final CharData[] currentData = this.charData;
        final boolean bold = false;
        final boolean italic = false;
        for (int index = 0; index < text.length(); ++index) {
            final char c = text.charAt(index);
            if (c == '§') {
                final int colorIndex = "0123456789abcdefklmnor".indexOf(c);
                ++index;
            }
            else if (c < currentData.length) {
                width += currentData[c].width - kerning + this.charOffset;
            }
        }
        return width / 2.0f;
    }
    
    public int getHeight() {
        return (this.fontHeight - 8) / 2;
    }
    
    @Override
    public void setFont(final Font font) {
        super.setFont(font);
        this.setupBoldItalicIDs();
    }
    
    @Override
    public void setAntiAlias(final boolean antiAlias) {
        super.setAntiAlias(antiAlias);
        this.setupBoldItalicIDs();
    }
    
    @Override
    public void setFractionalMetrics(final boolean fractionalMetrics) {
        super.setFractionalMetrics(fractionalMetrics);
        this.setupBoldItalicIDs();
    }
    
    private void setupBoldItalicIDs() {
        this.texBold = this.setupTexture(this.font.deriveFont(1), this.antiAlias, this.fractionalMetrics, this.boldChars);
        this.texItalic = this.setupTexture(this.font.deriveFont(2), this.antiAlias, this.fractionalMetrics, this.italicChars);
        this.texItalicBold = this.setupTexture(this.font.deriveFont(3), this.antiAlias, this.fractionalMetrics, this.boldItalicChars);
    }
    
    private void drawLine(final double x2, final double y2, final double x1, final double y1, final float width) {
        GL11.glDisable(3553);
        GL11.glLineWidth(width);
        GL11.glBegin(1);
        GL11.glVertex2d(x2, y2);
        GL11.glVertex2d(x1, y1);
        GL11.glEnd();
        GL11.glEnable(3553);
    }
    
    public List<String> wrapWords(final String text, final double width) {
        final ArrayList<String> finalWords = new ArrayList<String>();
        if (this.getStringWidth(text) > width) {
            final String[] words = text.split(" ");
            StringBuilder currentWord = new StringBuilder();
            char lastColorCode = '\uffff';
            for (final String word : words) {
                for (int innerIndex = 0; innerIndex < word.toCharArray().length; ++innerIndex) {
                    final char c = word.toCharArray()[innerIndex];
                    if (c == '§' && innerIndex < word.toCharArray().length - 1) {
                        lastColorCode = word.toCharArray()[innerIndex + 1];
                    }
                }
                if (this.getStringWidth((Object)currentWord + word + " ") < width) {
                    currentWord.append(word).append(" ");
                }
                else {
                    finalWords.add(currentWord.toString());
                    currentWord = new StringBuilder("§" + lastColorCode + word + " ");
                }
            }
            if (currentWord.length() > 0) {
                if (this.getStringWidth(currentWord.toString()) < width) {
                    finalWords.add("§" + lastColorCode + (Object)currentWord + " ");
                    currentWord = new StringBuilder();
                }
                else {
                    finalWords.addAll(this.formatString(currentWord.toString(), width));
                }
            }
        }
        else {
            finalWords.add(text);
        }
        return finalWords;
    }
    
    public List<String> formatString(final String string, final double width) {
        final ArrayList<String> finalWords = new ArrayList<String>();
        StringBuilder currentWord = new StringBuilder();
        char lastColorCode = '\uffff';
        final char[] chars = string.toCharArray();
        for (int index = 0; index < chars.length; ++index) {
            final char c = chars[index];
            if (c == '§' && index < chars.length - 1) {
                lastColorCode = chars[index + 1];
            }
            if (this.getStringWidth(currentWord.toString() + c) < width) {
                currentWord.append(c);
            }
            else {
                finalWords.add(currentWord.toString());
                currentWord = new StringBuilder("§" + lastColorCode + c);
            }
        }
        if (currentWord.length() > 0) {
            finalWords.add(currentWord.toString());
        }
        return finalWords;
    }
    
    private void setupMinecraftColorcodes() {
        for (int index = 0; index < 32; ++index) {
            final int noClue = (index >> 3 & 0x1) * 85;
            int red = (index >> 2 & 0x1) * 170 + noClue;
            int green = (index >> 1 & 0x1) * 170 + noClue;
            int blue = (index & 0x1) * 170 + noClue;
            if (index == 6) {
                red += 85;
            }
            if (index >= 16) {
                red /= 4;
                green /= 4;
                blue /= 4;
            }
            this.colorCode[index] = ((red & 0xFF) << 16 | (green & 0xFF) << 8 | (blue & 0xFF));
        }
    }
    
    public String trimStringToWidth(final String text, final int width) {
        return this.trimStringToWidth(text, width, false);
    }
    
    private float getCharWidthFloat(final char c) {
        if (c == '§') {
            return -1.0f;
        }
        if (c == ' ') {
            return 2.0f;
        }
        if (this.charData[c] == null) {
            return 0.0f;
        }
        final int var2 = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8£\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1ªº¿®¬½¼¡«»\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261±\u2265\u2264\u2320\u2321\u00f7\u2248°\u2219·\u221a\u207f²\u25a0\u0000".indexOf(c);
        if (c > '\0' && var2 != -1) {
            return this.charData[var2].width / 2.0f - 4.0f;
        }
        if (this.charData[c].width / 2.0f - 4.0f != 0.0f) {
            int var3 = (int)(this.charData[c].width / 2.0f - 4.0f) >>> 4;
            int var4 = (int)(this.charData[c].width / 2.0f - 4.0f) & 0xF;
            var3 &= 0xF;
            return (float)((++var4 - var3) / 2 + 1);
        }
        return 0.0f;
    }
    
    public String trimStringToWidth(final String text, final int width, final boolean custom) {
        final StringBuilder buffer = new StringBuilder();
        float lineWidth = 0.0f;
        final int offset = custom ? (text.length() - 1) : 0;
        final int increment = custom ? -1 : 1;
        boolean var8 = false;
        boolean var9 = false;
        for (int index = offset; index >= 0 && index < text.length() && lineWidth < width; index += increment) {
            final char character = text.charAt(index);
            final float charWidth = this.getCharWidthFloat(character);
            if (var8) {
                var8 = false;
                if (character != 'l' && character != 'L') {
                    if (character == 'r' || character == 'R') {
                        var9 = false;
                    }
                }
                else {
                    var9 = true;
                }
            }
            else if (charWidth < 0.0f) {
                var8 = true;
            }
            else {
                lineWidth += charWidth;
                if (var9) {
                    ++lineWidth;
                }
            }
            if (lineWidth > width) {
                break;
            }
            if (custom) {
                buffer.insert(0, character);
            }
            else {
                buffer.append(character);
            }
        }
        return buffer.toString();
    }
}
