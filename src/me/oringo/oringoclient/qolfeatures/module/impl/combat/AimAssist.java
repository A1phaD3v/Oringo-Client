// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.combat;

import net.minecraft.util.Vec3;
import me.oringo.oringoclient.utils.RotationUtils;
import me.oringo.oringoclient.utils.PlayerUtils;
import net.minecraft.entity.passive.EntityVillager;
import me.oringo.oringoclient.utils.EntityUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.item.EntityArmorStand;
import java.util.List;
import java.util.Comparator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.oringo.oringoclient.utils.Rotation;
import net.minecraft.entity.Entity;
import me.oringo.oringoclient.utils.MathUtil;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class AimAssist extends Module
{
    public NumberSetting fov;
    public NumberSetting speed;
    public NumberSetting minSpeed;
    public NumberSetting range;
    public BooleanSetting vertical;
    public BooleanSetting players;
    public BooleanSetting mobs;
    public BooleanSetting invisibles;
    public BooleanSetting teams;
    
    public AimAssist() {
        super("Aim Assist", Category.COMBAT);
        this.fov = new NumberSetting("Fov", 60.0, 30.0, 180.0, 1.0);
        this.speed = new NumberSetting("Max speed", 30.0, 1.0, 40.0, 0.1) {
            @Override
            public void setValue(final double value) {
                super.setValue(value);
                if (value < AimAssist.this.minSpeed.getValue()) {
                    this.setValue(AimAssist.this.minSpeed.getValue());
                }
            }
        };
        this.minSpeed = new NumberSetting("Min speed", 20.0, 1.0, 40.0, 0.1) {
            @Override
            public void setValue(final double value) {
                super.setValue(value);
                if (this.getValue() > AimAssist.this.speed.getValue()) {
                    this.setValue(AimAssist.this.speed.getValue());
                }
            }
        };
        this.range = new NumberSetting("Range", 5.0, 0.0, 6.0, 0.1);
        this.vertical = new BooleanSetting("Vertical", true);
        this.players = new BooleanSetting("Players", true);
        this.mobs = new BooleanSetting("Mobs", false);
        this.invisibles = new BooleanSetting("Invisibles", false);
        this.teams = new BooleanSetting("Teams", true);
        this.addSettings(this.fov, this.range, this.minSpeed, this.speed, this.players, this.mobs, this.teams, this.invisibles, this.vertical);
    }
    
    @SubscribeEvent
    public void onRender(final RenderWorldLastEvent event) {
        if (this.isToggled()) {
            final Entity target = this.getTarget();
            if (target != null && AimAssist.mc.field_71476_x != null && AimAssist.mc.field_71476_x.field_72308_g != target) {
                final Rotation rotation = this.getRotation(target);
                final float yaw = AimAssist.mc.field_71439_g.field_70177_z + MathHelper.func_76142_g(rotation.getYaw() - AimAssist.mc.field_71439_g.field_70177_z);
                final float pitch = AimAssist.mc.field_71439_g.field_70125_A + MathHelper.func_76142_g(rotation.getPitch() - AimAssist.mc.field_71439_g.field_70125_A);
                final float diffY = (float)((yaw - AimAssist.mc.field_71439_g.field_70177_z) / MathUtil.getRandomInRange(this.speed.getValue(), this.minSpeed.getValue()));
                final float diffP = (float)((pitch - AimAssist.mc.field_71439_g.field_70125_A) / MathUtil.getRandomInRange(this.speed.getValue(), this.minSpeed.getValue()));
                AimAssist.mc.field_71439_g.field_70177_z += diffY;
                if (this.vertical.isEnabled()) {
                    AimAssist.mc.field_71439_g.field_70125_A += diffP;
                }
            }
        }
    }
    
    public Entity getTarget() {
        final List<Entity> validEntities = (List<Entity>)AimAssist.mc.field_71441_e.func_175644_a((Class)EntityLivingBase.class, entity -> this.isValid((EntityLivingBase)entity));
        validEntities.sort(Comparator.comparingDouble(entity -> AimAssist.mc.field_71439_g.func_70032_d(entity)));
        if (!validEntities.isEmpty()) {
            return validEntities.get(0);
        }
        return null;
    }
    
    private boolean isValid(final EntityLivingBase entity) {
        return entity != AimAssist.mc.field_71439_g && AntiBot.isValidEntity((Entity)entity) && (this.invisibles.isEnabled() || !entity.func_82150_aj()) && !(entity instanceof EntityArmorStand) && AimAssist.mc.field_71439_g.func_70685_l((Entity)entity) && entity.func_110143_aJ() > 0.0f && entity.func_70032_d((Entity)AimAssist.mc.field_71439_g) <= this.range.getValue() && Math.abs(MathHelper.func_76142_g(AimAssist.mc.field_71439_g.field_70177_z) - MathHelper.func_76142_g(this.getRotation((Entity)entity).getYaw())) <= this.fov.getValue() && ((!(entity instanceof EntityMob) && !(entity instanceof EntityAmbientCreature) && !(entity instanceof EntityWaterMob) && !(entity instanceof EntityAnimal) && !(entity instanceof EntitySlime)) || this.mobs.isEnabled()) && (!(entity instanceof EntityPlayer) || ((!EntityUtils.isTeam(entity) || !this.teams.isEnabled()) && this.players.isEnabled())) && !(entity instanceof EntityVillager);
    }
    
    private Rotation getRotation(final Entity entity) {
        if (entity != null) {
            final Vec3 vec3 = AimAssist.mc.field_71439_g.func_174824_e(1.0f);
            final Vec3 vec4 = PlayerUtils.getVectorForRotation(AimAssist.mc.field_71439_g.field_70177_z, AimAssist.mc.field_71439_g.field_70125_A);
            final Vec3 vec5 = vec3.func_72441_c(vec4.field_72450_a, vec4.field_72448_b, vec4.field_72449_c);
            return RotationUtils.getRotations(RotationUtils.getClosestPointInAABB(vec5, entity.func_174813_aQ()));
        }
        return null;
    }
}
