// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.combat;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.item.ItemSword;
import me.oringo.oringoclient.events.MotionUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.oringo.oringoclient.utils.MovementUtils;
import net.minecraft.entity.player.EntityPlayer;
import me.oringo.oringoclient.OringoClient;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.utils.MilliTimer;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.NumberSetting;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.ModeSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class AutoBlock extends Module
{
    public ModeSetting mode;
    public NumberSetting blockTime;
    public BooleanSetting players;
    public BooleanSetting mobs;
    public BooleanSetting onDamage;
    public BooleanSetting noSlow;
    public MilliTimer blockTimer;
    private boolean isBlocking;
    
    public AutoBlock() {
        super("AutoBlock", Category.COMBAT);
        this.mode = new ModeSetting("Mode", "Hypixel", new String[] { "Hypixel", "Vanilla" });
        this.blockTime = new NumberSetting("Block time", 500.0, 50.0, 2000.0, 50.0);
        this.players = new BooleanSetting("Players", true);
        this.mobs = new BooleanSetting("Mobs", false);
        this.onDamage = new BooleanSetting("on Damage", true);
        this.noSlow = new BooleanSetting("No Slow", false);
        this.blockTimer = new MilliTimer();
        this.addSettings(this.mode, this.blockTime, this.players, this.mobs, this.onDamage, this.noSlow);
    }
    
    @SubscribeEvent
    public void onAttacK(final AttackEntityEvent event) {
        if (!this.isToggled() || OringoClient.killAura.isToggled()) {
            return;
        }
        if ((event.entityPlayer == AutoBlock.mc.field_71439_g && ((event.target instanceof EntityPlayer && this.players.isEnabled()) || (!(event.target instanceof EntityPlayer) && this.mobs.isEnabled()))) || (event.target == AutoBlock.mc.field_71439_g && this.onDamage.isEnabled())) {
            this.blockTimer.reset();
            if (event.entityPlayer == AutoBlock.mc.field_71439_g && (!MovementUtils.isMoving() || this.mode.is("Vanilla")) && this.isBlocking) {
                this.stopBlocking();
            }
        }
    }
    
    @SubscribeEvent
    public void onUpdate(final MotionUpdateEvent.Post event) {
        if (!this.isToggled() || OringoClient.killAura.isToggled()) {
            return;
        }
        if (!this.blockTimer.hasTimePassed((long)this.blockTime.getValue())) {
            if ((!this.isBlocking || this.mode.is("Hypixel")) && this.canBlock()) {
                this.startBlocking();
            }
        }
        else if (this.isBlocking) {
            this.stopBlocking();
        }
    }
    
    public boolean canBlock() {
        return AutoBlock.mc.field_71439_g.func_70694_bm() != null && AutoBlock.mc.field_71439_g.func_70694_bm().func_77973_b() instanceof ItemSword;
    }
    
    private void startBlocking() {
        AutoBlock.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C08PacketPlayerBlockPlacement(AutoBlock.mc.field_71439_g.func_70694_bm()));
        this.isBlocking = true;
    }
    
    private void stopBlocking() {
        if (this.isBlocking) {
            AutoBlock.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.field_177992_a, EnumFacing.DOWN));
            this.isBlocking = false;
        }
    }
    
    public boolean isBlocking() {
        return OringoClient.autoBlock.canBlock() && this.isBlocking && !this.noSlow.isEnabled();
    }
}
