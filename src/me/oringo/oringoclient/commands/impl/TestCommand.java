// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.commands.impl;

import me.oringo.oringoclient.ui.notifications.Notifications;
import me.oringo.oringoclient.commands.Command;

public class TestCommand extends Command
{
    public TestCommand() {
        super("test", new String[0]);
    }
    
    @Override
    public void execute(final String[] args) throws Exception {
        Notifications.showNotification("Test", 3000, Notifications.NotificationType.INFO);
        Notifications.showNotification("Test", 3000, Notifications.NotificationType.ERROR);
        Notifications.showNotification("Test", 3000, Notifications.NotificationType.WARNING);
    }
    
    @Override
    public String getDescription() {
        return null;
    }
}
