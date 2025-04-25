// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures;

import java.lang.reflect.Field;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.Reader;
import java.io.InputStreamReader;
import java.net.URL;
import com.google.gson.JsonParser;
import javax.swing.JOptionPane;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import me.oringo.oringoclient.OringoClient;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraft.util.Session;

public class LoginWithSession
{
    private Session original;
    
    public LoginWithSession() {
        this.original = null;
    }
    
    @SubscribeEvent
    public void onGuiCreate(final GuiScreenEvent.InitGuiEvent.Post event) {
        if (OringoClient.devMode && event.gui instanceof GuiMainMenu) {
            event.buttonList.add(new GuiButton(2137, 5, 5, 100, 20, "Login"));
            event.buttonList.add(new GuiButton(21370, 115, 5, 100, 20, "Save"));
        }
    }
    
    @SubscribeEvent
    public void onClick(final GuiScreenEvent.ActionPerformedEvent.Post event) {
        if (event.gui instanceof GuiMainMenu) {
            if (event.button.field_146127_k == 2137) {
                if (this.original == null) {
                    this.original = Minecraft.func_71410_x().func_110432_I();
                }
                final String login = JOptionPane.showInputDialog("Login");
                if (login == null || login.isEmpty()) {
                    return;
                }
                if (login.equalsIgnoreCase("reset")) {
                    try {
                        final Field field_71449_j = Minecraft.class.getDeclaredField("field_71449_j");
                        field_71449_j.setAccessible(true);
                        field_71449_j.set(Minecraft.func_71410_x(), this.original);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    return;
                }
                try {
                    final Field field_71449_j = Minecraft.class.getDeclaredField("field_71449_j");
                    field_71449_j.setAccessible(true);
                    final String username = new JsonParser().parse((Reader)new InputStreamReader(new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + login.split(": ")[1]).openStream())).getAsJsonObject().get("name").getAsString();
                    field_71449_j.set(Minecraft.func_71410_x(), new Session(username, login.split(": ")[1], login.split(": ")[0], "mojang"));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (event.button.field_146127_k == 21370) {
                try {
                    final BufferedWriter savedData = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("savedData")));
                    savedData.write(Minecraft.func_71410_x().func_110432_I().func_148254_d() + ": " + Minecraft.func_71410_x().func_110432_I().func_148255_b());
                    savedData.close();
                }
                catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
    }
}
