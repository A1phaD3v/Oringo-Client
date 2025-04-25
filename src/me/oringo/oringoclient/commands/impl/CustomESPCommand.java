// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.commands.impl;

import java.util.Iterator;
import java.awt.Color;
import me.oringo.oringoclient.qolfeatures.module.impl.render.Gui;
import me.oringo.oringoclient.OringoClient;
import me.oringo.oringoclient.qolfeatures.module.impl.render.CustomESP;
import me.oringo.oringoclient.commands.Command;

public class CustomESPCommand extends Command
{
    public CustomESPCommand() {
        super("esp", new String[] { "customesp" });
    }
    
    @Override
    public void execute(final String[] args) throws Exception {
        if (args.length == 0) {
            for (final String name : CustomESP.names.keySet()) {
                OringoClient.sendMessageWithPrefix(name);
            }
            OringoClient.sendMessageWithPrefix(String.format("%s%s add/remove", Gui.commandPrefix.getValue(), this.getNames()[0]));
        }
        else {
            final String s = args[0];
            switch (s) {
                case "add": {
                    if (args.length != 3) {
                        OringoClient.sendMessageWithPrefix(String.format("Usage: %s%s add name color", Gui.commandPrefix.getValue(), this.getNames()[0]));
                        break;
                    }
                    if (!CustomESP.names.containsKey(args[1])) {
                        CustomESP.names.put(args[1].toLowerCase(), Color.decode(args[2]));
                        break;
                    }
                    OringoClient.sendMessageWithPrefix("Name already added");
                    break;
                }
                case "remove": {
                    if (args.length == 2) {
                        CustomESP.names.remove(args[1]);
                        break;
                    }
                    break;
                }
                default: {
                    for (final String name2 : CustomESP.names.keySet()) {
                        OringoClient.sendMessageWithPrefix(name2);
                    }
                    break;
                }
            }
        }
    }
    
    @Override
    public String getDescription() {
        return "Adds or removes names to Custom ESP module";
    }
}
