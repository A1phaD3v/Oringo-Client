// hud nerd?
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient;

import net.minecraft.client.renderer.texture.DynamicTexture;
import java.nio.file.Files;
import java.nio.file.CopyOption;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentText;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.nio.ByteBuffer;
import java.io.InputStream;
import javax.net.ssl.SSLSession;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import java.security.KeyManagementException;
import javax.net.ssl.KeyManager;
import java.security.SecureRandom;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.SSLContext;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;
import javax.net.ssl.TrustManager;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.lang.reflect.Field;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.DestroyBlockProgress;
import java.util.Map;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import java.util.Iterator;
import me.oringo.oringoclient.utils.font.Fonts;
import me.oringo.oringoclient.qolfeatures.LoginWithSession;
import me.oringo.oringoclient.commands.impl.BanCommand;
import me.oringo.oringoclient.ui.hud.impl.TargetComponent;
import me.oringo.oringoclient.config.ConfigManager;
import me.oringo.oringoclient.utils.SkyblockUtils;
import me.oringo.oringoclient.qolfeatures.AttackQueue;
import me.oringo.oringoclient.qolfeatures.Updater;
import me.oringo.oringoclient.utils.ServerUtils;
import me.oringo.oringoclient.ui.notifications.Notifications;
import me.oringo.oringoclient.commands.impl.CustomESPCommand;
import me.oringo.oringoclient.commands.impl.TestCommand;
import me.oringo.oringoclient.commands.impl.SayCommand;
import me.oringo.oringoclient.commands.impl.SettingsCommand;
import me.oringo.oringoclient.commands.impl.FireworkCommand;
import me.oringo.oringoclient.commands.impl.ConfigCommand;
import me.oringo.oringoclient.commands.impl.ClipCommand;
import me.oringo.oringoclient.commands.impl.ChecknameCommand;
import me.oringo.oringoclient.commands.impl.ArmorStandsCommand;
import me.oringo.oringoclient.commands.impl.HelpCommand;
import me.oringo.oringoclient.commands.impl.WardrobeCommand;
import me.oringo.oringoclient.commands.Command;
import me.oringo.oringoclient.commands.CommandHandler;
import me.oringo.oringoclient.commands.impl.StalkCommand;
import net.minecraftforge.common.MinecraftForge;
import me.oringo.oringoclient.utils.shader.BlurUtils;
import me.oringo.oringoclient.qolfeatures.module.impl.other.Timer;
import me.oringo.oringoclient.qolfeatures.module.impl.skyblock.BlazeSwapper;
import me.oringo.oringoclient.qolfeatures.module.impl.render.Nametags;
import me.oringo.oringoclient.qolfeatures.module.impl.player.AutoHeal;
import me.oringo.oringoclient.qolfeatures.module.impl.combat.AntiBot;
import me.oringo.oringoclient.qolfeatures.module.impl.combat.AimAssist;
import me.oringo.oringoclient.qolfeatures.module.impl.player.ArmorSwap;
import me.oringo.oringoclient.qolfeatures.module.impl.other.StaffAnalyser;
import me.oringo.oringoclient.qolfeatures.module.impl.combat.AutoClicker;
import me.oringo.oringoclient.qolfeatures.module.impl.render.Trial;
import me.oringo.oringoclient.qolfeatures.module.impl.render.NoRender;
import me.oringo.oringoclient.qolfeatures.module.impl.skyblock.AntiNukebi;
import me.oringo.oringoclient.qolfeatures.module.impl.movement.Step;
import me.oringo.oringoclient.qolfeatures.module.impl.player.NoFall;
import me.oringo.oringoclient.qolfeatures.module.impl.player.AutoPot;
import me.oringo.oringoclient.qolfeatures.module.impl.skyblock.BoneThrower;
import me.oringo.oringoclient.qolfeatures.module.impl.render.ChestESP;
import me.oringo.oringoclient.qolfeatures.module.impl.other.MurdererFinder;
import me.oringo.oringoclient.qolfeatures.module.impl.other.Blink;
import me.oringo.oringoclient.qolfeatures.module.impl.player.FastPlace;
import me.oringo.oringoclient.qolfeatures.module.impl.other.ServerBeamer;
import me.oringo.oringoclient.qolfeatures.module.impl.player.AutoTool;
import me.oringo.oringoclient.qolfeatures.module.impl.combat.WTap;
import me.oringo.oringoclient.qolfeatures.module.impl.render.TargetHUD;
import me.oringo.oringoclient.qolfeatures.module.impl.render.ServerRotations;
import me.oringo.oringoclient.qolfeatures.module.impl.skyblock.IceFillHelp;
import me.oringo.oringoclient.qolfeatures.module.impl.skyblock.Snowballs;
import me.oringo.oringoclient.qolfeatures.module.impl.other.GuessTheBuildAFK;
import me.oringo.oringoclient.qolfeatures.module.impl.skyblock.AutoRogueSword;
import me.oringo.oringoclient.qolfeatures.module.impl.render.CustomESP;
import me.oringo.oringoclient.qolfeatures.module.impl.render.RichPresenceModule;
import me.oringo.oringoclient.qolfeatures.module.impl.render.ChinaHat;
import me.oringo.oringoclient.qolfeatures.module.impl.render.PlayerESP;
import me.oringo.oringoclient.qolfeatures.module.impl.player.ChestStealer;
import me.oringo.oringoclient.qolfeatures.module.impl.player.InvManager;
import me.oringo.oringoclient.qolfeatures.module.impl.skyblock.AutoS1;
import me.oringo.oringoclient.qolfeatures.module.impl.other.TntRunPing;
import me.oringo.oringoclient.qolfeatures.module.impl.skyblock.CrimsonQOL;
import me.oringo.oringoclient.qolfeatures.module.impl.combat.SumoFences;
import me.oringo.oringoclient.qolfeatures.module.impl.skyblock.GhostBlocks;
import me.oringo.oringoclient.qolfeatures.module.impl.skyblock.RemoveAnnoyingMobs;
import me.oringo.oringoclient.qolfeatures.module.impl.movement.SafeWalk;
import me.oringo.oringoclient.qolfeatures.module.impl.render.DungeonESP;
import me.oringo.oringoclient.qolfeatures.module.impl.skyblock.SecretAura;
import me.oringo.oringoclient.qolfeatures.module.impl.skyblock.TerminatorAura;
import me.oringo.oringoclient.qolfeatures.module.impl.other.ChatBypass;
import me.oringo.oringoclient.qolfeatures.module.impl.skyblock.TerminalAura;
import me.oringo.oringoclient.qolfeatures.module.impl.other.AntiNicker;
import me.oringo.oringoclient.qolfeatures.module.impl.player.AntiVoid;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import me.oringo.oringoclient.utils.MilliTimer;
import java.io.File;
import net.minecraft.util.ResourceLocation;
import java.util.HashMap;
import net.minecraft.util.BlockPos;
import java.util.ArrayList;
import me.oringo.oringoclient.qolfeatures.module.impl.render.InventoryDisplay;
import me.oringo.oringoclient.qolfeatures.module.impl.movement.Flight;
import me.oringo.oringoclient.qolfeatures.module.impl.movement.Scaffold;
import me.oringo.oringoclient.qolfeatures.module.impl.other.Disabler;
import me.oringo.oringoclient.qolfeatures.module.impl.render.PopupAnimation;
import me.oringo.oringoclient.qolfeatures.module.impl.skyblock.DojoHelper;
import me.oringo.oringoclient.qolfeatures.module.impl.movement.GuiMove;
import me.oringo.oringoclient.qolfeatures.module.impl.combat.TargetStrafe;
import me.oringo.oringoclient.qolfeatures.module.impl.other.Test;
import me.oringo.oringoclient.qolfeatures.module.impl.movement.Speed;
import me.oringo.oringoclient.qolfeatures.module.impl.combat.AutoBlock;
import me.oringo.oringoclient.qolfeatures.module.impl.render.CustomInterfaces;
import me.oringo.oringoclient.qolfeatures.module.impl.render.Giants;
import me.oringo.oringoclient.qolfeatures.module.impl.render.FreeCam;
import me.oringo.oringoclient.qolfeatures.module.impl.skyblock.Phase;
import me.oringo.oringoclient.qolfeatures.module.impl.player.NoRotate;
import me.oringo.oringoclient.qolfeatures.module.impl.combat.Hitboxes;
import me.oringo.oringoclient.qolfeatures.module.impl.other.Derp;
import me.oringo.oringoclient.qolfeatures.module.impl.macro.MithrilMacro;
import me.oringo.oringoclient.qolfeatures.module.impl.render.Camera;
import me.oringo.oringoclient.qolfeatures.module.impl.render.AnimationCreator;
import me.oringo.oringoclient.qolfeatures.module.impl.render.Animations;
import me.oringo.oringoclient.qolfeatures.module.impl.render.NickHider;
import me.oringo.oringoclient.qolfeatures.module.impl.macro.AOTVReturn;
import me.oringo.oringoclient.qolfeatures.module.impl.player.FastBreak;
import me.oringo.oringoclient.qolfeatures.module.impl.macro.AutoSumoBot;
import me.oringo.oringoclient.qolfeatures.module.impl.combat.Reach;
import me.oringo.oringoclient.qolfeatures.module.impl.movement.Sprint;
import me.oringo.oringoclient.qolfeatures.module.impl.combat.NoSlow;
import me.oringo.oringoclient.qolfeatures.module.impl.combat.NoHitDelay;
import me.oringo.oringoclient.qolfeatures.module.impl.other.Modless;
import me.oringo.oringoclient.qolfeatures.module.impl.skyblock.Aimbot;
import me.oringo.oringoclient.qolfeatures.module.impl.player.Velocity;
import me.oringo.oringoclient.qolfeatures.module.impl.combat.KillAura;
import me.oringo.oringoclient.qolfeatures.module.impl.render.Gui;
import me.oringo.oringoclient.qolfeatures.module.Module;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;

