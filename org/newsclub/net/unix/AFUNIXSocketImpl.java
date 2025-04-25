// 
// Decompiled by Procyon v0.5.36
// 

package org.newsclub.net.unix;

import java.io.OutputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketAddress;
import java.io.IOException;
import java.io.FileDescriptor;
import java.net.SocketImpl;

class AFUNIXSocketImpl extends SocketImpl
{
    private static final int SHUT_RD = 0;
    private static final int SHUT_WR = 1;
    private static final int SHUT_RD_WR = 2;
    private String socketFile;
    private boolean closed;
    private boolean bound;
    private boolean connected;
    private boolean closedInputStream;
    private boolean closedOutputStream;
    private final AFUNIXInputStream in;
    private final AFUNIXOutputStream out;
    
    AFUNIXSocketImpl() {
        this.closed = false;
        this.bound = false;
        this.connected = false;
        this.closedInputStream = false;
        this.closedOutputStream = false;
        this.in = new AFUNIXInputStream();
        this.out = new AFUNIXOutputStream();
        this.fd = new FileDescriptor();
    }
    
    FileDescriptor getFD() {
        return this.fd;
    }
    
    @Override
    protected void accept(final SocketImpl socket) throws IOException {
        final AFUNIXSocketImpl si = (AFUNIXSocketImpl)socket;
        NativeUnixSocket.accept(this.socketFile, this.fd, si.fd);
        si.socketFile = this.socketFile;
        si.connected = true;
    }
    
    @Override
    protected int available() throws IOException {
        return NativeUnixSocket.available(this.fd);
    }
    
    protected void bind(final SocketAddress addr) throws IOException {
        this.bind(0, addr);
    }
    
    protected void bind(final int backlog, final SocketAddress addr) throws IOException {
        if (!(addr instanceof AFUNIXSocketAddress)) {
            throw new SocketException("Cannot bind to this type of address: " + addr.getClass());
        }
        final AFUNIXSocketAddress socketAddress = (AFUNIXSocketAddress)addr;
        NativeUnixSocket.bind(this.socketFile = socketAddress.getSocketFile(), this.fd, backlog);
        this.bound = true;
        this.localport = socketAddress.getPort();
    }
    
    @Override
    protected void bind(final InetAddress host, final int port) throws IOException {
        throw new SocketException("Cannot bind to this type of address: " + InetAddress.class);
    }
    
    private void checkClose() throws IOException {
        if (!this.closedInputStream || this.closedOutputStream) {}
    }
    
    @Override
    protected synchronized void close() throws IOException {
        if (this.closed) {
            return;
        }
        this.closed = true;
        if (this.fd.valid()) {
            NativeUnixSocket.shutdown(this.fd, 2);
            NativeUnixSocket.close(this.fd);
        }
        if (this.bound) {
            NativeUnixSocket.unlink(this.socketFile);
        }
        this.connected = false;
    }
    
    @Override
    protected void connect(final String host, final int port) throws IOException {
        throw new SocketException("Cannot bind to this type of address: " + InetAddress.class);
    }
    
    @Override
    protected void connect(final InetAddress address, final int port) throws IOException {
        throw new SocketException("Cannot bind to this type of address: " + InetAddress.class);
    }
    
    @Override
    protected void connect(final SocketAddress addr, final int timeout) throws IOException {
        if (!(addr instanceof AFUNIXSocketAddress)) {
            throw new SocketException("Cannot bind to this type of address: " + addr.getClass());
        }
        final AFUNIXSocketAddress socketAddress = (AFUNIXSocketAddress)addr;
        NativeUnixSocket.connect(this.socketFile = socketAddress.getSocketFile(), this.fd);
        this.address = socketAddress.getAddress();
        this.port = socketAddress.getPort();
        this.localport = 0;
        this.connected = true;
    }
    
    @Override
    protected void create(final boolean stream) throws IOException {
    }
    
    @Override
    protected InputStream getInputStream() throws IOException {
        if (!this.connected && !this.bound) {
            throw new IOException("Not connected/not bound");
        }
        return this.in;
    }
    
    @Override
    protected OutputStream getOutputStream() throws IOException {
        if (!this.connected && !this.bound) {
            throw new IOException("Not connected/not bound");
        }
        return this.out;
    }
    
    @Override
    protected void listen(final int backlog) throws IOException {
        NativeUnixSocket.listen(this.fd, backlog);
    }
    
    @Override
    protected void sendUrgentData(final int data) throws IOException {
        NativeUnixSocket.write(this.fd, new byte[] { (byte)(data & 0xFF) }, 0, 1);
    }
    
    @Override
    public String toString() {
        return super.toString() + "[fd=" + this.fd + "; file=" + this.socketFile + "; connected=" + this.connected + "; bound=" + this.bound + "]";
    }
    
    private static int expectInteger(final Object value) throws SocketException {
        try {
            return (int)value;
        }
        catch (ClassCastException e) {
            throw new AFUNIXSocketException("Unsupported value: " + value, e);
        }
        catch (NullPointerException e2) {
            throw new AFUNIXSocketException("Value must not be null", e2);
        }
    }
    
    private static int expectBoolean(final Object value) throws SocketException {
        try {
            return ((boolean)value) ? 1 : 0;
        }
        catch (ClassCastException e) {
            throw new AFUNIXSocketException("Unsupported value: " + value, e);
        }
        catch (NullPointerException e2) {
            throw new AFUNIXSocketException("Value must not be null", e2);
        }
    }
    
