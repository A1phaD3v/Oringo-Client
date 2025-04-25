// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.commands.impl;

import me.oringo.oringoclient.ui.notifications.Notifications;
import me.oringo.oringoclient.commands.Command;

public class ClipCommand extends Command
{
    public ClipCommand() {
        super("clip", new String[] { "vclip" });
    }
    
    @Override
    public void execute(final String[] args) throws Exception {
        if (args.length == 1) {
            ClipCommand.mc.field_71439_g.func_70107_b(ClipCommand.mc.field_71439_g.field_70165_t, ClipCommand.mc.field_71439_g.field_70163_u + Double.parseDouble(args[0]), ClipCommand.mc.field_71439_g.field_70161_v);
        }
        else {
            Notifications.showNotification(".clip distance", 1500, Notifications.NotificationType.ERROR);
        }
    }
    
    @Override
    public String getDescription() {
        return "Clips you up x blocks";
    }
}
