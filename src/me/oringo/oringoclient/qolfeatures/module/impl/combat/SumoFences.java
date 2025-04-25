// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.combat;

import java.util.Iterator;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraftforge.client.event.GuiOpenEvent;
import java.io.IOException;
import java.util.Random;
import java.awt.Color;
import me.oringo.oringoclient.utils.DiscordWebhook;
import me.oringo.oringoclient.OringoClient;
import me.oringo.oringoclient.utils.ReflectionUtils;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.oringo.oringoclient.qolfeatures.module.impl.macro.AutoSumoBot;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.init.Blocks;
import net.minecraft.client.Minecraft;
import me.oringo.oringoclient.utils.SkyblockUtils;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import java.util.Arrays;
import java.util.List;
import net.minecraft.util.BlockPos;
import java.util.ArrayList;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class SumoFences extends Module
{
    private boolean sumo;
    private ArrayList<BlockPos> posList;
    private List<String> win;
    private List<String> lose;
    private String winTime;
    private static int shouldReconnect;
    private static int wins;
    private static int winstreak;
    private static int lost;
    
    public SumoFences() {
        super("Sumo Fences", 0, Category.COMBAT);
        this.sumo = false;
        this.posList = new ArrayList<BlockPos>();
        this.win = Arrays.asList("gg", "gf");
        this.lose = Arrays.asList("gg", "gf", "how");
        this.winTime = "";
    }
    
    @SubscribeEvent
    public void onSumoStart(final TickEvent.ClientTickEvent event) {
        try {
            if (this.isToggled()) {
                if (SkyblockUtils.hasLine("Mode: Sumo") && SkyblockUtils.hasLine("Opponent:")) {
                    if (!this.sumo) {
                        this.sumo = true;
                        final Minecraft mc = Minecraft.func_71410_x();
                        BlockPos pos = mc.field_71439_g.func_180425_c();
                        int i = 0;
                        while (i++ != 10) {
                            if (Minecraft.func_71410_x().field_71441_e.func_180495_p(pos = pos.func_177977_b()).func_177230_c().equals(Blocks.field_150350_a)) {
                                continue;
                            }
                            final BlockPos finalPos = pos;
                            final BlockPos blockPos;
                            new Thread(() -> {
                                try {
                                    Thread.sleep(1000L);
                                }
                                catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                this.sumo = false;
                                this.posList.clear();
                                this.checkBlock(blockPos.func_177977_b());
                            }).start();
                        }
                    }
                }
                else {
                    if (this.sumo) {
                        this.posList.clear();
                    }
                    this.sumo = false;
                }
            }
        }
        catch (Exception ex) {}
    }
    
    @SubscribeEvent
    public void onChat(final ClientChatReceivedEvent event) {
        if (AutoSumoBot.thread == null) {
            return;
        }
        if (ChatFormatting.stripFormatting(event.message.func_150254_d()).contains("                           Sumo Duel - ")) {
            this.winTime = ChatFormatting.stripFormatting(event.message.func_150254_d()).replaceFirst("                           Sumo Duel - ", "");
        }
        if (event.message.func_150254_d().contains("§f§lMelee Accuracy")) {
            String title;
            DiscordWebhook webhook;
            DiscordWebhook.EmbedObject embed;
            DiscordWebhook webhook2;
            DiscordWebhook.EmbedObject embed2;
            DiscordWebhook webhook3;
            DiscordWebhook.EmbedObject embed3;
            final Exception ex;
            Exception e;
            new Thread(() -> {
                try {
                    Thread.sleep(250L);
                    if (Minecraft.func_71410_x().field_71439_g.field_71075_bZ.field_75100_b) {
                        title = (String)ReflectionUtils.getFieldByName(Minecraft.func_71410_x().field_71456_v, "field_175201_x");
                        if (title != null && title.toUpperCase().contains("DRAW")) {
                            webhook = new DiscordWebhook(OringoClient.autoSumo.webhook.getValue());
                            webhook.setUsername("AutoSumo bot");
                            webhook.setAvatarUrl("https://cdn.discordapp.com/icons/913088401262137424/496d604510a63242db77526d8bfab9fa.png");
                            embed = new DiscordWebhook.EmbedObject();
                            embed.setTitle("Draw").setDescription("Opponent: " + AutoSumoBot.target.func_70005_c_() + " Time: " + this.winTime).setColor(Color.ORANGE);
                            webhook.addEmbed(embed);
                            webhook.execute();
                            return;
                        }
                        else {
                            Minecraft.func_71410_x().field_71439_g.func_71165_d("/ac " + this.lose.get(new Random().nextInt(this.lose.size())));
                            webhook2 = new DiscordWebhook(OringoClient.autoSumo.webhook.getValue());
                            webhook2.setUsername("AutoSumo bot");
                            webhook2.setAvatarUrl("https://cdn.discordapp.com/icons/913088401262137424/496d604510a63242db77526d8bfab9fa.png");
                            embed2 = new DiscordWebhook.EmbedObject();
                            SumoFences.winstreak = 0;
                            ++SumoFences.lost;
                            embed2.setTitle("Lost").setDescription("Opponent: " + AutoSumoBot.target.func_70005_c_() + " Time: " + this.winTime + " Loses this session: " + SumoFences.lost).addField("Blacklist", " Added " + AutoSumoBot.target.func_110124_au().toString() + " to blacklist!", false).setColor(Color.RED);
                            webhook2.addEmbed(embed2);
                            webhook2.execute();
                        }
                    }
                    else {
                        Minecraft.func_71410_x().field_71439_g.func_71165_d("/ac " + this.win.get(new Random().nextInt(this.win.size())));
                        webhook3 = new DiscordWebhook(OringoClient.autoSumo.webhook.getValue());
                        webhook3.setUsername("AutoSumo bot");
                        webhook3.setAvatarUrl("https://cdn.discordapp.com/icons/913088401262137424/496d604510a63242db77526d8bfab9fa.png");
                        embed3 = new DiscordWebhook.EmbedObject();
                        ++SumoFences.wins;
                        ++SumoFences.winstreak;
                        embed3.setTitle("WIN").setDescription("Opponent: " + AutoSumoBot.target.func_70005_c_() + " Time: " + this.winTime + " Wins: " + SumoFences.wins + " winstreak: " + SumoFences.winstreak).setColor(Color.GREEN);
                        webhook3.addEmbed(embed3);
                        webhook3.execute();
                    }
                    Thread.sleep(100L);
                }
                catch (InterruptedException | IOException ex2) {
                    e = ex;
                    e.printStackTrace();
                }
                Minecraft.func_71410_x().field_71439_g.func_71165_d("/play duels_sumo_duel");
            }).start();
        }
    }
    
    @SubscribeEvent
    public void onDisconnect(final GuiOpenEvent event) {
        if (event.gui instanceof GuiDisconnected && AutoSumoBot.thread != null) {
            final DiscordWebhook webhook = new DiscordWebhook(OringoClient.autoSumo.webhook.getValue());
            webhook.setUsername("AutoSumo bot");
            webhook.setAvatarUrl("https://cdn.discordapp.com/icons/913088401262137424/496d604510a63242db77526d8bfab9fa.png");
            webhook.setContent("Bot Disconnected! <@884509916868517898>");
            SumoFences.shouldReconnect = 2000;
            try {
                webhook.execute();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    @SubscribeEvent
    public void reconnect(final TickEvent.ClientTickEvent event) {
        if (SumoFences.shouldReconnect-- == 0 && AutoSumoBot.thread != null) {
            SumoFences.mc.func_147108_a((GuiScreen)new GuiConnecting((GuiScreen)new GuiMainMenu(), SumoFences.mc, new ServerData("Hypixel", "play.Hypixel.net", false)));
        }
    }
    
    private void checkBlock(final BlockPos pos) {
        for (final BlockPos blockPos : Arrays.asList(pos.func_177976_e(), pos.func_177978_c().func_177974_f(), pos.func_177978_c().func_177976_e(), pos.func_177968_d().func_177976_e(), pos.func_177968_d().func_177974_f(), pos.func_177974_f(), pos.func_177978_c(), pos.func_177968_d())) {
            if (Minecraft.func_71410_x().field_71441_e.func_180495_p(blockPos).func_177230_c().equals(Blocks.field_150350_a)) {
                Minecraft.func_71410_x().field_71441_e.func_175656_a(blockPos.func_177984_a().func_177984_a().func_177984_a(), Blocks.field_150350_a.func_176223_P());
                Minecraft.func_71410_x().field_71441_e.func_175656_a(blockPos.func_177984_a().func_177984_a().func_177984_a(), Blocks.field_180407_aO.func_176223_P());
                Minecraft.func_71410_x().field_71441_e.func_175656_a(blockPos.func_177984_a().func_177984_a().func_177984_a().func_177984_a(), Blocks.field_180407_aO.func_176223_P());
            }
            else {
                if (this.posList.contains(blockPos)) {
                    continue;
                }
                this.posList.add(blockPos);
                this.checkBlock(blockPos);
            }
        }
    }
    
    static {
        SumoFences.shouldReconnect = -1;
        SumoFences.wins = -1;
        SumoFences.winstreak = 0;
        SumoFences.lost = 0;
    }
}
