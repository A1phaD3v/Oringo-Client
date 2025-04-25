// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.skyblock;

import me.oringo.oringoclient.utils.EntityUtils;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import java.util.Iterator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.ToDoubleFunction;
import java.util.Comparator;
import java.util.List;
import me.oringo.oringoclient.utils.PlayerUtils;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.oringo.oringoclient.utils.Rotation;
import net.minecraft.entity.Entity;
import me.oringo.oringoclient.utils.RotationUtils;
import me.oringo.oringoclient.utils.SkyblockUtils;
import me.oringo.oringoclient.qolfeatures.module.impl.combat.KillAura;
import me.oringo.oringoclient.events.MotionUpdateEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import java.util.ArrayList;
import net.minecraft.entity.EntityLivingBase;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.StringSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.ModeSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class TerminatorAura extends Module
{
    public NumberSetting range;
    public NumberSetting delay;
    public ModeSetting mode;
    public ModeSetting button;
    public BooleanSetting bossLock;
    public BooleanSetting inDungeon;
    public BooleanSetting teamCheck;
    public StringSetting customItem;
    public static EntityLivingBase target;
    private static boolean attack;
    private static ArrayList<EntityLivingBase> attackedMobs;
    
    public TerminatorAura() {
        super("Terminator Aura", 0, Category.SKYBLOCK);
        this.range = new NumberSetting("Range", 15.0, 5.0, 30.0, 1.0);
        this.delay = new NumberSetting("Use delay", 3.0, 1.0, 10.0, 1.0);
        this.mode = new ModeSetting("Mode", "Swap", new String[] { "Swap", "Held" });
        this.button = new ModeSetting("Mouse", "Right", new String[] { "Left", "Right" });
        this.bossLock = new BooleanSetting("Boss Lock", true);
        this.inDungeon = new BooleanSetting("only Dungeon", true);
        this.teamCheck = new BooleanSetting("Teamcheck", false);
        this.customItem = new StringSetting("Custom Item");
        this.addSettings(this.delay, this.range, this.button, this.mode, this.customItem, this.bossLock, this.inDungeon, this.teamCheck);
    }
    
    @SubscribeEvent(priority = EventPriority.LOW)
    public void onUpdate(final MotionUpdateEvent.Pre event) {
        if (KillAura.target != null || Aimbot.attack || !this.isToggled() || TerminatorAura.mc.field_71439_g.field_70173_aa % this.delay.getValue() != 0.0 || (!SkyblockUtils.inDungeon && this.inDungeon.isEnabled())) {
            return;
        }
        boolean hasTerm = TerminatorAura.mc.field_71439_g.func_70694_bm() != null && (TerminatorAura.mc.field_71439_g.func_70694_bm().func_82833_r().contains("Juju") || TerminatorAura.mc.field_71439_g.func_70694_bm().func_82833_r().contains("Terminator") || (!this.customItem.getValue().equals("") && TerminatorAura.mc.field_71439_g.func_70694_bm().func_82833_r().contains(this.customItem.getValue())));
        if (this.mode.getSelected().equals("Swap")) {
            for (int i = 0; i < 9; ++i) {
                if (TerminatorAura.mc.field_71439_g.field_71071_by.func_70301_a(i) != null && (TerminatorAura.mc.field_71439_g.field_71071_by.func_70301_a(i).func_82833_r().contains("Juju") || TerminatorAura.mc.field_71439_g.field_71071_by.func_70301_a(i).func_82833_r().contains("Terminator") || (!this.customItem.is("") && TerminatorAura.mc.field_71439_g.field_71071_by.func_70301_a(i).func_82833_r().contains(this.customItem.getValue())))) {
                    hasTerm = true;
                    break;
                }
            }
        }
        if (!hasTerm) {
            return;
        }
        TerminatorAura.target = this.getTarget(TerminatorAura.target);
        if (TerminatorAura.target != null) {
            TerminatorAura.attack = true;
            final Rotation angles = RotationUtils.getBowRotation((Entity)TerminatorAura.target);
            event.yaw = angles.getYaw();
            event.pitch = angles.getPitch();
        }
    }
    
    @SubscribeEvent
    public void onUpdatePost(final MotionUpdateEvent.Post event) {
        if (!TerminatorAura.attack) {
            return;
        }
        final int held = TerminatorAura.mc.field_71439_g.field_71071_by.field_70461_c;
        if (this.mode.getSelected().equals("Swap")) {
            for (int i = 0; i < 9; ++i) {
                if (TerminatorAura.mc.field_71439_g.field_71071_by.func_70301_a(i) != null && (TerminatorAura.mc.field_71439_g.field_71071_by.func_70301_a(i).func_82833_r().contains("Juju") || TerminatorAura.mc.field_71439_g.field_71071_by.func_70301_a(i).func_82833_r().contains("Terminator") || (!this.customItem.is("") && TerminatorAura.mc.field_71439_g.field_71071_by.func_70301_a(i).func_82833_r().contains(this.customItem.getValue())))) {
                    TerminatorAura.mc.field_71439_g.field_71071_by.field_70461_c = i;
                    break;
                }
            }
        }
        PlayerUtils.syncHeldItem();
        this.click();
        TerminatorAura.mc.field_71439_g.field_71071_by.field_70461_c = held;
        PlayerUtils.syncHeldItem();
        TerminatorAura.attack = false;
    }
    
    private EntityLivingBase getTarget(final EntityLivingBase lastTarget) {
        if (this.bossLock.isEnabled() && lastTarget != null && SkyblockUtils.isMiniboss((Entity)lastTarget) && lastTarget.func_110143_aJ() > 0.0f && !lastTarget.field_70128_L && lastTarget.func_70685_l((Entity)TerminatorAura.mc.field_71439_g) && lastTarget.func_70032_d((Entity)TerminatorAura.mc.field_71439_g) < this.range.getValue()) {
            return lastTarget;
        }
        final List<Entity> validTargets = (List<Entity>)TerminatorAura.mc.field_71441_e.func_72910_y().stream().filter(entity -> entity instanceof EntityLivingBase).filter(entity -> this.isValid(entity)).sorted(Comparator.comparingDouble((ToDoubleFunction<? super T>)TerminatorAura.mc.field_71439_g::func_70032_d)).sorted(Comparator.comparing(entity -> RotationUtils.getRotationDifference(RotationUtils.getRotations((lastTarget != null) ? lastTarget : entity), RotationUtils.getRotations(entity))).reversed()).collect(Collectors.toList());
        final Iterator<Entity> iterator = validTargets.iterator();
        if (iterator.hasNext()) {
            final Entity entity2 = iterator.next();
            TerminatorAura.attackedMobs.add((EntityLivingBase)entity2);
            final Object o;
            new Thread(() -> {
                try {
                    Thread.sleep(350L);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                TerminatorAura.attackedMobs.remove(o);
                return;
            }).start();
            return (EntityLivingBase)entity2;
        }
        return null;
    }
    
    private void click() {
        final String selected = this.button.getSelected();
        switch (selected) {
            case "Left": {
                TerminatorAura.mc.field_71439_g.func_71038_i();
                break;
            }
            case "Right": {
                TerminatorAura.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C08PacketPlayerBlockPlacement(TerminatorAura.mc.field_71439_g.func_70694_bm()));
                break;
            }
        }
    }
    
    private boolean isValid(final EntityLivingBase entity) {
        return entity != TerminatorAura.mc.field_71439_g && !(entity instanceof EntityArmorStand) && TerminatorAura.mc.field_71439_g.func_70685_l((Entity)entity) && entity.func_110143_aJ() > 0.0f && entity.func_70032_d((Entity)TerminatorAura.mc.field_71439_g) <= this.range.getValue() && ((!(entity instanceof EntityPlayer) && !(entity instanceof EntityBat) && !(entity instanceof EntityZombie) && !(entity instanceof EntityGiantZombie)) || !entity.func_82150_aj()) && !entity.func_70005_c_().equals("Dummy") && !entity.func_70005_c_().startsWith("Decoy") && !TerminatorAura.attackedMobs.contains(entity) && !(entity instanceof EntityBlaze) && (!EntityUtils.isTeam(entity) || !this.teamCheck.isEnabled());
    }
    
    static {
        TerminatorAura.attackedMobs = new ArrayList<EntityLivingBase>();
    }
}
