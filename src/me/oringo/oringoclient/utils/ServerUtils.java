// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.utils;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.oringo.oringoclient.OringoClient;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

public class ServerUtils
{
    public static ServerUtils instance;
    private static boolean onHypixel;
    
    @SubscribeEvent
    public void onPacketRecived(final FMLNetworkEvent.ClientConnectedToServerEvent event) {
        if (!event.isLocal) {
            ServerUtils.onHypixel = OringoClient.mc.func_147104_D().field_78845_b.toLowerCase().contains("hypixel");
        }
    }
    
    @SubscribeEvent
    public void onDisconnect(final FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        ServerUtils.onHypixel = false;
        System.out.println("Detected leaving hypixel");
    }
    
    public static boolean isOnHypixel() {
        return ServerUtils.onHypixel;
    }
    
    static {
        ServerUtils.instance = new ServerUtils();
    }
}
