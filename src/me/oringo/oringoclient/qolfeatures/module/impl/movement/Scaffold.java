// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.movement;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import java.util.Comparator;
import net.minecraft.util.Vec3;
import java.util.ArrayList;
import me.oringo.oringoclient.events.MoveEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemBlock;
import me.oringo.oringoclient.events.PacketSentEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.oringo.oringoclient.utils.Rotation;
import net.minecraft.util.BlockPos;
import java.util.Random;
import net.minecraft.util.MovingObjectPosition;
import me.oringo.oringoclient.utils.PlayerUtils;
import net.minecraft.potion.Potion;
import me.oringo.oringoclient.utils.RotationUtils;
import net.minecraft.world.World;
import me.oringo.oringoclient.utils.MathUtil;
import me.oringo.oringoclient.utils.MovementUtils;
import me.oringo.oringoclient.events.MotionUpdateEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import me.oringo.oringoclient.utils.TimerUtil;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.utils.MilliTimer;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.ModeSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class Scaffold extends Module
{
    public static final NumberSetting distance;
    public static final NumberSetting timer;
    public static final NumberSetting towerTimer;
    public static final NumberSetting maxDelay;
    public static final NumberSetting minDelay;
    public static final NumberSetting test;
    public static final BooleanSetting safeWalk;
    public static final BooleanSetting disableSpeed;
    public static final BooleanSetting disableAura;
    public static final BooleanSetting safe;
    public static final ModeSetting tower;
    public static final ModeSetting sprint;
    private int ticks;
    private MilliTimer timer1;
    private MilliTimer slowdowntimer;
    private int blocksPlaced;
    boolean flag;
    
    public Scaffold() {
        super("Scaffold", Category.MOVEMENT);
        this.timer1 = new MilliTimer();
        this.slowdowntimer = new MilliTimer();
        this.addSettings(Scaffold.distance, Scaffold.minDelay, Scaffold.maxDelay, Scaffold.timer, Scaffold.towerTimer, Scaffold.tower, Scaffold.sprint, Scaffold.safeWalk, Scaffold.disableSpeed, Scaffold.disableAura, Scaffold.safe);
    }
    
    @Override
    public void onEnable() {
        if (Scaffold.mc.field_71439_g != null) {
            TimerUtil.setSpeed((float)Scaffold.timer.getValue());
            this.ticks = 0;
        }
    }
    
    @Override
    public void onDisable() {
        if (Scaffold.mc.field_71439_g != null) {
            TimerUtil.setSpeed(1.0f);
            Scaffold.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C09PacketHeldItemChange(Scaffold.mc.field_71439_g.field_71071_by.field_70461_c));
        }
    }
    
    @SubscribeEvent
    public void onUpdate(final MotionUpdateEvent event) {
        if (this.isToggled()) {
            if (event.isPre()) {
                event.setYaw(MovementUtils.getYaw() + 180.0f).setPitch(81.0f);
                this.flag = true;
                for (int j = 81; j > 72; --j) {
                    final MovingObjectPosition trace = rayTrace(event.yaw, (float)j);
                    if (trace != null) {
                        this.flag = false;
                        event.setPitch((float)(j + MathUtil.getRandomInRange(0.1, -0.1)));
                        break;
                    }
                }
                if (this.flag && !Scaffold.safe.isEnabled()) {
                    final BlockPos pos = this.getClosestBlock();
                    if (pos != null) {
                        final Rotation rotation = RotationUtils.getRotations(RotationUtils.getClosestPointInAABB(Scaffold.mc.field_71439_g.func_174824_e(1.0f), Scaffold.mc.field_71441_e.func_180495_p(pos).func_177230_c().func_180646_a((World)Scaffold.mc.field_71441_e, pos)));
                        final MovingObjectPosition position = rayTrace(rotation);
                        if (position != null) {
                            event.setRotation(rotation);
                        }
                    }
                }
                if (Scaffold.mc.field_71474_y.field_74314_A.func_151470_d()) {
                    TimerUtil.setSpeed((float)Scaffold.towerTimer.getValue());
                    if (Scaffold.tower.is("Hypixel")) {
                        if (!Scaffold.mc.field_71439_g.func_70644_a(Potion.field_76430_j) && PlayerUtils.isOnGround(0.3)) {
                            Scaffold.mc.field_71439_g.field_70181_x = 0.38999998569488525;
                        }
                        Scaffold.mc.field_71439_g.func_70637_d(false);
                    }
                }
                else {
                    this.timer1.reset();
                    this.slowdowntimer.reset();
                    this.blocksPlaced = 0;
                    TimerUtil.setSpeed((float)Scaffold.timer.getValue());
                }
            }
            else {
                final int selectedSlot = this.getBlock();
                if (selectedSlot == -1) {
                    return;
                }
                Scaffold.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C09PacketHeldItemChange(selectedSlot));
                if (this.ticks <= 0) {
                    final MovingObjectPosition rayrace = rayTrace(event.getRotation());
                    if (rayrace != null && rayrace.field_72313_a == MovingObjectPosition.MovingObjectType.BLOCK && Scaffold.mc.field_71441_e.func_180495_p(rayrace.func_178782_a()).func_177230_c().func_149730_j()) {
                        if (Scaffold.mc.field_71474_y.field_74314_A.func_151470_d() && Scaffold.tower.is("Hypixel")) {
                            if (!PlayerUtils.isInsideBlock()) {
                                this.placeBlock();
                            }
                        }
                        else if (Scaffold.mc.field_71442_b.func_178890_a(Scaffold.mc.field_71439_g, Scaffold.mc.field_71441_e, Scaffold.mc.field_71439_g.field_71071_by.func_70301_a(selectedSlot), rayrace.func_178782_a(), rayrace.field_178784_b, rayrace.field_72307_f)) {
                            Scaffold.mc.field_71439_g.func_71038_i();
                        }
                        ++this.blocksPlaced;
                        if (!this.flag) {
                            this.ticks = (int)(Scaffold.minDelay.getValue() + new Random().nextInt((int)(Scaffold.maxDelay.getValue() - Scaffold.minDelay.getValue() + 1.0)));
                        }
                        else {
                            this.ticks = Math.max(2, (int)(Scaffold.minDelay.getValue() + new Random().nextInt((int)(Scaffold.maxDelay.getValue() - Scaffold.minDelay.getValue() + 1.0))));
                        }
                        if (Scaffold.mc.field_71439_g.field_71071_by.func_70301_a(selectedSlot) != null && Scaffold.mc.field_71439_g.field_71071_by.func_70301_a(selectedSlot).field_77994_a <= 0) {
                            Scaffold.mc.field_71439_g.field_71071_by.func_70304_b(selectedSlot);
                        }
                    }
                }
                --this.ticks;
            }
        }
    }
    
    @SubscribeEvent
    public void onPacket(final PacketSentEvent event) {
    }
    
    private int getBlock() {
        int current = -1;
        int stackSize = 0;
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = Scaffold.mc.field_71439_g.field_71071_by.func_70301_a(i);
            if (stack != null && stackSize < stack.field_77994_a && stack.func_77973_b() instanceof ItemBlock && ((ItemBlock)stack.func_77973_b()).field_150939_a.func_149730_j()) {
                stackSize = stack.field_77994_a;
                current = i;
            }
        }
        return current;
    }
    
    @SubscribeEvent
    public void onMove(final MoveEvent event) {
        if (this.isToggled() && !Scaffold.sprint.is("Sprint")) {
            final double speed = Scaffold.sprint.is("Semi") ? 0.2575 : 0.225;
            final double x = MathUtil.clamp(event.getX(), speed, -speed);
            final double z = MathUtil.clamp(event.getZ(), speed, -speed);
            event.setX(x).setZ(z);
        }
    }
    
    private BlockPos getClosestBlock() {
        final ArrayList<Vec3> posList = new ArrayList<Vec3>();
        for (int range = (int)Math.ceil(Scaffold.distance.getValue()), x = -range; x <= range; ++x) {
            for (int y = -range + 2; y < 0; ++y) {
                for (int z = -range; z <= range; ++z) {
                    final Vec3 vec = new Vec3((double)x, (double)y, (double)z).func_72441_c(Scaffold.mc.field_71439_g.field_70165_t, Scaffold.mc.field_71439_g.field_70163_u, Scaffold.mc.field_71439_g.field_70161_v);
                    final BlockPos pos2 = new BlockPos(vec);
                    if (Scaffold.mc.field_71441_e.func_180495_p(pos2).func_177230_c().func_149730_j()) {
                        posList.add(vec);
                    }
                }
            }
        }
        if (posList.isEmpty()) {
            return null;
        }
        posList.sort(Comparator.comparingDouble(pos -> Scaffold.mc.field_71439_g.func_70011_f(pos.field_72450_a, pos.field_72448_b + 1.0, pos.field_72449_c)));
        return new BlockPos((Vec3)posList.get(0));
    }
    
    private static MovingObjectPosition rayTrace(final Rotation rotation) {
        return rayTrace(rotation.getYaw(), rotation.getPitch());
    }
    
    private static MovingObjectPosition rayTrace(final float yaw, final float pitch) {
        final Vec3 vec3 = Scaffold.mc.field_71439_g.func_174824_e(1.0f);
        final Vec3 vec4 = PlayerUtils.getVectorForRotation(yaw, pitch);
        final Vec3 vec5 = vec3.func_72441_c(vec4.field_72450_a * Scaffold.mc.field_71442_b.func_78757_d(), vec4.field_72448_b * Scaffold.mc.field_71442_b.func_78757_d(), vec4.field_72449_c * Scaffold.mc.field_71442_b.func_78757_d());
        return Scaffold.mc.field_71441_e.func_72933_a(vec3, vec5);
    }
    
    private void placeBlock() {
        final MovingObjectPosition rayrace = rayTrace(0.0f, 90.0f);
        if (rayrace != null) {
            final Vec3 hitVec = rayrace.field_72307_f;
            final BlockPos hitPos = rayrace.func_178782_a();
            final float f = (float)(hitVec.field_72450_a - hitPos.func_177958_n());
            final float f2 = (float)(hitVec.field_72448_b - hitPos.func_177956_o());
            final float f3 = (float)(hitVec.field_72449_c - hitPos.func_177952_p());
            Scaffold.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C08PacketPlayerBlockPlacement(rayrace.func_178782_a(), rayrace.field_178784_b.func_176745_a(), Scaffold.mc.field_71439_g.func_70694_bm(), f, f2, f3));
            Scaffold.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C0APacketAnimation());
            Scaffold.mc.field_71439_g.func_70694_bm().func_179546_a((EntityPlayer)Scaffold.mc.field_71439_g, (World)Scaffold.mc.field_71441_e, hitPos, rayrace.field_178784_b, f, f2, f3);
        }
    }
    
    static {
        distance = new NumberSetting("Range", 4.5, 1.0, 4.5, 0.1);
        timer = new NumberSetting("Timer", 1.0, 0.1, 3.0, 0.05);
        towerTimer = new NumberSetting("Tower timer", 1.0, 0.1, 3.0, 0.05);
        maxDelay = new NumberSetting("Max delay", 1.0, 0.0, 4.0, 1.0) {
            @Override
            public void setValue(final double value) {
                super.setValue(value);
                if (this.getValue() < Scaffold.minDelay.getValue()) {
                    this.setValue(Scaffold.minDelay.getValue());
                }
            }
        };
        minDelay = new NumberSetting("Min delay", 1.0, 0.0, 4.0, 1.0) {
            @Override
            public void setValue(final double value) {
                super.setValue(value);
                if (this.getValue() > Scaffold.maxDelay.getValue()) {
                    this.setValue(Scaffold.maxDelay.getValue());
                }
            }
        };
        test = new NumberSetting("Test", 5.15, 5.0, 5.5, 0.05);
        safeWalk = new BooleanSetting("Safe walk", true);
        disableSpeed = new BooleanSetting("Disable speed", true);
        disableAura = new BooleanSetting("Disable aura", true);
        safe = new BooleanSetting("Safe", true);
        tower = new ModeSetting("Tower", "None", new String[] { "None", "Hypixel" });
        sprint = new ModeSetting("Sprint", "Semi", new String[] { "None", "Semi", "Sprint" });
    }
    
    private static class BlockPlaceData
    {
        public final BlockPos pos;
        public final BlockPos targetPos;
        
        public BlockPlaceData(final BlockPos pos, final BlockPos targetPos) {
            this.pos = pos;
            this.targetPos = targetPos;
        }
    }
}
