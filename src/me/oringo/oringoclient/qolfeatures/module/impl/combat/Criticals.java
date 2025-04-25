// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.combat;

import me.oringo.oringoclient.events.PacketSentEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.oringo.oringoclient.utils.PacketUtils;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import me.oringo.oringoclient.utils.MathUtil;
import me.oringo.oringoclient.mixins.entity.PlayerSPAccessor;
import net.minecraft.world.World;
import net.minecraft.entity.EntityLivingBase;
import me.oringo.oringoclient.OringoClient;
import me.oringo.oringoclient.events.MotionUpdateEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.utils.MilliTimer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.ModeSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class Criticals extends Module
{
    public static final NumberSetting delay;
    public static final NumberSetting hurtTime;
    public static final ModeSetting mode;
    private C02PacketUseEntity attack;
    private int ticks;
    private float[] offsets;
    private MilliTimer timer;
    
    public Criticals() {
        super("Criticals", Category.COMBAT);
        this.ticks = 0;
        this.offsets = new float[] { 0.0625f, 0.03125f };
        this.timer = new MilliTimer();
        this.addSettings(Criticals.mode, Criticals.delay, Criticals.hurtTime);
    }
    
    @SubscribeEvent
    public void onUpdate(final MotionUpdateEvent.Pre event) {
        if (this.isToggled() && this.attack != null && !OringoClient.speed.isToggled()) {
            final String selected = Criticals.mode.getSelected();
            switch (selected) {
                case "Hypixel 2": {
                    if (Criticals.mc.field_71439_g.field_70122_E && event.onGround && this.attack.func_149564_a((World)Criticals.mc.field_71441_e) instanceof EntityLivingBase && ((EntityLivingBase)this.attack.func_149564_a((World)Criticals.mc.field_71441_e)).field_70737_aN <= Criticals.hurtTime.getValue()) {
                        Criticals.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(((PlayerSPAccessor)Criticals.mc.field_71439_g).getLastReportedPosX(), ((PlayerSPAccessor)Criticals.mc.field_71439_g).getLastReportedPosY() + this.offsets[0] + MathUtil.getRandomInRange(0.0, 0.0010000000474974513), ((PlayerSPAccessor)Criticals.mc.field_71439_g).getLastReportedPosZ(), false));
                        Criticals.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(((PlayerSPAccessor)Criticals.mc.field_71439_g).getLastReportedPosX(), ((PlayerSPAccessor)Criticals.mc.field_71439_g).getLastReportedPosY() + this.offsets[1] + MathUtil.getRandomInRange(0.0, 0.0010000000474974513), ((PlayerSPAccessor)Criticals.mc.field_71439_g).getLastReportedPosZ(), false));
                        PacketUtils.sendPacketNoEvent((Packet<?>)this.attack);
                        this.attack = null;
                        OringoClient.sendMessageWithPrefix("Hypixel");
                        break;
                    }
                    this.attack = null;
                    break;
                }
                case "Hypixel": {
                    if (Criticals.mc.field_71439_g.field_70122_E && this.attack != null && event.onGround && this.attack.func_149564_a((World)Criticals.mc.field_71441_e) instanceof EntityLivingBase && ((EntityLivingBase)this.attack.func_149564_a((World)Criticals.mc.field_71441_e)).field_70737_aN <= Criticals.hurtTime.getValue()) {
                        switch (this.ticks++) {
                            case 0:
                            case 1: {
                                event.y += this.offsets[this.ticks - 1] + MathUtil.getRandomInRange(0.0, 0.0010000000474974513);
                                event.setOnGround(false);
                                OringoClient.sendMessageWithPrefix("Hypixel 2");
                                break;
                            }
                            case 2: {
                                PacketUtils.sendPacketNoEvent((Packet<?>)this.attack);
                                this.ticks = 0;
                                this.attack = null;
                                break;
                            }
                        }
                        break;
                    }
                    this.ticks = 0;
                    this.attack = null;
                    break;
                }
            }
        }
    }
    
    @Override
    public void onEnable() {
        this.attack = null;
        this.ticks = 0;
    }
    
    @SubscribeEvent
    public void onPacket(final PacketSentEvent event) {
        if (this.isToggled() && !OringoClient.speed.isToggled() && event.packet instanceof C02PacketUseEntity && ((C02PacketUseEntity)event.packet).func_149565_c() == C02PacketUseEntity.Action.ATTACK && ((C02PacketUseEntity)event.packet).func_149564_a((World)Criticals.mc.field_71441_e) instanceof EntityLivingBase && ((EntityLivingBase)((C02PacketUseEntity)event.packet).func_149564_a((World)Criticals.mc.field_71441_e)).field_70737_aN <= Criticals.hurtTime.getValue() && this.timer.hasTimePassed((long)Criticals.delay.getValue())) {
            this.attack = (C02PacketUseEntity)event.packet;
            event.setCanceled(true);
            this.timer.reset();
        }
    }
    
    static {
        delay = new NumberSetting("Delay", 500.0, 0.0, 2000.0, 50.0);
        hurtTime = new NumberSetting("Hurt time", 2.0, 0.0, 10.0, 1.0);
        mode = new ModeSetting("Mode", "Hypixel", new String[] { "Hypixel", "Hypixel 2" });
    }
}
