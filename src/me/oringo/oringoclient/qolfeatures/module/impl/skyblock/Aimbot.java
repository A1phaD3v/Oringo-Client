// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.skyblock;

import java.util.ArrayList;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.oringo.oringoclient.utils.Rotation;
import java.util.Iterator;
import me.oringo.oringoclient.utils.RotationUtils;
import net.minecraft.util.Vec3;
import net.minecraft.entity.player.EntityPlayer;
import me.oringo.oringoclient.utils.SkyblockUtils;
import me.oringo.oringoclient.events.MotionUpdateEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import net.minecraft.entity.Entity;
import java.util.List;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class Aimbot extends Module
{
    public NumberSetting yOffset;
    private static List<Entity> killed;
    public static boolean attack;
    
    public Aimbot() {
        super("Blood aimbot", 0, Category.SKYBLOCK);
        this.addSetting(this.yOffset = new NumberSetting("Y offset", 0.0, -2.0, 2.0, 0.1));
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onMove(final MotionUpdateEvent.Pre event) {
        if (!this.isToggled() || !SkyblockUtils.inDungeon || !SkyblockUtils.inBlood || Aimbot.mc.field_71441_e == null) {
            return;
        }
        for (final Entity entity : Aimbot.mc.field_71441_e.field_73010_i) {
            if (entity.func_70032_d((Entity)Aimbot.mc.field_71439_g) < 20.0f && entity instanceof EntityPlayer && !entity.field_70128_L && !Aimbot.killed.contains(entity)) {
                for (final String name : new String[] { "Revoker", "Psycho", "Reaper", "Cannibal", "Mute", "Ooze", "Putrid", "Freak", "Leech", "Tear", "Parasite", "Flamer", "Skull", "Mr. Dead", "Vader", "Frost", "Walker", "WanderingSoul" }) {
                    if (entity.func_70005_c_().contains(name)) {
                        Aimbot.attack = true;
                        final Rotation angles = RotationUtils.getRotations(new Vec3(entity.field_70165_t, entity.field_70163_u + this.yOffset.getValue(), entity.field_70161_v));
                        event.yaw = angles.getYaw();
                        event.pitch = angles.getPitch();
                        Aimbot.killed.add(entity);
                        break;
                    }
                }
            }
        }
    }
    
    @SubscribeEvent
    public void onMovePost(final MotionUpdateEvent.Post event) {
        if (!Aimbot.attack) {
            return;
        }
        Aimbot.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C0APacketAnimation());
        Aimbot.attack = false;
    }
    
    @SubscribeEvent
    public void onWorldLoad(final WorldEvent.Load event) {
        Aimbot.killed.clear();
    }
    
    static {
        Aimbot.killed = new ArrayList<Entity>();
    }
}
