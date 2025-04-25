// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.other;

import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import me.oringo.oringoclient.OringoClient;
import me.oringo.oringoclient.utils.SkyblockUtils;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import net.minecraft.scoreboard.ScoreObjective;
import java.util.List;
import java.util.Collections;
import me.oringo.oringoclient.ui.notifications.Notifications;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import java.util.ArrayList;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class GuessTheBuildAFK extends Module
{
    private ArrayList<String> wordList;
    private int tips;
    private Thread guesses;
    private int period;
    private long lastGuess;
    
    public GuessTheBuildAFK() {
        super("Auto GTB", 0, Category.OTHER);
        this.wordList = new ArrayList<String>();
        this.tips = 0;
        this.guesses = null;
        this.period = 3200;
        this.lastGuess = 0L;
    }
    
    @SubscribeEvent
    public void onChat(final ClientChatReceivedEvent event) {
        if (!this.isToggled()) {
            return;
        }
        try {
            final ScoreObjective o = Minecraft.func_71410_x().field_71439_g.func_96123_co().func_96539_a(0);
            if (ChatFormatting.stripFormatting(((o == null) ? Minecraft.func_71410_x().field_71439_g.func_96123_co().func_96539_a(1) : o).func_96678_d()).contains("GUESS THE BUILD") && ChatFormatting.stripFormatting(event.message.func_150254_d()).startsWith("This game has been recorded")) {
                Minecraft.func_71410_x().field_71439_g.func_71165_d("/play build_battle_guess_the_build");
            }
        }
        catch (Exception ex) {}
        if (event.message.func_150254_d().startsWith("§eThe theme was") && this.guesses != null) {
            this.guesses.stop();
            this.guesses = null;
        }
        if (ChatFormatting.stripFormatting(event.message.func_150254_d()).startsWith(Minecraft.func_71410_x().field_71439_g.func_70005_c_() + " correctly guessed the theme!") && this.guesses != null) {
            this.guesses.stop();
            this.guesses = null;
        }
        if (event.type == 2) {
            if (event.message.func_150254_d().contains("The theme is") && event.message.func_150254_d().contains("_")) {
                if (this.wordList.isEmpty()) {
                    this.loadWords();
                }
                final int newTips = this.getTips(event.message.func_150254_d());
                if (newTips != this.tips) {
                    this.tips = newTips;
                    final String tip = ChatFormatting.stripFormatting(event.message.func_150254_d()).replaceFirst("The theme is ", "");
                    final ArrayList<String> matchingWords = this.getMatchingWords(tip);
                    if (matchingWords.size() == 1) {
                        Notifications.showNotification("Oringo Client", "Found 1 matching word! Sending: §f" + matchingWords.get(0), 2000);
                        if (this.guesses != null) {
                            this.guesses.stop();
                            this.guesses = null;
                            final ArrayList<String> list;
                            new Thread(() -> {
                                try {
                                    Thread.sleep(this.period - (System.currentTimeMillis() - this.lastGuess));
                                }
                                catch (Exception e) {
                                    e.printStackTrace();
                                }
                                Minecraft.func_71410_x().field_71439_g.func_71165_d("/ac " + list.get(0).toLowerCase());
                            }).start();
                            return;
                        }
                        Minecraft.func_71410_x().field_71439_g.func_71165_d("/ac " + matchingWords.get(0).toLowerCase());
                    }
                    else {
                        Notifications.showNotification("Oringo Client", String.format("Found %s matching words!", matchingWords.size()), 1500);
                        if (matchingWords.size() <= 15) {
                            Collections.shuffle(matchingWords);
                            final Iterator<String> iterator;
                            String matchingWord;
                            (this.guesses = new Thread(() -> {
                                matchingWords.iterator();
                                while (iterator.hasNext()) {
                                    matchingWord = iterator.next();
                                    this.lastGuess = System.currentTimeMillis();
                                    Minecraft.func_71410_x().field_71439_g.func_71165_d("/ac " + matchingWord.toLowerCase());
                                    Notifications.showNotification("Oringo Client", "Trying: §f" + matchingWord, 2000);
                                    try {
                                        Thread.sleep(this.period);
                                    }
                                    catch (Exception e2) {
                                        e2.printStackTrace();
                                    }
                                }
                            })).start();
                        }
                    }
                }
            }
            else {
                this.tips = 0;
            }
        }
    }
    
    @SubscribeEvent
    public void onGuiOpen(final GuiOpenEvent event) {
        if (!this.isToggled()) {
            return;
        }
        Minecraft mc;
        ScoreObjective o;
        new Thread(() -> {
            try {
                Thread.sleep(1000L);
                if (event.gui instanceof GuiChest) {
                    mc = Minecraft.func_71410_x();
                    o = mc.field_71439_g.func_96123_co().func_96539_a(0);
                    if (ChatFormatting.stripFormatting(((o == null) ? mc.field_71439_g.func_96123_co().func_96539_a(1) : o).func_96678_d()).contains("GUESS THE BUILD")) {
                        mc.field_71442_b.func_78753_a(((GuiChest)event.gui).field_147002_h.field_75152_c, 15, 0, 0, (EntityPlayer)mc.field_71439_g);
                        Thread.sleep(2000L);
                        KeyBinding.func_74510_a(mc.field_71474_y.field_74311_E.func_151463_i(), true);
                        Thread.sleep(2000L);
                        KeyBinding.func_74510_a(mc.field_71474_y.field_74311_E.func_151463_i(), false);
                        mc.field_71439_g.field_71071_by.field_70461_c = 1;
                        mc.field_71439_g.field_70125_A = 40.0f;
                        Thread.sleep(500L);
                        SkyblockUtils.rightClick();
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
    
    private int getTips(final String text) {
        return text.replaceAll(" ", "").replaceAll("_", "").length();
    }
    
    private void loadWords() {
        try {
            final BufferedReader br = new BufferedReader(new InputStreamReader(OringoClient.class.getClassLoader().getResourceAsStream("words.txt")));
            String line;
            while ((line = br.readLine()) != null) {
                this.wordList.add(line);
            }
            br.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            OringoClient.sendMessageWithPrefix("Couldn't load word list!");
        }
    }
    
    public ArrayList<String> getMatchingWords(final String tip) {
        final ArrayList<String> list = new ArrayList<String>();
        for (final String word : this.wordList) {
            if (word.length() == tip.length()) {
                boolean matching = true;
                for (int i = 0; i < word.length(); ++i) {
                    if (tip.charAt(i) == '_') {
                        if (word.charAt(i) != ' ') {
                            continue;
                        }
                        matching = false;
                    }
                    if (tip.charAt(i) != word.charAt(i)) {
                        matching = false;
                    }
                    if (!matching) {
                        break;
                    }
                }
                if (!matching) {
                    continue;
                }
                list.add(word);
            }
        }
        return list;
    }
}
