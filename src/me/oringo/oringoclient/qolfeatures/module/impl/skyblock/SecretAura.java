// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.skyblock;

import net.minecraftforge.event.world.WorldEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.network.play.server.S02PacketChat;
import me.oringo.oringoclient.events.PacketReceivedEvent;
import me.oringo.oringoclient.OringoClient;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.Block;
import com.mojang.authlib.properties.Property;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.oringo.oringoclient.utils.Rotation;
import java.util.Iterator;
import me.oringo.oringoclient.utils.RotationUtils;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import me.oringo.oringoclient.utils.SkyblockUtils;
import me.oringo.oringoclient.events.MotionUpdateEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import net.minecraft.util.BlockPos;
import java.util.ArrayList;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.StringSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class SecretAura extends Module
{
    public NumberSetting reach;
    public StringSetting item;
    public BooleanSetting cancelChest;
    public BooleanSetting clickedCheck;
    public BooleanSetting rotation;
    public static ArrayList<BlockPos> clicked;
    public static boolean inBoss;
    private boolean sent;
    
    public SecretAura() {
        super("Secret Aura", 0, Category.SKYBLOCK);
        this.reach = new NumberSetting("Reach", 5.0, 2.0, 6.0, 0.1);
        this.item = new StringSetting("Item");
        this.cancelChest = new BooleanSetting("Cancel chests", true);
        this.clickedCheck = new BooleanSetting("Clicked check", true);
        this.rotation = new BooleanSetting("Rotation", false);
        this.addSettings(this.reach, this.item, this.rotation, this.cancelChest, this.clickedCheck);
    }
    
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onUpdatePre(final MotionUpdateEvent.Pre event) {
        if (SecretAura.mc.field_71439_g != null && this.isToggled() && SkyblockUtils.inDungeon && this.rotation.isEnabled()) {
            final Vec3i vec3i = new Vec3i(10, 10, 10);
            for (final BlockPos blockPos : BlockPos.func_177980_a(new BlockPos((Vec3i)SecretAura.mc.field_71439_g.func_180425_c()).func_177971_a(vec3i), new BlockPos((Vec3i)SecretAura.mc.field_71439_g.func_180425_c().func_177973_b(vec3i)))) {
                if (this.isValidBlock(blockPos) && SecretAura.mc.field_71439_g.func_70011_f((double)blockPos.func_177958_n(), (double)(blockPos.func_177956_o() - SecretAura.mc.field_71439_g.func_70047_e()), (double)blockPos.func_177952_p()) < this.reach.getValue()) {
                    final Rotation floats = RotationUtils.getRotations(new Vec3(blockPos.func_177958_n() + 0.5, blockPos.func_177956_o() + 0.5, blockPos.func_177952_p() + 0.5));
                    event.yaw = floats.getYaw();
                    event.pitch = floats.getPitch();
                    break;
                }
            }
        }
    }
    
    @SubscribeEvent
    public void onTick(final MotionUpdateEvent.Post event) {
        if (SecretAura.mc.field_71439_g != null && this.isToggled() && SkyblockUtils.inDungeon) {
            final Vec3i vec3i = new Vec3i(10, 10, 10);
            for (final BlockPos blockPos : BlockPos.func_177980_a(new BlockPos((Vec3i)SecretAura.mc.field_71439_g.func_180425_c()).func_177971_a(vec3i), new BlockPos((Vec3i)SecretAura.mc.field_71439_g.func_180425_c().func_177973_b(vec3i)))) {
                if (this.isValidBlock(blockPos) && SecretAura.mc.field_71439_g.func_70011_f((double)blockPos.func_177958_n(), (double)(blockPos.func_177956_o() - SecretAura.mc.field_71439_g.func_70047_e()), (double)blockPos.func_177952_p()) < this.reach.getValue()) {
                    this.interactWithBlock(blockPos);
                    if (this.rotation.isEnabled()) {
                        break;
                    }
                    continue;
                }
            }
        }
    }
    
    private boolean isValidBlock(final BlockPos blockPos) {
        final Block block = SecretAura.mc.field_71441_e.func_180495_p(blockPos).func_177230_c();
        if (block == Blocks.field_150465_bP) {
            final TileEntitySkull tileEntity = (TileEntitySkull)SecretAura.mc.field_71441_e.func_175625_s(blockPos);
            if (tileEntity.func_145904_a() == 3 && tileEntity.func_152108_a() != null && tileEntity.func_152108_a().getProperties() != null) {
                final Property property = SkyblockUtils.firstOrNull((Iterable<Property>)tileEntity.func_152108_a().getProperties().get((Object)"textures"));
                return property != null && property.getValue().equals("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzRkYjRhZGZhOWJmNDhmZjVkNDE3MDdhZTM0ZWE3OGJkMjM3MTY1OWZjZDhjZDg5MzQ3NDlhZjRjY2U5YiJ9fX0=") && (!SecretAura.clicked.contains(blockPos) || !this.clickedCheck.isEnabled());
            }
        }
        return (block == Blocks.field_150442_at || block == Blocks.field_150486_ae || block == Blocks.field_150447_bR) && (!SecretAura.clicked.contains(blockPos) || !this.clickedCheck.isEnabled());
    }
    
    private void interactWithBlock(final BlockPos pos) {
        for (int i = 0; i < 9; ++i) {
            if (SecretAura.mc.field_71439_g.field_71071_by.func_70301_a(i) != null && SecretAura.mc.field_71439_g.field_71071_by.func_70301_a(i).func_82833_r().toLowerCase().contains(this.item.getValue().toLowerCase())) {
                final int holding = SecretAura.mc.field_71439_g.field_71071_by.field_70461_c;
                SecretAura.mc.field_71439_g.field_71071_by.field_70461_c = i;
                if (SecretAura.mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150442_at && !SecretAura.inBoss) {
                    SecretAura.mc.field_71442_b.func_178890_a(SecretAura.mc.field_71439_g, SecretAura.mc.field_71441_e, SecretAura.mc.field_71439_g.field_71071_by.func_70448_g(), pos, EnumFacing.func_176733_a((double)SecretAura.mc.field_71439_g.field_70177_z), new Vec3(0.0, 0.0, 0.0));
                }
                SecretAura.mc.field_71442_b.func_178890_a(SecretAura.mc.field_71439_g, SecretAura.mc.field_71441_e, SecretAura.mc.field_71439_g.field_71071_by.func_70448_g(), pos, EnumFacing.func_176733_a((double)SecretAura.mc.field_71439_g.field_70177_z), new Vec3(0.0, 0.0, 0.0));
                SecretAura.mc.field_71439_g.field_71071_by.field_70461_c = holding;
                SecretAura.clicked.add(pos);
                return;
            }
        }
        if (!this.sent) {
            OringoClient.sendMessageWithPrefix("You don't have a required item in your hotbar!");
            this.sent = true;
        }
    }
    
    @SubscribeEvent
    public void onChat(final PacketReceivedEvent event) {
        if (event.packet instanceof S02PacketChat && ChatFormatting.stripFormatting(((S02PacketChat)event.packet).func_148915_c().func_150254_d()).startsWith("[BOSS] Necron")) {
            SecretAura.inBoss = true;
        }
    }
    
    @SubscribeEvent
    public void onPacket(final PacketReceivedEvent event) {
        if (event.packet instanceof S2DPacketOpenWindow && ChatFormatting.stripFormatting(((S2DPacketOpenWindow)event.packet).func_179840_c().func_150254_d()).equals("Chest") && SkyblockUtils.inDungeon && this.cancelChest.isEnabled()) {
            event.setCanceled(true);
            SecretAura.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C0DPacketCloseWindow(((S2DPacketOpenWindow)event.packet).func_148901_c()));
        }
    }
    
    @SubscribeEvent
    public void clear(final WorldEvent.Load event) {
        SecretAura.inBoss = false;
        SecretAura.clicked.clear();
    }
    
    static {
        SecretAura.clicked = new ArrayList<BlockPos>();
    }
}
