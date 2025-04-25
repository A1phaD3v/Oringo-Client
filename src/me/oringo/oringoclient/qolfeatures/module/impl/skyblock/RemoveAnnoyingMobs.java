// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.skyblock;

import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import java.util.ArrayList;
import net.minecraft.entity.Entity;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class RemoveAnnoyingMobs extends Module
{
    private Entity golemEntity;
    public static ArrayList<Entity> seraphs;
    public BooleanSetting hidePlayers;
    
    public RemoveAnnoyingMobs() {
        super("Remove Mobs", 0, Category.SKYBLOCK);
        this.hidePlayers = new BooleanSetting("Hide players", false);
        this.addSettings(this.hidePlayers);
    }
    
    @SubscribeEvent
    public void onUpdate(final LivingEvent.LivingUpdateEvent event) {
        if (RemoveAnnoyingMobs.mc.field_71441_e == null || RemoveAnnoyingMobs.mc.field_71439_g == null) {
            return;
        }
        if (this.isToggled()) {
            if (event.entity instanceof EntityPlayer && !event.entity.equals((Object)Minecraft.func_71410_x().field_71439_g) && this.golemEntity != null && !this.golemEntity.field_70128_L && this.golemEntity.func_70032_d(event.entity) < 9.0f) {
                event.entity.field_70163_u = 999999.0;
                event.entity.field_70137_T = 999999.0;
                event.setCanceled(true);
            }
            if (!(event.entity instanceof EntityArmorStand) && !(event.entity instanceof EntityEnderman) && !(event.entity instanceof EntityGuardian) && !(event.entity instanceof EntityFallingBlock) && !event.entity.equals((Object)Minecraft.func_71410_x().field_71439_g)) {
                for (final Entity seraph : RemoveAnnoyingMobs.seraphs) {
                    if (event.entity.func_70032_d(seraph) < 5.0f) {
                        event.entity.field_70163_u = 999999.0;
                        event.entity.field_70137_T = 999999.0;
                        event.setCanceled(true);
                    }
                }
            }
            if (event.entity instanceof EntityOtherPlayerMP && this.hidePlayers.isEnabled()) {
                event.entity.field_70163_u = 999999.0;
                event.entity.field_70137_T = 999999.0;
                event.setCanceled(true);
            }
            if (event.entity.func_145748_c_().func_150254_d().contains("Endstone Protector")) {
                this.golemEntity = event.entity;
            }
            if (event.entity instanceof EntityCreeper && event.entity.func_82150_aj() && ((EntityCreeper)event.entity).func_110143_aJ() == 20.0f) {
                RemoveAnnoyingMobs.mc.field_71441_e.func_72900_e(event.entity);
            }
            if (event.entity instanceof EntityCreeper && event.entity.func_82150_aj() && ((EntityCreeper)event.entity).func_110143_aJ() != 20.0f) {
                event.entity.func_82142_c(false);
            }
        }
    }
    
    @SubscribeEvent
    public void onWorldRender(final TickEvent.ClientTickEvent event) {
        RemoveAnnoyingMobs.seraphs.clear();
        if (!this.isToggled() || RemoveAnnoyingMobs.mc.field_71441_e == null) {
            return;
        }
        for (final Entity entity : RemoveAnnoyingMobs.mc.field_71441_e.func_72910_y()) {
            if (entity.func_145748_c_().func_150254_d().contains("Voidgloom Seraph")) {
                RemoveAnnoyingMobs.seraphs.add(entity);
            }
            if (entity instanceof EntityFireworkRocket) {
                RemoveAnnoyingMobs.mc.field_71441_e.func_72900_e(entity);
            }
            if (entity instanceof EntityHorse) {
                RemoveAnnoyingMobs.mc.field_71441_e.func_72900_e(entity);
            }
        }
    }
    
    static {
        RemoveAnnoyingMobs.seraphs = new ArrayList<Entity>();
    }
}