@Mod(modid = "examplemod", dependencies = "before:*", version = "1.7.1")
public class OringoClient
{
    public static final Minecraft mc;
    public static final String KEY = "NIGGER";
    public static final Logger LOGGER;
    public static final CopyOnWriteArrayList<Module> modules;
    public static final Gui clickGui;
    public static final KillAura killAura;
    public static final Velocity velocity;
    public static final Aimbot bloodAimbot;
    public static final Modless modless;
    public static final NoHitDelay noHitDelay;
    public static final NoSlow noSlow;
    public static final Sprint sprint;
    public static final Reach reach;
    public static final AutoSumoBot autoSumo;
    public static final FastBreak fastBreak;
    public static final AOTVReturn aotvReturn;
    public static final NickHider nickHider;
    public static final Animations animations;
    public static final AnimationCreator animationCreator;
    public static final Camera camera;
    public static final MithrilMacro mithrilMacro;
    public static final Derp derp;
    public static final Hitboxes hitboxes;
    public static final NoRotate noRotate;
    public static final Phase phase;
    public static final FreeCam freeCam;
    public static final Giants giants;
    public static final CustomInterfaces interfaces;
    public static final AutoBlock autoBlock;
    public static final Speed speed;
    public static final Test test;
    public static final TargetStrafe targetStrafe;
    public static final GuiMove guiMove;
    public static final DojoHelper dojoHelper;
    public static final PopupAnimation popupAnimation;
    public static final Disabler disabler;
    public static final Scaffold scaffold;
    public static final Flight fly;
    public static final InventoryDisplay inventoryHUDModule;
    public static boolean shouldUpdate;
    public static String[] vers;
    public static ArrayList<BlockPos> stop;
    public static final String MODID = "examplemod";
    public static final String PREFIX = "§bOringoClient §3» §7";
    public static final String VERSION = "1.7.1";
    public static boolean devMode;
    public static ArrayList<Runnable> scheduledTasks;
    public static HashMap<String, ResourceLocation> capes;
    public static HashMap<File, ResourceLocation> capesLoaded;
    private MilliTimer timer;
    private boolean wasOnline;
    
