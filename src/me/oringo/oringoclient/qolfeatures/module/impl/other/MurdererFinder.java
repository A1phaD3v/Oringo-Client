// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.other;

import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityItem;
import me.oringo.oringoclient.utils.RenderUtils;
import java.awt.Color;
import net.minecraft.entity.Entity;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import net.minecraft.scoreboard.ScoreObjective;
import me.oringo.oringoclient.ui.notifications.Notifications;
import me.oringo.oringoclient.utils.SkyblockUtils;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import java.util.Collection;
import java.util.Arrays;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import java.util.ArrayList;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class MurdererFinder extends Module
{
    private ArrayList<Item> knives;
    public static ArrayList<EntityPlayer> murderers;
    public static ArrayList<EntityPlayer> detectives;
    private BooleanSetting autoSay;
    private BooleanSetting ingotESP;
    private BooleanSetting bowESP;
    private boolean inMurder;
    
    public MurdererFinder() {
        super("Murder Mystery", Category.OTHER);
        this.knives = new ArrayList<Item>(Arrays.asList(Items.field_151040_l, Items.field_151052_q, Items.field_151037_a, Items.field_151055_y, Items.field_151053_p, Items.field_151041_m, Blocks.field_150330_I.func_180665_b((World)null, (BlockPos)null), Items.field_151051_r, Items.field_151047_v, Items.field_151128_bU, Items.field_151158_bO, Items.field_151005_D, Items.field_151034_e, Items.field_151057_cb, Blocks.field_150360_v.func_180665_b((World)null, (BlockPos)null), Items.field_151146_bM, Items.field_151103_aS, Items.field_151172_bF, Items.field_151150_bK, Items.field_151106_aX, Items.field_151056_x, Blocks.field_150328_O.func_180665_b((World)null, (BlockPos)null), Items.field_179562_cC, Items.field_151083_be, Items.field_151010_B, Items.field_151048_u, Items.field_151012_L, (Item)Items.field_151097_aZ, Items.field_151115_aP, Items.field_151100_aR, Items.field_151124_az, Items.field_151060_bw, Items.field_151072_bj, Items.field_151115_aP));
        this.autoSay = new BooleanSetting("Say murderer", false);
        this.ingotESP = new BooleanSetting("Ingot ESP", true);
        this.bowESP = new BooleanSetting("Bow esp", true);
        this.addSettings(this.autoSay, this.ingotESP, this.bowESP);
    }
    
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (!this.isToggled() || MurdererFinder.mc.field_71439_g == null || MurdererFinder.mc.field_71441_e == null) {
            return;
        }
        try {
            if (MurdererFinder.mc.field_71439_g.func_96123_co() != null) {
                final ScoreObjective objective = MurdererFinder.mc.field_71439_g.func_96123_co().func_96539_a(1);
                if (objective != null && ChatFormatting.stripFormatting(objective.func_96678_d()).equals("MURDER MYSTERY") && SkyblockUtils.hasLine("Innocents Left:")) {
                    this.inMurder = true;
                    for (final EntityPlayer player : MurdererFinder.mc.field_71441_e.field_73010_i) {
                        if (!MurdererFinder.murderers.contains(player)) {
                            if (MurdererFinder.detectives.contains(player)) {
                                continue;
                            }
                            if (player.func_70694_bm() == null) {
                                continue;
                            }
                            if (MurdererFinder.detectives.size() < 2 && player.func_70694_bm().func_77973_b().equals(Items.field_151031_f)) {
                                MurdererFinder.detectives.add(player);
                                Notifications.showNotification("Oringo Client", String.format("§b%s is detective!", player.func_70005_c_()), 2500);
                            }
                            if (!this.knives.contains(player.func_70694_bm().func_77973_b())) {
                                continue;
                            }
                            MurdererFinder.murderers.add(player);
                            Notifications.showNotification("Oringo Client", String.format("§c%s is murderer!", player.func_70005_c_()), 2500);
                            if (!this.autoSay.isEnabled() || player == MurdererFinder.mc.field_71439_g) {
                                continue;
                            }
                            MurdererFinder.mc.field_71439_g.func_71165_d(String.format("%s is murderer!", ChatFormatting.stripFormatting(player.func_70005_c_())));
                        }
                    }
                    return;
                }
                this.inMurder = false;
                MurdererFinder.murderers.clear();
                MurdererFinder.detectives.clear();
            }
        }
        catch (Exception ex) {}
    }
    
    @SubscribeEvent
    public void onWorldRender(final RenderWorldLastEvent e) {
        if (!this.isToggled()) {
            return;
        }
        if (this.inMurder) {
            for (final Entity entity : MurdererFinder.mc.field_71441_e.field_72996_f) {
                if (entity instanceof EntityPlayer) {
                    if (((EntityPlayer)entity).func_70608_bn()) {
                        continue;
                    }
                    if (entity == MurdererFinder.mc.field_71439_g) {
                        continue;
                    }
                    if (MurdererFinder.murderers.contains(entity)) {
                        RenderUtils.draw2D(entity, e.partialTicks, 1.0f, Color.red);
                    }
                    else if (MurdererFinder.detectives.contains(entity)) {
                        RenderUtils.draw2D(entity, e.partialTicks, 1.0f, Color.blue);
                    }
                    else {
                        RenderUtils.draw2D(entity, e.partialTicks, 1.0f, Color.gray);
                    }
                }
                else if (entity instanceof EntityItem && ((EntityItem)entity).func_92059_d().func_77973_b() == Items.field_151043_k && this.ingotESP.isEnabled()) {
                    RenderUtils.draw2D(entity, e.partialTicks, 1.0f, Color.yellow);
                }
                else {
                    if (!this.bowESP.isEnabled() || !(entity instanceof EntityArmorStand) || ((EntityArmorStand)entity).func_71124_b(0) == null || ((EntityArmorStand)entity).func_71124_b(0).func_77973_b() != Items.field_151031_f) {
                        continue;
                    }
                    RenderUtils.tracerLine(entity, e.partialTicks, 1.0f, Color.CYAN);
                }
            }
        }
    }
    
    static {
        MurdererFinder.murderers = new ArrayList<EntityPlayer>();
        MurdererFinder.detectives = new ArrayList<EntityPlayer>();
    }
}
