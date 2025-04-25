// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.events;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class BlockChangeEvent extends Event
{
    public BlockPos pos;
    public IBlockState state;
    
    public BlockChangeEvent(final BlockPos pos, final IBlockState state) {
        this.pos = pos;
        this.state = state;
    }
}
