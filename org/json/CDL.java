// 
// Decompiled by Procyon v0.5.36
// 

package org.json;

public class CDL
{
    private static String getValue(final JSONTokener x) throws JSONException {
        char c;
        do {
            c = x.next();
        } while (c == ' ' || c == '\t');
        switch (c) {
            case '\0': {
                return null;
            }
            case '\"':
            case '\'': {
                final char q = c;
                final StringBuffer sb = new StringBuffer();
                while (true) {
                    c = x.next();
                    if (c == q) {
                        final char nextC = x.next();
                        if (nextC != '\"') {
                            if (nextC > '\0') {
                                x.back();
                            }
                            return sb.toString();
                        }
                    }
                    if (c == '\0' || c == '\n' || c == '\r') {
                        throw x.syntaxError("Missing close quote '" + q + "'.");
                    }
                    sb.append(c);
                }
                break;
            }
            case ',': {
                x.back();
                return "";
            }
            default: {
                x.back();
                return x.nextTo(',');
            }
        }
    }
    
    public static JSONArray rowToJSONArray(final JSONTokener x) throws JSONException {
        final JSONArray ja = new JSONArray();
        while (true) {
            final String value = getValue(x);
            char c = x.next();
            if (value == null || (ja.length() == 0 && value.length() == 0 && c != ',')) {
                return null;
            }
            ja.put(value);
            while (c != ',') {
                if (c != ' ') {
                    if (c == '\n' || c == '\r' || c == '\0') {
                        return ja;
                    }
                    throw x.syntaxError("Bad character '" + c + "' (" + (int)c + ").");
                }
                else {
                    c = x.next();
                }
            }
        }
    }
    
    public static JSONObject rowToJSONObject(final JSONArray names, final JSONTokener x) throws JSONException {
        final JSONArray ja = rowToJSONArray(x);
        return (ja != null) ? ja.toJSONObject(names) : null;
    }
    
    public static String rowToString(final JSONArray ja) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ja.length(); ++i) {
            if (i > 0) {
                sb.append(',');
            }
            final Object object = ja.opt(i);
            if (object != null) {
                final String string = object.toString();
                if (string.length() > 0 && (string.indexOf(44) >= 0 || string.indexOf(10) >= 0 || string.indexOf(13) >= 0 || string.indexOf(0) >= 0 || string.charAt(0) == '\"')) {
                    sb.append('\"');
                    for (int length = string.length(), j = 0; j < length; ++j) {
                        final char c = string.charAt(j);
                        if (c >= ' ' && c != '\"') {
                            sb.append(c);
                        }
                    }
                    sb.append('\"');
                }
                else {
                    sb.append(string);
                }
            }
        }
        sb.append('\n');
        return sb.toString();
    }
    
    public static JSONArray toJSONArray(final String string) throws JSONException {
        return toJSONArray(new JSONTokener(string));
    }
    
    public static JSONArray toJSONArray(final JSONTokener x) throws JSONException {
        return toJSONArray(rowToJSONArray(x), x);
    }
    
    public static JSONArray toJSONArray(final JSONArray names, final String string) throws JSONException {
        return toJSONArray(names, new JSONTokener(string));
    }
    
    public static JSONArray toJSONArray(final JSONArray names, final JSONTokener x) throws JSONException {
        if (names == null || names.length() == 0) {
            return null;
        }
        final JSONArray ja = new JSONArray();
        while (true) {
            final JSONObject jo = rowToJSONObject(names, x);
            if (jo == null) {
                break;
            }
            ja.put(jo);
        }
        if (ja.length() == 0) {
            return null;
        }
        return ja;
    }
    
    public static String toString(final JSONArray ja) throws JSONException {
        final JSONObject jo = ja.optJSONObject(0);
        if (jo != null) {
            final JSONArray names = jo.names();
            if (names != null) {
                return rowToString(names) + toString(names, ja);
            }
        }
        return null;
    }
    
    public static String toString(final JSONArray names, final JSONArray ja) throws JSONException {
        if (names == null || names.length() == 0) {
            return null;
        }
        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i < ja.length(); ++i) {
            final JSONObject jo = ja.optJSONObject(i);
            if (jo != null) {
                sb.append(rowToString(jo.toJSONArray(names)));
            }
        }
        return sb.toString();
    }
}
