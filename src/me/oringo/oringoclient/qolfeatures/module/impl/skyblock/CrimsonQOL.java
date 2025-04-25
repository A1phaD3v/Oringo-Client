// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.skyblock;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IChatComponent;
import net.minecraft.client.Minecraft;
import me.oringo.oringoclient.commands.CommandHandler;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.ChatComponentText;
import me.oringo.oringoclient.utils.OutlineUtils;
import java.awt.Color;
import net.minecraft.entity.boss.EntityWither;
import me.oringo.oringoclient.events.RenderLayersEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.regex.Matcher;
import java.util.Iterator;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import java.util.regex.Pattern;
import me.oringo.oringoclient.utils.PlayerUtils;
import net.minecraft.item.ItemSword;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class CrimsonQOL extends Module
{
    public static final BooleanSetting autoHostage;
    public static final BooleanSetting kuudraESP;
    public static final BooleanSetting autoCloak;
    public static final NumberSetting time;
    public static final NumberSetting distance;
    private int hostageId;
    private boolean hasCloaked;
    
    public CrimsonQOL() {
        super("Crimson QOL", Category.SKYBLOCK);
        this.hostageId = -1;
        this.addSettings(CrimsonQOL.autoHostage, CrimsonQOL.kuudraESP, CrimsonQOL.autoCloak, CrimsonQOL.time, CrimsonQOL.distance);
    }
    
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (this.isToggled() && event.phase == TickEvent.Phase.END && CrimsonQOL.mc.field_71439_g != null && CrimsonQOL.mc.field_71441_e != null) {
            final boolean hostage = this.hostageId != -1;
            boolean cloak = false;
            final int slot = PlayerUtils.getHotbar(stack -> stack.func_77973_b() instanceof ItemSword && stack.func_82833_r().contains("Wither Cloak"));
            this.hostageId = -1;
            final Pattern pattern = Pattern.compile("(.*)s [1-8] hits", 2);
            for (final EntityArmorStand entity : CrimsonQOL.mc.field_71441_e.func_175644_a((Class)EntityArmorStand.class, e -> true)) {
                if (CrimsonQOL.autoHostage.isEnabled() && entity.func_145748_c_().func_150260_c().contains("Hostage")) {
                    this.hostageId = entity.func_145782_y();
                }
                if (CrimsonQOL.autoCloak.isEnabled() && slot != -1 && entity.func_70068_e((Entity)CrimsonQOL.mc.field_71439_g) < CrimsonQOL.distance.getValue() * CrimsonQOL.distance.getValue()) {
                    final Matcher matcher = pattern.matcher(ChatFormatting.stripFormatting(entity.func_145748_c_().func_150260_c()));
                    if (!matcher.find()) {
                        continue;
                    }
                    final String name = matcher.group(1);
                    final int time = Integer.parseInt(name);
                    if (time > CrimsonQOL.time.getValue()) {
                        continue;
                    }
                    cloak = true;
                    if (this.hasCloaked) {
                        continue;
                    }
                    this.hasCloaked = true;
                    CrimsonQOL.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C09PacketHeldItemChange(slot));
                    CrimsonQOL.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C08PacketPlayerBlockPlacement(CrimsonQOL.mc.field_71439_g.field_71071_by.func_70301_a(slot)));
                    CrimsonQOL.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C09PacketHeldItemChange(CrimsonQOL.mc.field_71439_g.field_71071_by.field_70461_c));
                }
            }
            if (CrimsonQOL.autoCloak.isEnabled() && this.hasCloaked && !cloak) {
                if (slot != -1) {
                    CrimsonQOL.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C09PacketHeldItemChange(slot));
                    CrimsonQOL.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C08PacketPlayerBlockPlacement(CrimsonQOL.mc.field_71439_g.field_71071_by.func_70301_a(slot)));
                    CrimsonQOL.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C09PacketHeldItemChange(CrimsonQOL.mc.field_71439_g.field_71071_by.field_70461_c));
                }
                this.hasCloaked = false;
            }
            if (!hostage && this.hostageId != -1) {
                sendEntityInteract("§bOringoClient §3» §7§aClick here to interact with hostage!", this.hostageId);
            }
        }
    }
    
    @SubscribeEvent
    public void kuudraESP(final RenderLayersEvent event) {
        if (this.isToggled() && event.entity instanceof EntityWither && ((EntityWither)event.entity).func_82212_n() > 500 && CrimsonQOL.kuudraESP.isEnabled()) {
            OutlineUtils.outlineESP(event, Color.GREEN);
        }
    }
    
    private static void sendEntityInteract(final String message, final int entity) {
        final ChatComponentText chatComponentText = new ChatComponentText(message);
        final ChatStyle style = new ChatStyle();
        style.func_150241_a(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("%sarmorstands %s", CommandHandler.getCommandPrefix(), entity)));
        chatComponentText.func_150255_a(style);
        Minecraft.func_71410_x().field_71439_g.func_145747_a((IChatComponent)chatComponentText);
    }
    
    static {
        autoHostage = new BooleanSetting("Auto Hostage", true);
        kuudraESP = new BooleanSetting("Kuudra Outline", true);
        autoCloak = new BooleanSetting("Auto Cloak", true);
        time = new NumberSetting("Time", 1.0, 1.0, 8.0, 1.0, a -> !CrimsonQOL.autoCloak.isEnabled());
        distance = new NumberSetting("Distance", 30.0, 1.0, 64.0, 1.0, a -> !CrimsonQOL.autoCloak.isEnabled());
    }
}
