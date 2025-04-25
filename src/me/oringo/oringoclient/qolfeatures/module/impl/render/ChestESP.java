// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.render;

import me.oringo.oringoclient.events.RenderChestEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import me.oringo.oringoclient.utils.RenderUtils;
import me.oringo.oringoclient.OringoClient;
import net.minecraft.tileentity.TileEntity;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import net.minecraft.tileentity.TileEntityChest;
import java.util.List;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class ChestESP extends Module
{
    public BooleanSetting tracer;
    private boolean hasRendered;
    
    public ChestESP() {
        super("ChestESP", Category.RENDER);
        this.tracer = new BooleanSetting("Tracer", true);
        this.addSettings(this.tracer);
    }
    
    @SubscribeEvent
    public void onRender(final RenderWorldLastEvent event) {
        if (!this.isToggled() || !this.tracer.isEnabled()) {
            return;
        }
        for (final TileEntity tileEntityChest : (List)ChestESP.mc.field_71441_e.field_147482_g.stream().filter(tileEntity -> tileEntity instanceof TileEntityChest).collect(Collectors.toList())) {
            RenderUtils.tracerLine(tileEntityChest.func_174877_v().func_177958_n() + 0.5, tileEntityChest.func_174877_v().func_177956_o() + 0.5, tileEntityChest.func_174877_v().func_177952_p() + 0.5, OringoClient.clickGui.getColor());
        }
    }
    
    @SubscribeEvent
    public void onRenderChest(final RenderChestEvent event) {
        if (this.isToggled()) {
            if (event.isPre() && event.getChest() == ChestESP.mc.field_71441_e.func_175625_s(event.getChest().func_174877_v())) {
                RenderUtils.enableChams();
                this.hasRendered = true;
            }
            else if (this.hasRendered) {
                RenderUtils.disableChams();
                this.hasRendered = false;
            }
        }
    }
}
