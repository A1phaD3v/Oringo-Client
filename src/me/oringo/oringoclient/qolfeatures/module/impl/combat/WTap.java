// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.combat;

import me.oringo.oringoclient.mixins.entity.PlayerSPAccessor;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C02PacketUseEntity;
import me.oringo.oringoclient.events.PacketSentEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.ModeSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class WTap extends Module
{
    public ModeSetting mode;
    public BooleanSetting bow;
    
    public WTap() {
        super("WTap", Category.COMBAT);
        this.mode = new ModeSetting("mode", "Packet", new String[] { "Packet", "Extra Packet" });
        this.bow = new BooleanSetting("Bow", true);
        this.addSettings(this.mode, this.bow);
    }
    
    @SubscribeEvent
    public void onPacket(final PacketSentEvent event) {
        if (this.isToggled() && ((event.packet instanceof C02PacketUseEntity && ((C02PacketUseEntity)event.packet).func_149565_c() == C02PacketUseEntity.Action.ATTACK) || (this.bow.isEnabled() && event.packet instanceof C07PacketPlayerDigging && ((C07PacketPlayerDigging)event.packet).func_180762_c() == C07PacketPlayerDigging.Action.RELEASE_USE_ITEM && WTap.mc.field_71439_g.func_70694_bm() != null && WTap.mc.field_71439_g.func_70694_bm().func_77973_b() instanceof ItemBow))) {
            final String selected = this.mode.getSelected();
            switch (selected) {
                case "Extra Packet": {
                    for (int i = 0; i < 4; ++i) {
                        WTap.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C0BPacketEntityAction((Entity)WTap.mc.field_71439_g, C0BPacketEntityAction.Action.STOP_SPRINTING));
                        WTap.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C0BPacketEntityAction((Entity)WTap.mc.field_71439_g, C0BPacketEntityAction.Action.START_SPRINTING));
                    }
                    break;
                }
                default: {
                    if (WTap.mc.field_71439_g.func_70051_ag()) {
                        WTap.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C0BPacketEntityAction((Entity)WTap.mc.field_71439_g, C0BPacketEntityAction.Action.STOP_SPRINTING));
                    }
                    WTap.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C0BPacketEntityAction((Entity)WTap.mc.field_71439_g, C0BPacketEntityAction.Action.START_SPRINTING));
                    break;
                }
            }
        }
    }
    
    @SubscribeEvent
    public void onPacket(final PacketSentEvent.Post event) {
        if (this.isToggled() && ((event.packet instanceof C02PacketUseEntity && ((C02PacketUseEntity)event.packet).func_149565_c() == C02PacketUseEntity.Action.ATTACK) || (this.bow.isEnabled() && event.packet instanceof C07PacketPlayerDigging && ((C07PacketPlayerDigging)event.packet).func_180762_c() == C07PacketPlayerDigging.Action.RELEASE_USE_ITEM && WTap.mc.field_71439_g.func_70694_bm() != null && WTap.mc.field_71439_g.func_70694_bm().func_77973_b() instanceof ItemBow)) && !WTap.mc.field_71439_g.func_70051_ag()) {
            ((PlayerSPAccessor)WTap.mc.field_71439_g).setServerSprintState(false);
        }
    }
}
