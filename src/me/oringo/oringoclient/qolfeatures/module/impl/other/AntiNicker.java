// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.other;

import java.util.Map;
import me.oringo.oringoclient.events.WorldJoinEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.oringo.oringoclient.OringoClient;
import net.minecraft.entity.player.EntityPlayer;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.oringo.oringoclient.utils.SkyblockUtils;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import com.mojang.authlib.properties.Property;
import java.util.Base64;
import com.google.gson.JsonParser;
import java.util.concurrent.atomic.AtomicReference;
import com.mojang.authlib.GameProfile;
import java.util.UUID;
import java.util.ArrayList;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class AntiNicker extends Module
{
    public static ArrayList<UUID> nicked;
    
    public AntiNicker() {
        super("Anti Nicker", 0, Category.OTHER);
    }
    
    public static String getRealName(final GameProfile profile) {
        final AtomicReference<String> toReturn = new AtomicReference<String>("");
        JsonParser parser;
        final AtomicReference<String> atomicReference;
        profile.getProperties().entries().forEach(entry -> {
            if (entry.getKey().equals("textures")) {
                try {
                    parser = new JsonParser();
                    atomicReference.set(parser.parse(new String(Base64.getDecoder().decode(((Property)entry.getValue()).getValue()))).getAsJsonObject().get("profileName").getAsString());
                }
                catch (Exception ex) {}
            }
            return;
        });
        return toReturn.get();
    }
    
    @SubscribeEvent
    public void onWorldJoin(final EntityJoinWorldEvent e) {
        if (!this.isToggled()) {
            return;
        }
        if (e.entity instanceof EntityOtherPlayerMP && !AntiNicker.nicked.contains(e.entity.func_110124_au()) && !e.entity.equals((Object)AntiNicker.mc.field_71439_g) && !SkyblockUtils.isNPC(e.entity) && AntiNicker.mc.func_147114_u().func_175102_a(e.entity.func_110124_au()) != null && !e.entity.func_145748_c_().func_150260_c().contains(ChatFormatting.RED.toString()) && (SkyblockUtils.onSkyblock || SkyblockUtils.isInOtherGame)) {
            final String realName = getRealName(((EntityPlayer)e.entity).func_146103_bH());
            final String stipped = ChatFormatting.stripFormatting(e.entity.func_70005_c_());
            if (stipped.equals(e.entity.func_70005_c_()) && !realName.equals(stipped)) {
                AntiNicker.nicked.add(e.entity.func_110124_au());
                OringoClient.sendMessageWithPrefix((e.entity.func_145748_c_().func_150260_c().contains(ChatFormatting.OBFUSCATED.toString()) ? e.entity.func_70005_c_() : e.entity.func_145748_c_().func_150260_c()) + ChatFormatting.RESET + ChatFormatting.GRAY + " is nicked!" + ((realName.equals("") || realName.equals("Tactful")) ? "" : (" Their real name is " + realName + "!")));
            }
        }
    }
    
    @SubscribeEvent
    public void onWorldJoin(final WorldJoinEvent event) {
        AntiNicker.nicked.clear();
    }
    
    static {
        AntiNicker.nicked = new ArrayList<UUID>();
    }
}
