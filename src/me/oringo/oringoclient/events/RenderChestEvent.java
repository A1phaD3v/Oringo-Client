// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.events;

import net.minecraft.tileentity.TileEntityChest;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class RenderChestEvent extends Event
{
    private TileEntityChest chest;
    private double x;
    private double y;
    private double z;
    private float partialTicks;
    private int destroyStage;
    
    public RenderChestEvent(final TileEntityChest chest, final double x, final double y, final double z, final float partialTicks, final int destroyStage) {
        this.chest = chest;
        this.x = x;
        this.y = y;
        this.z = z;
        this.partialTicks = partialTicks;
        this.destroyStage = destroyStage;
    }
    
    public boolean isPre() {
        return this instanceof Pre;
    }
    
    public double getX() {
        return this.x;
    }
    
    public double getY() {
        return this.y;
    }
    
    public double getZ() {
        return this.z;
    }
    
    public float getPartialTicks() {
        return this.partialTicks;
    }
    
    public int getDestroyStage() {
        return this.destroyStage;
    }
    
    public TileEntityChest getChest() {
        return this.chest;
    }
    
    @Cancelable
    public static class Pre extends RenderChestEvent
    {
        public Pre(final TileEntityChest chest, final double x, final double y, final double z, final float partialTicks, final int destroyStage) {
            super(chest, x, y, z, partialTicks, destroyStage);
        }
    }
    
    @Cancelable
    public static class Post extends RenderChestEvent
    {
        public Post(final TileEntityChest chest, final double x, final double y, final double z, final float partialTicks, final int destroyStage) {
            super(chest, x, y, z, partialTicks, destroyStage);
        }
    }
}
