// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.combat;

import java.util.ArrayList;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import me.oringo.oringoclient.utils.PacketUtils;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.entity.passive.EntityVillager;
import me.oringo.oringoclient.utils.EntityUtils;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.monster.EntityMob;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Comparator;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import me.oringo.oringoclient.events.PacketReceivedEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import me.oringo.oringoclient.qolfeatures.module.impl.skyblock.AntiNukebi;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemArmor;
import me.oringo.oringoclient.events.MoveFlyingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import me.oringo.oringoclient.utils.Rotation;
import me.oringo.oringoclient.utils.PlayerUtils;
import net.minecraft.item.ItemSpade;
import net.minecraft.entity.player.EntityPlayer;
import me.oringo.oringoclient.utils.MathUtil;
import java.util.Random;
import me.oringo.oringoclient.utils.RotationUtils;
import net.minecraft.item.ItemSword;
import me.oringo.oringoclient.qolfeatures.module.impl.skyblock.Aimbot;
import me.oringo.oringoclient.qolfeatures.module.impl.movement.Scaffold;
import me.oringo.oringoclient.OringoClient;
import me.oringo.oringoclient.qolfeatures.module.impl.player.AntiVoid;
import me.oringo.oringoclient.events.MotionUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.oringo.oringoclient.ui.notifications.Notifications;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.item.EntityArmorStand;
import org.lwjgl.input.Mouse;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.utils.MilliTimer;
import java.util.List;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.ModeSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import net.minecraft.entity.EntityLivingBase;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class KillAura extends Module
{
    public static EntityLivingBase target;
    public BooleanSetting namesOnly;
    public BooleanSetting middleClick;
    public BooleanSetting players;
    public BooleanSetting mobs;
    public BooleanSetting walls;
    public BooleanSetting teams;
    public BooleanSetting toggleOnLoad;
    public BooleanSetting toggleInGui;
    public BooleanSetting onlySword;
    public BooleanSetting movementFix;
    public BooleanSetting rotationSwing;
    public BooleanSetting shovelSwap;
    public BooleanSetting attackOnly;
    public BooleanSetting invisibles;
    public ModeSetting sorting;
    public ModeSetting rotationMode;
    public ModeSetting blockMode;
    public ModeSetting namesonlyMode;
    public ModeSetting mode;
    public NumberSetting range;
    public NumberSetting rotationRange;
    public NumberSetting fov;
    public NumberSetting maxRotation;
    public NumberSetting minRotation;
    public NumberSetting maxCps;
    public NumberSetting minCps;
    public NumberSetting smoothing;
    public NumberSetting switchDelay;
    public static List<String> names;
    private boolean wasDown;
    private boolean isBlocking;
    private int nextCps;
    private int lastSlot;
    private int targetIndex;
    private int attacks;
    private MilliTimer lastAttack;
    private MilliTimer switchDelayTimer;
    private MilliTimer blockDelay;
    public static final MilliTimer DISABLE;
    
    public KillAura() {
        super("Kill Aura", 0, Category.COMBAT);
        this.namesOnly = new BooleanSetting("Names only", false);
        this.middleClick = new BooleanSetting("Middle click to add", false);
        this.players = new BooleanSetting("Players", false);
        this.mobs = new BooleanSetting("Mobs", true);
        this.walls = new BooleanSetting("Through walls", true);
        this.teams = new BooleanSetting("Teams", true);
        this.toggleOnLoad = new BooleanSetting("Disable on join", true);
        this.toggleInGui = new BooleanSetting("No containers", true);
        this.onlySword = new BooleanSetting("Only swords", false);
        this.movementFix = new BooleanSetting("Movement fix", false);
        this.rotationSwing = new BooleanSetting("Swing on rotation", false);
        this.shovelSwap = new BooleanSetting("Shovel swap", false);
        this.attackOnly = new BooleanSetting("Click only", false);
        this.invisibles = new BooleanSetting("Invisibles", false);
        this.sorting = new ModeSetting("Sorting", "Distance", new String[] { "Distance", "Health", "Hurt", "Hp reverse" });
        this.rotationMode = new ModeSetting("Rotation mode", "Simple", new String[] { "Simple", "Smooth", "None" });
        this.blockMode = new ModeSetting("Autoblock", "None", new String[] { "Vanilla", "Hypixel", "Fake", "None" });
        this.namesonlyMode = new ModeSetting("Names mode", "Enemies", new String[] { "Friends", "Enemies" });
        this.mode = new ModeSetting("Mode", "Single", new String[] { "Single", "Switch" });
        this.range = new NumberSetting("Range", 4.2, 2.0, 6.0, 0.1) {
            @Override
            public void setValue(final double value) {
                super.setValue(value);
                if (this.getValue() > KillAura.this.rotationRange.getValue()) {
                    this.setValue(KillAura.this.rotationRange.getValue());
                }
            }
        };
        this.rotationRange = new NumberSetting("Rotation Range", 6.0, 2.0, 12.0, 0.1) {
            @Override
            public void setValue(final double value) {
                super.setValue(value);
                if (this.getValue() < KillAura.this.range.getValue()) {
                    this.setValue(KillAura.this.range.getValue());
                }
            }
        };
        this.fov = new NumberSetting("Fov", 360.0, 30.0, 360.0, 1.0);
        this.maxRotation = new NumberSetting("Max rotation", 100.0, 10.0, 180.0, 0.1) {
            @Override
            public boolean isHidden() {
                return !KillAura.this.rotationMode.is("Simple");
            }
            
            @Override
            public void setValue(final double value) {
                super.setValue(value);
                if (KillAura.this.minRotation.getValue() > this.getValue()) {
                    this.setValue(KillAura.this.minRotation.getValue());
                }
            }
        };
        this.minRotation = new NumberSetting("Min rotation", 60.0, 5.0, 180.0, 0.1) {
            @Override
            public boolean isHidden() {
                return !KillAura.this.rotationMode.is("Simple");
            }
            
            @Override
            public void setValue(final double value) {
                super.setValue(value);
                if (this.getValue() > KillAura.this.maxRotation.getValue()) {
                    this.setValue(KillAura.this.maxRotation.getValue());
                }
            }
        };
        this.maxCps = new NumberSetting("Max CPS", 13.0, 1.0, 20.0, 1.0) {
            @Override
            public void setValue(final double value) {
                super.setValue(value);
                if (KillAura.this.minCps.getValue() > this.getValue()) {
                    this.setValue(KillAura.this.minCps.getValue());
                }
            }
        };
        this.minCps = new NumberSetting("Min CPS", 11.0, 1.0, 20.0, 1.0) {
            @Override
            public void setValue(final double value) {
                super.setValue(value);
                if (KillAura.this.maxCps.getValue() < this.getValue()) {
                    this.setValue(KillAura.this.maxCps.getValue());
                }
            }
        };
        this.smoothing = new NumberSetting("Smoothing", 12.0, 1.0, 20.0, 0.1) {
            @Override
            public boolean isHidden() {
                return !KillAura.this.rotationMode.is("Smooth");
            }
        };
        this.switchDelay = new NumberSetting("Switch delay", 100.0, 0.0, 250.0, 1.0, aBoolean -> !this.mode.is("Switch"));
        this.nextCps = 10;
        this.lastSlot = -1;
        this.lastAttack = new MilliTimer();
        this.switchDelayTimer = new MilliTimer();
        this.blockDelay = new MilliTimer();
        this.addSettings(this.mode, this.switchDelay, this.range, this.rotationRange, this.minCps, this.maxCps, this.sorting, this.rotationMode, this.smoothing, this.maxRotation, this.minRotation, this.fov, this.blockMode, this.players, this.mobs, this.invisibles, this.teams, this.rotationSwing, this.movementFix, this.namesOnly, this.namesonlyMode, this.middleClick, this.attackOnly, this.walls, this.toggleInGui, this.toggleOnLoad, this.onlySword, this.shovelSwap);
    }
    
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (KillAura.mc.field_71439_g == null || KillAura.mc.field_71441_e == null || !this.middleClick.isEnabled()) {
            return;
        }
        if (Mouse.isButtonDown(2) && KillAura.mc.field_71462_r == null) {
            if (KillAura.mc.field_147125_j != null && !this.wasDown && !(KillAura.mc.field_147125_j instanceof EntityArmorStand) && KillAura.mc.field_147125_j instanceof EntityLivingBase) {
                final String name = ChatFormatting.stripFormatting(KillAura.mc.field_147125_j.func_70005_c_());
                if (!KillAura.names.contains(name)) {
                    KillAura.names.add(name);
                    Notifications.showNotification("Oringo Client", "Added " + ChatFormatting.AQUA + name + ChatFormatting.RESET + " to name sorting", 2000);
                }
                else {
                    KillAura.names.remove(name);
                    Notifications.showNotification("Oringo Client", "Removed " + ChatFormatting.AQUA + name + ChatFormatting.RESET + " from name sorting", 2000);
                }
            }
            this.wasDown = true;
        }
        else {
            this.wasDown = false;
        }
    }
    
    @Override
    public void onEnable() {
        this.attacks = 0;
    }
    
    @Override
    public void onDisable() {
        KillAura.target = null;
        this.isBlocking = false;
    }
    
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onMovePre(final MotionUpdateEvent.Pre event) {
        if (AntiVoid.isBlinking() || (OringoClient.scaffold.isToggled() && Scaffold.disableAura.isEnabled()) || !KillAura.DISABLE.hasTimePassed(100L) || !this.isToggled() || Aimbot.attack || (this.onlySword.isEnabled() && (KillAura.mc.field_71439_g.func_70694_bm() == null || !(KillAura.mc.field_71439_g.func_70694_bm().func_77973_b() instanceof ItemSword)))) {
            KillAura.target = null;
            return;
        }
        KillAura.target = this.getTarget();
        if (this.attackOnly.isEnabled() && !KillAura.mc.field_71474_y.field_74312_F.func_151470_d()) {
            return;
        }
        if (KillAura.target != null) {
            final Rotation angles = RotationUtils.getRotations(KillAura.target, 0.2f);
            if (!OringoClient.speed.isToggled()) {
                final String selected = this.rotationMode.getSelected();
                switch (selected) {
                    case "Smooth": {
                        event.setRotation(RotationUtils.getSmoothRotation(RotationUtils.getLastReportedRotation(), angles, (float)this.smoothing.getValue()));
                        break;
                    }
                    case "Simple": {
                        event.setRotation(RotationUtils.getLimitedRotation(RotationUtils.getLastReportedRotation(), angles, (float)(this.minRotation.getValue() + Math.abs(this.maxRotation.getValue() - this.minRotation.getValue()) * new Random().nextFloat())));
                        break;
                    }
                }
            }
            event.setPitch(MathUtil.clamp(event.pitch, 90.0f, -90.0f));
            final String selected2 = this.blockMode.getSelected();
            switch (selected2) {
            }
            if (this.shovelSwap.isEnabled() && KillAura.target instanceof EntityPlayer && this.hasDiamondArmor((EntityPlayer)KillAura.target)) {
                this.lastSlot = KillAura.mc.field_71439_g.field_71071_by.field_70461_c;
                for (int i = 0; i < 9; ++i) {
                    if (KillAura.mc.field_71439_g.field_71071_by.func_70301_a(i) != null && KillAura.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() instanceof ItemSpade) {
                        PlayerUtils.swapToSlot(i);
                        this.isBlocking = false;
                        break;
                    }
                }
            }
        }
    }
    
    @SubscribeEvent
    public void onMoveFlying(final MoveFlyingEvent event) {
        if (this.isToggled() && KillAura.target != null && this.movementFix.isEnabled()) {
            event.setYaw(RotationUtils.getRotations(KillAura.target).getYaw());
        }
    }
    
    private boolean hasDiamondArmor(final EntityPlayer player) {
        for (int i = 1; i < 5; ++i) {
            if (player.func_71124_b(i) != null && player.func_71124_b(i).func_77973_b() instanceof ItemArmor && ((ItemArmor)player.func_71124_b(i).func_77973_b()).func_82812_d() == ItemArmor.ArmorMaterial.DIAMOND) {
                return true;
            }
        }
        return false;
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onMovePost(final MotionUpdateEvent.Post event) {
        if (this.attackOnly.isEnabled() && !KillAura.mc.field_71474_y.field_74312_F.func_151470_d()) {
            this.attacks = 0;
            return;
        }
        if (KillAura.target != null && KillAura.mc.field_71439_g.func_70032_d((Entity)KillAura.target) < Math.max(this.rotationRange.getValue(), this.range.getValue()) && this.attacks > 0) {
            final String selected = this.blockMode.getSelected();
            switch (selected) {
                case "None":
                case "Fake": {}
                case "Vanilla": {
                    this.stopBlocking();
                    break;
                }
            }
            while (this.attacks > 0) {
                KillAura.mc.field_71439_g.func_71038_i();
                if (KillAura.mc.field_71439_g.func_70032_d((Entity)KillAura.target) < this.range.getValue() && (RotationUtils.getRotationDifference(RotationUtils.getRotations(KillAura.target), RotationUtils.getLastReportedRotation()) < 15.0 || this.rotationMode.is("None") || OringoClient.speed.isToggled() || (AntiNukebi.currentNukebi != null && AntiNukebi.attack.isEnabled()))) {
                    KillAura.mc.field_71442_b.func_78764_a((EntityPlayer)KillAura.mc.field_71439_g, (Entity)KillAura.target);
                    if (this.switchDelayTimer.hasTimePassed((long)this.switchDelay.getValue())) {
                        ++this.targetIndex;
                        this.switchDelayTimer.reset();
                    }
                }
                --this.attacks;
            }
            if (KillAura.mc.field_71439_g.func_70694_bm() != null && KillAura.mc.field_71439_g.func_70694_bm().func_77973_b() instanceof ItemSword) {
                final String selected2 = this.blockMode.getSelected();
                switch (selected2) {
                    case "Vanilla": {
                        if (!this.isBlocking) {
                            this.startBlocking();
                            break;
                        }
                        break;
                    }
                    case "Hypixel": {
                        if (this.blockDelay.hasTimePassed(250L)) {
                            this.startBlocking();
                            KillAura.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new C0BPacketEntityAction((Entity)KillAura.mc.field_71439_g, C0BPacketEntityAction.Action.STOP_SPRINTING));
                            KillAura.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new C0BPacketEntityAction((Entity)KillAura.mc.field_71439_g, C0BPacketEntityAction.Action.START_SPRINTING));
                            this.blockDelay.reset();
                            break;
                        }
                        break;
                    }
                }
            }
        }
        else {
            this.attacks = 0;
        }
        if (this.shovelSwap.isEnabled() && this.lastSlot != -1) {
            PlayerUtils.swapToSlot(this.lastSlot);
            this.lastSlot = -1;
        }
    }
    
    @SubscribeEvent(receiveCanceled = true)
    public void onPacket(final PacketReceivedEvent event) {
        if (event.packet instanceof S08PacketPlayerPosLook) {
            KillAura.DISABLE.reset();
        }
    }
    
    @SubscribeEvent
    public void onRender(final RenderWorldLastEvent event) {
        if (this.isToggled() && KillAura.target != null && this.lastAttack.hasTimePassed(1000 / this.nextCps) && KillAura.mc.field_71439_g.func_70032_d((Entity)KillAura.target) < (this.rotationSwing.isEnabled() ? this.getRotationRange() : this.range.getValue())) {
            this.nextCps = (int)(this.minCps.getValue() + Math.abs(this.maxCps.getValue() - this.minCps.getValue()) * new Random().nextFloat());
            this.lastAttack.reset();
            ++this.attacks;
        }
    }
    
    private EntityLivingBase getTarget() {
        if ((KillAura.mc.field_71462_r instanceof GuiContainer && this.toggleInGui.isEnabled()) || KillAura.mc.field_71441_e == null) {
            return null;
        }
        final List<Entity> validTargets = (List<Entity>)KillAura.mc.field_71441_e.func_72910_y().stream().filter(entity -> entity instanceof EntityLivingBase).filter(entity -> this.isValid(entity)).sorted(Comparator.comparingDouble(e -> e.func_70032_d((Entity)KillAura.mc.field_71439_g))).collect(Collectors.toList());
        final String selected = this.sorting.getSelected();
        switch (selected) {
            case "Health": {
                validTargets.sort(Comparator.comparingDouble(e -> e.func_110143_aJ()));
                break;
            }
            case "Hurt": {
                validTargets.sort(Comparator.comparing(e -> e.field_70737_aN));
                break;
            }
            case "Hp reverse": {
                validTargets.sort(Comparator.comparingDouble(e -> e.func_110143_aJ()).reversed());
                break;
            }
        }
        if (!validTargets.isEmpty()) {
            if (this.targetIndex >= validTargets.size()) {
                this.targetIndex = 0;
            }
            final String selected2 = this.mode.getSelected();
            switch (selected2) {
                case "Switch": {
                    return (EntityLivingBase)validTargets.get(this.targetIndex);
                }
                case "Single": {
                    return (EntityLivingBase)validTargets.get(0);
                }
            }
        }
        return null;
    }
    
    private boolean isValid(final EntityLivingBase entity) {
        if (entity == KillAura.mc.field_71439_g || !AntiBot.isValidEntity((Entity)entity) || (!this.invisibles.isEnabled() && entity.func_82150_aj()) || entity instanceof EntityArmorStand || (!KillAura.mc.field_71439_g.func_70685_l((Entity)entity) && !this.walls.isEnabled()) || entity.func_110143_aJ() <= 0.0f || entity.func_70032_d((Entity)KillAura.mc.field_71439_g) > ((KillAura.target != null && KillAura.target != entity) ? this.range.getValue() : Math.max(this.rotationRange.getValue(), this.range.getValue())) || RotationUtils.getRotationDifference(RotationUtils.getRotations(entity), RotationUtils.getPlayerRotation()) > this.fov.getValue()) {
            return false;
        }
        if (this.namesOnly.isEnabled()) {
            final boolean flag = KillAura.names.contains(ChatFormatting.stripFormatting(entity.func_70005_c_()));
            if (this.namesonlyMode.is("Enemies") || flag) {
                return this.namesonlyMode.is("Enemies") && flag;
            }
        }
        return ((!(entity instanceof EntityMob) && !(entity instanceof EntityAmbientCreature) && !(entity instanceof EntityWaterMob) && !(entity instanceof EntityAnimal) && !(entity instanceof EntitySlime)) || this.mobs.isEnabled()) && (!(entity instanceof EntityPlayer) || ((!EntityUtils.isTeam(entity) || !this.teams.isEnabled()) && this.players.isEnabled())) && !(entity instanceof EntityVillager);
    }
    
    private double getRotationRange() {
        return Math.max(this.rotationRange.getValue(), this.range.getValue());
    }
    
    private void startBlocking() {
        PacketUtils.sendPacketNoEvent((Packet<?>)new C08PacketPlayerBlockPlacement(KillAura.mc.field_71439_g.func_70694_bm()));
        this.isBlocking = true;
    }
    
    private void stopBlocking() {
        if (this.isBlocking) {
            KillAura.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.field_177992_a, EnumFacing.DOWN));
            this.isBlocking = false;
        }
    }
    
    @SubscribeEvent
    public void onWorldLoad(final WorldEvent.Load event) {
        if (this.isToggled() && this.toggleOnLoad.isEnabled()) {
            this.toggle();
        }
    }
    
    static {
        KillAura.names = new ArrayList<String>();
        DISABLE = new MilliTimer();
    }
}
