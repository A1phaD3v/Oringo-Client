// 
// Decompiled by Procyon v0.5.36
// 

package org.json;

public class JSONException extends RuntimeException
{
    private static final long serialVersionUID = 0L;
    
    public JSONException(final String message) {
        super(message);
    }
    
    public JSONException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public JSONException(final Throwable cause) {
        super(cause.getMessage(), cause);
    }
}
