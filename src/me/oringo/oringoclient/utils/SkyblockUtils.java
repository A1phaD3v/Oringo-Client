// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.utils;

import net.minecraft.client.network.NetworkPlayerInfo;
import java.awt.Color;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import java.util.Locale;
import java.lang.reflect.Method;
import net.minecraft.entity.Entity;
import net.minecraft.scoreboard.ScoreObjective;
import java.util.Iterator;
import java.util.List;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.scoreboard.ScorePlayerTeam;
import java.util.Collection;
import net.minecraft.scoreboard.Score;
import java.util.ArrayList;
import com.google.common.collect.Iterables;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import me.oringo.oringoclient.events.WorldJoinEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.oringo.oringoclient.ui.notifications.Notifications;
import me.oringo.oringoclient.OringoClient;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.network.play.server.S02PacketChat;
import me.oringo.oringoclient.events.PacketReceivedEvent;
import net.minecraft.client.Minecraft;

public class SkyblockUtils
{
    private static final Minecraft mc;
    public static boolean inDungeon;
    public static boolean isInOtherGame;
    public static boolean onSkyblock;
    public static boolean inBlood;
    public static boolean inP3;
    
    public static boolean isTerminal(final String name) {
        return name.contains("Correct all the panes!") || name.contains("Navigate the maze!") || name.contains("Click in order!") || name.contains("What starts with:") || name.contains("Select all the") || name.contains("Change all to same color!") || name.contains("Click the button on time!");
    }
    
    @SubscribeEvent
    public void onChat(final PacketReceivedEvent event) {
        if (event.packet instanceof S02PacketChat) {
            if (ChatFormatting.stripFormatting(((S02PacketChat)event.packet).func_148915_c().func_150254_d()).startsWith("[BOSS] The Watcher: ") && !SkyblockUtils.inBlood) {
                SkyblockUtils.inBlood = true;
                if (OringoClient.bloodAimbot.isToggled()) {
                    Notifications.showNotification("Oringo Client", "Started Camp", 1000);
                }
            }
            if (ChatFormatting.stripFormatting(((S02PacketChat)event.packet).func_148915_c().func_150254_d()).equals("[BOSS] The Watcher: You have proven yourself. You may pass.")) {
                SkyblockUtils.inBlood = false;
                if (OringoClient.bloodAimbot.isToggled()) {
                    Notifications.showNotification("Oringo Client", "Stopped camp", 1000);
                }
            }
            if (ChatFormatting.stripFormatting(((S02PacketChat)event.packet).func_148915_c().func_150254_d()).startsWith("[BOSS] Necron: I hope you're in shape. BETTER GET RUNNING!")) {
                SkyblockUtils.inP3 = true;
            }
            if (ChatFormatting.stripFormatting(((S02PacketChat)event.packet).func_148915_c().func_150254_d()).startsWith("[BOSS] Necron: THAT'S IT YOU HAVE DONE IT! MY ENTIRE FACTORY IS RUINED! ARE YOU HAPPY?!")) {
                SkyblockUtils.inP3 = false;
            }
        }
    }
    
