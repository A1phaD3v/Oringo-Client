// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.mixins.entity;

import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.stats.AchievementList;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.block.Block;
import java.util.Iterator;
import java.util.List;
import net.minecraft.util.ReportedException;
import net.minecraft.crash.CrashReport;
import net.minecraft.init.Blocks;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockWall;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.Material;
import me.oringo.oringoclient.events.StepEvent;
import net.minecraft.util.AxisAlignedBB;
import me.oringo.oringoclient.events.MoveEvent;
import me.oringo.oringoclient.events.MoveHeadingEvent;
import me.oringo.oringoclient.events.PlayerUpdateEvent;
import net.minecraft.item.EnumAction;
import me.oringo.oringoclient.qolfeatures.module.impl.movement.Scaffold;
import me.oringo.oringoclient.qolfeatures.module.impl.movement.Speed;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.player.EntityPlayer;
import me.oringo.oringoclient.events.MoveFlyingEvent;
import net.minecraft.stats.StatList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.common.ForgeHooks;
import net.minecraft.util.MathHelper;
import me.oringo.oringoclient.qolfeatures.module.impl.combat.KillAura;
import me.oringo.oringoclient.OringoClient;
import me.oringo.oringoclient.utils.MovementUtils;
import net.minecraft.potion.Potion;
import org.spongepowered.asm.mixin.Overwrite;
import me.oringo.oringoclient.utils.RotationUtils;
import me.oringo.oringoclient.utils.PlayerUtils;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import me.oringo.oringoclient.events.MotionUpdateEvent;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import me.oringo.oringoclient.commands.CommandHandler;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.util.DamageSource;
import net.minecraft.stats.StatBase;
import net.minecraft.entity.Entity;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Final;
import net.minecraft.client.network.NetHandlerPlayClient;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.util.MovementInput;
import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = { EntityPlayerSP.class }, priority = 1)
public abstract class PlayerSPMixin extends AbstractClientPlayerMixin
{
    @Shadow
    public MovementInput field_71158_b;
    @Shadow
    @Final
    public NetHandlerPlayClient field_71174_a;
    @Shadow
    public float field_71086_bY;
    @Shadow
    public float field_71164_i;
    @Shadow
    public float field_71163_h;
    @Shadow
    public float field_71155_g;
    @Shadow
    public float field_71154_f;
    @Shadow
    private boolean field_175171_bO;
    @Shadow
    private float field_175165_bM;
    @Shadow
    private double field_175172_bI;
    @Shadow
    private double field_175166_bJ;
    @Shadow
    private double field_175167_bK;
    @Shadow
    private float field_175164_bL;
    @Shadow
    private int field_175168_bP;
    @Shadow
    private boolean field_175170_bN;
    @Shadow
    public int field_71157_e;
    @Shadow
    protected int field_71156_d;
    @Shadow
    protected Minecraft field_71159_c;
    
    @Shadow
    @Override
    public abstract void func_70031_b(final boolean p0);
    
    @Shadow
    public abstract boolean func_70093_af();
    
    @Shadow
    public abstract void func_71009_b(final Entity p0);
    
    @Shadow
    public abstract void func_71047_c(final Entity p0);
    
    @Shadow
    public abstract void func_71064_a(final StatBase p0, final int p1);
    
    @Shadow
    protected abstract boolean func_175160_A();
    
    @Shadow
    public abstract void func_85030_a(final String p0, final float p1, final float p2);
    
    @Shadow
    @Override
    public abstract boolean func_70097_a(final DamageSource p0, final float p1);
    
    @Shadow
    public abstract boolean func_70613_aW();
    
    @Shadow
    public abstract void func_70078_a(final Entity p0);
    
    @Inject(method = { "sendChatMessage" }, at = { @At("HEAD") }, cancellable = true)
    public void onSenChatMessage(final String message, final CallbackInfo ci) {
        if (CommandHandler.handle(message)) {
            ci.cancel();
        }
    }
    
