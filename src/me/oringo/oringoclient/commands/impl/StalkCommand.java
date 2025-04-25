// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.commands.impl;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.Entity;
import me.oringo.oringoclient.utils.RenderUtils;
import java.awt.Color;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import java.util.Iterator;
import me.oringo.oringoclient.ui.notifications.Notifications;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.Minecraft;
import java.util.UUID;
import me.oringo.oringoclient.commands.Command;

public class StalkCommand extends Command
{
    public static UUID stalking;
    
    public StalkCommand() {
        super("stalk", new String[] { "hunt" });
    }
    
    @Override
    public void execute(final String[] args) throws Exception {
        StalkCommand.stalking = null;
        if (args.length == 1) {
            for (final EntityPlayer player : Minecraft.func_71410_x().field_71441_e.field_73010_i) {
                if (player.func_70005_c_().equalsIgnoreCase(args[0])) {
                    StalkCommand.stalking = player.func_110124_au();
                    Notifications.showNotification("Oringo Client", "Enabled stalking!", 1000);
                    return;
                }
            }
            Notifications.showNotification("Player not found!", 1000, Notifications.NotificationType.ERROR);
            return;
        }
        Notifications.showNotification("Oringo Client", "Disabled Stalking!", 1000);
    }
    
    @SubscribeEvent
    public void onWorldRender(final RenderWorldLastEvent event) {
        if (StalkCommand.stalking != null) {
            for (final EntityPlayer player : StalkCommand.mc.field_71441_e.field_73010_i) {
                if (player.func_110124_au().equals(StalkCommand.stalking)) {
                    RenderUtils.tracerLine((Entity)player, event.partialTicks, 1.0f, Color.cyan);
                    break;
                }
            }
        }
    }
    
    @Override
    public String getDescription() {
        return "Shows you a player";
    }
}
