// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.commands.impl;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import me.oringo.oringoclient.events.PacketReceivedEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C01PacketChatMessage;
import me.oringo.oringoclient.utils.MilliTimer;
import me.oringo.oringoclient.commands.Command;

public class WardrobeCommand extends Command
{
    private int slot;
    private MilliTimer timeout;
    
    public WardrobeCommand() {
        super("wd", new String[] { "wardrobe" });
        this.slot = -1;
        this.timeout = new MilliTimer();
    }
    
    @Override
    public void execute(final String[] args) throws Exception {
        if (args.length > 0) {
            this.slot = Math.min(Math.max(Integer.parseInt(args[0]), 1), 18);
            WardrobeCommand.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C01PacketChatMessage("/pets"));
            this.timeout.reset();
        }
        else {
            WardrobeCommand.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C01PacketChatMessage("/wd"));
        }
    }
    
    @SubscribeEvent
    public void onPacket(final PacketReceivedEvent event) {
        if (this.slot != -1 && event.packet instanceof S2DPacketOpenWindow) {
            if (this.timeout.hasTimePassed(2500L)) {
                this.slot = -1;
                return;
            }
            if (((S2DPacketOpenWindow)event.packet).func_179840_c().func_150254_d().startsWith("Pets")) {
                final int windowId = ((S2DPacketOpenWindow)event.packet).func_148901_c();
                WardrobeCommand.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C0EPacketClickWindow(windowId, 48, 0, 3, (ItemStack)null, (short)0));
                WardrobeCommand.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C0EPacketClickWindow(windowId + 1, 32, 0, 3, (ItemStack)null, (short)0));
                if (this.slot <= 9) {
                    WardrobeCommand.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C0EPacketClickWindow(windowId + 2, 35 + this.slot, 0, 0, (ItemStack)null, (short)0));
                    WardrobeCommand.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C0DPacketCloseWindow(windowId + 2));
                }
                else {
                    WardrobeCommand.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C0EPacketClickWindow(windowId + 2, 53, 0, 3, (ItemStack)null, (short)0));
                    WardrobeCommand.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C0EPacketClickWindow(windowId + 3, 35 + this.slot, 0, 0, (ItemStack)null, (short)0));
                    WardrobeCommand.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C0DPacketCloseWindow(windowId + 3));
                }
                event.setCanceled(true);
            }
            else if (((S2DPacketOpenWindow)event.packet).func_179840_c().func_150254_d().startsWith("SkyBlock Menu")) {
                event.setCanceled(true);
            }
            else if (((S2DPacketOpenWindow)event.packet).func_179840_c().func_150254_d().startsWith("Wardrobe")) {
                event.setCanceled(true);
                this.slot = -1;
            }
        }
    }
    
    @Override
    public String getDescription() {
        return "Instant wardrobe";
    }
}
