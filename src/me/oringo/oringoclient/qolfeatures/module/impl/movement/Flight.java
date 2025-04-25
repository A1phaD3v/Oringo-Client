// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.movement;

import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.item.ItemBlock;
import net.minecraft.potion.Potion;
import me.oringo.oringoclient.utils.PlayerUtils;
import me.oringo.oringoclient.events.MotionUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import me.oringo.oringoclient.events.PacketReceivedEvent;
import me.oringo.oringoclient.events.MoveHeadingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.oringo.oringoclient.utils.MovementUtils;
import me.oringo.oringoclient.utils.TimerUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.entity.player.PlayerCapabilities;
import me.oringo.oringoclient.events.MoveEvent;
import me.oringo.oringoclient.mixins.MinecraftAccessor;
import me.oringo.oringoclient.ui.notifications.Notifications;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.utils.MilliTimer;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.ModeSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class Flight extends Module
{
    public static ModeSetting mode;
    public static NumberSetting speed;
    public static NumberSetting time;
    public static NumberSetting timerSpeed;
    public static NumberSetting autoDisable;
    public static NumberSetting test;
    public static BooleanSetting autoDisableHypixel;
    public static BooleanSetting timerBoost;
    public MilliTimer disablerTimer;
    public MilliTimer autoDisableTimer;
    private boolean isFlying;
    private boolean placed;
    private double distance;
    private int flyingTicks;
    private int stage;
    private int ticks;
    
    public Flight() {
        super("Flight", 0, Category.MOVEMENT);
        this.disablerTimer = new MilliTimer();
        this.autoDisableTimer = new MilliTimer();
        this.addSettings(Flight.speed, Flight.mode, Flight.timerSpeed, Flight.time, Flight.test, Flight.autoDisableHypixel, Flight.autoDisable);
    }
    
    @Override
    public void onDisable() {
        if (Flight.mode.is("Hypixel")) {
            if (this.distance > 4.0) {
                Notifications.showNotification(String.format("Distance flown: %.1f", this.distance), 4000, Notifications.NotificationType.INFO);
            }
            if (Flight.mc.field_71439_g != null) {
                Flight.mc.field_71439_g.field_70159_w = 0.0;
                Flight.mc.field_71439_g.field_70179_y = 0.0;
            }
        }
        else if (Flight.mc.field_71439_g != null) {
            Flight.mc.field_71439_g.func_70016_h(0.0, 0.0, 0.0);
        }
        this.isFlying = false;
        ((MinecraftAccessor)Flight.mc).getTimer().field_74278_d = 1.0f;
    }
    
    @Override
    public void onEnable() {
        final boolean b = false;
        this.placed = b;
        this.isFlying = b;
        final int flyingTicks = 0;
        this.ticks = flyingTicks;
        this.stage = flyingTicks;
        this.flyingTicks = flyingTicks;
        this.distance = flyingTicks;
        this.autoDisableTimer.reset();
        if (Flight.mode.is("Hypixel") && Flight.mc.field_71439_g != null) {
            if (!Flight.mc.field_71439_g.field_70122_E) {
                this.setToggled(false);
            }
            else {
                Flight.mc.field_71439_g.func_70664_aZ();
                Flight.mc.field_71439_g.field_70181_x = 0.41999998688697815;
            }
        }
    }
    
    @SubscribeEvent
    public void onMove(final MoveEvent event) {
        if (this.isToggled()) {
            final String selected = Flight.mode.getSelected();
            int n = -1;
            switch (selected.hashCode()) {
                case -1248403467: {
                    if (selected.equals("Hypixel")) {
                        n = 0;
                        break;
                    }
                    break;
                }
                case 376026813: {
                    if (selected.equals("Hypixel Slime")) {
                        n = 1;
                        break;
                    }
                    break;
                }
                case 1897755483: {
                    if (selected.equals("Vanilla")) {
                        n = 2;
                        break;
                    }
                    break;
                }
            }
            Label_0403: {
                switch (n) {
                    case 0: {
                        if (this.isFlying) {
                            event.setY(0.0);
                            Flight.mc.field_71439_g.field_70181_x = 0.0;
                        }
                        if (this.flyingTicks > 2) {
                            event.setMotion(event.getX() * Flight.test.getValue(), event.getY(), event.getZ() * Flight.test.getValue());
                            break;
                        }
                        event.setX(0.0).setZ(0.0);
                        break;
                    }
                    case 1: {
                        if (Flight.mc.field_71439_g.field_71075_bZ.field_75101_c) {
                            if (Flight.mc.field_71439_g.field_70173_aa % 6 == 0 || !this.isFlying || this.disablerTimer.hasTimePassed((long)Flight.time.getValue() - 150L)) {
                                final PlayerCapabilities capabilities = new PlayerCapabilities();
                                capabilities.field_75101_c = true;
                                capabilities.field_75100_b = false;
                                Flight.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C13PacketPlayerAbilities(capabilities));
                                capabilities.field_75100_b = true;
                                Flight.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C13PacketPlayerAbilities(capabilities));
                                this.isFlying = true;
                                this.disablerTimer.reset();
                            }
                            break Label_0403;
                        }
                        else {
                            if (!this.disablerTimer.hasTimePassed((long)Flight.time.getValue())) {
                                break Label_0403;
                            }
                            if (this.isFlying) {
                                Flight.mc.field_71439_g.func_70016_h(0.0, 0.0, 0.0);
                                this.isFlying = false;
                                ((MinecraftAccessor)Flight.mc).getTimer().field_74278_d = 1.0f;
                                break;
                            }
                            break;
                        }
                        break;
                    }
                    case 2: {
                        if (Flight.mode.is("Vanilla") && this.autoDisableTimer.hasTimePassed((long)Flight.autoDisable.getValue()) && Flight.autoDisable.getValue() != 0.0) {
                            this.setToggled(false);
                            return;
                        }
                        TimerUtil.setSpeed((float)Flight.timerSpeed.getValue());
                        event.setY(0.0);
                        MovementUtils.setMotion(event, Flight.speed.getValue());
                        if (Flight.mc.field_71474_y.field_74314_A.func_151470_d()) {
                            event.setY(Flight.speed.getValue());
                        }
                        if (Flight.mc.field_71474_y.field_74311_E.func_151470_d()) {
                            event.setY(Flight.speed.getValue() * -1.0);
                            break;
                        }
                        break;
                    }
                }
            }
        }
    }
    
    @SubscribeEvent
    public void onMoveHeading(final MoveHeadingEvent e) {
        if (this.isToggled() && Flight.mode.is("Hypixel") && this.isFlying) {
            e.setOnGround(true);
        }
    }
    
    @SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
    public void onPacket(final PacketReceivedEvent event) {
        if (this.isToggled() && Flight.mode.is("Hypixel") && event.packet instanceof S08PacketPlayerPosLook) {
            if (!this.isFlying || this.flyingTicks < 5) {
                this.isFlying = true;
            }
            else if (Flight.autoDisableHypixel.isEnabled()) {
                this.setToggled(false);
            }
        }
    }
    
    @SubscribeEvent
    public void onUpdate(final MotionUpdateEvent.Pre event) {
        if (this.isToggled()) {
            final String selected = Flight.mode.getSelected();
            switch (selected) {
                case "Hypixel": {
                    if (!this.placed) {
                        event.setPitch(90.0f);
                        if (!PlayerUtils.isOnGround(1.0)) {
                            this.placeBlock();
                            if (!this.placed) {
                                this.setToggled(false);
                            }
                            event.setOnGround(true);
                            break;
                        }
                        break;
                    }
                    else {
                        double timer = Flight.timerSpeed.getValue();
                        if (!Flight.mc.field_71439_g.func_70644_a(Potion.field_76424_c)) {
                            timer = 0.699999988079071;
                        }
                        TimerUtil.setSpeed((float)timer);
                        if (this.isFlying) {
                            ++this.flyingTicks;
                            this.distance += Math.hypot(Flight.mc.field_71439_g.field_70165_t - Flight.mc.field_71439_g.field_70169_q, Flight.mc.field_71439_g.field_70161_v - Flight.mc.field_71439_g.field_70166_s);
                            break;
                        }
                        ++this.stage;
                        event.setOnGround(false);
                        if (this.stage > 1 && this.stage < 5) {
                            event.setY(event.y - 0.2);
                            break;
                        }
                        break;
                    }
                    break;
                }
            }
        }
    }
    
    private void placeBlock() {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = Flight.mc.field_71439_g.field_71071_by.func_70301_a(i);
            if (stack != null && stack.func_77973_b() instanceof ItemBlock && ((ItemBlock)stack.func_77973_b()).field_150939_a.func_149730_j()) {
                slot = i;
                break;
            }
        }
        if (slot != -1) {
            final int prev = Flight.mc.field_71439_g.field_71071_by.field_70461_c;
            PlayerUtils.swapToSlot(slot);
            final Vec3 vec3 = Flight.mc.field_71439_g.func_174824_e(1.0f);
            final Vec3 vec4 = PlayerUtils.getVectorForRotation(0.0f, 90.0f);
            final Vec3 vec5 = vec3.func_72441_c(vec4.field_72450_a * Flight.mc.field_71442_b.func_78757_d(), vec4.field_72448_b * Flight.mc.field_71442_b.func_78757_d(), vec4.field_72449_c * Flight.mc.field_71442_b.func_78757_d());
            final MovingObjectPosition rayrace = Flight.mc.field_71441_e.func_147447_a(vec3, vec5, false, true, true);
            if (rayrace != null) {
                final Vec3 hitVec = rayrace.field_72307_f;
                final BlockPos hitPos = rayrace.func_178782_a();
                final float f = (float)(hitVec.field_72450_a - hitPos.func_177958_n());
                final float f2 = (float)(hitVec.field_72448_b - hitPos.func_177956_o());
                final float f3 = (float)(hitVec.field_72449_c - hitPos.func_177952_p());
                Flight.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C08PacketPlayerBlockPlacement(rayrace.func_178782_a(), rayrace.field_178784_b.func_176745_a(), Flight.mc.field_71439_g.func_70694_bm(), f, f2, f3));
                Flight.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C0APacketAnimation());
                Flight.mc.field_71439_g.func_70694_bm().func_179546_a((EntityPlayer)Flight.mc.field_71439_g, (World)Flight.mc.field_71441_e, hitPos, rayrace.field_178784_b, f, f2, f3);
                this.placed = true;
            }
            PlayerUtils.swapToSlot(prev);
        }
        else {
            Notifications.showNotification("Oringo client", "No blocks found", 2000);
        }
    }
    
    public boolean isFlying() {
        return this.isToggled() && (!Flight.mode.is("Hypixel Slime") || !this.disablerTimer.hasTimePassed((long)Flight.time.getValue()));
    }
    
    static {
        Flight.mode = new ModeSetting("Mode", "Vanilla", new String[] { "Hypixel Slime", "Vanilla", "Hypixel" });
        Flight.speed = new NumberSetting("Speed", 1.0, 0.1, 5.0, 0.1, aBoolean -> Flight.mode.is("Hypixel"));
        Flight.time = new NumberSetting("Disabler timer", 1200.0, 250.0, 2500.0, 1.0) {
            @Override
            public boolean isHidden() {
                return !Flight.mode.is("Hypixel Slime");
            }
        };
        Flight.timerSpeed = new NumberSetting("Timer Speed", 1.0, 0.1, 3.0, 0.1);
        Flight.autoDisable = new NumberSetting("Auto disable", 1500.0, 0.0, 5000.0, 50.0) {
            @Override
            public boolean isHidden() {
                return !Flight.mode.is("Vanilla");
            }
        };
        Flight.test = new NumberSetting("Test", 1.0, 0.1, 10.0, 0.1);
        Flight.autoDisableHypixel = new BooleanSetting("Disable on flag", true, aBoolean -> !Flight.mode.is("Hypixel"));
        Flight.timerBoost = new BooleanSetting("Timer boost", true, aBoolean -> !Flight.mode.is("Hypixel"));
    }
}
