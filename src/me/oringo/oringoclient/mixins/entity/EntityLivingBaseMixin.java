// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.mixins.entity;

import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.Potion;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ EntityLivingBase.class })
public abstract class EntityLivingBaseMixin extends EntityMixin
{
    @Shadow
    public float field_70759_as;
    @Shadow
    private int field_70773_bE;
    @Shadow
    protected boolean field_70703_bu;
    @Shadow
    public float field_70747_aH;
    @Shadow
    protected int field_70716_bi;
    @Shadow
    public float field_70701_bs;
    @Shadow
    public float field_70702_br;
    @Shadow
    protected float field_70764_aw;
    @Shadow
    protected int field_70708_bq;
    @Shadow
    protected double field_70709_bj;
    @Shadow
    public float field_70769_ao;
    @Shadow
    public float field_70761_aq;
    @Shadow
    protected float field_110154_aX;
    @Shadow
    public float field_70722_aY;
    @Shadow
    public float field_70721_aZ;
    @Shadow
    public float field_70754_ba;
    @Shadow
    protected float field_70768_au;
    @Shadow
    public float field_70770_ap;
    
    @Shadow
    protected abstract float func_175134_bD();
    
    @Shadow
    public abstract boolean func_82165_m(final int p0);
    
    @Shadow
    public abstract PotionEffect func_70660_b(final Potion p0);
    
    @Shadow
    protected abstract void func_70664_aZ();
    
    @Shadow
    public abstract IAttributeInstance func_110148_a(final IAttribute p0);
    
    @Shadow
    public abstract float func_110143_aJ();
    
    @Shadow
    public abstract boolean func_70617_f_();
    
    @Shadow
    public abstract boolean func_70644_a(final Potion p0);
    
    @Shadow
    public abstract void func_130011_c(final Entity p0);
    
    @Shadow
    public abstract float func_70678_g(final float p0);
    
    @Shadow
    protected abstract void func_180433_a(final double p0, final boolean p1, final Block p2, final BlockPos p3);
    
    @Shadow
    protected abstract void func_175133_bi();
    
    @Shadow
    public abstract ItemStack func_70694_bm();
    
    @Shadow
    @Override
    protected abstract void func_70088_a();
    
    @Shadow
    public abstract void func_70031_b(final boolean p0);
    
    public void setJumpTicks(final int jumpTicks) {
        this.field_70773_bE = jumpTicks;
    }
    
    public int getJumpTicks() {
        return this.field_70773_bE;
    }
}
