// 
// Decompiled by Procyon v0.5.36
// 

package org.json;

import java.io.IOException;

public class JSONWriter
{
    private static final int maxdepth = 200;
    private boolean comma;
    protected char mode;
    private final JSONObject[] stack;
    private int top;
    protected Appendable writer;
    
    public JSONWriter(final Appendable w) {
        this.comma = false;
        this.mode = 'i';
        this.stack = new JSONObject[200];
        this.top = 0;
        this.writer = w;
    }
    
    private JSONWriter append(final String string) throws JSONException {
        if (string == null) {
            throw new JSONException("Null pointer");
        }
        if (this.mode != 'o') {
            if (this.mode != 'a') {
                throw new JSONException("Value out of sequence.");
            }
        }
        try {
            if (this.comma && this.mode == 'a') {
                this.writer.append(',');
            }
            this.writer.append(string);
        }
        catch (IOException e) {
            throw new JSONException(e);
        }
        if (this.mode == 'o') {
            this.mode = 'k';
        }
        this.comma = true;
        return this;
    }
    
    public JSONWriter array() throws JSONException {
        if (this.mode == 'i' || this.mode == 'o' || this.mode == 'a') {
            this.push(null);
            this.append("[");
            this.comma = false;
            return this;
        }
        throw new JSONException("Misplaced array.");
    }
    
    private JSONWriter end(final char m, final char c) throws JSONException {
        if (this.mode != m) {
            throw new JSONException((m == 'a') ? "Misplaced endArray." : "Misplaced endObject.");
        }
        this.pop(m);
        try {
            this.writer.append(c);
        }
        catch (IOException e) {
            throw new JSONException(e);
        }
        this.comma = true;
        return this;
    }
    
    public JSONWriter endArray() throws JSONException {
        return this.end('a', ']');
    }
    
    public JSONWriter endObject() throws JSONException {
        return this.end('k', '}');
    }
    
    public JSONWriter key(final String string) throws JSONException {
        if (string == null) {
            throw new JSONException("Null key.");
        }
        if (this.mode == 'k') {
            try {
                this.stack[this.top - 1].putOnce(string, Boolean.TRUE);
                if (this.comma) {
                    this.writer.append(',');
                }
                this.writer.append(JSONObject.quote(string));
                this.writer.append(':');
                this.comma = false;
                this.mode = 'o';
                return this;
            }
            catch (IOException e) {
                throw new JSONException(e);
            }
        }
        throw new JSONException("Misplaced key.");
    }
    
    public JSONWriter object() throws JSONException {
        if (this.mode == 'i') {
            this.mode = 'o';
        }
        if (this.mode == 'o' || this.mode == 'a') {
            this.append("{");
            this.push(new JSONObject());
            this.comma = false;
            return this;
        }
        throw new JSONException("Misplaced object.");
    }
    
    private void pop(final char c) throws JSONException {
        if (this.top <= 0) {
            throw new JSONException("Nesting error.");
        }
        final char m = (this.stack[this.top - 1] == null) ? 'a' : 'k';
        if (m != c) {
            throw new JSONException("Nesting error.");
        }
        --this.top;
        this.mode = ((this.top == 0) ? 'd' : ((this.stack[this.top - 1] == null) ? 'a' : 'k'));
    }
    
    private void push(final JSONObject jo) throws JSONException {
        if (this.top >= 200) {
            throw new JSONException("Nesting too deep.");
        }
        this.stack[this.top] = jo;
        this.mode = ((jo == null) ? 'a' : 'k');
        ++this.top;
    }
    
    public JSONWriter value(final boolean b) throws JSONException {
        return this.append(b ? "true" : "false");
    }
    
    public JSONWriter value(final double d) throws JSONException {
        return this.value(new Double(d));
    }
    
    public JSONWriter value(final long l) throws JSONException {
        return this.append(Long.toString(l));
    }
    
    public JSONWriter value(final Object object) throws JSONException {
        return this.append(JSONObject.valueToString(object));
    }
}
