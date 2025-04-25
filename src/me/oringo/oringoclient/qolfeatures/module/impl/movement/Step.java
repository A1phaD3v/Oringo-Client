// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.movement;

import me.oringo.oringoclient.events.MoveEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.oringo.oringoclient.mixins.MinecraftAccessor;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import me.oringo.oringoclient.utils.PlayerUtils;
import me.oringo.oringoclient.OringoClient;
import me.oringo.oringoclient.events.StepEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.ModeSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class Step extends Module
{
    public ModeSetting mode;
    public NumberSetting timer;
    private boolean isStepping;
    private int stage;
    
    public Step() {
        super("Step", Category.MOVEMENT);
        this.mode = new ModeSetting("Mode", "NCP", new String[] { "NCP" });
        this.timer = new NumberSetting("Timer", 0.4, 0.1, 1.0, 0.1);
        this.addSettings(this.mode, this.timer);
    }
    
    @SubscribeEvent
    public void onStep(final StepEvent event) {
        if (this.isToggled() && !OringoClient.speed.isToggled() && !Step.mc.field_71439_g.field_71158_b.field_78901_c && !Step.mc.field_71439_g.func_70090_H() && !Step.mc.field_71439_g.func_180799_ab()) {
            if (event instanceof StepEvent.Post && PlayerUtils.getJumpMotion() < 1.0) {
                if (event.getHeight() > 0.87) {
                    for (final double offset : new double[] { event.getHeight() * 0.42, event.getHeight() * 0.75 }) {
                        Step.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Step.mc.field_71439_g.field_70165_t, Step.mc.field_71439_g.field_70163_u + offset, Step.mc.field_71439_g.field_70161_v, false));
                    }
                    if (this.timer.getValue() != 1.0) {
                        ((MinecraftAccessor)Step.mc).getTimer().field_74278_d = (float)this.timer.getValue();
                        this.isStepping = true;
                    }
                }
            }
            else if (Step.mc.field_71439_g.field_70122_E) {
                event.setHeight(1.0);
            }
        }
    }
    
    @SubscribeEvent
    public void onMove(final MoveEvent event) {
        if (this.isStepping && this.mode.is("NCP")) {
            this.isStepping = false;
            ((MinecraftAccessor)Step.mc).getTimer().field_74278_d = 1.0f;
        }
    }
}
