// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.player;

import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.item.ItemStack;
import java.util.function.Predicate;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.List;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import java.util.Comparator;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.init.Items;
import me.oringo.oringoclient.utils.PlayerUtils;
import me.oringo.oringoclient.events.MotionUpdateEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.utils.MilliTimer;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class AutoHeal extends Module
{
    public static final BooleanSetting heads;
    public static final BooleanSetting soup;
    public static final BooleanSetting witherImpact;
    public static final BooleanSetting wand;
    public static final BooleanSetting gloomLock;
    public static final BooleanSetting zombieSword;
    public static final BooleanSetting powerOrb;
    public static final BooleanSetting fromInv;
    public static final NumberSetting hp;
    public static final NumberSetting gloomLockHP;
    public static final NumberSetting overFlowMana;
    public static final NumberSetting orbHp;
    public static final NumberSetting witherImpactHP;
    private static final MilliTimer hypDelay;
    private static final MilliTimer wandDelay;
    private static final MilliTimer generalDelay;
    private int lastOverflow;
    
    public AutoHeal() {
        super("Auto Heal", Category.PLAYER);
        this.lastOverflow = 0;
        this.addSettings(AutoHeal.hp, AutoHeal.heads, AutoHeal.soup, AutoHeal.witherImpact, AutoHeal.witherImpactHP, AutoHeal.wand, AutoHeal.zombieSword, AutoHeal.powerOrb, AutoHeal.orbHp, AutoHeal.gloomLock, AutoHeal.gloomLockHP, AutoHeal.overFlowMana, AutoHeal.fromInv);
    }
    
    @SubscribeEvent
    public void onUpdate(final MotionUpdateEvent event) {
        if (this.isToggled()) {
            if (event.isPre()) {
                if (AutoHeal.mc.field_71439_g.func_110143_aJ() / AutoHeal.mc.field_71439_g.func_110138_aP() <= AutoHeal.witherImpactHP.getValue() / 100.0 && AutoHeal.witherImpact.isEnabled() && (AutoHeal.fromInv.isEnabled() ? PlayerUtils.getItem(AutoHeal::isImpact) : PlayerUtils.getHotbar(AutoHeal::isImpact)) != -1 && AutoHeal.hypDelay.hasTimePassed(5200L)) {
                    event.setPitch(90.0f);
                }
            }
            else {
                if (AutoHeal.generalDelay.hasTimePassed(500L) && AutoHeal.gloomLock.isEnabled() && AutoHeal.mc.field_71439_g.func_110143_aJ() / AutoHeal.mc.field_71439_g.func_110138_aP() >= AutoHeal.gloomLockHP.getValue() / 100.0 && this.lastOverflow < AutoHeal.overFlowMana.getValue() && swapToItem(itemstack -> itemstack.func_77973_b() == Items.field_151122_aG && itemstack.func_82833_r().toLowerCase().contains("gloomlock grimoire"), true)) {
                    AutoHeal.generalDelay.reset();
                }
                if (AutoHeal.generalDelay.hasTimePassed(500L) && AutoHeal.powerOrb.isEnabled() && AutoHeal.mc.field_71439_g.func_110143_aJ() / AutoHeal.mc.field_71439_g.func_110138_aP() <= AutoHeal.orbHp.getValue() / 100.0) {
                    final List<EntityArmorStand> stands = (List<EntityArmorStand>)AutoHeal.mc.field_71441_e.func_175644_a((Class)EntityArmorStand.class, e -> getPowerLevel(e.func_70005_c_()) < 4 && e.func_70032_d((Entity)AutoHeal.mc.field_71439_g) < 20.0f);
                    stands.sort(Comparator.comparingDouble(e -> getPowerLevel(e.func_70005_c_())));
                    int level = 4;
                    if (!stands.isEmpty()) {
                        level = getPowerLevel(stands.get(0).func_70005_c_());
                    }
                    int i = 0;
                    int slot = -1;
                    while (i < level) {
                        final int finalI = i;
                        if (AutoHeal.fromInv.isEnabled()) {
                            slot = PlayerUtils.getItem(itemstack -> getPowerLevel(itemstack.func_82833_r()) == finalI);
                        }
                        else {
                            slot = PlayerUtils.getHotbar(itemstack -> getPowerLevel(itemstack.func_82833_r()) == finalI);
                        }
                        if (slot != -1) {
                            break;
                        }
                        ++i;
                    }
                    if (slot != -1) {
                        if (AutoHeal.fromInv.isEnabled()) {
                            if (slot >= 36) {
                                AutoHeal.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C09PacketHeldItemChange(slot));
                                AutoHeal.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C08PacketPlayerBlockPlacement(AutoHeal.mc.field_71439_g.field_71069_bz.func_75139_a(slot).func_75211_c()));
                                AutoHeal.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C09PacketHeldItemChange(AutoHeal.mc.field_71439_g.field_71071_by.field_70461_c));
                            }
                            else {
                                final short short1 = AutoHeal.mc.field_71439_g.field_71070_bA.func_75136_a(AutoHeal.mc.field_71439_g.field_71071_by);
                                AutoHeal.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C0EPacketClickWindow(AutoHeal.mc.field_71439_g.field_71070_bA.field_75152_c, slot, AutoHeal.mc.field_71439_g.field_71071_by.field_70461_c, 2, AutoHeal.mc.field_71439_g.field_71070_bA.func_75139_a(slot).func_75211_c(), short1));
                                AutoHeal.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C08PacketPlayerBlockPlacement(AutoHeal.mc.field_71439_g.field_71069_bz.func_75139_a(slot).func_75211_c()));
                                AutoHeal.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C0EPacketClickWindow(AutoHeal.mc.field_71439_g.field_71070_bA.field_75152_c, slot, AutoHeal.mc.field_71439_g.field_71071_by.field_70461_c, 2, AutoHeal.mc.field_71439_g.field_71070_bA.func_75139_a(slot).func_75211_c(), short1));
                            }
                        }
                        else {
                            AutoHeal.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C09PacketHeldItemChange(slot));
                            AutoHeal.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C08PacketPlayerBlockPlacement(AutoHeal.mc.field_71439_g.field_71071_by.func_70301_a(slot)));
                            AutoHeal.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C09PacketHeldItemChange(AutoHeal.mc.field_71439_g.field_71071_by.field_70461_c));
                        }
                        AutoHeal.generalDelay.reset();
                    }
                }
                if (AutoHeal.witherImpact.isEnabled() && AutoHeal.mc.field_71439_g.func_110143_aJ() / AutoHeal.mc.field_71439_g.func_110138_aP() <= AutoHeal.witherImpactHP.getValue() / 100.0 && AutoHeal.hypDelay.hasTimePassed(5200L) && swapToItem(AutoHeal::isImpact, false)) {
                    AutoHeal.hypDelay.reset();
                }
                if (AutoHeal.mc.field_71439_g.func_110143_aJ() / AutoHeal.mc.field_71439_g.func_110138_aP() <= AutoHeal.hp.getValue() / 100.0) {
                    if (AutoHeal.generalDelay.hasTimePassed(500L)) {
                        if (AutoHeal.soup.isEnabled() && swapToItem(itemstack -> itemstack.func_77973_b() == Items.field_151009_A, false)) {
                            AutoHeal.generalDelay.reset();
                        }
                        if (AutoHeal.heads.isEnabled() && swapToItem(itemstack -> itemstack.func_77973_b() == Items.field_151144_bL && itemstack.func_82833_r().toLowerCase().contains("golden head"), false)) {
                            AutoHeal.generalDelay.reset();
                        }
                        if (AutoHeal.zombieSword.isEnabled() && swapToItem(itemstack -> itemstack.func_77973_b() instanceof ItemSword && itemstack.func_82833_r().contains("Zombie Sword"), false)) {
                            AutoHeal.generalDelay.reset();
                        }
                    }
                    if (AutoHeal.wandDelay.hasTimePassed(7200L) && AutoHeal.wand.isEnabled() && swapToItem(itemstack -> itemstack.func_82833_r().contains("Wand of "), false)) {
                        AutoHeal.wandDelay.reset();
                    }
                }
            }
        }
    }
    
    private static int getPowerLevel(final String name) {
        if (name.contains("Plasmaflux")) {
            return 0;
        }
        if (name.contains("Overflux")) {
            return 1;
        }
        if (name.contains("Mana Flux")) {
            return 2;
        }
        if (name.contains("Radiant")) {
            return 3;
        }
        return 4;
    }
    
    @SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
    public void onChat(final ClientChatReceivedEvent event) {
        if (event.type == 2 && event.message.func_150260_c().contains("\u2748 Defense")) {
            final Matcher pattern = Pattern.compile("§3(.*?)\u02ac").matcher(event.message.func_150260_c());
            if (pattern.find()) {
                final String string = pattern.group(1);
                this.lastOverflow = Integer.parseInt(string);
            }
            else {
                this.lastOverflow = 0;
            }
        }
    }
    
    private static boolean swapToItem(final Predicate<ItemStack> stack, final boolean left) {
        int slot;
        if (AutoHeal.fromInv.isEnabled()) {
            slot = PlayerUtils.getItem(stack);
            if (slot != -1) {
                if (slot >= 36) {
                    AutoHeal.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C09PacketHeldItemChange(slot));
                    if (left) {
                        click();
                    }
                    else {
                        AutoHeal.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C08PacketPlayerBlockPlacement(AutoHeal.mc.field_71439_g.field_71069_bz.func_75139_a(slot).func_75211_c()));
                    }
                    AutoHeal.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C09PacketHeldItemChange(AutoHeal.mc.field_71439_g.field_71071_by.field_70461_c));
                }
                else {
                    final short short1 = AutoHeal.mc.field_71439_g.field_71070_bA.func_75136_a(AutoHeal.mc.field_71439_g.field_71071_by);
                    AutoHeal.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C0EPacketClickWindow(AutoHeal.mc.field_71439_g.field_71070_bA.field_75152_c, slot, AutoHeal.mc.field_71439_g.field_71071_by.field_70461_c, 2, AutoHeal.mc.field_71439_g.field_71070_bA.func_75139_a(slot).func_75211_c(), short1));
                    if (left) {
                        click();
                    }
                    else {
                        AutoHeal.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C08PacketPlayerBlockPlacement(AutoHeal.mc.field_71439_g.field_71069_bz.func_75139_a(slot).func_75211_c()));
                    }
                    AutoHeal.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C0EPacketClickWindow(AutoHeal.mc.field_71439_g.field_71070_bA.field_75152_c, slot, AutoHeal.mc.field_71439_g.field_71071_by.field_70461_c, 2, AutoHeal.mc.field_71439_g.field_71070_bA.func_75139_a(slot).func_75211_c(), short1));
                }
            }
        }
        else {
            slot = PlayerUtils.getHotbar(stack);
            if (slot != -1) {
                AutoHeal.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C09PacketHeldItemChange(slot));
                if (left) {
                    click();
                }
                else {
                    AutoHeal.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C08PacketPlayerBlockPlacement(AutoHeal.mc.field_71439_g.field_71071_by.func_70301_a(slot)));
                }
                AutoHeal.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C09PacketHeldItemChange(AutoHeal.mc.field_71439_g.field_71071_by.field_70461_c));
            }
        }
        return slot != -1;
    }
    
    private static boolean isImpact(final ItemStack stack) {
        return stack.func_77973_b() instanceof ItemSword && (stack.func_82833_r().contains("Hyperion") || stack.func_82833_r().contains("Astraea") || stack.func_82833_r().contains("Scylla") || stack.func_82833_r().contains("Valkyrie"));
    }
    
    private static void click() {
        AutoHeal.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C0APacketAnimation());
    }
    
    static {
        heads = new BooleanSetting("Heads", false);
        soup = new BooleanSetting("Soup", false);
        witherImpact = new BooleanSetting("Wither Impact", true);
        wand = new BooleanSetting("Wand", false);
        gloomLock = new BooleanSetting("Gloom Lock", false);
        zombieSword = new BooleanSetting("Zombie Sword", false);
        powerOrb = new BooleanSetting("Power Orb", false);
        fromInv = new BooleanSetting("From Inv", false);
        hp = new NumberSetting("Health", 70.0, 0.0, 100.0, 1.0);
        gloomLockHP = new NumberSetting("Gloomlock Health", 70.0, 0.0, 100.0, 1.0, a -> !AutoHeal.gloomLock.isEnabled());
        overFlowMana = new NumberSetting("Overflow mana", 600.0, 0.0, 1000.0, 50.0, a -> !AutoHeal.gloomLock.isEnabled());
        orbHp = new NumberSetting("Power Orb Health", 85.0, 0.0, 100.0, 1.0, a -> !AutoHeal.powerOrb.isEnabled());
        witherImpactHP = new NumberSetting("Impact Health", 40.0, 0.0, 100.0, 1.0, a -> !AutoHeal.witherImpact.isEnabled());
        hypDelay = new MilliTimer();
        wandDelay = new MilliTimer();
        generalDelay = new MilliTimer();
    }
}
