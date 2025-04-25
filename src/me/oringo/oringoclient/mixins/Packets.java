// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.mixins;

import me.oringo.oringoclient.events.PacketReceivedEvent;
import me.oringo.oringoclient.events.WorldJoinEvent;
import net.minecraft.network.play.server.S01PacketJoinGame;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraftforge.fml.common.eventhandler.Event;
import me.oringo.oringoclient.events.PacketSentEvent;
import net.minecraftforge.common.MinecraftForge;
import me.oringo.oringoclient.utils.PacketUtils;
import me.oringo.oringoclient.utils.TimerUtil;
import me.oringo.oringoclient.OringoClient;
import me.oringo.oringoclient.qolfeatures.module.impl.other.Disabler;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.network.Packet;
import net.minecraft.util.Vec3;
import net.minecraft.network.NetworkManager;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ NetworkManager.class })
public abstract class Packets
{
    private static Vec3 initialPosition;
    
    @Inject(method = { "sendPacket(Lnet/minecraft/network/Packet;)V" }, at = { @At("HEAD") }, cancellable = true)
    private void onSendPacket(final Packet<?> packet, final CallbackInfo callbackInfo) {
        if (packet instanceof C03PacketPlayer && ((Disabler.timerSemi.isEnabled() && !((C03PacketPlayer)packet).func_149466_j()) || OringoClient.mc.field_71439_g == null || OringoClient.mc.field_71439_g.field_70173_aa < 80.0f * TimerUtil.getTimer().field_74278_d)) {
            if (OringoClient.disabler.isToggled()) {
                Disabler.wasEnabled = true;
                callbackInfo.cancel();
                return;
            }
            Disabler.wasEnabled = false;
        }
        if (!PacketUtils.noEvent.contains(packet) && MinecraftForge.EVENT_BUS.post((Event)new PacketSentEvent(packet))) {
            callbackInfo.cancel();
        }
    }
    
    @Inject(method = { "sendPacket(Lnet/minecraft/network/Packet;)V" }, at = { @At("RETURN") }, cancellable = true)
    private void onSendPacketPost(final Packet<?> packet, final CallbackInfo callbackInfo) {
        if (!PacketUtils.noEvent.contains(packet) && MinecraftForge.EVENT_BUS.post((Event)new PacketSentEvent.Post(packet))) {
            callbackInfo.cancel();
        }
        PacketUtils.noEvent.remove(packet);
    }
    
    @Inject(method = { "sendPacket(Lnet/minecraft/network/Packet;Lio/netty/util/concurrent/GenericFutureListener;[Lio/netty/util/concurrent/GenericFutureListener;)V" }, at = { @At("HEAD") }, cancellable = true)
    private void onSendPacket2(final Packet packetIn, final GenericFutureListener<? extends Future<? super Void>> listener, final GenericFutureListener<? extends Future<? super Void>>[] listeners, final CallbackInfo ci) {
        if (packetIn instanceof C03PacketPlayer && (OringoClient.mc.field_71439_g == null || OringoClient.mc.field_71439_g.field_70173_aa < 80.0f * TimerUtil.getTimer().field_74278_d)) {
            ci.cancel();
        }
    }
    
    @Inject(method = { "channelRead0" }, at = { @At("HEAD") }, cancellable = true)
    private void onChannelReadHead(final ChannelHandlerContext context, final Packet<?> packet, final CallbackInfo callbackInfo) {
        if (packet instanceof S01PacketJoinGame) {
            MinecraftForge.EVENT_BUS.post((Event)new WorldJoinEvent());
        }
        if (!PacketUtils.noEvent.contains(packet) && MinecraftForge.EVENT_BUS.post((Event)new PacketReceivedEvent(packet, context))) {
            callbackInfo.cancel();
        }
    }
    
    @Inject(method = { "channelRead0" }, at = { @At("RETURN") }, cancellable = true)
    private void onPost(final ChannelHandlerContext context, final Packet<?> packet, final CallbackInfo callbackInfo) {
        if (!PacketUtils.noEvent.contains(packet) && MinecraftForge.EVENT_BUS.post((Event)new PacketReceivedEvent.Post(packet, context))) {
            callbackInfo.cancel();
        }
        PacketUtils.noEvent.remove(packet);
    }
}
