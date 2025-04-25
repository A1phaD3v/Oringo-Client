// 
// Decompiled by Procyon v0.5.36
// 

package org.json;

import java.util.Iterator;
import java.util.Map;
import java.util.Enumeration;
import java.util.Properties;

public class Property
{
    public static JSONObject toJSONObject(final Properties properties) throws JSONException {
        final JSONObject jo = new JSONObject((properties == null) ? 0 : properties.size());
        if (properties != null && !properties.isEmpty()) {
            final Enumeration<?> enumProperties = properties.propertyNames();
            while (enumProperties.hasMoreElements()) {
                final String name = (String)enumProperties.nextElement();
                jo.put(name, properties.getProperty(name));
            }
        }
        return jo;
    }
    
    public static Properties toProperties(final JSONObject jo) throws JSONException {
        final Properties properties = new Properties();
        if (jo != null) {
            for (final Map.Entry<String, ?> entry : jo.entrySet()) {
                final Object value = entry.getValue();
                if (!JSONObject.NULL.equals(value)) {
                    properties.put(entry.getKey(), value.toString());
                }
            }
        }
        return properties;
    }
}
