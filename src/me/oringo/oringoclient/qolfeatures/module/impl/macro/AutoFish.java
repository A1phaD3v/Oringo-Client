// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.macro;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import me.oringo.oringoclient.utils.MathUtil;
import me.oringo.oringoclient.mixins.entity.EntityFishHookAccessor;
import me.oringo.oringoclient.events.PlayerUpdateEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.utils.MilliTimer;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class AutoFish extends Module
{
    private final MilliTimer hookDelay;
    private static int nextDelay;
    private static int waterticks;
    private boolean hook;
    
    public AutoFish() {
        super("Auto Fish", Category.OTHER);
        this.hookDelay = new MilliTimer();
        this.addSettings(new Setting[0]);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
    }
    
    @SubscribeEvent
    public void onTick(final PlayerUpdateEvent event) {
        if (this.isToggled()) {
            if (AutoFish.mc.field_71439_g.field_71104_cf == null || (((EntityFishHookAccessor)AutoFish.mc.field_71439_g.field_71104_cf).inGround() && MathUtil.hypot(AutoFish.mc.field_71439_g.field_71104_cf.field_70179_y, AutoFish.mc.field_71439_g.field_71104_cf.field_70159_w) < 0.001)) {
                if (this.hookDelay.hasTimePassed(300L)) {
                    AutoFish.mc.field_71442_b.func_78769_a((EntityPlayer)AutoFish.mc.field_71439_g, (World)AutoFish.mc.field_71441_e, AutoFish.mc.field_71439_g.func_70694_bm());
                    this.hookDelay.reset();
                }
                this.hook = false;
                AutoFish.waterticks = 0;
            }
            else if (AutoFish.mc.field_71439_g.field_71104_cf.func_70090_H() || AutoFish.mc.field_71439_g.field_71104_cf.func_180799_ab()) {
                ++AutoFish.waterticks;
                if (!this.hook && AutoFish.waterticks > 75 && MathUtil.hypot(AutoFish.mc.field_71439_g.field_71104_cf.field_70179_y, AutoFish.mc.field_71439_g.field_71104_cf.field_70159_w) < 0.001 && AutoFish.mc.field_71439_g.field_71104_cf.field_70163_u - AutoFish.mc.field_71439_g.field_71104_cf.field_70167_r < -0.04) {
                    this.hook = true;
                    AutoFish.nextDelay = (int)MathUtil.getRandomInRange(75.0, 200.0);
                    if (this.hookDelay.hasTimePassed(AutoFish.nextDelay)) {
                        this.hookDelay.reset();
                    }
                }
                if (this.hook && this.hookDelay.hasTimePassed(AutoFish.nextDelay)) {
                    AutoFish.mc.field_71442_b.func_78769_a((EntityPlayer)AutoFish.mc.field_71439_g, (World)AutoFish.mc.field_71441_e, AutoFish.mc.field_71439_g.func_70694_bm());
                    AutoFish.mc.field_71439_g.field_71104_cf = null;
                    this.hookDelay.reset();
                    AutoFish.waterticks = 0;
                    this.hook = false;
                }
            }
        }
    }
}
