// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.skyblock;

import java.util.Map;
import net.minecraft.block.Block;
import me.oringo.oringoclient.utils.SkyblockUtils;
import com.mojang.authlib.properties.Property;
import net.minecraft.tileentity.TileEntitySkull;
import me.oringo.oringoclient.events.WorldJoinEvent;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import me.oringo.oringoclient.events.PacketReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.events.BlockChangeEvent;
import java.util.HashMap;
import net.minecraft.util.BlockPos;
import java.util.ArrayList;
import me.oringo.oringoclient.utils.MilliTimer;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.ModeSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class GhostBlocks extends Module
{
    public NumberSetting range;
    public BooleanSetting cordGhostBlocks;
    public ModeSetting mode;
    private boolean wasPressed;
    private MilliTimer timer;
    private static ArrayList<BlockPos> ghostBlocks;
    private static HashMap<Long, BlockChangeEvent> eventQueue;
    private boolean hasSent;
    private static final int[][] cords;
    
    public GhostBlocks() {
        super("Ghost Blocks", 0, Category.SKYBLOCK);
        this.range = new NumberSetting("Range", 10.0, 1.0, 100.0, 1.0);
        this.cordGhostBlocks = new BooleanSetting("Cord blocks", true);
        this.mode = new ModeSetting("Speed", "Fast", new String[] { "Slow", "Fast" });
        this.timer = new MilliTimer();
        this.addSettings(this.range, this.mode, this.cordGhostBlocks);
    }
    
    @SubscribeEvent
    public void onKey(final TickEvent.ClientTickEvent event) {
        if (GhostBlocks.mc.field_71462_r != null || GhostBlocks.mc.field_71441_e == null || !this.isToggled()) {
            return;
        }
        if (this.cordGhostBlocks.isEnabled() && SecretAura.inBoss) {
            for (final int[] i : GhostBlocks.cords) {
                GhostBlocks.mc.field_71441_e.func_175698_g(new BlockPos(i[0], i[1], i[2]));
            }
        }
        this.hasSent = true;
        GhostBlocks.eventQueue.entrySet().removeIf(entry -> {
            if (System.currentTimeMillis() - entry.getKey() > 250L) {
                GhostBlocks.mc.field_71441_e.func_175656_a(((BlockChangeEvent)entry.getValue()).pos, ((BlockChangeEvent)entry.getValue()).state);
                GhostBlocks.ghostBlocks.remove(((BlockChangeEvent)entry.getValue()).pos);
                return true;
            }
            else {
                return false;
            }
        });
        this.hasSent = false;
        if (this.isPressed() && ((this.mode.getSelected().equals("Slow") && !this.wasPressed) || this.mode.getSelected().equals("Fast"))) {
            final Vec3 vec3 = GhostBlocks.mc.field_71439_g.func_174824_e(0.0f);
            final Vec3 vec4 = GhostBlocks.mc.field_71439_g.func_70676_i(0.0f);
            final Vec3 vec5 = vec3.func_72441_c(vec4.field_72450_a * this.range.getValue(), vec4.field_72448_b * this.range.getValue(), vec4.field_72449_c * this.range.getValue());
            final BlockPos obj = GhostBlocks.mc.field_71441_e.func_147447_a(vec3, vec5, true, false, true).func_178782_a();
            if (this.isValidBlock(obj)) {
                return;
            }
            GhostBlocks.mc.field_71441_e.func_175698_g(obj);
            GhostBlocks.ghostBlocks.add(obj);
        }
        this.wasPressed = this.isPressed();
    }
    
    @SubscribeEvent(receiveCanceled = true)
    public void onPacket(final PacketReceivedEvent event) {
        if (event.packet instanceof S08PacketPlayerPosLook) {
            GhostBlocks.eventQueue.clear();
        }
    }
    
    @SubscribeEvent
    public void onBlockChange(final BlockChangeEvent event) {
        if (event.state != null && GhostBlocks.ghostBlocks.contains(event.pos) && !this.hasSent && this.isToggled() && event.state.func_177230_c() != Blocks.field_150350_a) {
            event.setCanceled(true);
            GhostBlocks.eventQueue.put(System.currentTimeMillis(), event);
        }
    }
    
    @SubscribeEvent
    public void onWorldJoin(final WorldJoinEvent event) {
        GhostBlocks.eventQueue.clear();
        GhostBlocks.ghostBlocks.clear();
    }
    
    @Override
    public boolean isKeybind() {
        return true;
    }
    
    private boolean isValidBlock(final BlockPos blockPos) {
        final Block block = GhostBlocks.mc.field_71441_e.func_180495_p(blockPos).func_177230_c();
        if (block == Blocks.field_150465_bP) {
            final TileEntitySkull tileEntity = (TileEntitySkull)GhostBlocks.mc.field_71441_e.func_175625_s(blockPos);
            if (tileEntity.func_145904_a() == 3 && tileEntity.func_152108_a() != null && tileEntity.func_152108_a().getProperties() != null) {
                final Property property = SkyblockUtils.firstOrNull((Iterable<Property>)tileEntity.func_152108_a().getProperties().get((Object)"textures"));
                return property != null && property.getValue().equals("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzRkYjRhZGZhOWJmNDhmZjVkNDE3MDdhZTM0ZWE3OGJkMjM3MTY1OWZjZDhjZDg5MzQ3NDlhZjRjY2U5YiJ9fX0=");
            }
        }
        return block == Blocks.field_150442_at || block == Blocks.field_150486_ae || block == Blocks.field_150447_bR;
    }
    
    static {
        GhostBlocks.ghostBlocks = new ArrayList<BlockPos>();
        GhostBlocks.eventQueue = new HashMap<Long, BlockChangeEvent>();
        cords = new int[][] { { 275, 220, 231 }, { 275, 220, 232 }, { 299, 168, 243 }, { 299, 168, 244 }, { 299, 168, 246 }, { 299, 168, 247 }, { 299, 168, 247 }, { 300, 168, 247 }, { 300, 168, 246 }, { 300, 168, 244 }, { 300, 168, 243 }, { 298, 168, 247 }, { 298, 168, 246 }, { 298, 168, 244 }, { 298, 168, 243 }, { 287, 167, 240 }, { 288, 167, 240 }, { 289, 167, 240 }, { 290, 167, 240 }, { 291, 167, 240 }, { 292, 167, 240 }, { 293, 167, 240 }, { 294, 167, 240 }, { 295, 167, 240 }, { 290, 167, 239 }, { 291, 167, 239 }, { 292, 167, 239 }, { 293, 167, 239 }, { 294, 167, 239 }, { 295, 167, 239 }, { 290, 166, 239 }, { 291, 166, 239 }, { 292, 166, 239 }, { 293, 166, 239 }, { 294, 166, 239 }, { 295, 166, 239 }, { 290, 166, 240 }, { 291, 166, 240 }, { 292, 166, 240 }, { 293, 166, 240 }, { 294, 166, 240 }, { 295, 166, 240 } };
    }
}
