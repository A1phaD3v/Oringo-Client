// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.events;

import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class BlockBoundsEvent extends Event
{
    public AxisAlignedBB aabb;
    public Block block;
    public BlockPos pos;
    public Entity collidingEntity;
    
    public BlockBoundsEvent(final Block block, final AxisAlignedBB aabb, final BlockPos pos, final Entity collidingEntity) {
        this.aabb = aabb;
        this.block = block;
        this.pos = pos;
        this.collidingEntity = collidingEntity;
    }
}