    public OringoClient() {
        this.timer = new MilliTimer();
    }
    
    @Mod.EventHandler
    public void onPre(final FMLPreInitializationEvent event) {
    }
    
    @Mod.EventHandler
    public void onInit(final FMLPreInitializationEvent event) {
        new File(OringoClient.mc.field_71412_D.getPath() + "/config/OringoClient").mkdir();
        new File(OringoClient.mc.field_71412_D.getPath() + "/config/OringoClient/capes").mkdir();
        new File(OringoClient.mc.field_71412_D.getPath() + "/config/OringoClient/configs").mkdir();
        update();
        OringoClient.modless.setToggled(true);
        OringoClient.modules.add(new AntiVoid());
        OringoClient.modules.add(OringoClient.clickGui);
        OringoClient.modules.add(OringoClient.killAura);
        OringoClient.modules.add(OringoClient.noRotate);
        OringoClient.modules.add(OringoClient.velocity);
        OringoClient.modules.add(OringoClient.bloodAimbot);
        OringoClient.modules.add(new AntiNicker());
        OringoClient.modules.add(new TerminalAura());
        OringoClient.modules.add(new ChatBypass());
        OringoClient.modules.add(new TerminatorAura());
        OringoClient.modules.add(new SecretAura());
        OringoClient.modules.add(new DungeonESP());
        OringoClient.modules.add(new SafeWalk());
        OringoClient.modules.add(new RemoveAnnoyingMobs());
        OringoClient.modules.add(new GhostBlocks());
        OringoClient.modules.add(new SumoFences());
        OringoClient.modules.add(OringoClient.dojoHelper);
        OringoClient.modules.add(new CrimsonQOL());
        OringoClient.modules.add(OringoClient.modless);
        OringoClient.modules.add(OringoClient.noHitDelay);
        OringoClient.modules.add(new TntRunPing());
        OringoClient.modules.add(OringoClient.noSlow);
        OringoClient.modules.add(OringoClient.sprint);
        OringoClient.modules.add(OringoClient.reach);
        OringoClient.modules.add(new AutoS1());
        OringoClient.modules.add(new InvManager());
        OringoClient.modules.add(new ChestStealer());
        OringoClient.modules.add(new PlayerESP());
        OringoClient.modules.add(OringoClient.autoSumo);
        OringoClient.modules.add(OringoClient.fastBreak);
        OringoClient.modules.add(OringoClient.nickHider);
        OringoClient.modules.add(new ChinaHat());
        OringoClient.modules.add(OringoClient.aotvReturn);
        OringoClient.modules.add(OringoClient.mithrilMacro);
        final RichPresenceModule richPresence = new RichPresenceModule();
        OringoClient.modules.add(richPresence);
        OringoClient.modules.add(OringoClient.inventoryHUDModule);
        OringoClient.modules.add(new CustomESP());
        OringoClient.modules.add(OringoClient.fly);
        OringoClient.modules.add(new AutoRogueSword());
        OringoClient.modules.add(new GuessTheBuildAFK());
        OringoClient.modules.add(new Snowballs());
        OringoClient.modules.add(new IceFillHelp());
        OringoClient.modules.add(OringoClient.animations);
        OringoClient.modules.add(ServerRotations.getInstance());
        OringoClient.modules.add(TargetHUD.getInstance());
        OringoClient.modules.add(new WTap());
        OringoClient.modules.add(new AutoTool());
        OringoClient.modules.add(OringoClient.camera);
        OringoClient.modules.add(OringoClient.interfaces);
        OringoClient.modules.add(new ServerBeamer());
        OringoClient.modules.add(FastPlace.getInstance());
        OringoClient.modules.add(OringoClient.derp);
        OringoClient.modules.add(new Blink());
        OringoClient.modules.add(OringoClient.freeCam);
        OringoClient.modules.add(OringoClient.hitboxes);
        OringoClient.modules.add(new MurdererFinder());
        OringoClient.modules.add(new ChestESP());
        OringoClient.modules.add(OringoClient.test);
        OringoClient.modules.add(new BoneThrower());
        OringoClient.modules.add(OringoClient.phase);
        OringoClient.modules.add(OringoClient.giants);
        OringoClient.modules.add(new AutoPot());
        OringoClient.modules.add(OringoClient.disabler);
        OringoClient.modules.add(OringoClient.guiMove);
        OringoClient.modules.add(OringoClient.autoBlock);
        OringoClient.modules.add(OringoClient.speed);
        OringoClient.modules.add(new NoFall());
        OringoClient.modules.add(new Step());
        OringoClient.modules.add(OringoClient.popupAnimation);
        OringoClient.modules.add(OringoClient.targetStrafe);
        OringoClient.modules.add(new AntiNukebi());
        OringoClient.modules.add(new NoRender());
        OringoClient.modules.add(new Trial());
        OringoClient.modules.add(new AutoClicker());
        OringoClient.modules.add(new StaffAnalyser());
        OringoClient.modules.add(new ArmorSwap());
        OringoClient.modules.add(OringoClient.scaffold);
        OringoClient.modules.add(new AimAssist());
        OringoClient.modules.add(AntiBot.getAntiBot());
        OringoClient.modules.add(new AutoHeal());
        OringoClient.modules.add(new Nametags());
        OringoClient.modules.add(new BlazeSwapper());
        OringoClient.modules.add(new Timer());
        OringoClient.interfaces.setToggled(true);
        BlurUtils.registerListener();
        for (final Module m : OringoClient.modules) {
            MinecraftForge.EVENT_BUS.register((Object)m);
        }
        CommandHandler.register(new StalkCommand());
        CommandHandler.register(new WardrobeCommand());
        CommandHandler.register(new HelpCommand());
        CommandHandler.register(new ArmorStandsCommand());
        CommandHandler.register(new ChecknameCommand());
        CommandHandler.register(new ClipCommand());
        CommandHandler.register(new ConfigCommand());
        CommandHandler.register(new FireworkCommand());
        CommandHandler.register(new SettingsCommand());
        CommandHandler.register(new SayCommand());
        CommandHandler.register(new TestCommand());
        CommandHandler.register(new CustomESPCommand());
        MinecraftForge.EVENT_BUS.register((Object)new Notifications());
        MinecraftForge.EVENT_BUS.register((Object)this);
        MinecraftForge.EVENT_BUS.register((Object)ServerUtils.instance);
        MinecraftForge.EVENT_BUS.register((Object)new Updater());
        MinecraftForge.EVENT_BUS.register((Object)new AttackQueue());
        MinecraftForge.EVENT_BUS.register((Object)new SkyblockUtils());
        ConfigManager.loadConfig();
        TargetComponent.INSTANCE.setPosition(TargetHUD.getInstance().x.getValue(), TargetHUD.getInstance().y.getValue());
        if (Disabler.first.isEnabled()) {
            OringoClient.disabler.setToggled(true);
            Disabler.first.setEnabled(false);
        }
        if (richPresence.isToggled()) {
            richPresence.onEnable();
        }
        if (new File("OringoDev").exists()) {
            OringoClient.devMode = true;
        }
        if (OringoClient.devMode) {
            CommandHandler.register(new BanCommand());
            MinecraftForge.EVENT_BUS.register((Object)new LoginWithSession());
            OringoClient.modules.add(OringoClient.animationCreator);
        }
        Fonts.bootstrap();
    }
    
