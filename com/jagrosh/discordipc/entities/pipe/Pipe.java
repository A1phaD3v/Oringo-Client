// 
// Decompiled by Procyon v0.5.36
// 

package com.jagrosh.discordipc.entities.pipe;

import org.slf4j.LoggerFactory;
import java.util.UUID;
import com.jagrosh.discordipc.exceptions.NoDiscordClientException;
import org.json.JSONException;
import java.io.IOException;
import org.json.JSONObject;
import com.jagrosh.discordipc.entities.Packet;
import com.jagrosh.discordipc.entities.Callback;
import java.util.HashMap;
import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.entities.DiscordBuild;
import com.jagrosh.discordipc.IPCListener;
import org.slf4j.Logger;

public abstract class Pipe
{
    private static final Logger LOGGER;
    private static final int VERSION = 1;
    PipeStatus status;
    IPCListener listener;
    private DiscordBuild build;
    final IPCClient ipcClient;
    private final HashMap<String, Callback> callbacks;
    private static final String[] unixPaths;
    
    Pipe(final IPCClient ipcClient, final HashMap<String, Callback> callbacks) {
        this.status = PipeStatus.CONNECTING;
        this.ipcClient = ipcClient;
        this.callbacks = callbacks;
    }
    
    public static Pipe openPipe(final IPCClient ipcClient, final long clientId, final HashMap<String, Callback> callbacks, DiscordBuild... preferredOrder) throws NoDiscordClientException {
        if (preferredOrder == null || preferredOrder.length == 0) {
            preferredOrder = new DiscordBuild[] { DiscordBuild.ANY };
        }
        Pipe pipe = null;
        final Pipe[] open = new Pipe[DiscordBuild.values().length];
        for (int i = 0; i < 10; ++i) {
            try {
                final String location = getPipeLocation(i);
                Pipe.LOGGER.debug(String.format("Searching for IPC: %s", location));
                pipe = createPipe(ipcClient, callbacks, location);
                pipe.send(Packet.OpCode.HANDSHAKE, new JSONObject().put("v", 1).put("client_id", Long.toString(clientId)), null);
                final Packet p = pipe.read();
                pipe.build = DiscordBuild.from(p.getJson().getJSONObject("data").getJSONObject("config").getString("api_endpoint"));
                Pipe.LOGGER.debug(String.format("Found a valid client (%s) with packet: %s", pipe.build.name(), p.toString()));
                if (pipe.build == preferredOrder[0] || DiscordBuild.ANY == preferredOrder[0]) {
                    Pipe.LOGGER.info(String.format("Found preferred client: %s", pipe.build.name()));
                    break;
                }
                open[pipe.build.ordinal()] = pipe;
                open[DiscordBuild.ANY.ordinal()] = pipe;
                pipe.build = null;
                pipe = null;
            }
            catch (IOException | JSONException ex4) {
                final Exception ex3;
                final Exception ex = ex3;
                pipe = null;
            }
        }
        if (pipe == null) {
            for (int i = 1; i < preferredOrder.length; ++i) {
                final DiscordBuild cb = preferredOrder[i];
                Pipe.LOGGER.debug(String.format("Looking for client build: %s", cb.name()));
                if (open[cb.ordinal()] != null) {
                    pipe = open[cb.ordinal()];
                    open[cb.ordinal()] = null;
                    if (cb == DiscordBuild.ANY) {
                        for (int k = 0; k < open.length; ++k) {
                            if (open[k] == pipe) {
                                pipe.build = DiscordBuild.values()[k];
                                open[k] = null;
                            }
                        }
                    }
                    else {
                        pipe.build = cb;
                    }
                    Pipe.LOGGER.info(String.format("Found preferred client: %s", pipe.build.name()));
                    break;
                }
            }
            if (pipe == null) {
                throw new NoDiscordClientException();
            }
        }
        for (int i = 0; i < open.length; ++i) {
            if (i != DiscordBuild.ANY.ordinal()) {
                if (open[i] != null) {
                    try {
                        open[i].close();
                    }
                    catch (IOException ex2) {
                        Pipe.LOGGER.debug("Failed to close an open IPC pipe!", ex2);
                    }
                }
            }
        }
        pipe.status = PipeStatus.CONNECTED;
        return pipe;
    }
    
    private static Pipe createPipe(final IPCClient ipcClient, final HashMap<String, Callback> callbacks, final String location) {
        final String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            return new WindowsPipe(ipcClient, callbacks, location);
        }
        if (!osName.contains("linux")) {
            if (!osName.contains("mac")) {
                throw new RuntimeException("Unsupported OS: " + osName);
            }
        }
        try {
            return new UnixPipe(ipcClient, callbacks, location);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        throw new RuntimeException("Unsupported OS: " + osName);
    }
    
    public void send(final Packet.OpCode op, final JSONObject data, final Callback callback) {
        try {
            final String nonce = generateNonce();
            final Packet p = new Packet(op, data.put("nonce", nonce));
            if (callback != null && !callback.isEmpty()) {
                this.callbacks.put(nonce, callback);
            }
            this.write(p.toBytes());
            Pipe.LOGGER.debug(String.format("Sent packet: %s", p.toString()));
            if (this.listener != null) {
                this.listener.onPacketSent(this.ipcClient, p);
            }
        }
        catch (IOException ex) {
            Pipe.LOGGER.error("Encountered an IOException while sending a packet and disconnected!");
            this.status = PipeStatus.DISCONNECTED;
        }
    }
    
    public abstract Packet read() throws IOException, JSONException;
    
    public abstract void write(final byte[] p0) throws IOException;
    
    private static String generateNonce() {
        return UUID.randomUUID().toString();
    }
    
    public PipeStatus getStatus() {
        return this.status;
    }
    
    public void setStatus(final PipeStatus status) {
        this.status = status;
    }
    
    public void setListener(final IPCListener listener) {
        this.listener = listener;
    }
    
    public abstract void close() throws IOException;
    
    public DiscordBuild getDiscordBuild() {
        return this.build;
    }
    
    private static String getPipeLocation(final int i) {
        if (System.getProperty("os.name").contains("Win")) {
            return "\\\\?\\pipe\\discord-ipc-" + i;
        }
        String tmppath = null;
        for (final String str : Pipe.unixPaths) {
            tmppath = System.getenv(str);
            if (tmppath != null) {
                break;
            }
        }
        if (tmppath == null) {
            tmppath = "/tmp";
        }
        return tmppath + "/discord-ipc-" + i;
    }
    
    static {
        LOGGER = LoggerFactory.getLogger(Pipe.class);
        unixPaths = new String[] { "XDG_RUNTIME_DIR", "TMPDIR", "TMP", "TEMP" };
    }
}
