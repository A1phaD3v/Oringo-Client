// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.movement;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.settings.GameSettings;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class SafeWalk extends Module
{
    public SafeWalk() {
        super("Eagle", 0, Category.MOVEMENT);
    }
    
    @Override
    public void onDisable() {
        KeyBinding.func_74510_a(SafeWalk.mc.field_71474_y.field_74311_E.func_151463_i(), GameSettings.func_100015_a(SafeWalk.mc.field_71474_y.field_74311_E));
    }
    
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (SafeWalk.mc.field_71439_g == null || SafeWalk.mc.field_71441_e == null || !this.isToggled() || SafeWalk.mc.field_71462_r != null) {
            return;
        }
        final BlockPos BP = new BlockPos(SafeWalk.mc.field_71439_g.field_70165_t, SafeWalk.mc.field_71439_g.field_70163_u - 0.5, SafeWalk.mc.field_71439_g.field_70161_v);
        if (SafeWalk.mc.field_71441_e.func_180495_p(BP).func_177230_c() == Blocks.field_150350_a && SafeWalk.mc.field_71441_e.func_180495_p(BP.func_177977_b()).func_177230_c() == Blocks.field_150350_a && SafeWalk.mc.field_71439_g.field_70122_E && SafeWalk.mc.field_71439_g.field_71158_b.field_78900_b < 0.1f) {
            KeyBinding.func_74510_a(SafeWalk.mc.field_71474_y.field_74311_E.func_151463_i(), true);
        }
        else {
            KeyBinding.func_74510_a(SafeWalk.mc.field_71474_y.field_74311_E.func_151463_i(), GameSettings.func_100015_a(SafeWalk.mc.field_71474_y.field_74311_E));
        }
    }
}
