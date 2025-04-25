// 
// Decompiled by Procyon v0.5.36
// 

package org.newsclub.net.unix;

import java.net.SocketImpl;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketAddress;
import java.io.IOException;
import java.net.ServerSocket;

public class AFUNIXServerSocket extends ServerSocket
{
    private final AFUNIXSocketImpl implementation;
    private AFUNIXSocketAddress boundEndpoint;
    private final Thread shutdownThread;
    
    protected AFUNIXServerSocket() throws IOException {
        this.boundEndpoint = null;
        this.shutdownThread = new Thread() {
            @Override
            public void run() {
                try {
                    if (AFUNIXServerSocket.this.boundEndpoint != null) {
                        NativeUnixSocket.unlink(AFUNIXServerSocket.this.boundEndpoint.getSocketFile());
                    }
                }
                catch (IOException ex) {}
            }
        };
        NativeUnixSocket.initServerImpl(this, this.implementation = new AFUNIXSocketImpl());
        Runtime.getRuntime().addShutdownHook(this.shutdownThread);
        NativeUnixSocket.setCreatedServer(this);
    }
    
    public static AFUNIXServerSocket newInstance() throws IOException {
        final AFUNIXServerSocket instance = new AFUNIXServerSocket();
        return instance;
    }
    
    public static AFUNIXServerSocket bindOn(final AFUNIXSocketAddress addr) throws IOException {
        final AFUNIXServerSocket socket = newInstance();
        socket.bind(addr);
        return socket;
    }
    
    @Override
    public void bind(final SocketAddress endpoint, final int backlog) throws IOException {
        if (this.isClosed()) {
            throw new SocketException("Socket is closed");
        }
        if (this.isBound()) {
            throw new SocketException("Already bound");
        }
        if (!(endpoint instanceof AFUNIXSocketAddress)) {
            throw new IOException("Can only bind to endpoints of type " + AFUNIXSocketAddress.class.getName());
        }
        this.implementation.bind(backlog, endpoint);
        this.boundEndpoint = (AFUNIXSocketAddress)endpoint;
    }
    
    @Override
    public boolean isBound() {
        return this.boundEndpoint != null;
    }
    
    @Override
    public Socket accept() throws IOException {
        if (this.isClosed()) {
            throw new SocketException("Socket is closed");
        }
        final AFUNIXSocket as = AFUNIXSocket.newInstance();
        this.implementation.accept(as.impl);
        as.addr = this.boundEndpoint;
        NativeUnixSocket.setConnected(as);
        return as;
    }
    
    @Override
    public String toString() {
        if (!this.isBound()) {
            return "AFUNIXServerSocket[unbound]";
        }
        return "AFUNIXServerSocket[" + this.boundEndpoint.getSocketFile() + "]";
    }
    
    @Override
    public void close() throws IOException {
        if (this.isClosed()) {
            return;
        }
        super.close();
        this.implementation.close();
        if (this.boundEndpoint != null) {
            NativeUnixSocket.unlink(this.boundEndpoint.getSocketFile());
        }
        try {
            Runtime.getRuntime().removeShutdownHook(this.shutdownThread);
        }
        catch (IllegalStateException ex) {}
    }
    
    public static boolean isSupported() {
        return NativeUnixSocket.isLoaded();
    }
}
