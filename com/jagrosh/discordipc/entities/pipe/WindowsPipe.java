// 
// Decompiled by Procyon v0.5.36
// 

package com.jagrosh.discordipc.entities.pipe;

import org.slf4j.LoggerFactory;
import org.json.JSONException;
import org.json.JSONObject;
import com.jagrosh.discordipc.entities.Packet;
import java.io.IOException;
import java.io.FileNotFoundException;
import com.jagrosh.discordipc.entities.Callback;
import java.util.HashMap;
import com.jagrosh.discordipc.IPCClient;
import java.io.RandomAccessFile;
import org.slf4j.Logger;

public class WindowsPipe extends Pipe
{
    private static final Logger LOGGER;
    private final RandomAccessFile file;
    
    WindowsPipe(final IPCClient ipcClient, final HashMap<String, Callback> callbacks, final String location) {
        super(ipcClient, callbacks);
        try {
            this.file = new RandomAccessFile(location, "rw");
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void write(final byte[] b) throws IOException {
        this.file.write(b);
    }
    
    @Override
    public Packet read() throws IOException, JSONException {
        while ((this.status == PipeStatus.CONNECTED || this.status == PipeStatus.CLOSING) && this.file.length() == 0L) {
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
        final Packet.OpCode op = Packet.OpCode.values()[Integer.reverseBytes(this.file.readInt())];
        final int len = Integer.reverseBytes(this.file.readInt());
        final byte[] d = new byte[len];
        this.file.readFully(d);
        final Packet p = new Packet(op, new JSONObject(new String(d)));
        WindowsPipe.LOGGER.debug(String.format("Received packet: %s", p.toString()));
        if (this.listener != null) {
            this.listener.onPacketReceived(this.ipcClient, p);
        }
        return p;
    }
    
    @Override
    public void close() throws IOException {
        WindowsPipe.LOGGER.debug("Closing IPC pipe...");
        this.status = PipeStatus.CLOSING;
        this.send(Packet.OpCode.CLOSE, new JSONObject(), null);
        this.status = PipeStatus.CLOSED;
        this.file.close();
    }
    
    static {
        LOGGER = LoggerFactory.getLogger(WindowsPipe.class);
    }
}