    @Mod.EventHandler
    public void onPost(final FMLPostInitializationEvent event) {
        loadCapes();
    }
    
    public static Map<Integer, DestroyBlockProgress> getBlockBreakProgress() {
        try {
            final Field field_72738_e = RenderGlobal.class.getDeclaredField("field_72738_E");
            field_72738_e.setAccessible(true);
            return (Map<Integer, DestroyBlockProgress>)field_72738_e.get(Minecraft.func_71410_x().field_71438_f);
        }
        catch (Exception exception) {
            return new HashMap<Integer, DestroyBlockProgress>();
        }
    }
    
    public static void handleKeypress(final int key) {
        if (key == 0) {
            return;
        }
        for (final Module m : OringoClient.modules) {
            if (m.getKeycode() == key) {
                if (m.isKeybind()) {
                    continue;
                }
                m.toggle();
                if (OringoClient.clickGui.disableNotifs.isEnabled()) {
                    continue;
                }
                Notifications.showNotification("Oringo Client", m.getName() + (m.isToggled() ? " enabled!" : " disabled!"), 2500);
            }
        }
    }
    
    private static void update() {
        checkForUpdates();
        try {
            OringoClient.vers = new BufferedReader(new InputStreamReader(new URL("http://niger.5v.pl/version").openStream())).readLine().split(" ");
            if (!"1.7.1".equals(OringoClient.vers[0])) {
                OringoClient.shouldUpdate = true;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Couldn't update");
        }
    }
    
    private static void checkForUpdates() {
    }
    
    private static void loadCapes() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: invokespecial   com/google/gson/Gson.<init>:()V
        //     7: new             Ljava/io/InputStreamReader;
        //    10: dup            
        //    11: new             Ljava/net/URL;
        //    14: dup            
        //    15: ldc_w           "http://niger.5v.pl/capes.txt"
        //    18: invokespecial   java/net/URL.<init>:(Ljava/lang/String;)V
        //    21: invokevirtual   java/net/URL.openStream:()Ljava/io/InputStream;
        //    24: invokespecial   java/io/InputStreamReader.<init>:(Ljava/io/InputStream;)V
        //    27: ldc             Ljava/util/HashMap;.class
        //    29: invokevirtual   com/google/gson/Gson.fromJson:(Ljava/io/Reader;Ljava/lang/Class;)Ljava/lang/Object;
        //    32: checkcast       Ljava/util/HashMap;
        //    35: astore_0        /* capeData */
        //    36: getstatic       me/oringo/oringoclient/OringoClient.capes:Ljava/util/HashMap;
        //    39: invokevirtual   java/util/HashMap.clear:()V
        //    42: aload_0         /* capeData */
        //    43: ifnull          87
        //    46: new             Ljava/util/HashMap;
        //    49: dup            
        //    50: invokespecial   java/util/HashMap.<init>:()V
        //    53: astore_1        /* cache */
        //    54: aload_0         /* capeData */
        //    55: aload_1         /* cache */
        //    56: invokedynamic   BootstrapMethod #0, accept:(Ljava/util/HashMap;)Ljava/util/function/BiConsumer;
        //    61: invokevirtual   java/util/HashMap.forEach:(Ljava/util/function/BiConsumer;)V
        //    64: getstatic       me/oringo/oringoclient/OringoClient.devMode:Z
        //    67: ifeq            87
        //    70: getstatic       java/lang/System.out:Ljava/io/PrintStream;
        //    73: new             Lcom/google/gson/Gson;
        //    76: dup            
        //    77: invokespecial   com/google/gson/Gson.<init>:()V
        //    80: aload_0         /* capeData */
        //    81: invokevirtual   com/google/gson/Gson.toJson:(Ljava/lang/Object;)Ljava/lang/String;
        //    84: invokevirtual   java/io/PrintStream.println:(Ljava/lang/String;)V
        //    87: goto            95
        //    90: astore_0        /* e */
        //    91: aload_0         /* e */
        //    92: invokevirtual   java/lang/Exception.printStackTrace:()V
        //    95: return         
        //    StackMapTable: 00 03 FB 00 57 42 07 02 12 04
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  0      87     90     95     Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Could not infer any expression.
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:374)
        //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:344)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
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
    
