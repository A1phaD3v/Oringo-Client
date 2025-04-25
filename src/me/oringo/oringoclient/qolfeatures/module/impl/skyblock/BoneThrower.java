// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.skyblock;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import me.oringo.oringoclient.utils.PlayerUtils;
import me.oringo.oringoclient.events.PlayerUpdateEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class BoneThrower extends Module
{
    public BooleanSetting autoDisable;
    public BooleanSetting inventory;
    private boolean wasPressed;
    
    public BoneThrower() {
        super("BoneThrower", Category.SKYBLOCK);
        this.autoDisable = new BooleanSetting("Disable", true);
        this.inventory = new BooleanSetting("Inventory", false);
        this.addSettings(this.autoDisable, this.inventory);
    }
    
    @SubscribeEvent
    public void onUpdate(final PlayerUpdateEvent event) {
        if (this.isToggled()) {
            if (!this.wasPressed && this.isPressed()) {
                if (!this.inventory.isEnabled()) {
                    final int last = BoneThrower.mc.field_71439_g.field_71071_by.field_70461_c;
                    for (int i = 0; i < 9; ++i) {
                        final ItemStack stack = BoneThrower.mc.field_71439_g.field_71071_by.func_70301_a(i);
                        if (stack != null && stack.func_82833_r().contains("Bonemerang")) {
                            BoneThrower.mc.field_71439_g.field_71071_by.field_70461_c = i;
                            PlayerUtils.syncHeldItem();
                            BoneThrower.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C08PacketPlayerBlockPlacement(BoneThrower.mc.field_71439_g.func_70694_bm()));
                        }
                    }
                    BoneThrower.mc.field_71439_g.field_71071_by.field_70461_c = last;
                    PlayerUtils.syncHeldItem();
                }
                else {
                    for (int j = 9; j < 45; ++j) {
                        if (BoneThrower.mc.field_71439_g.field_71069_bz.func_75139_a(j).func_75216_d() && BoneThrower.mc.field_71439_g.field_71069_bz.func_75139_a(j).func_75211_c().func_82833_r().contains("Bonemerang")) {
                            if (j >= 36) {
                                final int held = BoneThrower.mc.field_71439_g.field_71071_by.field_70461_c;
                                PlayerUtils.swapToSlot(j - 36);
                                BoneThrower.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C08PacketPlayerBlockPlacement(BoneThrower.mc.field_71439_g.func_70694_bm()));
                                PlayerUtils.swapToSlot(held);
                            }
                            else {
                                PlayerUtils.numberClick(j, BoneThrower.mc.field_71439_g.field_71071_by.field_70461_c);
                                BoneThrower.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C08PacketPlayerBlockPlacement(BoneThrower.mc.field_71439_g.func_70694_bm()));
                                PlayerUtils.numberClick(j, BoneThrower.mc.field_71439_g.field_71071_by.field_70461_c);
                            }
                        }
                    }
                }
            }
            this.wasPressed = this.isPressed();
        }
    }
    
    @Override
    public boolean isKeybind() {
        return true;
    }
}
