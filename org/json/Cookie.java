// 
// Decompiled by Procyon v0.5.36
// 

package org.json;

public class Cookie
{
    public static String escape(final String string) {
        final String s = string.trim();
        final int length = s.length();
        final StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; ++i) {
            final char c = s.charAt(i);
            if (c < ' ' || c == '+' || c == '%' || c == '=' || c == ';') {
                sb.append('%');
                sb.append(Character.forDigit((char)(c >>> 4 & 0xF), 16));
                sb.append(Character.forDigit((char)(c & '\u000f'), 16));
            }
            else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
    
    public static JSONObject toJSONObject(final String string) throws JSONException {
        final JSONObject jo = new JSONObject();
        final JSONTokener x = new JSONTokener(string);
        jo.put("name", x.nextTo('='));
        x.next('=');
        jo.put("value", x.nextTo(';'));
        x.next();
        while (x.more()) {
            final String name = unescape(x.nextTo("=;"));
            Object value;
            if (x.next() != '=') {
                if (!name.equals("secure")) {
                    throw x.syntaxError("Missing '=' in cookie parameter.");
                }
                value = Boolean.TRUE;
            }
            else {
                value = unescape(x.nextTo(';'));
                x.next();
            }
            jo.put(name, value);
        }
        return jo;
    }
    
    public static String toString(final JSONObject jo) throws JSONException {
        final StringBuilder sb = new StringBuilder();
        sb.append(escape(jo.getString("name")));
        sb.append("=");
        sb.append(escape(jo.getString("value")));
        if (jo.has("expires")) {
            sb.append(";expires=");
            sb.append(jo.getString("expires"));
        }
        if (jo.has("domain")) {
            sb.append(";domain=");
            sb.append(escape(jo.getString("domain")));
        }
        if (jo.has("path")) {
            sb.append(";path=");
            sb.append(escape(jo.getString("path")));
        }
        if (jo.optBoolean("secure")) {
            sb.append(";secure");
        }
        return sb.toString();
    }
    
    public static String unescape(final String string) {
        final int length = string.length();
        final StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; ++i) {
            char c = string.charAt(i);
            if (c == '+') {
                c = ' ';
            }
            else if (c == '%' && i + 2 < length) {
                final int d = JSONTokener.dehexchar(string.charAt(i + 1));
                final int e = JSONTokener.dehexchar(string.charAt(i + 2));
                if (d >= 0 && e >= 0) {
                    c = (char)(d * 16 + e);
                    i += 2;
                }
            }
            sb.append(c);
        }
        return sb.toString();
    }
}
