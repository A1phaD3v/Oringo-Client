// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.render;

import java.util.HashMap;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.List;
import java.util.Iterator;
import me.oringo.oringoclient.utils.RenderUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.ModeSetting;
import java.awt.Color;
import java.util.Map;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class CustomESP extends Module
{
    public static Map<String, Color> names;
    public ModeSetting mode;
    
    public CustomESP() {
        super("Custom ESP", Category.RENDER);
        this.mode = new ModeSetting("Mode", "2D", new String[] { "2D", "Box", "Tracers" });
        this.addSettings(this.mode);
    }
    
    @SubscribeEvent
    public void onRender(final RenderWorldLastEvent event) {
        if (!this.isToggled()) {
            return;
        }
        for (final Entity entity : CustomESP.mc.field_71441_e.func_175644_a((Class)EntityArmorStand.class, entity -> entity.func_70068_e((Entity)CustomESP.mc.field_71439_g) < 10000.0)) {
            for (final Map.Entry<String, Color> entry : CustomESP.names.entrySet()) {
                if (entity.func_145748_c_().func_150260_c().toLowerCase().contains(entry.getKey())) {
                    final List<Entity> entities = (List<Entity>)CustomESP.mc.field_71441_e.func_72839_b(entity, entity.func_174813_aQ().func_72314_b(0.0, 2.0, 0.0));
                    if (!entities.isEmpty()) {
                        final Color color = entry.getValue();
                        final Entity toRender = entities.get(0);
                        final String selected = this.mode.getSelected();
                        switch (selected) {
                            case "2D": {
                                RenderUtils.draw2D(toRender, event.partialTicks, 1.0f, color);
                                break;
                            }
                            case "Box": {
                                RenderUtils.entityESPBox(toRender, event.partialTicks, color);
                                break;
                            }
                            case "Tracers": {
                                RenderUtils.tracerLine(toRender, event.partialTicks, 1.0f, color);
                                break;
                            }
                        }
                        break;
                    }
                    break;
                }
            }
        }
    }
    
    static {
        CustomESP.names = new HashMap<String, Color>();
    }
}
