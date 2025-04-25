// 
// Decompiled by Procyon v0.5.36
// 

package org.newsclub.net.unix;

import java.net.SocketAddress;
import java.io.IOException;
import java.net.SocketImpl;
import java.net.Socket;

public class AFUNIXSocket extends Socket
{
    protected AFUNIXSocketImpl impl;
    AFUNIXSocketAddress addr;
    
    private AFUNIXSocket(final AFUNIXSocketImpl impl) throws IOException {
        super(impl);
        try {
            NativeUnixSocket.setCreated(this);
        }
        catch (UnsatisfiedLinkError e) {
            e.printStackTrace();
        }
    }
    
    public static AFUNIXSocket newInstance() throws IOException {
        final AFUNIXSocketImpl impl = new AFUNIXSocketImpl.Lenient();
        final AFUNIXSocket instance = new AFUNIXSocket(impl);
        instance.impl = impl;
        return instance;
    }
    
    public static AFUNIXSocket newStrictInstance() throws IOException {
        final AFUNIXSocketImpl impl = new AFUNIXSocketImpl();
        final AFUNIXSocket instance = new AFUNIXSocket(impl);
        instance.impl = impl;
        return instance;
    }
    
    public static AFUNIXSocket connectTo(final AFUNIXSocketAddress addr) throws IOException {
        final AFUNIXSocket socket = newInstance();
        socket.connect(addr);
        return socket;
    }
    
    @Override
    public void bind(final SocketAddress bindpoint) throws IOException {
        super.bind(bindpoint);
        this.addr = (AFUNIXSocketAddress)bindpoint;
    }
    
    @Override
    public void connect(final SocketAddress endpoint) throws IOException {
        this.connect(endpoint, 0);
    }
    
    @Override
    public void connect(final SocketAddress endpoint, final int timeout) throws IOException {
        if (!(endpoint instanceof AFUNIXSocketAddress)) {
            throw new IOException("Can only connect to endpoints of type " + AFUNIXSocketAddress.class.getName());
        }
        this.impl.connect(endpoint, timeout);
        this.addr = (AFUNIXSocketAddress)endpoint;
        NativeUnixSocket.setConnected(this);
    }
    
    @Override
    public String toString() {
        if (this.isConnected()) {
            return "AFUNIXSocket[fd=" + this.impl.getFD() + ";path=" + this.addr.getSocketFile() + "]";
        }
        return "AFUNIXSocket[unconnected]";
    }
    
    public static boolean isSupported() {
        return NativeUnixSocket.isLoaded();
    }
}
