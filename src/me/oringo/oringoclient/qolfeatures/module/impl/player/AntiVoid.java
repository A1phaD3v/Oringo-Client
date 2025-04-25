// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.player;

import java.util.concurrent.ConcurrentLinkedQueue;
import me.oringo.oringoclient.events.WorldJoinEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import me.oringo.oringoclient.utils.PacketUtils;
import net.minecraft.network.Packet;
import me.oringo.oringoclient.events.PacketSentEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.oringo.oringoclient.utils.PlayerUtils;
import me.oringo.oringoclient.OringoClient;
import me.oringo.oringoclient.events.MotionUpdateEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import net.minecraft.util.Vec3;
import net.minecraft.network.play.client.C03PacketPlayer;
import java.util.Queue;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.ModeSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class AntiVoid extends Module
{
    public NumberSetting fallDistance;
    public ModeSetting mode;
    private static BooleanSetting disableFly;
    private static final Queue<C03PacketPlayer> packetQueue;
    private Vec3 lastPos;
    private double motionY;
    
    public AntiVoid() {
        super("Anti Void", 0, Category.PLAYER);
        this.fallDistance = new NumberSetting("Fall distance", 1.0, 0.5, 10.0, 0.1);
        this.mode = new ModeSetting("Mode", "Blink", new String[] { "Flag", "Blink" });
        this.lastPos = new Vec3(0.0, 0.0, 0.0);
        this.motionY = 0.0;
        this.addSettings(this.mode, this.fallDistance, AntiVoid.disableFly);
    }
    
    @SubscribeEvent
    public void onMovePre(final MotionUpdateEvent.Pre event) {
        if (this.isToggled() && (!AntiVoid.disableFly.isEnabled() || !OringoClient.fly.isToggled()) && this.mode.is("Flag") && AntiVoid.mc.field_71439_g.field_70143_R > this.fallDistance.getValue() && PlayerUtils.isOverVoid()) {
            event.setPosition(AntiVoid.mc.field_71439_g.field_70165_t + 100.0, AntiVoid.mc.field_71439_g.field_70163_u + 100.0, AntiVoid.mc.field_71439_g.field_70161_v + 100.0);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPacket(final PacketSentEvent event) {
        if (this.isToggled() && this.mode.is("Blink") && event.packet instanceof C03PacketPlayer && (!AntiVoid.disableFly.isEnabled() || !OringoClient.fly.isToggled())) {
            if (PlayerUtils.isOverVoid()) {
                event.setCanceled(true);
                AntiVoid.packetQueue.offer((C03PacketPlayer)event.packet);
                if (AntiVoid.mc.field_71439_g.field_70143_R > this.fallDistance.getValue()) {
                    AntiVoid.packetQueue.clear();
                    AntiVoid.mc.field_71439_g.field_70143_R = 0.0f;
                    AntiVoid.mc.field_71439_g.func_70107_b(this.lastPos.field_72450_a, this.lastPos.field_72448_b, this.lastPos.field_72449_c);
                    AntiVoid.mc.field_71439_g.func_70016_h(0.0, this.motionY, 0.0);
                }
            }
            else {
                this.lastPos = AntiVoid.mc.field_71439_g.func_174791_d();
                this.motionY = AntiVoid.mc.field_71439_g.field_70181_x;
                while (!AntiVoid.packetQueue.isEmpty()) {
                    PacketUtils.sendPacketNoEvent((Packet<?>)AntiVoid.packetQueue.poll());
                }
            }
        }
    }
    
    public static boolean isBlinking() {
        return !AntiVoid.packetQueue.isEmpty();
    }
    
    @Override
    public void onDisable() {
        AntiVoid.packetQueue.clear();
    }
    
    @SubscribeEvent
    public void onRespawn(final WorldJoinEvent event) {
        AntiVoid.packetQueue.clear();
    }
    
    static {
        AntiVoid.disableFly = new BooleanSetting("Disable fly", true);
        packetQueue = new ConcurrentLinkedQueue<C03PacketPlayer>();
    }
}
