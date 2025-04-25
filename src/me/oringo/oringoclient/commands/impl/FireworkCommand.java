// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.commands.impl;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import me.oringo.oringoclient.ui.notifications.Notifications;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import me.oringo.oringoclient.commands.Command;

public class FireworkCommand extends Command
{
    public FireworkCommand() {
        super("firework", new String[0]);
    }
    
    @Override
    public void execute(final String[] args) throws Exception {
        if (args.length == 2) {
            final ItemStack item = new ItemStack(Items.field_151152_bP);
            item.field_77994_a = 64;
            item.func_151001_c("crash");
            final NBTTagList value = new NBTTagList();
            final NBTTagCompound nbtTagCompound = item.serializeNBT();
            final NBTTagCompound display = nbtTagCompound.func_74775_l("tag").func_74775_l("Fireworks");
            final NBTTagList explosions = new NBTTagList();
            final NBTTagCompound exp1 = new NBTTagCompound();
            exp1.func_74782_a("Type", (NBTBase)new NBTTagByte((byte)1));
            exp1.func_74782_a("Flicker", (NBTBase)new NBTTagByte((byte)1));
            exp1.func_74782_a("Trail", (NBTBase)new NBTTagByte((byte)3));
            int[] colors = new int[Integer.parseInt(args[1])];
            for (int i = 0; i < Integer.parseInt(args[1]); ++i) {
                colors[i] = 261799 + i;
            }
            exp1.func_74783_a("Colors", colors);
            colors = new int[100];
            for (int i = 0; i < 100; ++i) {
                colors[i] = 11250603 + i;
            }
            exp1.func_74783_a("FadeColors", colors);
            for (int x = 0; x < Integer.parseInt(args[0]); ++x) {
                explosions.func_74742_a((NBTBase)exp1);
            }
            display.func_74782_a("Explosions", (NBTBase)explosions);
            nbtTagCompound.func_74775_l("tag").func_74782_a("Fireworks", (NBTBase)display);
            Notifications.showNotification("Oringo Client", "NBT Size: " + nbtTagCompound.toString().length(), 2000);
            item.deserializeNBT(nbtTagCompound);
            FireworkCommand.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new C10PacketCreativeInventoryAction(36, item));
        }
        else {
            Notifications.showNotification("Oringo Client", "/firework explosions colors", 1000);
        }
    }
    
    @Override
    public String getDescription() {
        return "Gives you a crash firework. You need to have creative";
    }
}
