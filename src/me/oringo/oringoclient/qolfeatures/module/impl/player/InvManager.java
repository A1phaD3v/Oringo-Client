// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.player;

import java.util.ArrayList;
import net.minecraft.entity.Entity;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import net.minecraft.item.ItemBlock;
import me.oringo.oringoclient.mixins.item.ItemToolAccessor;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemTool;
import net.minecraft.item.ItemSword;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemArmor;
import java.util.Iterator;
import net.minecraft.inventory.Slot;
import me.oringo.oringoclient.ui.notifications.Notifications;
import com.mojang.realmsclient.gui.ChatFormatting;
import org.lwjgl.input.Mouse;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.ToDoubleFunction;
import java.util.Comparator;
import me.oringo.oringoclient.utils.SkyblockUtils;
import net.minecraft.entity.EntityLivingBase;
import me.oringo.oringoclient.utils.EntityUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C02PacketUseEntity;
import me.oringo.oringoclient.events.PacketSentEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.oringo.oringoclient.OringoClient;
import me.oringo.oringoclient.qolfeatures.module.impl.combat.KillAura;
import net.minecraft.client.gui.inventory.GuiInventory;
import me.oringo.oringoclient.events.MotionUpdateEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import java.util.Arrays;
import java.util.List;
import me.oringo.oringoclient.utils.MilliTimer;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.ModeSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class InvManager extends Module
{
    public NumberSetting delay;
    public BooleanSetting dropTrash;
    public BooleanSetting autoArmor;
    public BooleanSetting middleClick;
    public ModeSetting trashItems;
    public ModeSetting mode;
    private MilliTimer delayTimer;
    private boolean wasPressed;
    public NumberSetting swordSlot;
    public NumberSetting blockSlot;
    public NumberSetting gappleSlot;
    public NumberSetting pickaxeSlot;
    public NumberSetting axeSlot;
    public NumberSetting shovelSlot;
    public NumberSetting bowSlot;
    private List<String> dropSkyblock;
    private List<String> dropSkywars;
    public static List<String> dropCustom;
    
    public InvManager() {
        super("Inventory Manager", 0, Category.PLAYER);
        this.delay = new NumberSetting("Delay", 30.0, 0.0, 300.0, 1.0);
        this.dropTrash = new BooleanSetting("Drop trash", true);
        this.autoArmor = new BooleanSetting("Auto Armor", false);
        this.middleClick = new BooleanSetting("Middle click to drop", false);
        this.trashItems = new ModeSetting("Trash items", "Skyblock", new String[] { "Skyblock", "Skywars", "Custom" });
        this.mode = new ModeSetting("Mode", "Inv open", new String[] { "Inv open", "Always" });
        this.delayTimer = new MilliTimer();
        this.swordSlot = new NumberSetting("Sword slot", 0.0, 0.0, 9.0, 1.0);
        this.blockSlot = new NumberSetting("Block slot", 0.0, 0.0, 9.0, 1.0);
        this.gappleSlot = new NumberSetting("Gapple slot", 0.0, 0.0, 9.0, 1.0);
        this.pickaxeSlot = new NumberSetting("Pickaxe slot", 0.0, 0.0, 9.0, 1.0);
        this.axeSlot = new NumberSetting("Axe slot", 0.0, 0.0, 9.0, 1.0);
        this.shovelSlot = new NumberSetting("Shovel slot", 0.0, 0.0, 9.0, 1.0);
        this.bowSlot = new NumberSetting("Bow slot", 0.0, 0.0, 9.0, 1.0);
        this.dropSkyblock = Arrays.asList("Training Weight", "Healing Potion", "Beating Heart", "Premium Flesh", "Mimic Fragment", "Enchanted Rotten Flesh", "Machine Gun Bow", "Enchanted Bone", "Defuse Kit", "Enchanted Ice", "Diamond Atom", "Silent Death", "Cutlass", "Soulstealer Bow", "Sniper Bow", "Optical Lens", "Tripwire Hook", "Button", "Carpet", "Lever", "Journal Entry", "Sign", "Zombie Commander", "Zombie Lord", "Skeleton Master, Skeleton Grunt, Skeleton Lord, Zombie Soldier", "Zombie Knight", "Heavy", "Super Heavy", "Undead", "Bouncy", "Skeletor", "Trap", "Inflatable Jerry");
        this.dropSkywars = Arrays.asList("Egg", "Snowball", "Poison", "Lava", "Steak", "Enchanting", "Poison");
        this.addSettings(this.mode, this.delay, this.dropTrash, this.trashItems, this.middleClick, this.autoArmor, this.swordSlot, this.pickaxeSlot, this.axeSlot, this.shovelSlot, this.blockSlot);
    }
    
    @SubscribeEvent
    public void onUpdate(final MotionUpdateEvent.Pre event) {
        if (this.isToggled() && (InvManager.mc.field_71462_r instanceof GuiInventory || (InvManager.mc.field_71462_r == null && this.mode.is("Always") && KillAura.target == null)) && !OringoClient.scaffold.isToggled()) {
            if (this.autoArmor.isEnabled()) {
                this.getBestArmor();
            }
            if (this.dropTrash.isEnabled()) {
                this.dropTrash();
            }
            if (this.swordSlot.getValue() != 0.0) {
                this.getBestSword();
            }
            this.getBestTools();
            if (this.blockSlot.getValue() != 0.0) {
                this.getBestBlock();
            }
        }
        else {
            this.delayTimer.reset();
        }
    }
    
    @SubscribeEvent
    public void onPacket(final PacketSentEvent event) {
        if (event.packet instanceof C02PacketUseEntity || event.packet instanceof C08PacketPlayerBlockPlacement) {
            this.delayTimer.reset();
        }
    }
    
    private EntityPlayer getClosestPlayer(final double distance) {
        final List<EntityPlayer> players = (List<EntityPlayer>)InvManager.mc.field_71441_e.field_73010_i.stream().filter(entityPlayer -> entityPlayer != InvManager.mc.field_71439_g && !EntityUtils.isTeam((EntityLivingBase)entityPlayer) && !SkyblockUtils.isNPC(entityPlayer) && InvManager.mc.field_71439_g.func_70032_d(entityPlayer) < distance).sorted(Comparator.comparingDouble((ToDoubleFunction<? super T>)InvManager.mc.field_71439_g::func_70032_d)).collect(Collectors.toList());
        if (!players.isEmpty()) {
            return players.get(0);
        }
        return null;
    }
    
    @SubscribeEvent
    public void onTooltip(final ItemTooltipEvent event) {
        if (Mouse.isButtonDown(2) && InvManager.mc.field_71462_r instanceof GuiInventory && this.middleClick.isEnabled()) {
            if (!this.wasPressed) {
                this.wasPressed = true;
                final String name = ChatFormatting.stripFormatting(event.itemStack.func_82833_r());
                if (InvManager.dropCustom.contains(name)) {
                    InvManager.dropCustom.remove(name);
                    Notifications.showNotification("Oringo Client", "Removed " + name + " from custom drop list", 2000);
                }
                else {
                    InvManager.dropCustom.add(name);
                    Notifications.showNotification("Oringo Client", "Added " + ChatFormatting.AQUA + name + ChatFormatting.RESET + " to custom drop list", 2000);
                }
                save();
            }
        }
        else {
            this.wasPressed = false;
        }
    }
    
    public void dropTrash() {
        for (final Slot slot : InvManager.mc.field_71439_g.field_71069_bz.field_75151_b) {
            if (slot.func_75216_d() && this.canInteract()) {
                if (this.trashItems.getSelected().equals("Custom")) {
                    if (!InvManager.dropCustom.contains(ChatFormatting.stripFormatting(slot.func_75211_c().func_82833_r()))) {
                        continue;
                    }
                    this.drop(slot.field_75222_d);
                }
                else if (this.trashItems.getSelected().equals("Skyblock") && this.dropSkyblock.stream().anyMatch(a -> a.contains(ChatFormatting.stripFormatting(slot.func_75211_c().func_82833_r())))) {
                    this.drop(slot.field_75222_d);
                }
                else {
                    if (!this.trashItems.getSelected().equals("Skywars") || !this.dropSkywars.stream().anyMatch(a -> a.contains(ChatFormatting.stripFormatting(slot.func_75211_c().func_82833_r())))) {
                        continue;
                    }
                    this.drop(slot.field_75222_d);
                }
            }
        }
    }
    
    public void getBestArmor() {
        for (int i = 5; i < 9; ++i) {
            if (InvManager.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75216_d() && this.canInteract()) {
                final ItemStack armor = InvManager.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75211_c();
                if (!isBestArmor(armor, i)) {
                    this.drop(i);
                }
            }
        }
        for (int i = 9; i < 45; ++i) {
            if (InvManager.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75216_d() && this.canInteract()) {
                final ItemStack stack = InvManager.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75211_c();
                if (stack.func_77973_b() instanceof ItemArmor) {
                    if (isBestArmor(stack, i)) {
                        this.shiftClick(i);
                    }
                    else {
                        this.drop(i);
                    }
                }
            }
        }
    }
    
    public static boolean isBestArmor(final ItemStack armor, final int slot) {
        if (!(armor.func_77973_b() instanceof ItemArmor)) {
            return false;
        }
        for (int i = 5; i < 45; ++i) {
            if (InvManager.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75216_d()) {
                final ItemStack is = InvManager.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75211_c();
                if (is.func_77973_b() instanceof ItemArmor && ((getProtection(is) > getProtection(armor) && slot < 9) || (slot >= 9 && getProtection(is) >= getProtection(armor) && slot != i)) && ((ItemArmor)is.func_77973_b()).field_77881_a == ((ItemArmor)armor.func_77973_b()).field_77881_a) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static float getProtection(final ItemStack stack) {
        float prot = 0.0f;
        if (stack.func_77973_b() instanceof ItemArmor) {
            final ItemArmor armor = (ItemArmor)stack.func_77973_b();
            prot += (float)(armor.field_77879_b + (100 - armor.field_77879_b) * EnchantmentHelper.func_77506_a(Enchantment.field_180310_c.field_77352_x, stack) * 0.0075);
            prot += (float)(EnchantmentHelper.func_77506_a(Enchantment.field_77327_f.field_77352_x, stack) / 100.0);
            prot += (float)(EnchantmentHelper.func_77506_a(Enchantment.field_77329_d.field_77352_x, stack) / 100.0);
            prot += (float)(EnchantmentHelper.func_77506_a(Enchantment.field_92091_k.field_77352_x, stack) / 100.0);
            prot += (float)(EnchantmentHelper.func_77506_a(Enchantment.field_77347_r.field_77352_x, stack) / 50.0);
            prot += (float)(EnchantmentHelper.func_77506_a(Enchantment.field_180308_g.field_77352_x, stack) / 100.0);
            prot += (float)(stack.func_77958_k() / 1000.0);
        }
        return prot;
    }
    
    public void getBestSword() {
        for (int i = 9; i < 45; ++i) {
            if (InvManager.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75216_d() && this.canInteract()) {
                final ItemStack stack = InvManager.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75211_c();
                if (stack.func_77973_b() instanceof ItemSword) {
                    if (this.isBestSword(stack, i)) {
                        if (this.getHotbarID((int)this.swordSlot.getValue()) != i) {
                            this.numberClick(i, (int)this.swordSlot.getValue() - 1);
                        }
                    }
                    else {
                        this.drop(i);
                    }
                }
            }
        }
    }
    
    public boolean isBestSword(final ItemStack sword, final int slot) {
        if (!(sword.func_77973_b() instanceof ItemSword)) {
            return false;
        }
        for (int i = 9; i < 45; ++i) {
            if (InvManager.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75216_d()) {
                final ItemStack is = InvManager.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75211_c();
                if (is.func_77973_b() instanceof ItemSword && ((getToolDamage(is) > getToolDamage(sword) && slot == this.getHotbarID((int)this.swordSlot.getValue())) || (slot != this.getHotbarID((int)this.swordSlot.getValue()) && getToolDamage(is) >= getToolDamage(sword) && slot != i))) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public void getBestTools() {
        for (int i = 9; i < 45; ++i) {
            if (InvManager.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75216_d() && this.canInteract()) {
                final ItemStack stack = InvManager.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75211_c();
                if (stack.func_77973_b() instanceof ItemTool && this.getToolHotbarSlot(stack) != 0) {
                    if (this.isBestTool(stack, i)) {
                        if (this.getHotbarID(this.getToolHotbarSlot(stack)) != i) {
                            this.numberClick(i, this.getToolHotbarSlot(stack) - 1);
                        }
                    }
                    else {
                        this.drop(i);
                    }
                }
            }
        }
    }
    
    public boolean isBestTool(final ItemStack tool, final int slot) {
        if (!(tool.func_77973_b() instanceof ItemTool)) {
            return false;
        }
        for (int i = 9; i < 45; ++i) {
            if (InvManager.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75216_d()) {
                final ItemStack is = InvManager.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75211_c();
                if (this.getToolHotbarSlot(is) != 0) {
                    if (tool.func_77973_b() instanceof ItemAxe && is.func_77973_b() instanceof ItemAxe) {
                        if ((getMaterial(is) > getMaterial(tool) && slot == this.getHotbarID((int)this.axeSlot.getValue())) || (slot != this.getHotbarID((int)this.axeSlot.getValue()) && getToolDamage(is) >= getToolDamage(tool) && slot != i)) {
                            return false;
                        }
                    }
                    else if (tool.func_77973_b() instanceof ItemPickaxe && is.func_77973_b() instanceof ItemPickaxe) {
                        if ((getMaterial(is) > getMaterial(tool) && slot == this.getHotbarID((int)this.pickaxeSlot.getValue())) || (slot != this.getHotbarID((int)this.pickaxeSlot.getValue()) && getToolDamage(is) >= getToolDamage(tool) && slot != i)) {
                            return false;
                        }
                    }
                    else if (tool.func_77973_b() instanceof ItemSpade && is.func_77973_b() instanceof ItemSpade && ((getMaterial(is) > getMaterial(tool) && slot == this.getHotbarID((int)this.shovelSlot.getValue())) || (slot != this.getHotbarID((int)this.pickaxeSlot.getValue()) && getToolDamage(is) >= getToolDamage(tool) && slot != i))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    public int getToolHotbarSlot(final ItemStack tool) {
        if (tool == null || !(tool.func_77973_b() instanceof ItemTool)) {
            return 0;
        }
        final String toolClass = ((ItemToolAccessor)tool.func_77973_b()).getToolClass();
        switch (toolClass) {
            case "pickaxe": {
                return (int)this.pickaxeSlot.getValue();
            }
            case "axe": {
                return (int)this.axeSlot.getValue();
            }
            case "shovel": {
                return (int)this.shovelSlot.getValue();
            }
            default: {
                return 0;
            }
        }
    }
    
    public static float getMaterial(final ItemStack item) {
        if (item.func_77973_b() instanceof ItemTool) {
            return (float)(((ItemTool)item.func_77973_b()).func_150913_i().func_77996_d() + EnchantmentHelper.func_77506_a(Enchantment.field_77349_p.field_77352_x, item) * 0.75);
        }
        return 0.0f;
    }
    
    public void getBestBlock() {
        for (int i = 9; i < 45; ++i) {
            if (InvManager.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75216_d() && this.canInteract()) {
                final ItemStack stack = InvManager.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75211_c();
                if (stack.func_77973_b() instanceof ItemBlock && ((ItemBlock)stack.func_77973_b()).field_150939_a.func_149730_j() && this.isBestBlock(stack, i) && this.getHotbarID((int)this.blockSlot.getValue()) != i) {
                    this.numberClick(i, (int)this.blockSlot.getValue() - 1);
                }
            }
        }
    }
    
    public boolean isBestBlock(final ItemStack stack, final int slot) {
        if (!(stack.func_77973_b() instanceof ItemBlock) || !((ItemBlock)stack.func_77973_b()).field_150939_a.func_149730_j()) {
            return false;
        }
        for (int i = 9; i < 45; ++i) {
            if (InvManager.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75216_d()) {
                final ItemStack is = InvManager.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75211_c();
                if (is.func_77973_b() instanceof ItemBlock && ((ItemBlock)is.func_77973_b()).field_150939_a.func_149730_j() && is.field_77994_a >= stack.field_77994_a && slot != this.getHotbarID((int)this.blockSlot.getValue()) && slot != i) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public int getHotbarID(final int hotbarNumber) {
        return hotbarNumber + 35;
    }
    
    public static int getBowDamage(final ItemStack bow) {
        return EnchantmentHelper.func_77506_a(Enchantment.field_77345_t.field_77352_x, bow) + EnchantmentHelper.func_77506_a(Enchantment.field_77343_v.field_77352_x, bow) * 2;
    }
    
    public static float getToolDamage(final ItemStack tool) {
        float damage = 0.0f;
        if (tool != null && (tool.func_77973_b() instanceof ItemTool || tool.func_77973_b() instanceof ItemSword)) {
            if (tool.func_77973_b() instanceof ItemSword) {
                damage += 4.0f;
                ++damage;
            }
            else if (tool.func_77973_b() instanceof ItemAxe) {
                damage += 3.0f;
            }
            else if (tool.func_77973_b() instanceof ItemPickaxe) {
                damage += 2.0f;
            }
            else if (tool.func_77973_b() instanceof ItemSpade) {
                ++damage;
            }
            damage += ((tool.func_77973_b() instanceof ItemTool) ? ((ItemTool)tool.func_77973_b()).func_150913_i().func_78000_c() : ((ItemSword)tool.func_77973_b()).func_150931_i());
            damage += (float)(1.25 * EnchantmentHelper.func_77506_a(Enchantment.field_180314_l.field_77352_x, tool));
            damage += (float)(EnchantmentHelper.func_77506_a(Enchantment.field_180314_l.field_77352_x, tool) * 0.5);
        }
        return damage;
    }
    
    private boolean canInteract() {
        return this.delayTimer.hasTimePassed((long)this.delay.getValue());
    }
    
    private static void save() {
        try {
            final DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream("config/OringoClient/InventoryManager.cfg"));
            dataOutputStream.writeInt(InvManager.dropCustom.size());
            for (final String s : InvManager.dropCustom) {
                dataOutputStream.writeUTF(s);
            }
            dataOutputStream.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void shiftClick(final int slot) {
        this.delayTimer.reset();
        KillAura.DISABLE.reset();
        InvManager.mc.field_71442_b.func_78753_a(InvManager.mc.field_71439_g.field_71069_bz.field_75152_c, slot, 0, 1, (EntityPlayer)InvManager.mc.field_71439_g);
    }
    
    public void numberClick(final int slot, final int button) {
        this.delayTimer.reset();
        KillAura.DISABLE.reset();
        InvManager.mc.field_71442_b.func_78753_a(InvManager.mc.field_71439_g.field_71069_bz.field_75152_c, slot, button, 2, (EntityPlayer)InvManager.mc.field_71439_g);
    }
    
    public void drop(final int slot) {
        this.delayTimer.reset();
        KillAura.DISABLE.reset();
        InvManager.mc.field_71442_b.func_78753_a(InvManager.mc.field_71439_g.field_71069_bz.field_75152_c, slot, 1, 4, (EntityPlayer)InvManager.mc.field_71439_g);
    }
    
    static {
        InvManager.dropCustom = new ArrayList<String>();
    }
}
