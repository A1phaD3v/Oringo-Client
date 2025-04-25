// 
// Decompiled by Procyon v0.5.36
// 

package com.jagrosh.discordipc.entities.pipe;

import org.slf4j.LoggerFactory;
import org.json.JSONException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import org.json.JSONObject;
import com.jagrosh.discordipc.entities.Packet;
import java.io.IOException;
import java.net.SocketAddress;
import org.newsclub.net.unix.AFUNIXSocketAddress;
import java.io.File;
import com.jagrosh.discordipc.entities.Callback;
import java.util.HashMap;
import com.jagrosh.discordipc.IPCClient;
import org.newsclub.net.unix.AFUNIXSocket;
import org.slf4j.Logger;

public class UnixPipe extends Pipe
{
    private static final Logger LOGGER;
    private final AFUNIXSocket socket;
    
    UnixPipe(final IPCClient ipcClient, final HashMap<String, Callback> callbacks, final String location) throws IOException {
        super(ipcClient, callbacks);
        (this.socket = AFUNIXSocket.newInstance()).connect(new AFUNIXSocketAddress(new File(location)));
    }
    
    @Override
    public Packet read() throws IOException, JSONException {
        final InputStream is = this.socket.getInputStream();
        while ((this.status == PipeStatus.CONNECTED || this.status == PipeStatus.CLOSING) && is.available() == 0) {
            try {
                Thread.sleep(50L);
            }
            catch (InterruptedException ex) {}
        }
        if (this.status == PipeStatus.DISCONNECTED) {
            throw new IOException("Disconnected!");
        }
        if (this.status == PipeStatus.CLOSED) {
            return new Packet(Packet.OpCode.CLOSE, null);
        }
        byte[] d = new byte[8];
        is.read(d);
        final ByteBuffer bb = ByteBuffer.wrap(d);
        final Packet.OpCode op = Packet.OpCode.values()[Integer.reverseBytes(bb.getInt())];
        d = new byte[Integer.reverseBytes(bb.getInt())];
        is.read(d);
        final Packet p = new Packet(op, new JSONObject(new String(d)));
        UnixPipe.LOGGER.debug(String.format("Received packet: %s", p.toString()));
        if (this.listener != null) {
            this.listener.onPacketReceived(this.ipcClient, p);
        }
        return p;
    }
    
    @Override
    public void write(final byte[] b) throws IOException {
        this.socket.getOutputStream().write(b);
    }
    
    @Override
    public void close() throws IOException {
        UnixPipe.LOGGER.debug("Closing IPC pipe...");
        this.status = PipeStatus.CLOSING;
        this.send(Packet.OpCode.CLOSE, new JSONObject(), null);
        this.status = PipeStatus.CLOSED;
        this.socket.close();
    }
    
    static {
        LOGGER = LoggerFactory.getLogger(UnixPipe.class);
    }
}
