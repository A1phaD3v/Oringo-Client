// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.skyblock;

import java.util.ArrayList;
import me.oringo.oringoclient.utils.RenderUtils;
import java.awt.Color;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import me.oringo.oringoclient.utils.Rotation;
import java.util.Iterator;
import me.oringo.oringoclient.utils.RotationUtils;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import net.minecraft.entity.Entity;
import me.oringo.oringoclient.events.MotionUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.oringo.oringoclient.events.WorldJoinEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.utils.MilliTimer;
import net.minecraft.entity.item.EntityArmorStand;
import java.util.List;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class AntiNukebi extends Module
{
    public static final BooleanSetting attack;
    public static final BooleanSetting tracer;
    public static final NumberSetting timeOut;
    public static final NumberSetting distance;
    private static final List<EntityArmorStand> attackedList;
    public static EntityArmorStand currentNukebi;
    private final MilliTimer timeoutTimer;
    
    public AntiNukebi() {
        super("AntiNukekubi", Category.SKYBLOCK);
        this.timeoutTimer = new MilliTimer();
        this.addSettings(AntiNukebi.distance, AntiNukebi.attack, AntiNukebi.timeOut, AntiNukebi.tracer);
    }
    
    private void reset() {
        AntiNukebi.currentNukebi = null;
        AntiNukebi.attackedList.clear();
    }
    
    @Override
    public void onDisable() {
        this.reset();
    }
    
    @SubscribeEvent
    public void onWorldJoin(final WorldJoinEvent event) {
        this.reset();
    }
    
    @SubscribeEvent(priority = EventPriority.LOW)
    public void onMotionUpdate(final MotionUpdateEvent.Pre event) {
        if (this.isToggled()) {
            if (AntiNukebi.currentNukebi == null || this.timeoutTimer.hasTimePassed((long)(AntiNukebi.timeOut.getValue() * 50.0)) || AntiNukebi.currentNukebi.field_70128_L || !AntiNukebi.currentNukebi.func_70685_l((Entity)AntiNukebi.mc.field_71439_g)) {
                AntiNukebi.currentNukebi = null;
                final Iterator<Entity> iterator = ((List)AntiNukebi.mc.field_71441_e.field_72996_f.stream().filter(entity -> entity instanceof EntityArmorStand && isNukebi((EntityArmorStand)entity) && !AntiNukebi.attackedList.contains(entity) && entity.func_70032_d((Entity)AntiNukebi.mc.field_71439_g) < AntiNukebi.distance.getValue() && ((EntityArmorStand)entity).func_70685_l((Entity)AntiNukebi.mc.field_71439_g) && Math.hypot(entity.field_70165_t - entity.field_70169_q, entity.field_70161_v - entity.field_70166_s) < 0.1).collect(Collectors.toList())).iterator();
                if (iterator.hasNext()) {
                    final Entity entity2 = iterator.next();
                    final EntityArmorStand armorStand = AntiNukebi.currentNukebi = (EntityArmorStand)entity2;
                    this.timeoutTimer.reset();
                    AntiNukebi.attackedList.add(armorStand);
                }
            }
            if (AntiNukebi.currentNukebi != null) {
                final Rotation angle = RotationUtils.getRotations(AntiNukebi.currentNukebi.field_70165_t, AntiNukebi.currentNukebi.field_70163_u + 0.85, AntiNukebi.currentNukebi.field_70161_v);
                event.setRotation(angle);
            }
        }
    }
    
    @SubscribeEvent
    public void onWorldRender(final RenderWorldLastEvent event) {
        if (this.isToggled() && AntiNukebi.currentNukebi != null) {
            RenderUtils.tracerLine((Entity)AntiNukebi.currentNukebi, event.partialTicks, 1.0f, Color.white);
        }
    }
    
    public static boolean isNukebi(final EntityArmorStand entity) {
        return entity.func_82169_q(3) != null && entity.func_82169_q(3).serializeNBT().func_74775_l("tag").func_74775_l("SkullOwner").func_74775_l("Properties").toString().contains("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWIwNzU5NGUyZGYyNzM5MjFhNzdjMTAxZDBiZmRmYTExMTVhYmVkNWI5YjIwMjllYjQ5NmNlYmE5YmRiYjRiMyJ9fX0=");
    }
    
    static {
        attack = new BooleanSetting("Attack with aura", true);
        tracer = new BooleanSetting("Tracer", true);
        timeOut = new NumberSetting("Timeout", 100.0, 10.0, 250.0, 1.0);
        distance = new NumberSetting("Distance", 10.0, 5.0, 20.0, 1.0);
        attackedList = new ArrayList<EntityArmorStand>();
    }
}
