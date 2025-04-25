// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.commands.impl;

import net.minecraft.scoreboard.ScorePlayerTeam;
import java.util.Iterator;
import java.util.List;
import net.minecraft.scoreboard.Scoreboard;
import me.oringo.oringoclient.OringoClient;
import java.util.Collection;
import net.minecraft.scoreboard.Score;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import me.oringo.oringoclient.commands.Command;

public class SettingsCommand extends Command
{
    public SettingsCommand() {
        super("oringo", new String[0]);
    }
    
    @Override
    public void execute(final String[] args) throws Exception {
        if (args.length != 0 && args[0].equalsIgnoreCase("scoreboard") && SettingsCommand.mc.field_71439_g.func_96123_co() != null) {
            final StringBuilder builder = new StringBuilder();
            final Scoreboard sb = Minecraft.func_71410_x().field_71439_g.func_96123_co();
            final List<Score> list = new ArrayList<Score>(sb.func_96534_i(sb.func_96539_a(1)));
            for (final Score score : list) {
                final ScorePlayerTeam team = sb.func_96509_i(score.func_96653_e());
                final String s = team.func_96668_e() + score.func_96653_e() + team.func_96663_f();
                for (final char c : s.toCharArray()) {
                    if (c < '\u0100') {
                        builder.append(c);
                    }
                }
                builder.append("\n");
            }
            builder.append(SettingsCommand.mc.field_71439_g.func_96123_co().func_96539_a(1).func_96678_d());
            System.out.println(builder);
            return;
        }
        OringoClient.clickGui.toggle();
    }
    
    @Override
    public String getDescription() {
        return "Opens the menu";
    }
}
