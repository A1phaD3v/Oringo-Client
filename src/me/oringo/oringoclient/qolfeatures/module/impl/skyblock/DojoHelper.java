// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.skyblock;

import java.util.Map;
import net.minecraft.item.ItemStack;
import java.util.function.Predicate;
import net.minecraft.item.Item;
import me.oringo.oringoclient.events.PreAttackEvent;
import net.minecraft.entity.monster.EntityZombie;
import java.util.Collection;
import java.util.ArrayList;
import me.oringo.oringoclient.utils.SkyblockUtils;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.init.Blocks;
import me.oringo.oringoclient.events.BlockBoundsEvent;
import me.oringo.oringoclient.events.MoveEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.util.MovingObjectPosition;
import me.oringo.oringoclient.utils.Rotation;
import java.util.Iterator;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import me.oringo.oringoclient.utils.PlayerUtils;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.item.ItemBow;
import me.oringo.oringoclient.utils.RotationUtils;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Comparator;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.item.EntityArmorStand;
import java.util.List;
import java.util.regex.Pattern;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import me.oringo.oringoclient.events.MotionUpdateEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.ModeSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import net.minecraft.entity.Entity;
import java.util.HashMap;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class DojoHelper extends Module
{
    private int jumpStage;
    private int ticks;
    private static boolean inTenacity;
    private static boolean inMastery;
    private static final HashMap<Entity, Long> shot;
    public static final BooleanSetting hideZombies;
    public static final BooleanSetting swordSwap;
    public static final BooleanSetting tenacity;
    public static final BooleanSetting masteryAimbot;
    public static final BooleanSetting wTap;
    public static final NumberSetting time;
    public static final NumberSetting bowCharge;
    public static final ModeSetting color;
    
    public DojoHelper() {
        super("Dojo Helper", Category.SKYBLOCK);
        this.addSettings(DojoHelper.hideZombies, DojoHelper.swordSwap, DojoHelper.tenacity, DojoHelper.masteryAimbot, DojoHelper.wTap, DojoHelper.time, DojoHelper.bowCharge, DojoHelper.color);
    }
    
    @SubscribeEvent
    public void onPlayerUpdate(final MotionUpdateEvent event) {
        if (this.isToggled()) {
            if (DojoHelper.masteryAimbot.isEnabled() && DojoHelper.inMastery && DojoHelper.mc.field_71439_g.func_70694_bm() != null && DojoHelper.mc.field_71439_g.func_70694_bm().func_77973_b() == Items.field_151031_f) {
                if (!DojoHelper.mc.field_71439_g.func_71039_bw()) {
                    DojoHelper.mc.field_71442_b.func_78769_a((EntityPlayer)DojoHelper.mc.field_71439_g, (World)DojoHelper.mc.field_71441_e, DojoHelper.mc.field_71439_g.func_70694_bm());
                }
                KeyBinding.func_74510_a(DojoHelper.mc.field_71474_y.field_74313_G.func_151463_i(), true);
                final Pattern pattern = Pattern.compile("\\d:\\d\\d\\d");
                Entity target = null;
                double time = 100.0;
                DojoHelper.shot.entrySet().removeIf(entry -> System.currentTimeMillis() - entry.getValue() > 5000L);
                final Pattern pattern2;
                final Iterator<Entity> iterator = ((List)DojoHelper.mc.field_71441_e.field_72996_f.stream().filter(e -> e instanceof EntityArmorStand && getColor(e.func_70005_c_()) && !DojoHelper.shot.containsKey(e) && pattern2.matcher(e.func_70005_c_()).find()).sorted(Comparator.comparingDouble(entity -> this.getPriority(ChatFormatting.stripFormatting(entity.func_70005_c_())))).collect(Collectors.toList())).iterator();
                if (iterator.hasNext()) {
                    final Entity entity2 = iterator.next();
                    final Rotation rotation = RotationUtils.getRotations(entity2.func_174791_d().func_72441_c(0.0, 4.0, 0.0));
                    target = entity2;
                    time = this.getPriority(ChatFormatting.stripFormatting(entity2.func_70005_c_()));
                    event.setRotation(rotation);
                }
                if (DojoHelper.mc.field_71439_g.func_71039_bw() && DojoHelper.mc.field_71439_g.func_71011_bu().func_77973_b() == Items.field_151031_f && target != null && !event.isPre()) {
                    final ItemBow bow = (ItemBow)DojoHelper.mc.field_71439_g.func_71011_bu().func_77973_b();
                    final int i = bow.func_77626_a(DojoHelper.mc.field_71439_g.func_71011_bu()) - DojoHelper.mc.field_71439_g.func_71052_bv();
                    float f = i / 20.0f;
                    f = (f * f + f * 2.0f) / 3.0f;
                    if (f >= DojoHelper.bowCharge.getValue() && time <= DojoHelper.time.getValue()) {
                        if (DojoHelper.wTap.isEnabled()) {
                            DojoHelper.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C0BPacketEntityAction((Entity)DojoHelper.mc.field_71439_g, C0BPacketEntityAction.Action.STOP_SPRINTING));
                            DojoHelper.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C0BPacketEntityAction((Entity)DojoHelper.mc.field_71439_g, C0BPacketEntityAction.Action.START_SPRINTING));
                        }
                        DojoHelper.mc.field_71442_b.func_78766_c((EntityPlayer)DojoHelper.mc.field_71439_g);
                        DojoHelper.shot.put(target, System.currentTimeMillis());
                    }
                }
            }
            if (event.isPre()) {
                if (DojoHelper.tenacity.isEnabled() && DojoHelper.inTenacity) {
                    if (this.jumpStage == 0) {
                        event.setPitch(90.0f);
                        if (PlayerUtils.isLiquid(0.01f) && DojoHelper.mc.field_71439_g.field_70122_E) {
                            final MovingObjectPosition rayrace = PlayerUtils.rayTrace(0.0f, 90.0f, 4.5f);
                            if (rayrace != null) {
                                final int held = DojoHelper.mc.field_71439_g.field_71071_by.field_70461_c;
                                PlayerUtils.swapToSlot(8);
                                DojoHelper.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C08PacketPlayerBlockPlacement(DojoHelper.mc.field_71439_g.func_70694_bm()));
                                final Vec3 hitVec = rayrace.field_72307_f;
                                final BlockPos hitPos = rayrace.func_178782_a();
                                final float f2 = (float)(hitVec.field_72450_a - hitPos.func_177958_n());
                                final float f3 = (float)(hitVec.field_72448_b - hitPos.func_177956_o());
                                final float f4 = (float)(hitVec.field_72449_c - hitPos.func_177952_p());
                                DojoHelper.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C08PacketPlayerBlockPlacement(rayrace.func_178782_a(), rayrace.field_178784_b.func_176745_a(), DojoHelper.mc.field_71439_g.func_70694_bm(), f2, f3, f4));
                                DojoHelper.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C0APacketAnimation());
                                PlayerUtils.swapToSlot(held);
                            }
                            DojoHelper.mc.field_71439_g.func_70664_aZ();
                            this.jumpStage = 1;
                        }
                    }
                    else if (this.jumpStage == 1) {
                        if (PlayerUtils.isLiquid(0.5f) && DojoHelper.mc.field_71439_g.field_70181_x < 0.0) {
                            this.jumpStage = 2;
                        }
                    }
                    else if (this.jumpStage == 2) {
                        this.ticks %= 40;
                        ++this.ticks;
                        if (this.ticks == 40) {
                            event.setY(event.y - 0.20000000298023224);
                        }
                        else if (this.ticks == 39) {
                            event.setY(event.y - 0.10000000149011612);
                        }
                        else if (this.ticks == 38) {
                            event.setY(event.y - 0.07999999821186066);
                            event.setX(event.x + 0.20000000298023224);
                            event.setZ(event.z + 0.20000000298023224);
                        }
                    }
                }
                else {
                    final int n = 0;
                    this.jumpStage = n;
                    this.ticks = n;
                }
            }
        }
    }
    
    private double getPriority(String name) {
        double timeLeft = 100000.0;
        name = name.replaceAll(":", ".");
        timeLeft = Double.parseDouble(name);
        return timeLeft;
    }
    
    @SubscribeEvent
    public void onMove(final MoveEvent event) {
        if (this.isToggled() && DojoHelper.tenacity.isEnabled() && DojoHelper.inTenacity && this.jumpStage == 2) {
            event.stop();
            DojoHelper.mc.field_71439_g.func_70016_h(0.0, 0.0, 0.0);
        }
    }
    
    private static boolean getColor(final String name) {
        if (DojoHelper.color.is("Red")) {
            return name.startsWith("§c§l");
        }
        if (DojoHelper.color.is("Green")) {
            return name.startsWith("§a§l");
        }
        return DojoHelper.color.is("Yellow") && name.startsWith("§e§l");
    }
    
    @SubscribeEvent
    public void onBlockBounds(final BlockBoundsEvent event) {
        if (event.block == Blocks.field_150353_l && DojoHelper.inTenacity && this.isToggled() && DojoHelper.tenacity.isEnabled()) {
            event.aabb = new AxisAlignedBB((double)event.pos.func_177958_n(), (double)event.pos.func_177956_o(), (double)event.pos.func_177952_p(), (double)(event.pos.func_177958_n() + 1), (double)(event.pos.func_177956_o() + 1), (double)(event.pos.func_177952_p() + 1));
        }
    }
    
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (this.isToggled()) {
            DojoHelper.inTenacity = SkyblockUtils.hasLine("Challenge: Tenacity");
            DojoHelper.inMastery = SkyblockUtils.hasLine("Challenge: Mastery");
            if (DojoHelper.hideZombies.isEnabled() && DojoHelper.mc.field_71439_g != null && DojoHelper.mc.field_71441_e != null && DojoHelper.mc.field_71439_g.func_96123_co() != null && SkyblockUtils.hasLine("Challenge: Force")) {
                for (final Entity entity : new ArrayList<Entity>(DojoHelper.mc.field_71441_e.field_72996_f)) {
                    if (entity instanceof EntityZombie && ((EntityZombie)entity).func_82169_q(3) != null && ((EntityZombie)entity).func_82169_q(3).func_77973_b() == Items.field_151024_Q) {
                        entity.field_70163_u = -100.0;
                        entity.field_70137_T = -100.0;
                    }
                    if (entity instanceof EntityArmorStand && entity.func_145748_c_().func_150260_c().startsWith("§c-")) {
                        entity.field_70163_u = -100.0;
                        entity.field_70137_T = -100.0;
                    }
                }
            }
        }
    }
    
    @SubscribeEvent
    public void onLeftClick(final PreAttackEvent event) {
        if (this.isToggled() && DojoHelper.swordSwap.isEnabled()) {
            this.left(event.entity);
        }
    }
    
    public void left(final Entity target) {
        if (SkyblockUtils.hasLine("Challenge: Discipline") && target instanceof EntityZombie && ((EntityZombie)target).func_82169_q(3) != null) {
            final Item item = ((EntityZombie)target).func_82169_q(3).func_77973_b();
            if (Items.field_151024_Q.equals(item)) {
                this.pickItem(stack -> stack.func_77973_b() == Items.field_151041_m);
            }
            else if (Items.field_151169_ag.equals(item)) {
                this.pickItem(stack -> stack.func_77973_b() == Items.field_151010_B);
            }
            else if (Items.field_151161_ac.equals(item)) {
                this.pickItem(stack -> stack.func_77973_b() == Items.field_151048_u);
            }
            else if (Items.field_151028_Y.equals(item)) {
                this.pickItem(stack -> stack.func_77973_b() == Items.field_151040_l);
            }
        }
    }
    
    private void pickItem(final Predicate<ItemStack> predicate) {
        final int slot = PlayerUtils.getHotbar(predicate);
        if (slot != -1) {
            PlayerUtils.swapToSlot(slot);
        }
    }
    
    static {
        shot = new HashMap<Entity, Long>();
        hideZombies = new BooleanSetting("Hide bad zombies", true);
        swordSwap = new BooleanSetting("Auto sword swap", true);
        tenacity = new BooleanSetting("Tenacity float", true);
        masteryAimbot = new BooleanSetting("Mastery aimbot", true);
        wTap = new BooleanSetting("W tap", true, a -> !DojoHelper.masteryAimbot.isEnabled());
        time = new NumberSetting("Time", 0.3, 0.1, 5.0, 0.05, a -> !DojoHelper.masteryAimbot.isEnabled());
        bowCharge = new NumberSetting("Bow charge", 0.6, 0.1, 1.0, 0.1, a -> !DojoHelper.masteryAimbot.isEnabled());
        color = new ModeSetting("Color", a -> !DojoHelper.masteryAimbot.isEnabled(), "Yellow", new String[] { "Red", "Yellow", "Green" });
    }
}
