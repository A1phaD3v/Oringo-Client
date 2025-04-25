// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.player;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.item.Item;
import net.minecraft.inventory.Container;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.init.Items;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemTool;
import net.minecraft.item.ItemEnderPearl;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraftforge.client.event.GuiScreenEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.utils.MilliTimer;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class ChestStealer extends Module
{
    private MilliTimer timer;
    public NumberSetting delay;
    public BooleanSetting close;
    public BooleanSetting nameCheck;
    public BooleanSetting stealTrash;
    
    public ChestStealer() {
        super("Chest stealer", 0, Category.PLAYER);
        this.timer = new MilliTimer();
        this.delay = new NumberSetting("Delay", 100.0, 30.0, 200.0, 1.0);
        this.close = new BooleanSetting("Auto close", true);
        this.nameCheck = new BooleanSetting("Name check", true);
        this.stealTrash = new BooleanSetting("Steal trash", false);
        this.addSettings(this.delay, this.nameCheck, this.stealTrash, this.close);
    }
    
    @SubscribeEvent
    public void onGui(final GuiScreenEvent.BackgroundDrawnEvent event) {
        if (event.gui instanceof GuiChest && this.isToggled()) {
            final Container container = ((GuiChest)event.gui).field_147002_h;
            if (container instanceof ContainerChest && (!this.nameCheck.isEnabled() || ChatFormatting.stripFormatting(((ContainerChest)container).func_85151_d().func_145748_c_().func_150254_d()).equals("Chest") || ChatFormatting.stripFormatting(((ContainerChest)container).func_85151_d().func_145748_c_().func_150254_d()).equals("LOW"))) {
                for (int i = 0; i < ((ContainerChest)container).func_85151_d().func_70302_i_(); ++i) {
                    if (container.func_75139_a(i).func_75216_d() && this.timer.hasTimePassed((long)this.delay.getValue())) {
                        final Item item = container.func_75139_a(i).func_75211_c().func_77973_b();
                        if (this.stealTrash.isEnabled() || item instanceof ItemEnderPearl || item instanceof ItemTool || item instanceof ItemArmor || item instanceof ItemBow || item instanceof ItemPotion || item == Items.field_151032_g || item instanceof ItemAppleGold || item instanceof ItemSword || item instanceof ItemBlock) {
                            ChestStealer.mc.field_71442_b.func_78753_a(container.field_75152_c, i, 0, 1, (EntityPlayer)ChestStealer.mc.field_71439_g);
                            this.timer.reset();
                            return;
                        }
                    }
                }
                for (int i = 0; i < ((ContainerChest)container).func_85151_d().func_70302_i_(); ++i) {
                    if (container.func_75139_a(i).func_75216_d()) {
                        final Item item = container.func_75139_a(i).func_75211_c().func_77973_b();
                        if (this.stealTrash.isEnabled() || item instanceof ItemEnderPearl || item instanceof ItemTool || item instanceof ItemArmor || item instanceof ItemBow || item instanceof ItemPotion || item == Items.field_151032_g || item instanceof ItemAppleGold || item instanceof ItemSword || item instanceof ItemBlock) {
                            return;
                        }
                    }
                }
                if (this.close.isEnabled()) {
                    ChestStealer.mc.field_71439_g.func_71053_j();
                }
            }
        }
    }
}