    private void disableSSLVerification() {
        final TrustManager[] trustAllCerts = { new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                
                @Override
                public void checkClientTrusted(final X509Certificate[] arg0, final String arg1) throws CertificateException {
                }
                
                @Override
                public void checkServerTrusted(final X509Certificate[] arg0, final String arg1) throws CertificateException {
                }
            } };
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSL");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            sc.init(null, trustAllCerts, new SecureRandom());
        }
        catch (KeyManagementException e2) {
            e2.printStackTrace();
        }
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        final HostnameVerifier validHosts = new HostnameVerifier() {
            @Override
            public boolean verify(final String arg0, final SSLSession arg1) {
                return true;
            }
        };
        HttpsURLConnection.setDefaultHostnameVerifier(validHosts);
    }
    
    private ByteBuffer readImageToBuffer(final InputStream imageStream) throws IOException {
        final BufferedImage bufferedimage = ImageIO.read(imageStream);
        final int[] aint = bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), null, 0, bufferedimage.getWidth());
        final ByteBuffer bytebuffer = ByteBuffer.allocate(4 * aint.length);
        for (final int i : aint) {
            bytebuffer.putInt(i << 8 | (i >> 24 & 0xFF));
        }
        bytebuffer.flip();
        return bytebuffer;
    }
    
    public static void sendMessage(final String message) {
        Minecraft.func_71410_x().field_71439_g.func_145747_a((IChatComponent)new ChatComponentText(message));
    }
    
    public static void sendMessageWithPrefix(final String message) {
        Minecraft.func_71410_x().field_71439_g.func_145747_a((IChatComponent)new ChatComponentText("§bOringoClient §3» §7" + message));
    }
    
    static {
        mc = Minecraft.func_71410_x();
        LOGGER = Logger.getLogger("Oringo Client");
        modules = new CopyOnWriteArrayList<Module>();
        clickGui = new Gui();
        killAura = new KillAura();
        velocity = new Velocity();
        bloodAimbot = new Aimbot();
        modless = new Modless();
        noHitDelay = new NoHitDelay();
        noSlow = new NoSlow();
        sprint = new Sprint();
        reach = new Reach();
        autoSumo = new AutoSumoBot();
        fastBreak = new FastBreak();
        aotvReturn = new AOTVReturn();
        nickHider = new NickHider();
        animations = new Animations();
        animationCreator = new AnimationCreator();
        camera = new Camera();
        mithrilMacro = new MithrilMacro();
        derp = new Derp();
        hitboxes = new Hitboxes();
        noRotate = new NoRotate();
        phase = new Phase();
        freeCam = new FreeCam();
        giants = new Giants();
        interfaces = new CustomInterfaces();
        autoBlock = new AutoBlock();
        speed = new Speed();
        test = new Test();
        targetStrafe = new TargetStrafe();
        guiMove = new GuiMove();
        dojoHelper = new DojoHelper();
        popupAnimation = new PopupAnimation();
        disabler = new Disabler();
        scaffold = new Scaffold();
        fly = new Flight();
        inventoryHUDModule = new InventoryDisplay();
        OringoClient.stop = new ArrayList<BlockPos>();
        OringoClient.devMode = false;
        OringoClient.scheduledTasks = new ArrayList<Runnable>();
        OringoClient.capes = new HashMap<String, ResourceLocation>();
        OringoClient.capesLoaded = new HashMap<File, ResourceLocation>();
    }
}
