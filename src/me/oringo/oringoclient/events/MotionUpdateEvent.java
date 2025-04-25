// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.events;

import net.minecraft.util.Vec3;
import me.oringo.oringoclient.utils.Rotation;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class MotionUpdateEvent extends Event
{
    public float yaw;
    public float pitch;
    public double x;
    public double y;
    public double z;
    public boolean onGround;
    public boolean sprinting;
    public boolean sneaking;
    
    protected MotionUpdateEvent(final double x, final double y, final double z, final float yaw, final float pitch, final boolean onGround, final boolean sprinting, final boolean sneaking) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
        this.sneaking = sneaking;
        this.sprinting = sprinting;
    }
    
    public MotionUpdateEvent setPosition(final double x, final double y, final double z) {
        this.x = x;
        this.z = z;
        this.y = y;
        return this;
    }
    
    public MotionUpdateEvent setRotation(final float yaw, final float pitch) {
        this.pitch = pitch;
        this.yaw = yaw;
        return this;
    }
    
    public MotionUpdateEvent setRotation(final Rotation rotation) {
        return this.setRotation(rotation.getYaw(), rotation.getPitch());
    }
    
    public MotionUpdateEvent setPosition(final Vec3 vec3) {
        return this.setPosition(vec3.field_72450_a, vec3.field_72448_b, vec3.field_72449_c);
    }
    
    public Rotation getRotation() {
        return new Rotation(this.yaw, this.pitch);
    }
    
    public Vec3 getPosition() {
        return new Vec3(this.x, this.y, this.z);
    }
    
    public MotionUpdateEvent setY(final double y) {
        this.y = y;
        return this;
    }
    
    public MotionUpdateEvent setX(final double x) {
        this.x = x;
        return this;
    }
    
    public MotionUpdateEvent setZ(final double z) {
        this.z = z;
        return this;
    }
    
    public MotionUpdateEvent setYaw(final float yaw) {
        this.yaw = yaw;
        return this;
    }
    
    public MotionUpdateEvent setPitch(final float pitch) {
        this.pitch = pitch;
        return this;
    }
    
    public MotionUpdateEvent setOnGround(final boolean onGround) {
        this.onGround = onGround;
        return this;
    }
    
    public MotionUpdateEvent setSneaking(final boolean sneaking) {
        this.sneaking = sneaking;
        return this;
    }
    
    public MotionUpdateEvent setSprinting(final boolean sprinting) {
        this.sprinting = sprinting;
        return this;
    }
    
    public boolean isPre() {
        return this instanceof Pre;
    }
    
    @Cancelable
    public static class Pre extends MotionUpdateEvent
    {
        public Pre(final double x, final double y, final double z, final float yaw, final float pitch, final boolean onGround, final boolean sprinting, final boolean sneaking) {
            super(x, y, z, yaw, pitch, onGround, sprinting, sneaking);
        }
    }
    
    @Cancelable
    public static class Post extends MotionUpdateEvent
    {
        public Post(final double x, final double y, final double z, final float yaw, final float pitch, final boolean onGround, final boolean sprinting, final boolean sneaking) {
            super(x, y, z, yaw, pitch, onGround, sprinting, sneaking);
        }
        
        public Post(final MotionUpdateEvent event) {
            super(event.x, event.y, event.z, event.yaw, event.pitch, event.onGround, event.sprinting, event.sneaking);
        }
    }
}
