// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.player;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C02PacketUseEntity;
import me.oringo.oringoclient.utils.PlayerUtils;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import me.oringo.oringoclient.events.PacketSentEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.utils.MilliTimer;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class AutoTool extends Module
{
    public BooleanSetting tools;
    public BooleanSetting swords;
    private MilliTimer delay;
    
    public AutoTool() {
        super("Auto Tool", Category.PLAYER);
        this.tools = new BooleanSetting("Tools", true);
        this.swords = new BooleanSetting("Swords", true);
        this.delay = new MilliTimer();
        this.addSettings(this.tools, this.swords);
    }
    
    @SubscribeEvent
    public void onPacket(final PacketSentEvent event) {
        if (!this.isToggled() || AutoTool.mc.field_71439_g == null) {
            return;
        }
        if (this.tools.isEnabled() && !AutoTool.mc.field_71439_g.func_71039_bw() && event.packet instanceof C07PacketPlayerDigging && ((C07PacketPlayerDigging)event.packet).func_180762_c() == C07PacketPlayerDigging.Action.START_DESTROY_BLOCK) {
            for (int i = 0; i < 9; ++i) {
                final ItemStack stack = AutoTool.mc.field_71439_g.field_71071_by.func_70301_a(i);
                final Block block = AutoTool.mc.field_71441_e.func_180495_p(((C07PacketPlayerDigging)event.packet).func_179715_a()).func_177230_c();
                if (stack != null && block != null && stack.func_150997_a(block) > ((AutoTool.mc.field_71439_g.field_71071_by.func_70448_g() == null) ? 1.0f : AutoTool.mc.field_71439_g.field_71071_by.func_70448_g().func_150997_a(block))) {
                    AutoTool.mc.field_71439_g.field_71071_by.field_70461_c = i;
                }
            }
            PlayerUtils.syncHeldItem();
        }
        else if (this.delay.hasTimePassed(500L) && !AutoTool.mc.field_71439_g.func_71039_bw() && this.swords.isEnabled() && event.packet instanceof C02PacketUseEntity && ((C02PacketUseEntity)event.packet).func_149565_c() == C02PacketUseEntity.Action.ATTACK) {
            for (int i = 0; i < 9; ++i) {
                final ItemStack stack = AutoTool.mc.field_71439_g.field_71071_by.func_70301_a(i);
                if (stack != null && getToolDamage(stack) > ((AutoTool.mc.field_71439_g.field_71071_by.func_70448_g() == null) ? 0.0f : getToolDamage(AutoTool.mc.field_71439_g.field_71071_by.func_70448_g()))) {
                    AutoTool.mc.field_71439_g.field_71071_by.field_70461_c = i;
                }
            }
            PlayerUtils.syncHeldItem();
        }
        if ((event.packet instanceof C09PacketHeldItemChange && AutoTool.mc.field_71439_g.field_71071_by.func_70301_a(((C09PacketHeldItemChange)event.packet).func_149614_c()) != null) || (event.packet instanceof C08PacketPlayerBlockPlacement && ((C08PacketPlayerBlockPlacement)event.packet).func_149574_g() != null)) {
            this.delay.reset();
        }
    }
    
    public static float getToolDamage(final ItemStack tool) {
        float damage = 0.0f;
        if (tool != null && (tool.func_77973_b() instanceof ItemTool || tool.func_77973_b() instanceof ItemSword)) {
            if (tool.func_77973_b() instanceof ItemSword) {
                damage += 4.0f;
            }
            else if (tool.func_77973_b() instanceof ItemAxe) {
                damage += 3.0f;
            }
            else if (tool.func_77973_b() instanceof ItemPickaxe) {
                damage += 2.0f;
            }
            else if (tool.func_77973_b() instanceof ItemSpade) {
                ++damage;
            }
            damage += ((tool.func_77973_b() instanceof ItemTool) ? ((ItemTool)tool.func_77973_b()).func_150913_i().func_78000_c() : ((ItemSword)tool.func_77973_b()).func_150931_i());
            damage += (float)(1.25 * EnchantmentHelper.func_77506_a(Enchantment.field_180314_l.field_77352_x, tool));
            damage += (float)(EnchantmentHelper.func_77506_a(Enchantment.field_180314_l.field_77352_x, tool) * 0.5);
        }
        return damage;
    }
}
