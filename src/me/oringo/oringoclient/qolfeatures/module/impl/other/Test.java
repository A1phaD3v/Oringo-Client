// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.other;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.oringo.oringoclient.events.PacketSentEvent;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class Test extends Module
{
    public Test() {
        super("Test", Category.OTHER);
    }
    
    @SubscribeEvent
    public void onPacket(final PacketSentEvent event) {
    }
    
    @Override
    public boolean isDevOnly() {
        return true;
    }
}
