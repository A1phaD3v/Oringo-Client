// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.commands.impl;

import net.minecraft.util.IChatComponent;
import net.minecraft.client.Minecraft;
import me.oringo.oringoclient.commands.CommandHandler;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.ChatComponentText;
import net.minecraft.entity.Entity;
import java.util.function.Consumer;
import java.util.function.ToDoubleFunction;
import java.util.Comparator;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.network.Packet;
import me.oringo.oringoclient.mixins.packet.C02Accessor;
import net.minecraft.network.play.client.C02PacketUseEntity;
import me.oringo.oringoclient.commands.Command;

public class ArmorStandsCommand extends Command
{
    public ArmorStandsCommand() {
        super("armorstands", new String[0]);
    }
    
    @Override
    public void execute(final String[] args) throws Exception {
        if (args.length == 1) {
            final C02PacketUseEntity packet = new C02PacketUseEntity();
            ((C02Accessor)packet).setEntityId(Integer.parseInt(args[0]));
            ((C02Accessor)packet).setAction(C02PacketUseEntity.Action.INTERACT);
            ArmorStandsCommand.mc.func_147114_u().func_147298_b().func_179290_a((Packet)packet);
            return;
        }
        ArmorStandsCommand.mc.field_71441_e.field_72996_f.stream().filter(entity -> entity instanceof EntityArmorStand && entity.func_145748_c_().func_150254_d().length() > 5 && entity.func_145818_k_()).sorted(Comparator.comparingDouble((ToDoubleFunction<? super Object>)ArmorStandsCommand.mc.field_71439_g::func_70032_d).reversed()).forEach(ArmorStandsCommand::sendEntityInteract);
    }
    
    private static void sendEntityInteract(final Entity entity) {
        final ChatComponentText chatComponentText = new ChatComponentText("Name: " + entity.func_145748_c_().func_150254_d());
        final ChatStyle style = new ChatStyle();
        style.func_150241_a(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("%sarmorstands %s", CommandHandler.getCommandPrefix(), entity.func_145782_y())));
        chatComponentText.func_150255_a(style);
        Minecraft.func_71410_x().field_71439_g.func_145747_a((IChatComponent)chatComponentText);
    }
    
    @Override
    public String getDescription() {
        return "Shows you a list of loaded armor stands.";
    }
}
