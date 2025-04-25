// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.macro;

import net.minecraft.client.renderer.DestroyBlockProgress;
import java.util.Map;
import net.minecraft.block.BlockColored;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockStone;
import java.util.Optional;
import java.util.Comparator;
import java.util.function.Predicate;
import me.oringo.oringoclient.utils.Rotation;
import me.oringo.oringoclient.utils.RotationUtils;
import java.util.Iterator;
import net.minecraft.init.Blocks;
import me.oringo.oringoclient.utils.SkyblockUtils;
import me.oringo.oringoclient.utils.EntityUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MovingObjectPosition;
import java.util.Random;
import me.oringo.oringoclient.utils.PlayerUtils;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.List;
import net.minecraft.item.ItemMap;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import me.oringo.oringoclient.events.PacketReceivedEvent;
import net.minecraft.inventory.Slot;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.init.Items;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.entity.Entity;
import net.minecraft.client.settings.KeyBinding;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import me.oringo.oringoclient.utils.RenderUtils;
import java.awt.Color;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.oringo.oringoclient.OringoClient;
import net.minecraftforge.event.world.WorldEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.ModeSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import net.minecraft.entity.item.EntityArmorStand;
import java.util.ArrayList;
import net.minecraft.util.Vec3;
import net.minecraft.util.BlockPos;
import net.minecraft.client.Minecraft;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class MithrilMacro extends Module
{
    private Minecraft mc;
    private BlockPos target;
    private BlockPos test;
    private Vec3 targetRotation;
    private Vec3 targetRotation2;
    private ArrayList<Float> yaw;
    private ArrayList<Float> pitch;
    private boolean stopLoop;
    private int ticksTargeting;
    private int ticksMining;
    private int ticks;
    private int ticksSeen;
    private int shouldReconnect;
    public EntityArmorStand drillnpc;
    private int lastKey;
    private int timeLeft;
    private int pause;
    private BooleanSetting drillRefuel;
    private NumberSetting rotations;
    private NumberSetting accuracyChecks;
    private NumberSetting maxBreakTime;
    private NumberSetting quickBreak;
    private NumberSetting panic;
    private BooleanSetting titanium;
    private BooleanSetting sneak;
    private BooleanSetting under;
    private BooleanSetting autoAbility;
    private NumberSetting moreMovement;
    private NumberSetting walking;
    private NumberSetting walkingTime;
    private ModeSetting mode;
    
    public MithrilMacro() {
        super("Mithril Macro", 0, Category.OTHER);
        this.mc = Minecraft.func_71410_x();
        this.target = null;
        this.test = null;
        this.targetRotation = null;
        this.targetRotation2 = null;
        this.yaw = new ArrayList<Float>();
        this.pitch = new ArrayList<Float>();
        this.stopLoop = false;
        this.ticksTargeting = 0;
        this.ticksMining = 0;
        this.ticks = 0;
        this.ticksSeen = 0;
        this.shouldReconnect = -1;
        this.lastKey = -1;
        this.timeLeft = 0;
        this.pause = 0;
        this.drillRefuel = new BooleanSetting("Drill Refuel", false);
        this.rotations = new NumberSetting("Rotations", 10.0, 1.0, 20.0, 1.0);
        this.accuracyChecks = new NumberSetting("Accuracy", 5.0, 3.0, 10.0, 1.0);
        this.maxBreakTime = new NumberSetting("Max break time", 160.0, 40.0, 400.0, 1.0);
        this.quickBreak = new NumberSetting("Block skip progress", 0.9, 0.0, 1.0, 0.1);
        this.panic = new NumberSetting("Auto leave", 100.0, 0.0, 200.0, 1.0);
        this.titanium = new BooleanSetting("Prioritize titanium", true);
        this.sneak = new BooleanSetting("Sneak", false);
        this.under = new BooleanSetting("Mine under", false);
        this.autoAbility = new BooleanSetting("Auto ability", true);
        this.moreMovement = new NumberSetting("Head movements", 5.0, 0.0, 50.0, 1.0);
        this.walking = new NumberSetting("Walking %", 0.1, 0.0, 5.0, 0.1);
        this.walkingTime = new NumberSetting("Walking ticks", 5.0, 0.0, 60.0, 1.0);
        this.mode = new ModeSetting("Target", "Clay", new String[] { "Clay", "Prismarine", "Wool", "Blue", "Gold" });
        this.addSettings(this.rotations, this.drillRefuel, this.accuracyChecks, this.titanium, this.sneak, this.quickBreak, this.maxBreakTime, this.autoAbility, this.under, this.panic, this.moreMovement, this.walking, this.walkingTime, this.mode);
    }
    
    @SubscribeEvent
    public void onLoad(final WorldEvent.Load event) {
        this.drillnpc = null;
        if (this.isToggled()) {
            this.setToggled(false);
            if (OringoClient.aotvReturn.isToggled()) {
                OringoClient.aotvReturn.start(() -> this.setToggled(true), false);
            }
        }
    }
    
    @SubscribeEvent
    public void onWorldRender(final RenderWorldLastEvent event) {
        if (!this.isToggled()) {
            return;
        }
        if (this.target != null) {
            RenderUtils.blockBox(this.target, Color.CYAN);
        }
        if (this.targetRotation != null) {
            RenderUtils.miniBlockBox(this.targetRotation, Color.GREEN);
        }
        if (this.targetRotation2 != null) {
            RenderUtils.miniBlockBox(this.targetRotation2, Color.RED);
        }
    }
    
    @SubscribeEvent
    public void reconnect(final TickEvent.ClientTickEvent event) {
        if (this.mc.field_71462_r instanceof GuiDisconnected && this.shouldReconnect < 0 && this.isToggled()) {
            this.shouldReconnect = 250;
            this.setToggled(false);
        }
        if (this.shouldReconnect-- == 0) {
            this.mc.func_147108_a((GuiScreen)new GuiConnecting((GuiScreen)new GuiMainMenu(), this.mc, new ServerData("Hypixel", "play.Hypixel.net", false)));
            new Thread(() -> {
                try {
                    Thread.sleep(15000L);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (this.mc.field_71439_g != null && OringoClient.aotvReturn.isToggled()) {
                    OringoClient.aotvReturn.start(() -> this.setToggled(true), false);
                }
            }).start();
        }
    }
    
    @Override
    public void onEnable() {
        this.ticksSeen = 0;
        this.ticksMining = 0;
        this.ticksTargeting = 0;
        if (this.autoAbility.isEnabled() && this.mc.field_71439_g.func_70694_bm() != null) {
            this.mc.field_71442_b.func_78769_a((EntityPlayer)this.mc.field_71439_g, (World)this.mc.field_71441_e, this.mc.field_71439_g.func_70694_bm());
        }
    }
    
    @SubscribeEvent
    public void onChat(final ClientChatReceivedEvent event) {
        if (!this.isToggled()) {
            return;
        }
        final String message = event.message.func_150254_d();
        if (this.drillRefuel.isEnabled() && ChatFormatting.stripFormatting(message).startsWith("Your") && ChatFormatting.stripFormatting(message).endsWith("Refuel it by talking to a Drill Mechanic!") && this.drillnpc != null) {
            this.setToggled(false);
            int[] array;
            int length;
            int l = 0;
            int a;
            int i;
            Slot slot;
            int j;
            Slot slot2;
            new Thread(() -> {
                try {
                    array = new int[] { this.mc.field_71474_y.field_74351_w.func_151463_i(), this.mc.field_71474_y.field_74370_x.func_151463_i(), this.mc.field_71474_y.field_74368_y.func_151463_i(), this.mc.field_71474_y.field_74366_z.func_151463_i(), this.mc.field_71474_y.field_74311_E.func_151463_i(), this.mc.field_71474_y.field_74312_F.func_151463_i() };
                    for (length = array.length; l < length; ++l) {
                        a = array[l];
                        KeyBinding.func_74510_a(a, false);
                    }
                    Thread.sleep(500L);
                    this.mc.field_71442_b.func_78768_b((EntityPlayer)this.mc.field_71439_g, (Entity)this.drillnpc);
                    Thread.sleep(2500L);
                    if (this.mc.field_71439_g.field_71070_bA instanceof ContainerChest && ((ContainerChest)this.mc.field_71439_g.field_71070_bA).func_85151_d().func_145748_c_().func_150260_c().contains("Drill Anvil")) {
                        i = 0;
                        while (i < this.mc.field_71439_g.field_71070_bA.field_75151_b.size()) {
                            slot = this.mc.field_71439_g.field_71070_bA.func_75139_a(i);
                            if (slot.func_75216_d() && slot.func_75211_c().func_82833_r().contains("Drill") && slot.func_75211_c().func_77973_b() == Items.field_179562_cC) {
                                this.mc.field_71442_b.func_78753_a(this.mc.field_71439_g.field_71070_bA.field_75152_c, slot.field_75222_d, 0, 1, (EntityPlayer)this.mc.field_71439_g);
                                break;
                            }
                            else {
                                ++i;
                            }
                        }
                        Thread.sleep(500L);
                        j = 0;
                        while (j < this.mc.field_71439_g.field_71070_bA.field_75151_b.size()) {
                            slot2 = this.mc.field_71439_g.field_71070_bA.func_75139_a(j);
                            if (slot2.func_75216_d() && (slot2.func_75211_c().func_82833_r().contains("Volta") || slot2.func_75211_c().func_82833_r().contains("Oil Barrel") || slot2.func_75211_c().func_82833_r().contains("Biofuel"))) {
                                this.mc.field_71442_b.func_78753_a(this.mc.field_71439_g.field_71070_bA.field_75152_c, slot2.field_75222_d, 0, 1, (EntityPlayer)this.mc.field_71439_g);
                                break;
                            }
                            else {
                                ++j;
                            }
                        }
                        Thread.sleep(500L);
                        this.mc.field_71442_b.func_78753_a(this.mc.field_71439_g.field_71070_bA.field_75152_c, 22, 0, 0, (EntityPlayer)this.mc.field_71439_g);
                        Thread.sleep(250L);
                        this.mc.field_71442_b.func_78753_a(this.mc.field_71439_g.field_71070_bA.field_75152_c, 13, 0, 1, (EntityPlayer)this.mc.field_71439_g);
                        Thread.sleep(250L);
                        this.mc.field_71439_g.func_71053_j();
                    }
                    Thread.sleep(2500L);
                    this.setToggled(true);
                    KeyBinding.func_74510_a(this.mc.field_71474_y.field_74312_F.func_151463_i(), true);
                    this.mc.func_147108_a((GuiScreen)new GuiChat());
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return;
            }).start();
        }
        if (ChatFormatting.stripFormatting(event.message.func_150260_c()).equals("Mining Speed Boost is now available!") && this.autoAbility.isEnabled() && this.mc.field_71439_g.func_70694_bm() != null) {
            OringoClient.sendMessageWithPrefix("Auto ability");
            this.mc.field_71442_b.func_78769_a((EntityPlayer)this.mc.field_71439_g, (World)this.mc.field_71441_e, this.mc.field_71439_g.func_70694_bm());
        }
        if (ChatFormatting.stripFormatting(event.message.func_150260_c()).equals("Oh no! Your Pickonimbus 2000 broke!")) {
            int k;
            new Thread(() -> {
                try {
                    Thread.sleep(1000L);
                }
                catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
                k = 0;
                while (k < 9) {
                    if (this.mc.field_71439_g.field_71071_by.func_70301_a(k) != null && this.mc.field_71439_g.field_71071_by.func_70301_a(k).func_82833_r().contains("Pickonimbus")) {
                        this.mc.field_71439_g.field_71071_by.field_70461_c = k;
                        break;
                    }
                    else {
                        ++k;
                    }
                }
            }).start();
        }
    }
    
    @Override
    public void onDisable() {
        KeyBinding.func_74510_a(Minecraft.func_71410_x().field_71474_y.field_74312_F.func_151463_i(), false);
        KeyBinding.func_74510_a(Minecraft.func_71410_x().field_71474_y.field_74311_E.func_151463_i(), false);
    }
    
    @SubscribeEvent(receiveCanceled = true)
    public void onPacket(final PacketReceivedEvent event) {
        if (event.packet instanceof S08PacketPlayerPosLook && this.isToggled()) {
            this.pause = 200;
            this.target = null;
            KeyBinding.func_74510_a(Minecraft.func_71410_x().field_71474_y.field_74312_F.func_151463_i(), false);
            KeyBinding.func_74510_a(Minecraft.func_71410_x().field_71474_y.field_74311_E.func_151463_i(), false);
            for (final int a : new int[] { this.mc.field_71474_y.field_74351_w.func_151463_i(), this.mc.field_71474_y.field_74370_x.func_151463_i(), this.mc.field_71474_y.field_74368_y.func_151463_i(), this.mc.field_71474_y.field_74366_z.func_151463_i() }) {
                KeyBinding.func_74510_a(a, false);
            }
        }
    }
    
    private boolean isPickaxe(final ItemStack itemStack) {
        return itemStack != null && (itemStack.func_82833_r().contains("Pickaxe") || itemStack.func_77973_b() == Items.field_179562_cC || itemStack.func_82833_r().contains("Gauntlet"));
    }
    
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        --this.pause;
        if (this.isToggled() && !(this.mc.field_71462_r instanceof GuiContainer) && !(this.mc.field_71462_r instanceof GuiEditSign) && this.pause < 1) {
            ++this.ticks;
            if (this.mc.field_71439_g != null && this.mc.field_71439_g.func_70694_bm() != null && this.mc.field_71439_g.func_70694_bm().func_77973_b() instanceof ItemMap) {
                this.setToggled(false);
                this.mc.field_71439_g.func_71165_d("/l");
            }
            if (this.mc.field_71441_e != null) {
                if (this.drillnpc == null && this.drillRefuel.isEnabled()) {
                    for (final Entity entityArmorStand : (List)this.mc.field_71441_e.func_72910_y().stream().filter(entity -> entity instanceof EntityArmorStand).collect(Collectors.toList())) {
                        if (entityArmorStand.func_145748_c_().func_150254_d().contains("§e§lDRILL MECHANIC§r")) {
                            OringoClient.mithrilMacro.drillnpc = (EntityArmorStand)entityArmorStand;
                            OringoClient.sendMessageWithPrefix("Mechanic");
                            return;
                        }
                    }
                    this.setToggled(false);
                    OringoClient.aotvReturn.start(() -> this.setToggled(true), false);
                    return;
                }
                if (!this.isPickaxe(this.mc.field_71439_g.func_70694_bm())) {
                    for (int i = 0; i < 9; ++i) {
                        if (this.isPickaxe(this.mc.field_71439_g.field_71071_by.func_70301_a(i))) {
                            PlayerUtils.swapToSlot(i);
                        }
                    }
                }
                if (this.timeLeft-- <= 0) {
                    final int[] keybinds = { this.mc.field_71474_y.field_74351_w.func_151463_i(), this.mc.field_71474_y.field_74370_x.func_151463_i(), this.mc.field_71474_y.field_74368_y.func_151463_i(), this.mc.field_71474_y.field_74366_z.func_151463_i(), this.mc.field_71474_y.field_74370_x.func_151463_i(), this.mc.field_71474_y.field_74368_y.func_151463_i(), this.mc.field_71474_y.field_74366_z.func_151463_i(), this.mc.field_71474_y.field_74368_y.func_151463_i(), this.mc.field_71474_y.field_74368_y.func_151463_i() };
                    if (this.lastKey != -1) {
                        KeyBinding.func_74510_a(this.lastKey, false);
                        KeyBinding.func_74510_a(Minecraft.func_71410_x().field_71474_y.field_74311_E.func_151463_i(), this.sneak.isEnabled());
                    }
                    if (new Random().nextFloat() < this.walking.getValue() / 100.0) {
                        this.lastKey = keybinds[new Random().nextInt(keybinds.length)];
                        KeyBinding.func_74510_a(Minecraft.func_71410_x().field_71474_y.field_74311_E.func_151463_i(), true);
                        KeyBinding.func_74510_a(this.lastKey, true);
                        this.timeLeft = (int)this.walkingTime.getValue();
                    }
                }
                else {
                    KeyBinding.func_74510_a(this.lastKey, true);
                    KeyBinding.func_74510_a(this.mc.field_71474_y.field_74311_E.func_151463_i(), true);
                }
                if (this.mc.field_71476_x != null && this.mc.field_71476_x.field_72313_a == MovingObjectPosition.MovingObjectType.ENTITY) {
                    final Entity entity2 = this.mc.field_71476_x.field_72308_g;
                    if (entity2 instanceof EntityPlayer && !EntityUtils.isTeam((EntityLivingBase)entity2)) {
                        SkyblockUtils.click();
                        this.pause = 5;
                        return;
                    }
                }
                if (this.mc.field_71441_e.field_73010_i.stream().anyMatch(playerEntity -> !playerEntity.equals((Object)this.mc.field_71439_g) && playerEntity.func_70032_d((Entity)this.mc.field_71439_g) < 10.0f && EntityUtils.isTeam((EntityLivingBase)playerEntity) && (!playerEntity.func_82150_aj() || playerEntity.field_70163_u - this.mc.field_71439_g.field_70163_u <= 5.0))) {
                    ++this.ticksSeen;
                }
                else {
                    this.ticksSeen = 0;
                }
                final boolean inDwarven = SkyblockUtils.anyTab("Dwarven Mines");
                if ((this.panic.getValue() <= this.ticksSeen && this.panic.getValue() != 0.0) || !inDwarven) {
                    this.setToggled(false);
                    if (OringoClient.aotvReturn.isToggled()) {
                        OringoClient.aotvReturn.start(() -> this.setToggled(true), false);
                    }
                    this.ticksSeen = 0;
                    OringoClient.sendMessageWithPrefix(inDwarven ? ("You have been seen by " + ((EntityPlayer)this.mc.field_71441_e.field_73010_i.stream().filter(playerEntity -> !((EntityPlayer)playerEntity).equals((Object)this.mc.field_71439_g) && ((EntityPlayer)playerEntity).func_70032_d((Entity)this.mc.field_71439_g) < 10.0f && EntityUtils.isTeam(playerEntity)).findFirst().get()).func_70005_c_()) : "Not in dwarven");
                    return;
                }
                if (this.target == null) {
                    if (!this.findTarget()) {
                        OringoClient.sendMessageWithPrefix("No possible target found");
                    }
                    return;
                }
                if (this.mc.field_71476_x != null && this.mc.field_71476_x.field_72313_a == MovingObjectPosition.MovingObjectType.ENTITY) {
                    if (this.ticksTargeting++ == 40) {
                        this.setToggled(false);
                        if (OringoClient.aotvReturn.isToggled()) {
                            OringoClient.aotvReturn.start(() -> this.setToggled(true), false);
                        }
                        return;
                    }
                }
                else {
                    this.ticksTargeting = 0;
                }
                KeyBinding.func_74510_a(Minecraft.func_71410_x().field_71474_y.field_74312_F.func_151463_i(), true);
                if (this.sneak.isEnabled() || this.timeLeft != 0) {
                    KeyBinding.func_74510_a(Minecraft.func_71410_x().field_71474_y.field_74311_E.func_151463_i(), true);
                }
                if (this.mc.field_71476_x.field_72313_a == MovingObjectPosition.MovingObjectType.BLOCK && this.mc.field_71462_r != null && !(this.mc.field_71462_r instanceof GuiContainer) && this.ticks % 2 == 0) {
                    SkyblockUtils.click();
                }
                if (!this.yaw.isEmpty() && (this.stopLoop || !this.isTitanium(this.target))) {
                    this.mc.field_71439_g.field_70177_z = this.yaw.get(0);
                    this.mc.field_71439_g.field_70125_A = this.pitch.get(0);
                    this.yaw.remove(0);
                    this.pitch.remove(0);
                    if (this.yaw.isEmpty() && this.isBlockVisible(this.target) && this.moreMovement.getValue() != 0.0) {
                        this.stopLoop = false;
                        final Vec3 targetRotationTemp = this.targetRotation;
                        this.targetRotation = this.getRandomVisibilityLine(this.target);
                        this.targetRotation2 = this.targetRotation;
                        this.getRotations(false);
                        this.targetRotation = targetRotationTemp;
                        return;
                    }
                    if (this.moreMovement.getValue() == 0.0) {
                        this.targetRotation2 = null;
                    }
                    if (this.stopLoop) {
                        return;
                    }
                }
                if (this.mc.field_71441_e.func_180495_p(this.target).func_177230_c().equals(Blocks.field_150357_h)) {
                    if (!this.findTarget()) {}
                    return;
                }
                if (this.mc.field_71476_x.field_72313_a != MovingObjectPosition.MovingObjectType.BLOCK) {
                    if (!this.findTarget()) {}
                    return;
                }
                final BlockPos pos = this.mc.field_71476_x.func_178782_a();
                if (!pos.equals((Object)this.target)) {
                    if (!this.findTarget()) {}
                    return;
                }
                if (this.quickBreak.getValue() != 0.0 && !this.isTitanium(this.target) && OringoClient.getBlockBreakProgress().values().stream().anyMatch(progress -> progress.func_180246_b().equals((Object)this.target)) && OringoClient.getBlockBreakProgress().values().stream().anyMatch(progress -> progress.func_180246_b().equals((Object)this.target) && progress.func_73106_e() == (int)(this.quickBreak.getValue() * 10.0))) {
                    this.findTarget();
                }
                if (this.ticksMining++ == this.maxBreakTime.getValue()) {
                    OringoClient.sendMessageWithPrefix("Mining one block took too long");
                    this.findTarget();
                }
            }
        }
    }
    
    private void getRotations(final boolean stop) {
        final Vec3 lookVec = this.mc.field_71439_g.func_70040_Z().func_178787_e(this.mc.field_71439_g.func_174824_e(0.0f));
        if (!this.yaw.isEmpty()) {
            this.yaw.clear();
            this.pitch.clear();
        }
        final double max = (this.rotations.getValue() + 1.0) * (stop ? 1.0 : this.moreMovement.getValue());
        for (int i = 0; i < max; ++i) {
            final Vec3 target = new Vec3(lookVec.field_72450_a + (this.targetRotation.field_72450_a - lookVec.field_72450_a) / max * i, lookVec.field_72448_b + (this.targetRotation.field_72448_b - lookVec.field_72448_b) / max * i, lookVec.field_72449_c + (this.targetRotation.field_72449_c - lookVec.field_72449_c) / max * i);
            final Rotation rotation = RotationUtils.getRotations(target);
            this.yaw.add(rotation.getYaw());
            this.pitch.add(rotation.getPitch());
        }
        this.stopLoop = stop;
    }
    
    private boolean findTarget() {
        final ArrayList<BlockPos> blocks = new ArrayList<BlockPos>();
        for (int x = -5; x < 6; ++x) {
            for (int y = -5; y < 6; ++y) {
                for (int z = -5; z < 6; ++z) {
                    blocks.add(new BlockPos(this.mc.field_71439_g.field_70165_t + x, this.mc.field_71439_g.field_70163_u + y, this.mc.field_71439_g.field_70161_v + z));
                }
            }
        }
        final BlockPos sortingCenter = (this.target != null) ? this.target : this.mc.field_71439_g.func_180425_c();
        final BlockPos pos2;
        Optional<BlockPos> any = blocks.stream().filter(pos -> !pos.equals((Object)this.target)).filter(this::matchesMode).filter(pos -> this.mc.field_71439_g.func_70011_f((double)pos.func_177958_n(), (double)(pos.func_177956_o() - this.mc.field_71439_g.func_70047_e()), (double)pos.func_177952_p()) < 5.5).filter(this::isBlockVisible).min(Comparator.comparingDouble(pos -> (this.isTitanium(pos) && this.titanium.isEnabled()) ? 0.0 : this.getDistance(pos, pos2, 0.6)));
        if (any.isPresent()) {
            this.target = any.get();
            this.targetRotation2 = null;
            this.targetRotation = this.getRandomVisibilityLine(any.get());
            this.getRotations(true);
        }
        else {
            final BlockPos pos3;
            any = blocks.stream().filter(pos -> !pos.equals((Object)this.target)).filter(this::matchesAny).filter(pos -> this.mc.field_71439_g.func_70011_f((double)pos.func_177958_n(), (double)(pos.func_177956_o() - this.mc.field_71439_g.func_70047_e()), (double)pos.func_177952_p()) < 5.5).filter(this::isBlockVisible).min(Comparator.comparingDouble(pos -> (this.isTitanium(pos) && this.titanium.isEnabled()) ? 0.0 : this.getDistance(pos, pos3, 0.6)));
            if (any.isPresent()) {
                this.target = any.get();
                this.targetRotation2 = null;
                this.targetRotation = this.getRandomVisibilityLine(any.get());
                this.getRotations(true);
            }
        }
        this.ticksMining = 0;
        return any.isPresent();
    }
    
    private double getDistance(final BlockPos pos1, final BlockPos pos2, final double multiY) {
        final double deltaX = pos1.func_177958_n() - pos2.func_177958_n();
        final double deltaY = (pos1.func_177956_o() - pos2.func_177956_o()) * multiY;
        final double deltaZ = pos1.func_177952_p() - pos2.func_177952_p();
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
    }
    
    private boolean isBlockVisible(final BlockPos pos) {
        return this.getRandomVisibilityLine(pos) != null;
    }
    
    private Vec3 getRandomVisibilityLine(final BlockPos pos) {
        final List<Vec3> lines = new ArrayList<Vec3>();
        for (int x = 0; x < this.accuracyChecks.getValue(); ++x) {
            for (int y = 0; y < this.accuracyChecks.getValue(); ++y) {
                for (int z = 0; z < this.accuracyChecks.getValue(); ++z) {
                    final Vec3 target = new Vec3(pos.func_177958_n() + x / this.accuracyChecks.getValue(), pos.func_177956_o() + y / this.accuracyChecks.getValue(), pos.func_177952_p() + z / this.accuracyChecks.getValue());
                    this.test = new BlockPos(target.field_72450_a, target.field_72448_b, target.field_72449_c);
                    final MovingObjectPosition movingObjectPosition = this.mc.field_71441_e.func_147447_a(this.mc.field_71439_g.func_174824_e(0.0f), target, true, false, true);
                    if (movingObjectPosition != null) {
                        final BlockPos obj = movingObjectPosition.func_178782_a();
                        if (obj.equals((Object)this.test) && this.mc.field_71439_g.func_70011_f(target.field_72450_a, target.field_72448_b - this.mc.field_71439_g.func_70047_e(), target.field_72449_c) < 4.5 && (this.under.isEnabled() || Math.abs(this.mc.field_71439_g.field_70163_u - target.field_72448_b) > 1.3)) {
                            lines.add(target);
                        }
                    }
                }
            }
        }
        return lines.isEmpty() ? null : lines.get(new Random().nextInt(lines.size()));
    }
    
    private boolean isTitanium(final BlockPos pos) {
        final IBlockState state = this.mc.field_71441_e.func_180495_p(pos);
        return state.func_177230_c() == Blocks.field_150348_b && ((BlockStone.EnumType)state.func_177229_b((IProperty)BlockStone.field_176247_a)).equals((Object)BlockStone.EnumType.DIORITE_SMOOTH);
    }
    
    private boolean matchesMode(final BlockPos pos) {
        final IBlockState state = this.mc.field_71441_e.func_180495_p(pos);
        if (this.isTitanium(pos)) {
            return true;
        }
        final String selected = this.mode.getSelected();
        switch (selected) {
            case "Clay": {
                return state.func_177230_c().equals(Blocks.field_150406_ce) || (state.func_177230_c().equals(Blocks.field_150325_L) && ((EnumDyeColor)state.func_177229_b((IProperty)BlockColored.field_176581_a)).equals((Object)EnumDyeColor.GRAY));
            }
            case "Prismarine": {
                return state.func_177230_c().equals(Blocks.field_180397_cI);
            }
            case "Wool": {
                return state.func_177230_c().equals(Blocks.field_150325_L) && ((EnumDyeColor)state.func_177229_b((IProperty)BlockColored.field_176581_a)).equals((Object)EnumDyeColor.LIGHT_BLUE);
            }
            case "Blue": {
                return (state.func_177230_c().equals(Blocks.field_150325_L) && ((EnumDyeColor)state.func_177229_b((IProperty)BlockColored.field_176581_a)).equals((Object)EnumDyeColor.LIGHT_BLUE)) || state.func_177230_c().equals(Blocks.field_180397_cI);
            }
            case "Gold": {
                return state.func_177230_c().equals(Blocks.field_150340_R);
            }
            default: {
                return false;
            }
        }
    }
    
    private boolean matchesAny(final BlockPos pos) {
        final IBlockState state = this.mc.field_71441_e.func_180495_p(pos);
        return (state.func_177230_c().equals(Blocks.field_150325_L) && state.func_177228_b().entrySet().stream().anyMatch(entry -> entry.toString().contains("lightBlue"))) || state.func_177230_c().equals(Blocks.field_180397_cI) || state.func_177230_c().equals(Blocks.field_150406_ce) || (state.func_177230_c().equals(Blocks.field_150325_L) && state.func_177228_b().entrySet().stream().anyMatch(entry -> entry.toString().contains("gray"))) || this.isTitanium(pos);
    }
}
