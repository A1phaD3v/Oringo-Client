// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.events;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.Event;

public class PreAttackEvent extends Event
{
    public Entity entity;
    
    public PreAttackEvent(final Entity entity) {
        this.entity = entity;
    }
}
