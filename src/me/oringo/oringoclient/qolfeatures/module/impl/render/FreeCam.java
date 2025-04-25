// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.render;

import net.minecraft.network.play.client.C03PacketPlayer;
import me.oringo.oringoclient.events.PacketSentEvent;
import net.minecraftforge.event.world.WorldEvent;
import me.oringo.oringoclient.utils.RenderUtils;
import me.oringo.oringoclient.OringoClient;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import me.oringo.oringoclient.utils.MovementUtils;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class FreeCam extends Module
{
    private EntityOtherPlayerMP playerEntity;
    public NumberSetting speed;
    public BooleanSetting tracer;
    
    public FreeCam() {
        super("FreeCam", Category.RENDER);
        this.speed = new NumberSetting("Speed", 3.0, 0.1, 5.0, 0.1);
        this.tracer = new BooleanSetting("Show tracer", false);
        this.addSettings(this.speed, this.tracer);
    }
    
    @Override
    public void onEnable() {
        if (FreeCam.mc.field_71441_e != null) {
            (this.playerEntity = new EntityOtherPlayerMP((World)FreeCam.mc.field_71441_e, FreeCam.mc.field_71439_g.func_146103_bH())).func_82149_j((Entity)FreeCam.mc.field_71439_g);
            this.playerEntity.field_70122_E = FreeCam.mc.field_71439_g.field_70122_E;
            FreeCam.mc.field_71441_e.func_73027_a(-2137, (Entity)this.playerEntity);
        }
    }
    
    @Override
    public void onDisable() {
        if (FreeCam.mc.field_71439_g == null || FreeCam.mc.field_71441_e == null || this.playerEntity == null) {
            return;
        }
        FreeCam.mc.field_71439_g.field_70145_X = false;
        FreeCam.mc.field_71439_g.func_70107_b(this.playerEntity.field_70165_t, this.playerEntity.field_70163_u, this.playerEntity.field_70161_v);
        FreeCam.mc.field_71441_e.func_73028_b(-2137);
        this.playerEntity = null;
        FreeCam.mc.field_71439_g.func_70016_h(0.0, 0.0, 0.0);
    }
    
    @SubscribeEvent
    public void onLivingUpdate(final LivingEvent.LivingUpdateEvent event) {
        if (this.isToggled()) {
            FreeCam.mc.field_71439_g.field_70145_X = true;
            FreeCam.mc.field_71439_g.field_70143_R = 0.0f;
            FreeCam.mc.field_71439_g.field_70122_E = false;
            FreeCam.mc.field_71439_g.field_71075_bZ.field_75100_b = false;
            FreeCam.mc.field_71439_g.field_70181_x = 0.0;
            if (!MovementUtils.isMoving()) {
                FreeCam.mc.field_71439_g.field_70179_y = 0.0;
                FreeCam.mc.field_71439_g.field_70159_w = 0.0;
            }
            final double speed = this.speed.getValue() * 0.1;
            FreeCam.mc.field_71439_g.field_70747_aH = (float)speed;
            if (FreeCam.mc.field_71474_y.field_74314_A.func_151470_d()) {
                final EntityPlayerSP field_71439_g = FreeCam.mc.field_71439_g;
                field_71439_g.field_70181_x += speed * 3.0;
            }
            if (FreeCam.mc.field_71474_y.field_74311_E.func_151470_d()) {
                final EntityPlayerSP field_71439_g2 = FreeCam.mc.field_71439_g;
                field_71439_g2.field_70181_x -= speed * 3.0;
            }
        }
    }
    
    @SubscribeEvent
    public void onRenderWorld(final RenderWorldLastEvent event) {
        if (this.isToggled() && this.playerEntity != null && this.tracer.isEnabled()) {
            RenderUtils.tracerLine((Entity)this.playerEntity, event.partialTicks, 1.0f, OringoClient.clickGui.getColor());
        }
    }
    
    @SubscribeEvent
    public void onWorldChange(final WorldEvent.Load event) {
        if (this.isToggled()) {
            this.toggle();
        }
    }
    
    @SubscribeEvent
    public void onPacket(final PacketSentEvent event) {
        if (this.isToggled() && event.packet instanceof C03PacketPlayer) {
            event.setCanceled(true);
        }
    }
}
