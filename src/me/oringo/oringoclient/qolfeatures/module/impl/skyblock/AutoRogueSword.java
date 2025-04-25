// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.skyblock;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.Packet;
import me.oringo.oringoclient.utils.PacketUtils;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import me.oringo.oringoclient.utils.SkyblockUtils;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.utils.MilliTimer;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class AutoRogueSword extends Module
{
    public NumberSetting clicks;
    public NumberSetting delay;
    public BooleanSetting onlyDung;
    private final MilliTimer time;
    
    public AutoRogueSword() {
        super("Auto Rogue", 0, Category.SKYBLOCK);
        this.clicks = new NumberSetting("Clicks", 50.0, 1.0, 1000.0, 10.0);
        this.delay = new NumberSetting("Delay", 29.0, 0.0, 100.0, 1.0);
        this.onlyDung = new BooleanSetting("Only dungeon", false);
        this.time = new MilliTimer();
        this.addSettings(this.clicks, this.delay, this.onlyDung);
    }
    
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (AutoRogueSword.mc.field_71439_g == null || (!SkyblockUtils.inDungeon && this.onlyDung.isEnabled()) || !this.isToggled()) {
            return;
        }
        if (this.time.hasTimePassed((long)this.delay.getValue())) {
            for (int i = 0; i < 9; ++i) {
                if (AutoRogueSword.mc.field_71439_g.field_71071_by.func_70301_a(i) != null && AutoRogueSword.mc.field_71439_g.field_71071_by.func_70301_a(i).func_82833_r().toLowerCase().contains("rogue sword")) {
                    PacketUtils.sendPacketNoEvent((Packet<?>)new C09PacketHeldItemChange(i));
                    for (int x = 0; x < this.clicks.getValue(); ++x) {
                        AutoRogueSword.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C08PacketPlayerBlockPlacement(AutoRogueSword.mc.field_71439_g.field_71071_by.func_70301_a(i)));
                    }
                    PacketUtils.sendPacketNoEvent((Packet<?>)new C09PacketHeldItemChange(AutoRogueSword.mc.field_71439_g.field_71071_by.field_70461_c));
                    this.time.reset();
                    break;
                }
            }
        }
    }
}
