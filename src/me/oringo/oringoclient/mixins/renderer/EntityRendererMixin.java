// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.mixins.renderer;

import net.minecraft.client.multiplayer.PlayerControllerMP;
import me.oringo.oringoclient.utils.MathUtil;
import net.minecraft.util.Vec3;
import org.spongepowered.asm.mixin.injection.Inject;
import me.oringo.oringoclient.OringoClient;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraft.potion.Potion;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ EntityRenderer.class })
public class EntityRendererMixin
{
    @Shadow
    private float field_78491_C;
    @Shadow
    private float field_78490_B;
    
    @Redirect(method = { "setupFog" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;isPotionActive(Lnet/minecraft/potion/Potion;)Z"))
    public boolean removeBlindness(final EntityLivingBase instance, final Potion potionIn) {
        return false;
    }
    
    @Inject(method = { "hurtCameraEffect" }, at = { @At("HEAD") }, cancellable = true)
    public void hurtCam(final float entitylivingbase, final CallbackInfo ci) {
        if (OringoClient.camera.noHurtCam.isEnabled() && OringoClient.camera.isToggled()) {
            ci.cancel();
        }
    }
    
    @Redirect(method = { "orientCamera" }, at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/EntityRenderer;thirdPersonDistanceTemp:F"))
    public float thirdPersonDistanceTemp(final EntityRenderer instance) {
        return (OringoClient.camera.isToggled() && !OringoClient.camera.smoothF5.isEnabled()) ? ((float)OringoClient.camera.cameraDistance.getValue()) : this.field_78491_C;
    }
    
    @Redirect(method = { "orientCamera" }, at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/EntityRenderer;thirdPersonDistance:F"))
    public float thirdPersonDistance(final EntityRenderer instance) {
        return (OringoClient.camera.isToggled() && !OringoClient.camera.smoothF5.isEnabled()) ? ((float)OringoClient.camera.cameraDistance.getValue()) : this.field_78490_B;
    }
    
    @Redirect(method = { "orientCamera" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Vec3;distanceTo(Lnet/minecraft/util/Vec3;)D"))
    public double onCamera(final Vec3 instance, final Vec3 vec) {
        return (OringoClient.camera.isToggled() && OringoClient.camera.cameraClip.isEnabled()) ? OringoClient.camera.cameraDistance.getValue() : instance.func_72438_d(vec);
    }
    
    @Inject(method = { "updateRenderer" }, at = { @At("RETURN") })
    public void onUpdate(final CallbackInfo ci) {
        if (OringoClient.camera.isToggled() && OringoClient.camera.smoothF5.isEnabled()) {
            if (OringoClient.mc.field_71474_y.field_74320_O > 0) {
                this.field_78490_B = (float)MathUtil.clamp(this.field_78490_B + OringoClient.camera.speed.getValue(), OringoClient.camera.cameraDistance.getValue(), 0.0);
            }
            else {
                final float n = (float)OringoClient.camera.startSize.getValue();
                this.field_78491_C = n;
                this.field_78490_B = n;
            }
        }
    }
    
    @Redirect(method = { "getMouseOver" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/PlayerControllerMP;getBlockReachDistance()F"))
    private float getBlockReachDistance(final PlayerControllerMP instance) {
        return OringoClient.reach.isToggled() ? ((float)OringoClient.reach.blockReach.getValue()) : OringoClient.mc.field_71442_b.func_78757_d();
    }
    
    @Redirect(method = { "getMouseOver" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Vec3;distanceTo(Lnet/minecraft/util/Vec3;)D", ordinal = 2))
    private double distanceTo(final Vec3 instance, final Vec3 vec) {
        return (OringoClient.reach.isToggled() && instance.func_72438_d(vec) <= OringoClient.reach.reach.getValue()) ? 0.0 : instance.func_72438_d(vec);
    }
}
