// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.player;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.item.ItemStack;
import me.oringo.oringoclient.utils.PlayerUtils;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemArmor;
import me.oringo.oringoclient.qolfeatures.module.impl.other.Disabler;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class ArmorSwap extends Module
{
    public static NumberSetting items;
    public static NumberSetting startIndex;
    private boolean wasPressed;
    
    public ArmorSwap() {
        super("ArmorSwapper", Category.PLAYER);
        this.addSettings(ArmorSwap.items, ArmorSwap.startIndex);
    }
    
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (this.isToggled() && ArmorSwap.mc.field_71439_g != null && Disabler.wasEnabled) {
            if (this.isPressed() && !this.wasPressed) {
                for (int i = 0; i < ArmorSwap.items.getValue(); ++i) {
                    if (ArmorSwap.mc.field_71439_g.field_71069_bz.func_75139_a((int)(ArmorSwap.startIndex.getValue() + i)).func_75216_d()) {
                        final ItemStack stack = ArmorSwap.mc.field_71439_g.field_71069_bz.func_75139_a((int)(ArmorSwap.startIndex.getValue() + i)).func_75211_c();
                        int button = -1;
                        if (stack.func_77973_b() instanceof ItemArmor) {
                            button = ((ItemArmor)stack.func_77973_b()).field_77881_a;
                        }
                        else if (stack.func_77973_b() instanceof ItemSkull) {
                            button = 0;
                        }
                        if (button != -1) {
                            PlayerUtils.numberClick((int)(ArmorSwap.startIndex.getValue() + i), 0);
                            PlayerUtils.numberClick(5 + button, 0);
                            PlayerUtils.numberClick((int)(ArmorSwap.startIndex.getValue() + i), 0);
                        }
                    }
                }
            }
            this.wasPressed = this.isPressed();
        }
    }
    
    @Override
    public boolean isKeybind() {
        return true;
    }
    
    static {
        ArmorSwap.items = new NumberSetting("Armor count", 1.0, 1.0, 4.0, 1.0);
        ArmorSwap.startIndex = new NumberSetting("Start index", 9.0, 9.0, 35.0, 1.0);
    }
}
