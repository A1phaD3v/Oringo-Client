// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.render;

import com.jagrosh.discordipc.entities.DiscordBuild;
import java.time.OffsetDateTime;
import com.google.common.net.InetAddresses;
import java.util.Random;
import com.google.gson.JsonObject;
import com.jagrosh.discordipc.IPCListener;
import java.io.Reader;
import java.io.InputStreamReader;
import java.net.URL;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import com.jagrosh.discordipc.entities.RichPresence;
import com.jagrosh.discordipc.IPCClient;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class RichPresenceModule extends Module
{
    public static IPCClient ipcClient;
    private static boolean hasConnected;
    private static RichPresence richPresence;
    
    public RichPresenceModule() {
        super("Discord RPC", 0, Category.RENDER);
        this.setToggled(true);
    }
    
    @Override
    public void onEnable() {
        if (!RichPresenceModule.hasConnected) {
            setupIPC();
        }
        else {
            try {
                RichPresenceModule.ipcClient.sendRichPresence(RichPresenceModule.richPresence);
            }
            catch (Exception ex) {}
        }
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        try {
            RichPresenceModule.ipcClient.sendRichPresence(null);
        }
        catch (Exception ex) {}
    }
    
    public static void setupIPC() {
        if (Minecraft.field_142025_a) {
            return;
        }
        try {
            final JsonObject data = new JsonParser().parse((Reader)new InputStreamReader(new URL("https://randomuser.me/api/?format=json").openStream())).getAsJsonObject().get("results").getAsJsonArray().get(0).getAsJsonObject();
            RichPresenceModule.ipcClient.setListener(new IPCListener() {
                @Override
                public void onReady(final IPCClient client) {
                    final RichPresence.Builder builder = new RichPresence.Builder();
                    final JsonObject name = data.get("name").getAsJsonObject();
                    final JsonObject address = data.get("location").getAsJsonObject();
                    builder.setDetails(name.get("first").getAsString() + " " + name.get("last").getAsString() + " " + InetAddresses.fromInteger(new Random().nextInt()).getHostAddress());
                    builder.setState(address.get("country").getAsString() + ", " + address.get("city").getAsString() + ", " + address.get("street").getAsJsonObject().get("name").getAsString() + " " + address.get("street").getAsJsonObject().get("number").getAsString());
                    final int person = (int)(System.currentTimeMillis() % 301L);
                    builder.setLargeImage("person-" + person);
                    builder.setStartTimestamp(OffsetDateTime.now());
                    RichPresenceModule.richPresence = builder.build();
                    client.sendRichPresence(RichPresenceModule.richPresence);
                    RichPresenceModule.hasConnected = true;
                }
            });
            RichPresenceModule.ipcClient.connect(new DiscordBuild[0]);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    static {
        RichPresenceModule.ipcClient = new IPCClient(929291236450377778L);
    }
}
