// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.commands.impl;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.item.ItemStack;
import me.oringo.oringoclient.ui.notifications.Notifications;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.init.Items;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraftforge.client.event.GuiOpenEvent;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import me.oringo.oringoclient.OringoClient;
import me.oringo.oringoclient.commands.Command;

public class ChecknameCommand extends Command
{
    public static String profileView;
    
    public ChecknameCommand() {
        super("checkname", new String[] { "shownicker", "denick", "revealname" });
    }
    
    @Override
    public void execute(final String[] args) throws Exception {
        if (args.length != 1) {
            OringoClient.sendMessageWithPrefix("/checkname [IGN]");
            return;
        }
        for (final EntityPlayer entity : ChecknameCommand.mc.field_71441_e.field_73010_i) {
            if (entity.func_70005_c_().equalsIgnoreCase(args[0])) {
                if (entity.func_70032_d((Entity)ChecknameCommand.mc.field_71439_g) > 6.0f) {
                    OringoClient.sendMessageWithPrefix("You are too far away!");
                    return;
                }
                if (ChecknameCommand.mc.field_71439_g.func_70694_bm() != null) {
                    OringoClient.sendMessageWithPrefix("Your hand must be empty!");
                    return;
                }
                ChecknameCommand.mc.field_71442_b.func_78768_b((EntityPlayer)ChecknameCommand.mc.field_71439_g, (Entity)entity);
                ChecknameCommand.profileView = args[0];
                return;
            }
        }
        OringoClient.sendMessageWithPrefix("Invalid name");
    }
    
    @SubscribeEvent
    public void onGui(final GuiOpenEvent event) {
        if (event.gui instanceof GuiChest && ChecknameCommand.profileView != null && ((ContainerChest)((GuiChest)event.gui).field_147002_h).func_85151_d().func_70005_c_().toLowerCase().startsWith(ChecknameCommand.profileView.toLowerCase())) {
            final ItemStack is;
            String name;
            new Thread(() -> {
                try {
                    Thread.sleep(100L);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                is = Minecraft.func_71410_x().field_71439_g.field_71070_bA.func_75139_a(22).func_75211_c();
                if (is != null && is.func_77973_b().equals(Items.field_151144_bL)) {
                    name = is.serializeNBT().func_74775_l("tag").func_74775_l("SkullOwner").func_74779_i("Name");
                    Minecraft.func_71410_x().field_71439_g.func_71053_j();
                    Notifications.showNotification("Oringo Client", "Real name: " + ChatFormatting.GOLD + name.replaceFirst("§", ""), 4000);
                }
                ChecknameCommand.profileView = null;
            }).start();
        }
    }
    
    @Override
    public String getDescription() {
        return null;
    }
}
