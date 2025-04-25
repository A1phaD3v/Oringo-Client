// 
// Decompiled by Procyon v0.5.36
// 

package org.newsclub.net.unix;

import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.io.IOException;
import java.io.FileDescriptor;

final class NativeUnixSocket
{
    private static boolean loaded;
    
    static boolean isLoaded() {
        return NativeUnixSocket.loaded;
    }
    
    static void checkSupported() {
    }
    
    static native void bind(final String p0, final FileDescriptor p1, final int p2) throws IOException;
    
    static native void listen(final FileDescriptor p0, final int p1) throws IOException;
    
    static native void accept(final String p0, final FileDescriptor p1, final FileDescriptor p2) throws IOException;
    
    static native void connect(final String p0, final FileDescriptor p1) throws IOException;
    
    static native int read(final FileDescriptor p0, final byte[] p1, final int p2, final int p3) throws IOException;
    
    static native int write(final FileDescriptor p0, final byte[] p1, final int p2, final int p3) throws IOException;
    
    static native void close(final FileDescriptor p0) throws IOException;
    
    static native void shutdown(final FileDescriptor p0, final int p1) throws IOException;
    
    static native int getSocketOptionInt(final FileDescriptor p0, final int p1) throws IOException;
    
    static native void setSocketOptionInt(final FileDescriptor p0, final int p1, final int p2) throws IOException;
    
    static native void unlink(final String p0) throws IOException;
    
    static native int available(final FileDescriptor p0) throws IOException;
    
    static native void initServerImpl(final AFUNIXServerSocket p0, final AFUNIXSocketImpl p1);
    
    static native void setCreated(final AFUNIXSocket p0);
    
    static native void setConnected(final AFUNIXSocket p0);
    
    static native void setBound(final AFUNIXSocket p0);
    
    static native void setCreatedServer(final AFUNIXServerSocket p0);
    
    static native void setBoundServer(final AFUNIXServerSocket p0);
    
    static native void setPort(final AFUNIXSocketAddress p0, final int p1);
    
    static void setPort1(final AFUNIXSocketAddress addr, final int port) throws AFUNIXSocketException {
        if (port < 0) {
            throw new IllegalArgumentException("port out of range:" + port);
        }
        boolean setOk = false;
        try {
            final Field holderField = InetSocketAddress.class.getDeclaredField("holder");
            if (holderField != null) {
                holderField.setAccessible(true);
                final Object holder = holderField.get(addr);
                if (holder != null) {
                    final Field portField = holder.getClass().getDeclaredField("port");
                    if (portField != null) {
                        portField.setAccessible(true);
                        portField.set(holder, port);
                        setOk = true;
                    }
                }
            }
            else {
                setPort(addr, port);
            }
        }
        catch (RuntimeException e) {
            throw e;
        }
        catch (Exception e2) {
            if (e2 instanceof AFUNIXSocketException) {
                throw (AFUNIXSocketException)e2;
            }
            throw new AFUNIXSocketException("Could not set port", e2);
        }
        if (!setOk) {
            throw new AFUNIXSocketException("Could not set port");
        }
    }
    
    static {
        NativeUnixSocket.loaded = false;
        try {
            Class.forName("org.newsclub.net.unix.NarSystem").getMethod("loadLibrary", (Class<?>[])new Class[0]).invoke(null, new Object[0]);
        }
        catch (ClassNotFoundException e) {
            throw new IllegalStateException("Could not find NarSystem class.\n\n*** ECLIPSE USERS ***\nIf you're running from within Eclipse, please try closing the \"junixsocket-native-common\" project\n", e);
        }
        catch (Exception e2) {
            throw new IllegalStateException(e2);
        }
        NativeUnixSocket.loaded = true;
    }
}
