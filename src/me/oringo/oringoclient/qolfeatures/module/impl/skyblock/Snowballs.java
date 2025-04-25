// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.skyblock;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import me.oringo.oringoclient.utils.PlayerUtils;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemSnowball;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class Snowballs extends Module
{
    private boolean wasPressed;
    public BooleanSetting pickupstash;
    public BooleanSetting inventory;
    
    public Snowballs() {
        super("Snowballs", Category.SKYBLOCK);
        this.pickupstash = new BooleanSetting("Pick up stash", true);
        this.inventory = new BooleanSetting("Inventory", false);
        this.addSettings(this.pickupstash, this.inventory);
    }
    
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (Snowballs.mc.field_71462_r != null || !this.isToggled()) {
            return;
        }
        if (this.isPressed() && !this.wasPressed) {
            if (this.inventory.isEnabled()) {
                for (int i = 9; i < 45; ++i) {
                    if (Snowballs.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75216_d() && (Snowballs.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75211_c().func_77973_b() instanceof ItemSnowball || Snowballs.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75211_c().func_77973_b() instanceof ItemEgg)) {
                        if (i >= 36) {
                            final int held = Snowballs.mc.field_71439_g.field_71071_by.field_70461_c;
                            PlayerUtils.swapToSlot(i - 36);
                            for (int e = 0; e < 16; ++e) {
                                Snowballs.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C08PacketPlayerBlockPlacement(Snowballs.mc.field_71439_g.func_70694_bm()));
                            }
                            PlayerUtils.swapToSlot(held);
                        }
                        else {
                            PlayerUtils.numberClick(i, Snowballs.mc.field_71439_g.field_71071_by.field_70461_c);
                            for (int e2 = 0; e2 < 16; ++e2) {
                                Snowballs.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C08PacketPlayerBlockPlacement(Snowballs.mc.field_71439_g.func_70694_bm()));
                            }
                            PlayerUtils.numberClick(i, Snowballs.mc.field_71439_g.field_71071_by.field_70461_c);
                        }
                    }
                }
            }
            else {
                final int holding = Snowballs.mc.field_71439_g.field_71071_by.field_70461_c;
                for (int x = 0; x < 9; ++x) {
                    if (Snowballs.mc.field_71439_g.field_71071_by.func_70301_a(x) != null && (Snowballs.mc.field_71439_g.field_71071_by.func_70301_a(x).func_77973_b() instanceof ItemSnowball || Snowballs.mc.field_71439_g.field_71071_by.func_70301_a(x).func_77973_b() instanceof ItemEgg)) {
                        Snowballs.mc.field_71439_g.field_71071_by.field_70461_c = x;
                        PlayerUtils.syncHeldItem();
                        for (int e = 0; e < 16; ++e) {
                            Snowballs.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C08PacketPlayerBlockPlacement(Snowballs.mc.field_71439_g.func_70694_bm()));
                        }
                    }
                }
                Snowballs.mc.field_71439_g.field_71071_by.field_70461_c = holding;
                PlayerUtils.syncHeldItem();
            }
            if (this.pickupstash.isEnabled()) {
                Snowballs.mc.field_71439_g.func_71165_d("/pickupstash");
            }
        }
        this.wasPressed = this.isPressed();
    }
    
    @Override
    public boolean isKeybind() {
        return true;
    }
}