    @Overwrite
    public void func_175161_p() {
        final MotionUpdateEvent event = new MotionUpdateEvent.Pre(this.field_70165_t, this.func_174813_aQ().field_72338_b, this.field_70161_v, this.field_70177_z, this.field_70125_A, this.field_70122_E, this.func_70051_ag(), this.func_70093_af());
        if (MinecraftForge.EVENT_BUS.post((Event)event)) {
            return;
        }
        final boolean flag = event.sprinting;
        if (flag != this.field_175171_bO) {
            if (flag) {
                this.field_71174_a.func_147297_a((Packet)new C0BPacketEntityAction((Entity)this, C0BPacketEntityAction.Action.START_SPRINTING));
            }
            else {
                this.field_71174_a.func_147297_a((Packet)new C0BPacketEntityAction((Entity)this, C0BPacketEntityAction.Action.STOP_SPRINTING));
            }
            this.field_175171_bO = flag;
        }
        final boolean flag2 = event.sneaking;
        if (flag2 != this.field_175170_bN) {
            if (flag2) {
                this.field_71174_a.func_147297_a((Packet)new C0BPacketEntityAction((Entity)this, C0BPacketEntityAction.Action.START_SNEAKING));
            }
            else {
                this.field_71174_a.func_147297_a((Packet)new C0BPacketEntityAction((Entity)this, C0BPacketEntityAction.Action.STOP_SNEAKING));
            }
            this.field_175170_bN = flag2;
        }
        if (this.func_175160_A()) {
            final double d0 = event.x - this.field_175172_bI;
            final double d2 = event.y - this.field_175166_bJ;
            final double d3 = event.z - this.field_175167_bK;
            final double d4 = event.yaw - this.field_175164_bL;
            final double d5 = event.pitch - this.field_175165_bM;
            boolean flag3 = d0 * d0 + d2 * d2 + d3 * d3 > 9.0E-4 || this.field_175168_bP >= 20;
            final boolean flag4 = d4 != 0.0 || d5 != 0.0;
            if (this.field_70154_o == null) {
                if (flag3 && flag4) {
                    this.field_71174_a.func_147297_a((Packet)new C03PacketPlayer.C06PacketPlayerPosLook(event.x, event.y, event.z, event.yaw, event.pitch, event.onGround));
                }
                else if (flag3) {
                    this.field_71174_a.func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(event.x, event.y, event.z, event.onGround));
                }
                else if (flag4) {
                    this.field_71174_a.func_147297_a((Packet)new C03PacketPlayer.C05PacketPlayerLook(event.yaw, event.pitch, event.onGround));
                }
                else {
                    this.field_71174_a.func_147297_a((Packet)new C03PacketPlayer(event.onGround));
                }
            }
            else {
                this.field_71174_a.func_147297_a((Packet)new C03PacketPlayer.C06PacketPlayerPosLook(this.field_70159_w, -999.0, this.field_70179_y, event.yaw, event.pitch, event.onGround));
                flag3 = false;
            }
            ++this.field_175168_bP;
            if (flag3) {
                this.field_175172_bI = event.x;
                this.field_175166_bJ = event.y;
                this.field_175167_bK = event.z;
                this.field_175168_bP = 0;
            }
            PlayerUtils.lastGround = event.onGround;
            RotationUtils.lastLastReportedPitch = this.field_175165_bM;
            if (flag4) {
                this.field_175164_bL = event.yaw;
                this.field_175165_bM = event.pitch;
            }
        }
        MinecraftForge.EVENT_BUS.post((Event)new MotionUpdateEvent.Post(event));
    }
    
    public void func_70664_aZ() {
        this.field_70181_x = this.func_175134_bD();
        if (this.func_82165_m(Potion.field_76430_j.field_76415_H)) {
            this.field_70181_x += (this.func_70660_b(Potion.field_76430_j).func_76458_c() + 1) * 0.1f;
        }
        if (this.func_70051_ag() && MovementUtils.isMoving()) {
            final float f = ((OringoClient.sprint.isToggled() && OringoClient.sprint.omni.isEnabled()) ? MovementUtils.getYaw() : ((OringoClient.killAura.isToggled() && KillAura.target != null && OringoClient.killAura.movementFix.isEnabled()) ? RotationUtils.getRotations(KillAura.target).getYaw() : this.field_70177_z)) * 0.017453292f;
            this.field_70159_w -= MathHelper.func_76126_a(f) * 0.2f;
            this.field_70179_y += MathHelper.func_76134_b(f) * 0.2f;
        }
        this.field_70160_al = true;
        ForgeHooks.onLivingJump((EntityLivingBase)this);
        this.func_71029_a(StatList.field_75953_u);
        if (this.func_70051_ag()) {
            this.func_71020_j(0.8f);
        }
        else {
            this.func_71020_j(0.2f);
        }
    }
    
    @Override
    public void func_70060_a(float strafe, float forward, float friction) {
        final MoveFlyingEvent event = new MoveFlyingEvent(forward, strafe, friction, this.field_70177_z);
        if (MinecraftForge.EVENT_BUS.post((Event)event)) {
            return;
        }
        strafe = event.getStrafe();
        forward = event.getForward();
        friction = event.getFriction();
        float f = strafe * strafe + forward * forward;
        if (f >= 1.0E-4f) {
            f = MathHelper.func_76129_c(f);
            if (f < 1.0f) {
                f = 1.0f;
            }
            f = friction / f;
            strafe *= f;
            forward *= f;
            final float yaw = event.getYaw();
            final float f2 = MathHelper.func_76126_a(yaw * 3.1415927f / 180.0f);
            final float f3 = MathHelper.func_76134_b(yaw * 3.1415927f / 180.0f);
            this.field_70159_w += strafe * f3 - forward * f2;
            this.field_70179_y += forward * f3 + strafe * f2;
        }
    }
    
    public void superMoveEntityWithHeading(final float strafe, final float forward, final boolean onGround, final float friction2Multi) {
        if (this.func_70613_aW()) {
            if (!this.func_70090_H() || (((Entity)this) instanceof EntityPlayer && this.field_71075_bZ.field_75100_b)) {
                if (!this.func_180799_ab() || (((Entity)this) instanceof EntityPlayer && this.field_71075_bZ.field_75100_b)) {
                    float f4 = 0.91f;
                    if (onGround) {
                        f4 = this.field_70170_p.func_180495_p(new BlockPos(MathHelper.func_76128_c(this.field_70165_t), MathHelper.func_76128_c(this.func_174813_aQ().field_72338_b) - 1, MathHelper.func_76128_c(this.field_70161_v))).func_177230_c().field_149765_K * 0.91f;
                    }
                    final float f5 = 0.16277136f / (f4 * f4 * f4);
                    float f6;
                    if (onGround) {
                        f6 = this.func_70689_ay() * f5;
                    }
                    else {
                        f6 = this.field_70747_aH;
                    }
                    this.func_70060_a(strafe, forward, f6);
                    f4 = 0.91f;
                    if (onGround) {
                        f4 = this.field_70170_p.func_180495_p(new BlockPos(MathHelper.func_76128_c(this.field_70165_t), MathHelper.func_76128_c(this.func_174813_aQ().field_72338_b) - 1, MathHelper.func_76128_c(this.field_70161_v))).func_177230_c().field_149765_K * friction2Multi;
                    }
                    if (this.func_70617_f_()) {
                        final float f7 = 0.15f;
                        this.field_70159_w = MathHelper.func_151237_a(this.field_70159_w, (double)(-f7), (double)f7);
                        this.field_70179_y = MathHelper.func_151237_a(this.field_70179_y, (double)(-f7), (double)f7);
                        this.field_70143_R = 0.0f;
                        if (this.field_70181_x < -0.15) {
                            this.field_70181_x = -0.15;
                        }
                        final boolean flag = this.func_70093_af() && ((Entity)this) instanceof EntityPlayer;
                        if (flag && this.field_70181_x < 0.0) {
                            this.field_70181_x = 0.0;
                        }
                    }
                    this.func_70091_d(this.field_70159_w, this.field_70181_x, this.field_70179_y);
                    if (this.field_70123_F && this.func_70617_f_()) {
                        this.field_70181_x = 0.2;
                    }
                    if (this.field_70170_p.field_72995_K && (!this.field_70170_p.func_175667_e(new BlockPos((int)this.field_70165_t, 0, (int)this.field_70161_v)) || !this.field_70170_p.func_175726_f(new BlockPos((int)this.field_70165_t, 0, (int)this.field_70161_v)).func_177410_o())) {
                        if (this.field_70163_u > 0.0) {
                            this.field_70181_x = -0.1;
                        }
                        else {
                            this.field_70181_x = 0.0;
                        }
                    }
                    else {
                        this.field_70181_x -= 0.08;
                    }
                    this.field_70181_x *= 0.9800000190734863;
                    this.field_70159_w *= f4;
                    this.field_70179_y *= f4;
                }
                else {
                    final double d1 = this.field_70163_u;
                    this.func_70060_a(strafe, forward, 0.02f);
                    this.func_70091_d(this.field_70159_w, this.field_70181_x, this.field_70179_y);
                    this.field_70159_w *= 0.5;
                    this.field_70181_x *= 0.5;
                    this.field_70179_y *= 0.5;
                    this.field_70181_x -= 0.02;
                    if (this.field_70123_F && this.func_70038_c(this.field_70159_w, this.field_70181_x + 0.6000000238418579 - this.field_70163_u + d1, this.field_70179_y)) {
                        this.field_70181_x = 0.30000001192092896;
                    }
                }
            }
            else {
                final double d2 = this.field_70163_u;
                float f8 = 0.8f;
                float f9 = 0.02f;
                float f10 = (float)EnchantmentHelper.func_180318_b((Entity)this);
                if (f10 > 3.0f) {
                    f10 = 3.0f;
                }
                if (!this.field_70122_E) {
                    f10 *= 0.5f;
                }
                if (f10 > 0.0f) {
                    f8 += (0.54600006f - f8) * f10 / 3.0f;
                    f9 += (this.func_70689_ay() * 1.0f - f9) * f10 / 3.0f;
                }
                this.func_70060_a(strafe, forward, f9);
                this.func_70091_d(this.field_70159_w, this.field_70181_x, this.field_70179_y);
                this.field_70159_w *= f8;
                this.field_70181_x *= 0.800000011920929;
                this.field_70179_y *= f8;
                this.field_70181_x -= 0.02;
                if (this.field_70123_F && this.func_70038_c(this.field_70159_w, this.field_70181_x + 0.6000000238418579 - this.field_70163_u + d2, this.field_70179_y)) {
                    this.field_70181_x = 0.30000001192092896;
                }
            }
        }
        this.field_70722_aY = this.field_70721_aZ;
        final double d3 = this.field_70165_t - this.field_70169_q;
        final double d4 = this.field_70161_v - this.field_70166_s;
        float f11 = MathHelper.func_76133_a(d3 * d3 + d4 * d4) * 4.0f;
        if (f11 > 1.0f) {
            f11 = 1.0f;
        }
        this.field_70721_aZ += (f11 - this.field_70721_aZ) * 0.4f;
        this.field_70754_ba += this.field_70721_aZ;
    }
    
    @Inject(method = { "pushOutOfBlocks" }, at = { @At("HEAD") }, cancellable = true)
    public void pushOutOfBlocks(final double d2, final double f, final double blockpos, final CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }
    
    @Redirect(method = { "onLivingUpdate" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;isUsingItem()Z"))
    public boolean isUsingItem(final EntityPlayerSP instance) {
        return !OringoClient.noSlow.isToggled() && (instance.func_71039_bw() || OringoClient.autoBlock.isBlocking());
    }
    
    @Override
    public boolean func_70094_T() {
        return false;
    }
    
    @Inject(method = { "onLivingUpdate" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/AbstractClientPlayer;onLivingUpdate()V") }, cancellable = true)
    public void onLivingUpdate(final CallbackInfo ci) {
        if (OringoClient.sprint.omni.isEnabled() && OringoClient.sprint.isToggled()) {
            if (!MovementUtils.isMoving() || this.func_70093_af() || (this.func_71024_bL().func_75116_a() <= 6.0f && !this.field_71075_bZ.field_75101_c)) {
                if (this.func_70051_ag()) {
                    this.func_70031_b(false);
                }
            }
            else if (!this.func_70051_ag()) {
                this.func_70031_b(true);
            }
        }
        if (OringoClient.speed.isToggled() && !Speed.isDisabled() && this.func_71024_bL().func_75116_a() > 6.0f && !this.func_70051_ag()) {
            this.func_70031_b(true);
        }
        if (OringoClient.scaffold.isToggled()) {
            if (Scaffold.sprint.is("None") && this.func_70051_ag()) {
                this.func_70031_b(false);
            }
            else if (!this.func_70051_ag() && MovementUtils.isMoving()) {
                this.func_70031_b(true);
            }
        }
        if (OringoClient.noSlow.isToggled() && (this.func_71039_bw() || OringoClient.autoBlock.isBlocking())) {
            final EnumAction action = this.func_70694_bm().func_77973_b().func_77661_b(this.func_70694_bm());
            if (action == EnumAction.BLOCK) {
                final MovementInput field_71158_b = this.field_71158_b;
                field_71158_b.field_78900_b *= (float)OringoClient.noSlow.swordSlowdown.getValue();
                final MovementInput field_71158_b2 = this.field_71158_b;
                field_71158_b2.field_78902_a *= (float)OringoClient.noSlow.swordSlowdown.getValue();
            }
            else if (action == EnumAction.BOW) {
                final MovementInput field_71158_b3 = this.field_71158_b;
                field_71158_b3.field_78900_b *= (float)OringoClient.noSlow.bowSlowdown.getValue();
                final MovementInput field_71158_b4 = this.field_71158_b;
                field_71158_b4.field_78902_a *= (float)OringoClient.noSlow.bowSlowdown.getValue();
            }
            else if (action != EnumAction.NONE) {
                final MovementInput field_71158_b5 = this.field_71158_b;
                field_71158_b5.field_78900_b *= (float)OringoClient.noSlow.eatingSlowdown.getValue();
                final MovementInput field_71158_b6 = this.field_71158_b;
                field_71158_b6.field_78902_a *= (float)OringoClient.noSlow.eatingSlowdown.getValue();
            }
        }
        if (OringoClient.freeCam.isToggled()) {
            this.field_70145_X = true;
        }
    }
    
    @Inject(method = { "onUpdate" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;isRiding()Z") }, cancellable = true)
    private void onUpdate(final CallbackInfo ci) {
        if (MinecraftForge.EVENT_BUS.post((Event)new PlayerUpdateEvent())) {
            ci.cancel();
        }
    }
    
    @Override
    public void func_70612_e(final float strafe, final float forward) {
        final MoveHeadingEvent event = new MoveHeadingEvent(this.field_70122_E);
        if (MinecraftForge.EVENT_BUS.post((Event)event)) {
            return;
        }
        final double d0 = this.field_70165_t;
        final double d2 = this.field_70163_u;
        final double d3 = this.field_70161_v;
        if (this.field_71075_bZ.field_75100_b && this.field_70154_o == null) {
            final double d4 = this.field_70181_x;
            final float f = this.field_70747_aH;
            this.field_70747_aH = this.field_71075_bZ.func_75093_a() * (this.func_70051_ag() ? 2 : 1);
            super.func_70612_e(strafe, forward);
            this.field_70181_x = d4 * 0.6;
            this.field_70747_aH = f;
        }
        else {
            this.superMoveEntityWithHeading(strafe, forward, event.isOnGround(), event.getFriction2Multi());
        }
        this.func_71000_j(this.field_70165_t - d0, this.field_70163_u - d2, this.field_70161_v - d3);
    }
    
    @Override
    public void func_70091_d(double x, double y, double z) {
        final MoveEvent event = new MoveEvent(x, y, z);
        if (MinecraftForge.EVENT_BUS.post((Event)event)) {
            return;
        }
        x = event.getX();
        y = event.getY();
        z = event.getZ();
        if (this.field_70145_X) {
            this.func_174826_a(this.func_174813_aQ().func_72317_d(x, y, z));
            this.doResetPositionToBB();
        }
        else {
            this.field_70170_p.field_72984_F.func_76320_a("move");
            final double d0 = this.field_70165_t;
            final double d2 = this.field_70163_u;
            final double d3 = this.field_70161_v;
            if (this.field_70134_J) {
                this.field_70134_J = false;
                x *= 0.25;
                y *= 0.05000000074505806;
                z *= 0.25;
                this.field_70159_w = 0.0;
                this.field_70181_x = 0.0;
                this.field_70179_y = 0.0;
            }
            double d4 = x;
            final double d5 = y;
            double d6 = z;
            final boolean flag = ((this.field_70122_E && this.func_70093_af()) || (PlayerUtils.isOnGround(1.0) && OringoClient.scaffold.isToggled() && Scaffold.safeWalk.isEnabled())) && ((Entity)this) instanceof EntityPlayer;
            if (flag) {
                final double d7 = 0.05;
                while (x != 0.0 && this.field_70170_p.func_72945_a((Entity)this, this.func_174813_aQ().func_72317_d(x, -1.0, 0.0)).isEmpty()) {
                    if (x < d7 && x >= -d7) {
                        x = 0.0;
                    }
                    else if (x > 0.0) {
                        x -= d7;
                    }
                    else {
                        x += d7;
                    }
                    d4 = x;
                }
                while (z != 0.0 && this.field_70170_p.func_72945_a((Entity)this, this.func_174813_aQ().func_72317_d(0.0, -1.0, z)).isEmpty()) {
                    if (z < d7 && z >= -d7) {
                        z = 0.0;
                    }
                    else if (z > 0.0) {
                        z -= d7;
                    }
                    else {
                        z += d7;
                    }
                    d6 = z;
                }
                while (x != 0.0 && z != 0.0 && this.field_70170_p.func_72945_a((Entity)this, this.func_174813_aQ().func_72317_d(x, -1.0, z)).isEmpty()) {
                    if (x < d7 && x >= -d7) {
                        x = 0.0;
                    }
                    else if (x > 0.0) {
                        x -= d7;
                    }
                    else {
                        x += d7;
                    }
                    d4 = x;
                    if (z < d7 && z >= -d7) {
                        z = 0.0;
                    }
                    else if (z > 0.0) {
                        z -= d7;
                    }
                    else {
                        z += d7;
                    }
                    d6 = z;
                }
            }
            final List<AxisAlignedBB> list1 = (List<AxisAlignedBB>)this.field_70170_p.func_72945_a((Entity)this, this.func_174813_aQ().func_72321_a(x, y, z));
            final AxisAlignedBB axisalignedbb = this.func_174813_aQ();
            for (final AxisAlignedBB axisalignedbb2 : list1) {
                y = axisalignedbb2.func_72323_b(this.func_174813_aQ(), y);
            }
            this.func_174826_a(this.func_174813_aQ().func_72317_d(0.0, y, 0.0));
            final boolean flag2 = this.field_70122_E || (d5 != y && d5 < 0.0);
            for (final AxisAlignedBB axisalignedbb3 : list1) {
                x = axisalignedbb3.func_72316_a(this.func_174813_aQ(), x);
            }
            this.func_174826_a(this.func_174813_aQ().func_72317_d(x, 0.0, 0.0));
            for (final AxisAlignedBB axisalignedbb4 : list1) {
                z = axisalignedbb4.func_72322_c(this.func_174813_aQ(), z);
            }
            this.func_174826_a(this.func_174813_aQ().func_72317_d(0.0, 0.0, z));
            if (this.field_70138_W > 0.0f && flag2 && (d4 != x || d6 != z)) {
                final double d8 = x;
                final double d9 = y;
                final double d10 = z;
                final AxisAlignedBB axisalignedbb5 = this.func_174813_aQ();
                this.func_174826_a(axisalignedbb);
                final StepEvent.Pre stepEvent = new StepEvent.Pre(this.field_70138_W);
                MinecraftForge.EVENT_BUS.post((Event)stepEvent);
                y = stepEvent.getHeight();
                final List<AxisAlignedBB> list2 = (List<AxisAlignedBB>)this.field_70170_p.func_72945_a((Entity)this, this.func_174813_aQ().func_72321_a(d4, y, d6));
                AxisAlignedBB axisalignedbb6 = this.func_174813_aQ();
                final AxisAlignedBB axisalignedbb7 = axisalignedbb6.func_72321_a(d4, 0.0, d6);
                double d11 = y;
                for (final AxisAlignedBB axisalignedbb8 : list2) {
                    d11 = axisalignedbb8.func_72323_b(axisalignedbb7, d11);
                }
                axisalignedbb6 = axisalignedbb6.func_72317_d(0.0, d11, 0.0);
                double d12 = d4;
                for (final AxisAlignedBB axisalignedbb9 : list2) {
                    d12 = axisalignedbb9.func_72316_a(axisalignedbb6, d12);
                }
                axisalignedbb6 = axisalignedbb6.func_72317_d(d12, 0.0, 0.0);
                double d13 = d6;
                for (final AxisAlignedBB axisalignedbb10 : list2) {
                    d13 = axisalignedbb10.func_72322_c(axisalignedbb6, d13);
                }
                axisalignedbb6 = axisalignedbb6.func_72317_d(0.0, 0.0, d13);
                AxisAlignedBB axisalignedbb11 = this.func_174813_aQ();
                double d14 = y;
                for (final AxisAlignedBB axisalignedbb12 : list2) {
                    d14 = axisalignedbb12.func_72323_b(axisalignedbb11, d14);
                }
                axisalignedbb11 = axisalignedbb11.func_72317_d(0.0, d14, 0.0);
                double d15 = d4;
                for (final AxisAlignedBB axisalignedbb13 : list2) {
                    d15 = axisalignedbb13.func_72316_a(axisalignedbb11, d15);
                }
                axisalignedbb11 = axisalignedbb11.func_72317_d(d15, 0.0, 0.0);
                double d16 = d6;
                for (final AxisAlignedBB axisalignedbb14 : list2) {
                    d16 = axisalignedbb14.func_72322_c(axisalignedbb11, d16);
                }
                axisalignedbb11 = axisalignedbb11.func_72317_d(0.0, 0.0, d16);
                final double d17 = d12 * d12 + d13 * d13;
                final double d18 = d15 * d15 + d16 * d16;
                if (d17 > d18) {
                    x = d12;
                    z = d13;
                    y = -d11;
                    this.func_174826_a(axisalignedbb6);
                }
                else {
                    x = d15;
                    z = d16;
                    y = -d14;
                    this.func_174826_a(axisalignedbb11);
                }
                for (final AxisAlignedBB axisalignedbb15 : list2) {
                    y = axisalignedbb15.func_72323_b(this.func_174813_aQ(), y);
                }
                this.func_174826_a(this.func_174813_aQ().func_72317_d(0.0, y, 0.0));
                if (d8 * d8 + d10 * d10 >= x * x + z * z) {
                    x = d8;
                    y = d9;
                    z = d10;
                    this.func_174826_a(axisalignedbb5);
                }
                else {
                    MinecraftForge.EVENT_BUS.post((Event)new StepEvent.Post(1.0 + y));
                }
            }
            this.field_70170_p.field_72984_F.func_76319_b();
            this.field_70170_p.field_72984_F.func_76320_a("rest");
            this.doResetPositionToBB();
            this.field_70123_F = (d4 != x || d6 != z);
            this.field_70124_G = (d5 != y);
            this.field_70122_E = (this.field_70124_G && d5 < 0.0);
            this.field_70132_H = (this.field_70123_F || this.field_70124_G);
            final int i = MathHelper.func_76128_c(this.field_70165_t);
            final int j = MathHelper.func_76128_c(this.field_70163_u - 0.20000000298023224);
            final int k = MathHelper.func_76128_c(this.field_70161_v);
            BlockPos blockpos = new BlockPos(i, j, k);
            Block block1 = this.field_70170_p.func_180495_p(blockpos).func_177230_c();
            if (block1.func_149688_o() == Material.field_151579_a) {
                final Block block2 = this.field_70170_p.func_180495_p(blockpos.func_177977_b()).func_177230_c();
                if (block2 instanceof BlockFence || block2 instanceof BlockWall || block2 instanceof BlockFenceGate) {
                    block1 = block2;
                    blockpos = blockpos.func_177977_b();
                }
            }
            this.func_180433_a(y, this.field_70122_E, block1, blockpos);
            if (d4 != x) {
                this.field_70159_w = 0.0;
            }
            if (d6 != z) {
                this.field_70179_y = 0.0;
            }
            if (d5 != y) {
                block1.func_176216_a(this.field_70170_p, (Entity)this);
            }
            if (this.func_70041_e_() && !flag && this.field_70154_o == null) {
                final double d19 = this.field_70165_t - d0;
                double d20 = this.field_70163_u - d2;
                final double d21 = this.field_70161_v - d3;
                if (block1 != Blocks.field_150468_ap) {
                    d20 = 0.0;
                }
                if (block1 != null && this.field_70122_E) {
                    block1.func_176199_a(this.field_70170_p, blockpos, (Entity)this);
                }
                this.field_70140_Q += (float)(MathHelper.func_76133_a(d19 * d19 + d21 * d21) * 0.6);
                this.field_82151_R += (float)(MathHelper.func_76133_a(d19 * d19 + d20 * d20 + d21 * d21) * 0.6);
                if (this.field_82151_R > this.getNextStepDistance() && block1.func_149688_o() != Material.field_151579_a) {
                    this.setNextStepDistance((int)this.field_82151_R + 1);
                    if (this.func_70090_H()) {
                        float f = MathHelper.func_76133_a(this.field_70159_w * this.field_70159_w * 0.20000000298023224 + this.field_70181_x * this.field_70181_x + this.field_70179_y * this.field_70179_y * 0.20000000298023224) * 0.35f;
                        if (f > 1.0f) {
                            f = 1.0f;
                        }
                        this.func_85030_a(this.func_145776_H(), f, 1.0f + (this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) * 0.4f);
                    }
                    this.func_180429_a(blockpos, block1);
                }
            }
            try {
                this.func_145775_I();
            }
            catch (Throwable throwable) {
                final CrashReport crashreport = CrashReport.func_85055_a(throwable, "Checking entity block collision");
                final CrashReportCategory crashreportcategory = crashreport.func_85058_a("Entity being checked for collision");
                this.func_85029_a(crashreportcategory);
                throw new ReportedException(crashreport);
            }
            final boolean flag3 = this.func_70026_G();
            if (this.field_70170_p.func_147470_e(this.func_174813_aQ().func_72331_e(0.001, 0.001, 0.001))) {
                this.func_70081_e(1);
                if (!flag3) {
                    this.plusPlusFire();
                    if (this.getFire() == 0) {
                        this.func_70015_d(8);
                    }
                }
            }
            else if (this.getFire() <= 0) {
                this.SetFire(-this.field_70174_ab);
            }
            if (flag3 && this.getFire() > 0) {
                this.func_85030_a("random.fizz", 0.7f, 1.6f + (this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) * 0.4f);
                this.SetFire(-this.field_70174_ab);
            }
            this.field_70170_p.field_72984_F.func_76319_b();
        }
    }
    
    @Inject(method = { "updateEntityActionState" }, at = { @At("RETURN") })
    public void onUpdateAction(final CallbackInfo ci) {
        if (OringoClient.speed.isToggled() && !Speed.isDisabled() && MovementUtils.isMoving()) {
            this.field_70703_bu = false;
        }
    }
    
    @Override
    public void func_71059_n(final Entity targetEntity) {
        if (ForgeHooks.onPlayerAttackTarget((EntityPlayer)this, targetEntity) && targetEntity.func_70075_an() && !targetEntity.func_85031_j((Entity)this)) {
            float f = (float)this.func_110148_a(SharedMonsterAttributes.field_111264_e).func_111126_e();
            int i = 0;
            float f2 = 0.0f;
            if (targetEntity instanceof EntityLivingBase) {
                f2 = EnchantmentHelper.func_152377_a(this.func_70694_bm(), ((EntityLivingBase)targetEntity).func_70668_bt());
            }
            else {
                f2 = EnchantmentHelper.func_152377_a(this.func_70694_bm(), EnumCreatureAttribute.UNDEFINED);
            }
            i += EnchantmentHelper.func_77501_a((EntityLivingBase)this);
            if (this.func_70051_ag()) {
                ++i;
            }
            if (f > 0.0f || f2 > 0.0f) {
                final boolean flag = this.field_70143_R > 0.0f && !this.field_70122_E && !this.func_70617_f_() && !this.func_70090_H() && !this.func_70644_a(Potion.field_76440_q) && this.field_70154_o == null && targetEntity instanceof EntityLivingBase;
                if (flag && f > 0.0f) {
                    f *= 1.5f;
                }
                f += f2;
                boolean flag2 = false;
                final int j = EnchantmentHelper.func_90036_a((EntityLivingBase)this);
                if (targetEntity instanceof EntityLivingBase && j > 0 && !targetEntity.func_70027_ad()) {
                    flag2 = true;
                    targetEntity.func_70015_d(1);
                }
                final double d0 = targetEntity.field_70159_w;
                final double d2 = targetEntity.field_70181_x;
                final double d3 = targetEntity.field_70179_y;
                final boolean flag3 = targetEntity.func_70097_a(DamageSource.func_76365_a((EntityPlayer)this), f);
                if (flag3) {
                    if (i > 0) {
                        targetEntity.func_70024_g((double)(-MathHelper.func_76126_a(this.field_70177_z * 3.1415927f / 180.0f) * i * 0.5f), 0.1, (double)(MathHelper.func_76134_b(this.field_70177_z * 3.1415927f / 180.0f) * i * 0.5f));
                        if (!OringoClient.sprint.isToggled() || !OringoClient.sprint.keep.isEnabled()) {
                            this.field_70159_w *= 0.6;
                            this.field_70179_y *= 0.6;
                            this.func_70031_b(false);
                        }
                    }
                    if (targetEntity instanceof EntityPlayerMP && targetEntity.field_70133_I) {
                        ((EntityPlayerMP)targetEntity).field_71135_a.func_147359_a((Packet)new S12PacketEntityVelocity(targetEntity));
                        targetEntity.field_70133_I = false;
                        targetEntity.field_70159_w = d0;
                        targetEntity.field_70181_x = d2;
                        targetEntity.field_70179_y = d3;
                    }
                    if (flag) {
                        this.func_71009_b(targetEntity);
                    }
                    if (f2 > 0.0f) {
                        this.func_71047_c(targetEntity);
                    }
                    if (f >= 18.0f) {
                        this.func_71029_a((StatBase)AchievementList.field_75999_E);
                    }
                    this.func_130011_c(targetEntity);
                    if (targetEntity instanceof EntityLivingBase) {
                        EnchantmentHelper.func_151384_a((EntityLivingBase)targetEntity, (Entity)this);
                    }
                    EnchantmentHelper.func_151385_b((EntityLivingBase)this, targetEntity);
                    final ItemStack itemstack = this.func_71045_bC();
                    Entity entity = targetEntity;
                    if (targetEntity instanceof EntityDragonPart) {
                        final IEntityMultiPart ientitymultipart = ((EntityDragonPart)targetEntity).field_70259_a;
                        if (ientitymultipart instanceof EntityLivingBase) {
                            entity = (Entity)ientitymultipart;
                        }
                    }
                    if (itemstack != null && entity instanceof EntityLivingBase) {
                        itemstack.func_77961_a((EntityLivingBase)entity, (EntityPlayer)this);
                        if (itemstack.field_77994_a <= 0) {
                            this.func_71028_bD();
                        }
                    }
                    if (targetEntity instanceof EntityLivingBase) {
                        this.func_71064_a(StatList.field_75951_w, Math.round(f * 10.0f));
                        if (j > 0) {
                            targetEntity.func_70015_d(j * 4);
                        }
                    }
                    this.func_71020_j(0.3f);
                }
                else if (flag2) {
                    targetEntity.func_70066_B();
                }
            }
        }
    }
}
