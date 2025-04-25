// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.ui.notifications;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.oringo.oringoclient.utils.RenderUtils;
import java.awt.Color;
import me.oringo.oringoclient.utils.font.Fonts;
import net.minecraft.client.gui.ScaledResolution;
import me.oringo.oringoclient.OringoClient;
import org.lwjgl.opengl.GL11;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import java.util.ArrayList;

public class Notifications
{
    private static final ArrayList<Notification> notifications;
    
    @SubscribeEvent
    public void onRender(final TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            GL11.glPushMatrix();
            Notifications.notifications.removeIf(n -> n.getEnd() <= System.currentTimeMillis());
            if (Notifications.notifications.size() > 0) {
                final ScaledResolution res = new ScaledResolution(OringoClient.mc);
                float y = (float)(res.func_78328_b() - 37);
                for (int i = 0; i < Notifications.notifications.size(); ++i) {
                    final Notification notification = Notifications.notifications.get(i);
                    GL11.glPushMatrix();
                    final float width = (float)Math.max(150.0, Fonts.robotoMediumBold.getStringWidth(notification.getDescription()) + 10.0);
                    float height = 35.0f;
                    float x = res.func_78326_a() - width - 2.0f;
                    if (notification.getCurrentTime() <= 250L) {
                        if (notification.getCurrentTime() >= 100L) {
                            x += (float)((250L - notification.getCurrentTime()) / 150.0 * (width + 2.0f));
                        }
                        else {
                            x += 10000.0f;
                        }
                    }
                    else if (notification.getEnd() - System.currentTimeMillis() <= 250L) {
                        final long time = notification.getEnd() - System.currentTimeMillis();
                        if (time >= 100L) {
                            x += (float)((250L - time) / 150.0 * (width + 2.0f));
                        }
                        else {
                            x += 10000.0f;
                        }
                    }
                    RenderUtils.drawRoundedRect(x, y, x + width, y + height, 3.0, new Color(21, 21, 21, 90).getRGB());
                    Fonts.robotoBig.drawSmoothStringWithShadow(notification.getTitle(), x + 3.0f, y + 5.0f, notification.getColor().getRGB());
                    Fonts.robotoMediumBold.drawSmoothStringWithShadow(notification.getDescription(), x + 5.0f, y + 10.0f + Fonts.robotoBig.getHeight(), Color.white.getRGB());
                    RenderUtils.drawRect(x, y + height - 2.0f, x + width * notification.getCurrentTime() / notification.getTime(), y + height, notification.getColor().getRGB());
                    if (notification.getCurrentTime() < 100L) {
                        height *= (float)(notification.getCurrentTime() / 100.0);
                    }
                    else if (notification.getEnd() - System.currentTimeMillis() < 100L) {
                        final long time = notification.getEnd() - System.currentTimeMillis();
                        height *= (float)(time / 100.0);
                    }
                    y -= height + 1.0f;
                    GL11.glPopMatrix();
                }
            }
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glPopMatrix();
        }
    }
    
    public static void showNotification(final String description, final int time) {
        showNotification("Oringo Client", description, time);
    }
    
    public static void showNotification(final String title, final String description, final int time) {
        showNotification(description, time, NotificationType.INFO);
    }
    
    public static void showNotification(final String description, final int time, final NotificationType type) {
        Notifications.notifications.add(new Notification(description, time, type));
    }
    
    static {
        notifications = new ArrayList<Notification>();
    }
    
    private static class Notification
    {
        private final NotificationType type;
        private final String description;
        private final long end;
        private final int time;
        private final int colorIndex;
        
        public Notification(final String description, int time, final NotificationType type) {
            time += 500;
            this.description = description;
            this.end = System.currentTimeMillis() + time;
            this.time = time;
            this.type = type;
            this.colorIndex = Notifications.notifications.size() + 1;
        }
        
        public int getTime() {
            return this.time;
        }
        
        public String getTitle() {
            return this.type.getName();
        }
        
        public long getCurrentTime() {
            return System.currentTimeMillis() - this.end + this.time;
        }
        
        public String getDescription() {
            return this.description;
        }
        
        public long getEnd() {
            return this.end;
        }
        
        public Color getColor() {
            return this.type.getColor(this.colorIndex);
        }
    }
    
    public enum NotificationType
    {
        WARNING("Warning", new Color(255, 204, 0)), 
        INFO("Notification", Color.white), 
        ERROR("Error", new Color(208, 3, 3));
        
        private final String name;
        private final Color color;
        
        private NotificationType(final String name, final Color color) {
            this.name = name;
            this.color = color;
        }
        
        public String getName() {
            return this.name;
        }
        
        public Color getColor(final int i) {
            return (this == NotificationType.INFO) ? OringoClient.clickGui.getColor(i) : this.color;
        }
    }
}
