// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.combat;

import net.minecraft.client.Minecraft;
import me.oringo.oringoclient.events.WorldJoinEvent;
import me.oringo.oringoclient.utils.SkyblockUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import java.util.HashMap;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.ModeSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class AntiBot extends Module
{
    private static AntiBot antiBot;
    private static final ModeSetting mode;
    private static final BooleanSetting ticksInvis;
    private static final BooleanSetting tabTicks;
    private static final BooleanSetting npcCheck;
    private static final HashMap<Integer, EntityData> entityData;
    
    public AntiBot() {
        super("Anti Bot", Category.COMBAT);
        this.addSettings(AntiBot.mode, AntiBot.ticksInvis, AntiBot.tabTicks, AntiBot.npcCheck);
    }
    
    public static AntiBot getAntiBot() {
        if (AntiBot.antiBot == null) {
            AntiBot.antiBot = new AntiBot();
        }
        return AntiBot.antiBot;
    }
    
    @SubscribeEvent
    public void onLivingUpdate(final LivingEvent.LivingUpdateEvent event) {
        final EntityData data = AntiBot.entityData.get(event.entity.func_145782_y());
        if (data == null) {
            AntiBot.entityData.put(event.entity.func_145782_y(), new EntityData(event.entity));
        }
        else {
            AntiBot.entityData.get(event.entity.func_145782_y()).update();
        }
    }
    
    public static boolean isValidEntity(final Entity entity) {
        if (AntiBot.antiBot.isToggled() && entity instanceof EntityPlayer && entity != AntiBot.mc.field_71439_g) {
            final EntityData data = AntiBot.entityData.get(entity.func_145782_y());
            if (data != null && AntiBot.mode.is("Hypixel")) {
                return (!AntiBot.tabTicks.isEnabled() || data.getTabTicks() >= 150) && (!AntiBot.ticksInvis.isEnabled() || data.getTicksExisted() - data.getTicksInvisible() >= 150) && (!AntiBot.npcCheck.isEnabled() || !SkyblockUtils.isNPC(entity));
            }
        }
        return true;
    }
    
    @SubscribeEvent
    public void onWorldJOin(final WorldJoinEvent event) {
        AntiBot.entityData.clear();
    }
    
    static {
        mode = new ModeSetting("Mode", "Hypixel", new String[] { "Hypixel" });
        ticksInvis = new BooleanSetting("Invis ticks check", true, aBoolean -> !AntiBot.mode.is("Hypixel"));
        tabTicks = new BooleanSetting("Tab ticks check", false, aBoolean -> !AntiBot.mode.is("Hypixel"));
        npcCheck = new BooleanSetting("NPC check", true, aBoolean -> !AntiBot.mode.is("Hypixel"));
        entityData = new HashMap<Integer, EntityData>();
    }
    
    private static class EntityData
    {
        private int ticksInvisible;
        private int tabTicks;
        private final Entity entity;
        
        public EntityData(final Entity entity) {
            this.entity = entity;
            this.update();
        }
        
        public int getTabTicks() {
            return this.tabTicks;
        }
        
        public int getTicksInvisible() {
            return this.ticksInvisible;
        }
        
        public int getTicksExisted() {
            return this.entity.field_70173_aa;
        }
        
        public void update() {
            if (this.entity instanceof EntityPlayer && AntiBot.mc.func_147114_u() != null && AntiBot.mc.func_147114_u().func_175102_a(this.entity.func_110124_au()) != null) {
                ++this.tabTicks;
            }
            if (this.entity.func_82150_aj()) {
                ++this.ticksInvisible;
            }
        }
    }
}
