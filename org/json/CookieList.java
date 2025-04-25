// 
// Decompiled by Procyon v0.5.36
// 

package org.json;

import java.util.Iterator;
import java.util.Map;

public class CookieList
{
    public static JSONObject toJSONObject(final String string) throws JSONException {
        final JSONObject jo = new JSONObject();
        final JSONTokener x = new JSONTokener(string);
        while (x.more()) {
            final String name = Cookie.unescape(x.nextTo('='));
            x.next('=');
            jo.put(name, Cookie.unescape(x.nextTo(';')));
            x.next();
        }
        return jo;
    }
    
    public static String toString(final JSONObject jo) throws JSONException {
        boolean b = false;
        final StringBuilder sb = new StringBuilder();
        for (final Map.Entry<String, ?> entry : jo.entrySet()) {
            final String key = entry.getKey();
            final Object value = entry.getValue();
            if (!JSONObject.NULL.equals(value)) {
                if (b) {
                    sb.append(';');
                }
                sb.append(Cookie.escape(key));
                sb.append("=");
                sb.append(Cookie.escape(value.toString()));
                b = true;
            }
        }
        return sb.toString();
    }
}
