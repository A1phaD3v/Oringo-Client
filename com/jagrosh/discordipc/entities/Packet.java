// 
// Decompiled by Procyon v0.5.36
// 

package com.jagrosh.discordipc.entities;

import java.nio.ByteBuffer;
import org.json.JSONObject;

public class Packet
{
    private final OpCode op;
    private final JSONObject data;
    
    public Packet(final OpCode op, final JSONObject data) {
        this.op = op;
        this.data = data;
    }
    
    public byte[] toBytes() {
        final byte[] d = this.data.toString().getBytes();
        final ByteBuffer packet = ByteBuffer.allocate(d.length + 8);
        packet.putInt(Integer.reverseBytes(this.op.ordinal()));
        packet.putInt(Integer.reverseBytes(d.length));
        packet.put(d);
        return packet.array();
    }
    
    public OpCode getOp() {
        return this.op;
    }
    
    public JSONObject getJson() {
        return this.data;
    }
    
    @Override
    public String toString() {
        return "Pkt:" + this.getOp() + this.getJson().toString();
    }
    
    public enum OpCode
    {
        HANDSHAKE, 
        FRAME, 
        CLOSE, 
        PING, 
        PONG;
    }
}