    @Override
    public Object getOption(final int optID) throws SocketException {
        try {
            switch (optID) {
                case 1:
                case 8: {
                    return NativeUnixSocket.getSocketOptionInt(this.fd, optID) != 0;
                }
                case 128:
                case 4097:
                case 4098:
                case 4102: {
                    return NativeUnixSocket.getSocketOptionInt(this.fd, optID);
                }
                default: {
                    throw new AFUNIXSocketException("Unsupported option: " + optID);
                }
            }
        }
        catch (AFUNIXSocketException e) {
            throw e;
        }
        catch (Exception e2) {
            throw new AFUNIXSocketException("Error while getting option", e2);
        }
    }
    
    @Override
    public void setOption(final int optID, final Object value) throws SocketException {
        try {
            switch (optID) {
                case 128: {
                    if (!(value instanceof Boolean)) {
                        NativeUnixSocket.setSocketOptionInt(this.fd, optID, expectInteger(value));
                        return;
                    }
                    final boolean b = (boolean)value;
                    if (b) {
                        throw new SocketException("Only accepting Boolean.FALSE here");
                    }
                    NativeUnixSocket.setSocketOptionInt(this.fd, optID, -1);
                }
                case 4097:
                case 4098:
                case 4102: {
                    NativeUnixSocket.setSocketOptionInt(this.fd, optID, expectInteger(value));
                }
                case 1:
                case 8: {
                    NativeUnixSocket.setSocketOptionInt(this.fd, optID, expectBoolean(value));
                }
                default: {
                    throw new AFUNIXSocketException("Unsupported option: " + optID);
                }
            }
        }
        catch (AFUNIXSocketException e) {
            throw e;
        }
        catch (Exception e2) {
            throw new AFUNIXSocketException("Error while setting option", e2);
        }
    }
    
    @Override
    protected void shutdownInput() throws IOException {
        if (!this.closed && this.fd.valid()) {
            NativeUnixSocket.shutdown(this.fd, 0);
        }
    }
    
    @Override
    protected void shutdownOutput() throws IOException {
        if (!this.closed && this.fd.valid()) {
            NativeUnixSocket.shutdown(this.fd, 1);
        }
    }
    
    private final class AFUNIXInputStream extends InputStream
    {
        private boolean streamClosed;
        
        private AFUNIXInputStream() {
            this.streamClosed = false;
        }
        
        @Override
        public int read(final byte[] buf, final int off, int len) throws IOException {
            if (this.streamClosed) {
                throw new IOException("This InputStream has already been closed.");
            }
            if (len == 0) {
                return 0;
            }
            final int maxRead = buf.length - off;
            if (len > maxRead) {
                len = maxRead;
            }
            try {
                return NativeUnixSocket.read(AFUNIXSocketImpl.this.fd, buf, off, len);
            }
            catch (IOException e) {
                throw (IOException)new IOException(e.getMessage() + " at " + AFUNIXSocketImpl.this.toString()).initCause(e);
            }
        }
        
        @Override
        public int read() throws IOException {
            final byte[] buf1 = { 0 };
            final int numRead = this.read(buf1, 0, 1);
            if (numRead <= 0) {
                return -1;
            }
            return buf1[0] & 0xFF;
        }
        
        @Override
        public void close() throws IOException {
            if (this.streamClosed) {
                return;
            }
            this.streamClosed = true;
            if (AFUNIXSocketImpl.this.fd.valid()) {
                NativeUnixSocket.shutdown(AFUNIXSocketImpl.this.fd, 0);
            }
            AFUNIXSocketImpl.this.closedInputStream = true;
            AFUNIXSocketImpl.this.checkClose();
        }
        
        @Override
        public int available() throws IOException {
            final int av = NativeUnixSocket.available(AFUNIXSocketImpl.this.fd);
            return av;
        }
    }
    
    private final class AFUNIXOutputStream extends OutputStream
    {
        private boolean streamClosed;
        
        private AFUNIXOutputStream() {
            this.streamClosed = false;
        }
        
        @Override
        public void write(final int oneByte) throws IOException {
            final byte[] buf1 = { (byte)oneByte };
            this.write(buf1, 0, 1);
        }
        
        @Override
        public void write(final byte[] buf, int off, int len) throws IOException {
            if (this.streamClosed) {
                throw new AFUNIXSocketException("This OutputStream has already been closed.");
            }
            if (len > buf.length - off) {
                throw new IndexOutOfBoundsException();
            }
            try {
                while (len > 0 && !Thread.interrupted()) {
                    final int written = NativeUnixSocket.write(AFUNIXSocketImpl.this.fd, buf, off, len);
                    if (written == -1) {
                        throw new IOException("Unspecific error while writing");
                    }
                    len -= written;
                    off += written;
                }
            }
            catch (IOException e) {
                throw (IOException)new IOException(e.getMessage() + " at " + AFUNIXSocketImpl.this.toString()).initCause(e);
            }
        }
        
        @Override
        public void close() throws IOException {
            if (this.streamClosed) {
                return;
            }
            this.streamClosed = true;
            if (AFUNIXSocketImpl.this.fd.valid()) {
                NativeUnixSocket.shutdown(AFUNIXSocketImpl.this.fd, 1);
            }
            AFUNIXSocketImpl.this.closedOutputStream = true;
            AFUNIXSocketImpl.this.checkClose();
        }
    }
    
    static class Lenient extends AFUNIXSocketImpl
    {
        @Override
        public void setOption(final int optID, final Object value) throws SocketException {
            try {
                super.setOption(optID, value);
            }
            catch (SocketException e) {
                switch (optID) {
                    case 1: {}
                    default: {
                        throw e;
                    }
                }
            }
        }
        
        @Override
        public Object getOption(final int optID) throws SocketException {
            try {
                return super.getOption(optID);
            }
            catch (SocketException e) {
                switch (optID) {
                    case 1:
                    case 8: {
                        return false;
                    }
                    default: {
                        throw e;
                    }
                }
            }
        }
    }
}
