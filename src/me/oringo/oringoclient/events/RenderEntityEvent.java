// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.events;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.eventhandler.Event;

public class RenderEntityEvent extends Event
{
    public EntityLivingBase entity;
    public float limbSwing;
    public float limbSwingAmount;
    public float rotationFloat;
    public float rotationYaw;
    public float rotationPitch;
    
    public RenderEntityEvent(final EntityLivingBase entity, final float limbSwing, final float limbSwingAmount, final float rotationFloat, final float rotationYaw, final float rotationPitch) {
        this.entity = entity;
        this.limbSwing = limbSwing;
        this.limbSwingAmount = limbSwingAmount;
        this.rotationFloat = rotationFloat;
        this.rotationYaw = rotationYaw;
        this.rotationPitch = rotationPitch;
    }
}
