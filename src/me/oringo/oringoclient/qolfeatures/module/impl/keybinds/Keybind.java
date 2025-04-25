// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.keybinds;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import me.oringo.oringoclient.utils.SkyblockUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.oringo.oringoclient.utils.PlayerUtils;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.oringo.oringoclient.qolfeatures.module.impl.other.Disabler;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import net.minecraftforge.common.MinecraftForge;
import me.oringo.oringoclient.OringoClient;
import me.oringo.oringoclient.utils.MilliTimer;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.RunnableSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.StringSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.ModeSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class Keybind extends Module
{
    public ModeSetting button;
    public ModeSetting mode;
    public NumberSetting delay;
    public NumberSetting clickCount;
    public StringSetting item;
    public BooleanSetting fromInv;
    public RunnableSetting remove;
    private boolean wasPressed;
    private final MilliTimer delayTimer;
    private boolean isEnabled;
    
    public Keybind(final String name) {
        super(name, Category.KEYBINDS);
        this.button = new ModeSetting("Button", "Right", new String[] { "Right", "Left", "Swing" });
        this.mode = new ModeSetting("Mode", "Normal", new String[] { "Normal", "Rapid", "Toggle" });
        this.delay = new NumberSetting("Delay", 50.0, 0.0, 5000.0, 1.0);
        this.clickCount = new NumberSetting("Click Count", 1.0, 1.0, 64.0, 1.0);
        this.item = new StringSetting("Item");
        this.fromInv = new BooleanSetting("From inventory", false);
        this.remove = new RunnableSetting("Remove keybinding", () -> {
            this.setToggled(false);
            OringoClient.modules.remove(this);
            MinecraftForge.EVENT_BUS.unregister((Object)this);
            return;
        });
        this.delayTimer = new MilliTimer();
        this.addSettings(this.item, this.button, this.mode, this.delay, this.fromInv, this.remove);
    }
    
    @Override
    public String getName() {
        return this.item.getValue().equals("") ? ("Keybind " + (Module.getModulesByCategory(Category.KEYBINDS).indexOf(this) + 1)) : this.item.getValue();
    }
    
    @Override
    public boolean isKeybind() {
        return true;
    }
    
    @SubscribeEvent
    public void onTick(final RenderWorldLastEvent event) {
        final boolean keyPressed = this.isPressed();
        if ((keyPressed || this.isEnabled) && this.isToggled() && !this.item.getValue().equals("") && Keybind.mc.field_71462_r == null && this.delayTimer.hasTimePassed((long)this.delay.getValue()) && (this.mode.is("Rapid") || !this.wasPressed || (this.mode.is("Toggle") && this.isEnabled))) {
            if (!this.fromInv.isEnabled() || !Disabler.wasEnabled) {
                for (int i = 0; i < 9; ++i) {
                    if (Keybind.mc.field_71439_g.field_71071_by.func_70301_a(i) != null && ChatFormatting.stripFormatting(Keybind.mc.field_71439_g.field_71071_by.func_70301_a(i).func_82833_r()).toLowerCase().contains(this.item.getValue().toLowerCase())) {
                        final int held = Keybind.mc.field_71439_g.field_71071_by.field_70461_c;
                        PlayerUtils.swapToSlot(i);
                        this.click();
                        PlayerUtils.swapToSlot(held);
                        break;
                    }
                }
            }
            else {
                final int slot = PlayerUtils.getItem(this.item.getValue());
                if (slot != -1) {
                    if (slot >= 36) {
                        final int held = Keybind.mc.field_71439_g.field_71071_by.field_70461_c;
                        PlayerUtils.swapToSlot(slot - 36);
                        this.click();
                        PlayerUtils.swapToSlot(held);
                    }
                    else {
                        this.numberClick(slot, Keybind.mc.field_71439_g.field_71071_by.field_70461_c);
                        this.click();
                        this.numberClick(slot, Keybind.mc.field_71439_g.field_71071_by.field_70461_c);
                    }
                }
            }
        }
        if (this.mode.is("Toggle") && !this.wasPressed && keyPressed && this.isToggled()) {
            this.isEnabled = !this.isEnabled;
        }
        this.wasPressed = keyPressed;
    }
    
    private void click() {
        for (int i = 0; i < this.clickCount.getValue(); ++i) {
            final String selected = this.button.getSelected();
            switch (selected) {
                case "Left": {
                    SkyblockUtils.click();
                    break;
                }
                case "Right": {
                    Keybind.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C08PacketPlayerBlockPlacement(Keybind.mc.field_71439_g.func_70694_bm()));
                    break;
                }
                case "Swing": {
                    Keybind.mc.field_71439_g.func_71038_i();
                    break;
                }
            }
        }
        this.delayTimer.reset();
    }
    
    public void numberClick(final int slot, final int button) {
        Keybind.mc.field_71442_b.func_78753_a(Keybind.mc.field_71439_g.field_71069_bz.field_75152_c, slot, button, 2, (EntityPlayer)Keybind.mc.field_71439_g);
    }
}
