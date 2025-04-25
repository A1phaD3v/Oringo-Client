// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.player;

import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.entity.player.InventoryPlayer;
import me.oringo.oringoclient.qolfeatures.module.impl.combat.KillAura;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import me.oringo.oringoclient.utils.PlayerUtils;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.item.ItemPotion;
import me.oringo.oringoclient.events.MotionUpdateEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.potion.Potion;
import java.util.HashMap;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class AutoPot extends Module
{
    public NumberSetting delay;
    public NumberSetting health;
    public BooleanSetting onGround;
    public BooleanSetting fromInv;
    private final HashMap<Potion, Long> delays;
    private final List<Integer> slots;
    
    public AutoPot() {
        super("AutoPot", Category.PLAYER);
        this.delay = new NumberSetting("Delay", 1000.0, 250.0, 2500.0, 1.0);
        this.health = new NumberSetting("Health", 15.0, 1.0, 20.0, 1.0);
        this.onGround = new BooleanSetting("Ground only", true);
        this.fromInv = new BooleanSetting("From inventory", false);
        this.delays = new HashMap<Potion, Long>();
        this.slots = new ArrayList<Integer>();
        this.addSettings(this.delay, this.onGround);
        this.addSettings(this.health, this.fromInv);
    }
    
    @SubscribeEvent(priority = EventPriority.LOW)
    public void onUpdate(final MotionUpdateEvent.Pre event) {
        this.slots.clear();
        if (!this.isToggled() || (!AutoPot.mc.field_71439_g.field_70122_E && this.onGround.isEnabled())) {
            return;
        }
        if (this.fromInv.isEnabled() && AutoPot.mc.field_71439_g.field_71070_bA.field_75152_c == AutoPot.mc.field_71439_g.field_71069_bz.field_75152_c) {
            for (int i = 9; i < 45; ++i) {
                if (AutoPot.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75216_d()) {
                    final ItemStack stack = AutoPot.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75211_c();
                    if (stack.func_77973_b() instanceof ItemPotion && ItemPotion.func_77831_g(stack.func_77960_j())) {
                        for (final PotionEffect effect : ((ItemPotion)stack.func_77973_b()).func_77832_l(stack)) {
                            final Potion potion = Potion.field_76425_a[effect.func_76456_a()];
                            if (this.isBestEffect(effect, stack) && !potion.func_76398_f() && !this.isDelayed(potion) && (!AutoPot.mc.field_71439_g.func_70644_a(potion) || effect.func_76458_c() > AutoPot.mc.field_71439_g.func_70660_b(potion).func_76458_c())) {
                                if (potion != Potion.field_76432_h && potion != Potion.field_76428_l) {
                                    this.updateDelay(potion);
                                    this.slots.add(i);
                                    event.pitch = 87.9f;
                                    break;
                                }
                                if (AutoPot.mc.field_71439_g.func_110143_aJ() <= this.health.getValue()) {
                                    this.updateDelay(potion);
                                    this.slots.add(i);
                                    event.pitch = 87.9f;
                                    break;
                                }
                                continue;
                            }
                        }
                    }
                }
            }
        }
        else {
            for (int i = 0; i < 9; ++i) {
                if (AutoPot.mc.field_71439_g.field_71071_by.func_70301_a(i) != null) {
                    final ItemStack stack = AutoPot.mc.field_71439_g.field_71071_by.func_70301_a(i);
                    if (stack.func_77973_b() instanceof ItemPotion && ItemPotion.func_77831_g(stack.func_77960_j())) {
                        for (final PotionEffect effect : ((ItemPotion)stack.func_77973_b()).func_77832_l(stack)) {
                            final Potion potion = Potion.field_76425_a[effect.func_76456_a()];
                            if (this.isBestEffect(effect, stack) && !potion.func_76398_f() && !this.isDelayed(potion) && (!AutoPot.mc.field_71439_g.func_70644_a(potion) || effect.func_76458_c() > AutoPot.mc.field_71439_g.func_70660_b(potion).func_76458_c())) {
                                if (potion != Potion.field_76432_h && potion != Potion.field_76428_l) {
                                    this.updateDelay(potion);
                                    this.slots.add(36 + i);
                                    event.pitch = 87.9f;
                                    break;
                                }
                                if (AutoPot.mc.field_71439_g.func_110143_aJ() <= this.health.getValue()) {
                                    this.updateDelay(potion);
                                    this.slots.add(36 + i);
                                    event.pitch = 87.9f;
                                    break;
                                }
                                continue;
                            }
                        }
                    }
                }
            }
        }
    }
    
    private boolean isBestEffect(final PotionEffect effect, final ItemStack itemStack) {
        if (this.fromInv.isEnabled()) {
            for (int i = 9; i < 45; ++i) {
                if (AutoPot.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75216_d()) {
                    final ItemStack stack = AutoPot.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75211_c();
                    if (stack != itemStack && stack.func_77973_b() instanceof ItemPotion && ItemPotion.func_77831_g(stack.func_77960_j())) {
                        for (final PotionEffect potionEffect : ((ItemPotion)stack.func_77973_b()).func_77832_l(stack)) {
                            if (potionEffect.func_76456_a() == effect.func_76456_a() && potionEffect.func_76458_c() > effect.func_76458_c()) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        else {
            for (int i = 0; i < 9; ++i) {
                if (AutoPot.mc.field_71439_g.field_71071_by.func_70301_a(i) != null) {
                    final ItemStack stack = AutoPot.mc.field_71439_g.field_71071_by.func_70301_a(i);
                    if (stack != itemStack && stack.func_77973_b() instanceof ItemPotion && ItemPotion.func_77831_g(stack.func_77960_j())) {
                        for (final PotionEffect potionEffect : ((ItemPotion)stack.func_77973_b()).func_77832_l(stack)) {
                            if (potionEffect.func_76456_a() == effect.func_76456_a() && potionEffect.func_76458_c() > effect.func_76458_c()) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
    
    @SubscribeEvent
    public void onUpdatePost(final MotionUpdateEvent.Post event) {
        final int slot = AutoPot.mc.field_71439_g.field_71071_by.field_70461_c;
        for (final int hotbar : this.slots) {
            if (hotbar >= 36) {
                PlayerUtils.swapToSlot(hotbar - 36);
                AutoPot.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C08PacketPlayerBlockPlacement(AutoPot.mc.field_71439_g.func_70694_bm()));
            }
            else {
                this.click(hotbar, AutoPot.mc.field_71439_g.field_71070_bA.func_75139_a(hotbar).func_75211_c());
                PlayerUtils.swapToSlot(1);
                AutoPot.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C08PacketPlayerBlockPlacement(AutoPot.mc.field_71439_g.field_71070_bA.func_75139_a(hotbar).func_75211_c()));
                this.click(hotbar, AutoPot.mc.field_71439_g.field_71070_bA.func_75139_a(37).func_75211_c());
                KillAura.DISABLE.reset();
            }
        }
        PlayerUtils.swapToSlot(slot);
    }
    
    private void click(final int slot, final ItemStack stack) {
        final short short1 = AutoPot.mc.field_71439_g.field_71070_bA.func_75136_a((InventoryPlayer)null);
        AutoPot.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C0EPacketClickWindow(AutoPot.mc.field_71439_g.field_71070_bA.field_75152_c, slot, 1, 2, stack, short1));
    }
    
    private void updateDelay(final Potion potion) {
        if (this.delays.containsKey(potion)) {
            this.delays.replace(potion, System.currentTimeMillis());
        }
        else {
            this.delays.put(potion, System.currentTimeMillis());
        }
    }
    
    private boolean isDelayed(final Potion potion) {
        return this.delays.containsKey(potion) && System.currentTimeMillis() - this.delays.get(potion) < this.delay.getValue();
    }
}
