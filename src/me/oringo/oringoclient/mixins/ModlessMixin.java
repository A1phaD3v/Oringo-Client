// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.mixins;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.client.Minecraft;
import me.oringo.oringoclient.OringoClient;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraftforge.fml.common.ModContainer;
import java.util.List;
import org.spongepowered.asm.mixin.Shadow;
import java.util.Map;
import net.minecraftforge.fml.common.network.handshake.FMLHandshakeMessage;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ FMLHandshakeMessage.ModList.class })
public class ModlessMixin
{
    @Shadow
    private Map<String, String> modTags;
    
    @Inject(method = { "<init>(Ljava/util/List;)V" }, at = { @At("RETURN") })
    public void test(final List<ModContainer> modList, final CallbackInfo ci) {
        if (!OringoClient.modless.isToggled()) {
            return;
        }
        try {
            if (Minecraft.func_71410_x().func_71356_B()) {
                return;
            }
        }
        catch (Exception e) {
            return;
        }
        this.modTags.entrySet().removeIf(mod -> !mod.getKey().equalsIgnoreCase("fml") && !mod.getKey().equalsIgnoreCase("forge") && !mod.getKey().equalsIgnoreCase("mcp"));
    }
}
