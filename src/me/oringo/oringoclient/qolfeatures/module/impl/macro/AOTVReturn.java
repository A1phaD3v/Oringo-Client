// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.macro;

import me.oringo.oringoclient.utils.Rotation;
import java.util.Iterator;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import me.oringo.oringoclient.utils.RotationUtils;
import net.minecraft.entity.Entity;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import net.minecraft.entity.item.EntityArmorStand;
import java.util.List;
import me.oringo.oringoclient.OringoClient;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.util.BlockPos;
import me.oringo.oringoclient.ui.notifications.Notifications;
import net.minecraft.block.material.Material;
import net.minecraft.util.MovingObjectPosition;
import org.lwjgl.input.Mouse;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import net.minecraft.util.Vec3;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.ModeSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.StringSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class AOTVReturn extends Module
{
    private StringSetting warp;
    private StringSetting coords;
    private BooleanSetting chat;
    private BooleanSetting middleClick;
    private NumberSetting delay;
    private ModeSetting mode;
    private Thread instance;
    private Vec3 rotate;
    private boolean openChat;
    private boolean wasDown;
    private boolean isRunning;
    
    public AOTVReturn() {
        super("AOTV Return", Category.OTHER);
        this.warp = new StringSetting("Warp command", "/warp forge");
        this.coords = new StringSetting("TP Coords", "0.5,167,-10.5;-23.5,180,-26.5;-64.5,212,-15.5;-33.5,244,-32.5");
        this.chat = new BooleanSetting("Open chat", true);
        this.middleClick = new BooleanSetting("Middle click", false);
        this.delay = new NumberSetting("Delay", 2000.0, 500.0, 5000.0, 1.0);
        this.mode = new ModeSetting("mode", "walk", new String[] { "jump", "walk" });
        this.instance = null;
        this.rotate = null;
        this.openChat = false;
        this.addSettings(this.warp, this.mode, this.coords, this.chat, this.middleClick, this.delay);
    }
    
    @Override
    public void onDisable() {
        this.isRunning = false;
        if (this.instance != null) {
            this.instance.stop();
        }
    }
    
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (this.openChat) {
            Minecraft.func_71410_x().func_147108_a((GuiScreen)new GuiChat());
            this.openChat = false;
        }
        if (AOTVReturn.mc.field_71439_g == null || AOTVReturn.mc.field_71441_e == null || !this.middleClick.isEnabled()) {
            return;
        }
        if (Mouse.isButtonDown(2) && AOTVReturn.mc.field_71462_r == null) {
            if (!this.wasDown && AOTVReturn.mc.field_71476_x != null && AOTVReturn.mc.field_71476_x.field_72313_a == MovingObjectPosition.MovingObjectType.BLOCK) {
                final BlockPos blockpos = AOTVReturn.mc.field_71476_x.func_178782_a();
                if (AOTVReturn.mc.field_71441_e.func_180495_p(blockpos).func_177230_c().func_149688_o() != Material.field_151579_a) {
                    this.coords.setValue(this.coords.getValue() + ((this.coords.getValue().length() > 0) ? ";" : "") + (blockpos.func_177958_n() + 0.5) + "," + (blockpos.func_177956_o() + 0.5) + "," + (blockpos.func_177952_p() + 0.5));
                    Notifications.showNotification("Oringo Client", "Added " + blockpos.func_177958_n() + " " + blockpos.func_177956_o() + " " + blockpos.func_177952_p() + " to coords!", 2500);
                }
            }
            this.wasDown = true;
        }
        else {
            this.wasDown = false;
        }
    }
    
    public boolean isRunning() {
        return this.isRunning;
    }
    
    public void start(final Runnable onFinish, final boolean stop) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        me/oringo/oringoclient/qolfeatures/module/impl/macro/AOTVReturn.instance:Ljava/lang/Thread;
        //     4: ifnull          14
        //     7: aload_0         /* this */
        //     8: getfield        me/oringo/oringoclient/qolfeatures/module/impl/macro/AOTVReturn.instance:Ljava/lang/Thread;
        //    11: invokevirtual   java/lang/Thread.stop:()V
        //    14: aload_0         /* this */
        //    15: iconst_1       
        //    16: putfield        me/oringo/oringoclient/qolfeatures/module/impl/macro/AOTVReturn.isRunning:Z
        //    19: invokestatic    net/minecraft/client/Minecraft.func_71410_x:()Lnet/minecraft/client/Minecraft;
        //    22: astore_3        /* mc */
        //    23: aload_0         /* this */
        //    24: new             Ljava/lang/Thread;
        //    27: dup            
        //    28: aload_0         /* this */
        //    29: aload_3         /* mc */
        //    30: aload_1         /* onFinish */
        //    31: iload_2         /* stop */
        //    32: invokedynamic   BootstrapMethod #0, run:(Lme/oringo/oringoclient/qolfeatures/module/impl/macro/AOTVReturn;Lnet/minecraft/client/Minecraft;Ljava/lang/Runnable;Z)Ljava/lang/Runnable;
        //    37: invokespecial   java/lang/Thread.<init>:(Ljava/lang/Runnable;)V
        //    40: dup_x1         
        //    41: putfield        me/oringo/oringoclient/qolfeatures/module/impl/macro/AOTVReturn.instance:Ljava/lang/Thread;
        //    44: invokevirtual   java/lang/Thread.start:()V
        //    47: return         
        //    StackMapTable: 00 01 0E
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.decompiler.languages.java.ast.NameVariables.generateNameForVariable(NameVariables.java:264)
        //     at com.strobel.decompiler.languages.java.ast.NameVariables.assignNamesToVariables(NameVariables.java:198)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:276)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
}
