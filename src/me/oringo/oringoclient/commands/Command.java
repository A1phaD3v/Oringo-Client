// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.commands;

import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentText;
import java.util.List;
import net.minecraftforge.common.MinecraftForge;
import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;

public abstract class Command
{
    private final String[] names;
    protected static final Minecraft mc;
    
    public Command(final String name, final String... names) {
        final List<String> names2 = new ArrayList<String>();
        names2.add(name);
        names2.addAll(Arrays.asList(names));
        this.names = names2.toArray(new String[0]);
        MinecraftForge.EVENT_BUS.register((Object)this);
    }
    
    public String[] getNames() {
        return this.names;
    }
    
    public abstract void execute(final String[] p0) throws Exception;
    
    public abstract String getDescription();
    
    public String getLongDesc() {
        return this.getDescription();
    }
    
    public static void sendMessage(final String message) {
        Minecraft.func_71410_x().field_71439_g.func_145747_a((IChatComponent)new ChatComponentText(message));
    }
    
    public static void sendMessageWithPrefix(final String message) {
        Minecraft.func_71410_x().field_71439_g.func_145747_a((IChatComponent)new ChatComponentText("§bOringoClient §3» §7" + message));
    }
    
    static {
        mc = Minecraft.func_71410_x();
    }
}
