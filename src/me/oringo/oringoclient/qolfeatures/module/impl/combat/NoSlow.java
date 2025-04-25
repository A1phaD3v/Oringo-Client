// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.combat;

import me.oringo.oringoclient.events.PacketSentEvent;
import me.oringo.oringoclient.utils.PacketUtils;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import me.oringo.oringoclient.events.MotionUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.server.S30PacketWindowItems;
import me.oringo.oringoclient.events.PacketReceivedEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.utils.MilliTimer;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.ModeSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class NoSlow extends Module
{
    public NumberSetting eatingSlowdown;
    public NumberSetting swordSlowdown;
    public NumberSetting bowSlowdown;
    public ModeSetting mode;
    private final MilliTimer blockDelay;
    
    public NoSlow() {
        super("NoSlow", 0, Category.COMBAT);
        this.eatingSlowdown = new NumberSetting("Eating slow", 1.0, 0.2, 1.0, 0.1);
        this.swordSlowdown = new NumberSetting("Sword slow", 1.0, 0.2, 1.0, 0.1);
        this.bowSlowdown = new NumberSetting("Bow slow", 1.0, 0.2, 1.0, 0.1);
        this.mode = new ModeSetting("Mode", "Hypixel", new String[] { "Hypixel", "Vanilla" });
        this.blockDelay = new MilliTimer();
        this.addSettings(this.mode, this.swordSlowdown, this.bowSlowdown, this.eatingSlowdown);
    }
    
    @SubscribeEvent
    public void onPacket(final PacketReceivedEvent event) {
        if (event.packet instanceof S30PacketWindowItems && NoSlow.mc.field_71439_g != null && this.isToggled() && this.mode.is("Hypixel") && NoSlow.mc.field_71439_g.func_71039_bw() && NoSlow.mc.field_71439_g.func_71011_bu().func_77973_b() instanceof ItemSword) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent
    public void unUpdate(final MotionUpdateEvent.Post event) {
        if (this.isToggled() && NoSlow.mc.field_71439_g.func_71039_bw() && this.mode.is("Hypixel")) {
            if (this.blockDelay.hasTimePassed(250L) && NoSlow.mc.field_71439_g.func_71011_bu().func_77973_b() instanceof ItemSword) {
                NoSlow.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new C08PacketPlayerBlockPlacement(NoSlow.mc.field_71439_g.func_70694_bm()));
                NoSlow.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new C0BPacketEntityAction((Entity)NoSlow.mc.field_71439_g, C0BPacketEntityAction.Action.STOP_SPRINTING));
                NoSlow.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new C0BPacketEntityAction((Entity)NoSlow.mc.field_71439_g, C0BPacketEntityAction.Action.START_SPRINTING));
                this.blockDelay.reset();
            }
            PacketUtils.sendPacketNoEvent((Packet<?>)new C09PacketHeldItemChange(NoSlow.mc.field_71439_g.field_71071_by.field_70461_c));
        }
    }
    
    @SubscribeEvent
    public void onPacket(final PacketSentEvent event) {
        if (this.isToggled() && this.mode.is("Hypixel") && event.packet instanceof C08PacketPlayerBlockPlacement && ((C08PacketPlayerBlockPlacement)event.packet).func_149574_g() != null && ((C08PacketPlayerBlockPlacement)event.packet).func_149574_g().func_77973_b() instanceof ItemSword) {
            this.blockDelay.reset();
        }
    }
}
