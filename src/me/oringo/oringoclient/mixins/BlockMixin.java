// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.mixins;

import org.spongepowered.asm.mixin.Overwrite;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import me.oringo.oringoclient.events.BlockBoundsEvent;
import net.minecraft.entity.Entity;
import java.util.List;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.block.Block;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = { Block.class }, priority = 1)
public abstract class BlockMixin
{
    @Shadow
    public abstract void func_149676_a(final float p0, final float p1, final float p2, final float p3, final float p4, final float p5);
    
    @Shadow
    public abstract AxisAlignedBB func_180640_a(final World p0, final BlockPos p1, final IBlockState p2);
    
    @Overwrite
    public void func_180638_a(final World worldIn, final BlockPos pos, final IBlockState state, final AxisAlignedBB mask, final List<AxisAlignedBB> list, final Entity collidingEntity) {
        final BlockBoundsEvent event = new BlockBoundsEvent((Block)this, this.func_180640_a(worldIn, pos, state), pos, collidingEntity);
        if (MinecraftForge.EVENT_BUS.post((Event)event)) {
            return;
        }
        if (event.aabb != null && mask.func_72326_a(event.aabb)) {
            list.add(event.aabb);
        }
    }
}
