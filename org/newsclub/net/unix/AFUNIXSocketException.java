// 
// Decompiled by Procyon v0.5.36
// 

package org.newsclub.net.unix;

import java.net.SocketException;

public class AFUNIXSocketException extends SocketException
{
    private static final long serialVersionUID = 1L;
    private final String socketFile;
    
    public AFUNIXSocketException(final String reason) {
        this(reason, (String)null);
    }
    
    public AFUNIXSocketException(final String reason, final Throwable cause) {
        this(reason, (String)null);
        this.initCause(cause);
    }
    
    public AFUNIXSocketException(final String reason, final String socketFile) {
        super(reason);
        this.socketFile = socketFile;
    }
    
    @Override
    public String toString() {
        if (this.socketFile == null) {
            return super.toString();
        }
        return super.toString() + " (socket: " + this.socketFile + ")";
    }
}
