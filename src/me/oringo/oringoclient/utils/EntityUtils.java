// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.utils;

import net.minecraft.entity.EntityLivingBase;
import java.util.Iterator;
import net.minecraft.scoreboard.Score;
import me.oringo.oringoclient.OringoClient;
import net.minecraft.entity.player.EntityPlayer;

public class EntityUtils
{
    private static boolean isOnTeam(final EntityPlayer player) {
        for (final Score score : OringoClient.mc.field_71439_g.func_96123_co().func_96528_e()) {
            if (score.func_96645_d().func_96679_b().equals("health") && score.func_96653_e().contains(player.func_70005_c_())) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isTeam(final EntityLivingBase e2) {
        if (!(e2 instanceof EntityPlayer) || e2.func_145748_c_().func_150260_c().length() < 4) {
            return false;
        }
        if (SkyblockUtils.onSkyblock) {
            return isOnTeam((EntityPlayer)e2);
        }
        return OringoClient.mc.field_71439_g.func_145748_c_().func_150254_d().charAt(2) == '§' && e2.func_145748_c_().func_150254_d().charAt(2) == '§' && OringoClient.mc.field_71439_g.func_145748_c_().func_150254_d().charAt(3) == e2.func_145748_c_().func_150254_d().charAt(3);
    }
}
