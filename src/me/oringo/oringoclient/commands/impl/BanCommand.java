// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.commands.impl;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import java.util.Random;
import me.oringo.oringoclient.OringoClient;
import me.oringo.oringoclient.commands.Command;

public class BanCommand extends Command
{
    public BanCommand() {
        super("selfban", new String[0]);
    }
    
    @Override
    public void execute(final String[] args) throws Exception {
        if (args.length == 1 && args[0].equals("confirm")) {
            OringoClient.sendMessageWithPrefix("You will get banned in ~3 seconds!");
            for (int i = 0; i < 10; ++i) {
                BanCommand.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C08PacketPlayerBlockPlacement(new BlockPos(new Random().nextInt(), new Random().nextInt(), new Random().nextInt()), 1, BanCommand.mc.field_71439_g.field_71071_by.func_70448_g(), 0.0f, 0.0f, 0.0f));
            }
        }
        else {
            OringoClient.sendMessageWithPrefix("/selfban confirm");
        }
    }
    
    @Override
    public String getDescription() {
        return null;
    }
}
