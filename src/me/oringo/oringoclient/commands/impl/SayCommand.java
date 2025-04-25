// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.commands.impl;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C01PacketChatMessage;
import joptsimple.internal.Strings;
import me.oringo.oringoclient.commands.Command;

public class SayCommand extends Command
{
    public SayCommand() {
        super("say", new String[0]);
    }
    
    @Override
    public void execute(final String[] args) throws Exception {
        SayCommand.mc.func_147114_u().func_147297_a((Packet)new C01PacketChatMessage(Strings.join(args, " ")));
    }
    
    @Override
    public String getDescription() {
        return null;
    }
}