    @SubscribeEvent
    public void onWorldLoad(final WorldJoinEvent event) {
        SkyblockUtils.inBlood = false;
        SkyblockUtils.inP3 = false;
    }
    
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (SkyblockUtils.mc.field_71441_e != null && event.phase == TickEvent.Phase.START) {
            SkyblockUtils.inDungeon = (hasLine("Cleared:") || hasLine("Start"));
            SkyblockUtils.isInOtherGame = isInOtherGame();
            SkyblockUtils.onSkyblock = isOnSkyBlock();
        }
    }
    
    public static <T> T firstOrNull(final Iterable<T> iterable) {
        return (T)Iterables.getFirst((Iterable)iterable, (Object)null);
    }
    
    public static boolean hasScoreboardTitle(final String title) {
        return SkyblockUtils.mc.field_71439_g != null && SkyblockUtils.mc.field_71439_g.func_96123_co() != null && SkyblockUtils.mc.field_71439_g.func_96123_co().func_96539_a(1) != null && ChatFormatting.stripFormatting(SkyblockUtils.mc.field_71439_g.func_96123_co().func_96539_a(1).func_96678_d()).equalsIgnoreCase(title);
    }
    
    public static boolean isInOtherGame() {
        try {
            final Scoreboard sb = SkyblockUtils.mc.field_71439_g.func_96123_co();
            final List<Score> list = new ArrayList<Score>(sb.func_96534_i(sb.func_96539_a(1)));
            for (final Score score : list) {
                final ScorePlayerTeam team = sb.func_96509_i(score.func_96653_e());
                final String s = ChatFormatting.stripFormatting(ScorePlayerTeam.func_96667_a((Team)team, score.func_96653_e()));
                if (s.contains("Map")) {
                    return true;
                }
            }
        }
        catch (Exception ex) {}
        return false;
    }
    
    public static boolean isOnSkyBlock() {
        try {
            final ScoreObjective titleObjective = SkyblockUtils.mc.field_71439_g.func_96123_co().func_96539_a(1);
            if (SkyblockUtils.mc.field_71439_g.func_96123_co().func_96539_a(0) != null) {
                return ChatFormatting.stripFormatting(SkyblockUtils.mc.field_71439_g.func_96123_co().func_96539_a(0).func_96678_d()).contains("SKYBLOCK");
            }
            return ChatFormatting.stripFormatting(SkyblockUtils.mc.field_71439_g.func_96123_co().func_96539_a(1).func_96678_d()).contains("SKYBLOCK");
        }
        catch (Exception e) {
            return false;
        }
    }
    
    public static boolean hasLine(final String line) {
        if (SkyblockUtils.mc.field_71439_g != null && SkyblockUtils.mc.field_71439_g.func_96123_co() != null && SkyblockUtils.mc.field_71439_g.func_96123_co().func_96539_a(1) != null) {
            final Scoreboard sb = Minecraft.func_71410_x().field_71439_g.func_96123_co();
            final List<Score> list = new ArrayList<Score>(sb.func_96534_i(sb.func_96539_a(1)));
            for (final Score score : list) {
                final ScorePlayerTeam team = sb.func_96509_i(score.func_96653_e());
                if (team != null) {
                    final String s = ChatFormatting.stripFormatting(team.func_96668_e() + score.func_96653_e() + team.func_96663_f());
                    final StringBuilder builder = new StringBuilder();
                    for (final char c : s.toCharArray()) {
                        if (c < '\u0100') {
                            builder.append(c);
                        }
                    }
                    if (builder.toString().toLowerCase().contains(line.toLowerCase())) {
                        return true;
                    }
                    continue;
                }
            }
        }
        return false;
    }
    
    public static boolean isMiniboss(final Entity entity) {
        return entity.func_70005_c_().equals("Shadow Assassin") || entity.func_70005_c_().equals("Lost Adventurer") || entity.func_70005_c_().equals("Diamond Guy");
    }
    
    public static void click() {
        try {
            Method clickMouse;
            try {
                clickMouse = Minecraft.class.getDeclaredMethod("func_147116_af", (Class<?>[])new Class[0]);
            }
            catch (NoSuchMethodException e2) {
                clickMouse = Minecraft.class.getDeclaredMethod("clickMouse", (Class<?>[])new Class[0]);
            }
            clickMouse.setAccessible(true);
            clickMouse.invoke(Minecraft.func_71410_x(), new Object[0]);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static boolean anyTab(final String s) {
        return Minecraft.func_71410_x().func_147114_u().func_175106_d().stream().anyMatch(player -> player.func_178854_k() != null && ChatFormatting.stripFormatting(player.func_178854_k().func_150254_d()).toLowerCase().contains(s.toLowerCase(Locale.ROOT)));
    }
    
    public static boolean isNPC(final Entity entity) {
        if (!(entity instanceof EntityOtherPlayerMP)) {
            return false;
        }
        final EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
        return ChatFormatting.stripFormatting(entity.func_145748_c_().func_150260_c()).startsWith("[NPC]") || (entity.func_110124_au().version() == 2 && entityLivingBase.func_110143_aJ() == 20.0f && entityLivingBase.func_110138_aP() == 20.0f);
    }
    
    public static void rightClick() {
        try {
            Method rightClickMouse = null;
            try {
                rightClickMouse = Minecraft.class.getDeclaredMethod("func_147121_ag", (Class<?>[])new Class[0]);
            }
            catch (NoSuchMethodException e2) {
                rightClickMouse = Minecraft.class.getDeclaredMethod("rightClickMouse", (Class<?>[])new Class[0]);
            }
            rightClickMouse.setAccessible(true);
            rightClickMouse.invoke(Minecraft.func_71410_x(), new Object[0]);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static String getDisplayName(final ItemStack item) {
        if (item == null) {
            return "null";
        }
        return item.func_82833_r();
    }
    
    public static Color rainbow(final int delay) {
        double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
        rainbowState %= 360.0;
        return Color.getHSBColor((float)(rainbowState / 360.0), 1.0f, 1.0f);
    }
    
    public static int getPing() {
        final NetworkPlayerInfo networkPlayerInfo = SkyblockUtils.mc.func_147114_u().func_175102_a(Minecraft.func_71410_x().field_71439_g.func_110124_au());
        return (networkPlayerInfo == null) ? 0 : networkPlayerInfo.func_178853_c();
    }
    
    static {
        mc = Minecraft.func_71410_x();
    }
}
