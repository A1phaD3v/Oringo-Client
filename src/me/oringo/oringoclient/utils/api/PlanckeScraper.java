// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.utils.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PlanckeScraper
{
    public static HttpURLConnection getConnection(final URL url) {
        try {
            final HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            return connection;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String getUserData(final String name) {
        try {
            final HttpURLConnection connection = getConnection(new URL(String.format("https://plancke.io/hypixel/player/stats/%s", name)));
            if (connection != null) {
                final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                final StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line).append("\n");
                }
                return builder.toString();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    
    public static boolean getActivity(final String name) {
        final Matcher matcher = Pattern.compile("<div class=\"card-box m-b-10\">\n<h4 class=\"m-t-0 header-title\">Status</h4>\n<b>(.*?)</b>\n</div>").matcher(getUserData(name));
        return !matcher.find();
    }
    
    public static int getBans() {
        try {
            final HttpURLConnection connection = (HttpURLConnection)new URL("https://api.plancke.io/hypixel/v1/punishmentStats").openConnection();
            connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
            connection.setRequestMethod("GET");
            final JsonObject object = new JsonParser().parse((Reader)new InputStreamReader(connection.getInputStream())).getAsJsonObject();
            return object.get("record").getAsJsonObject().get("staff_total").getAsInt();
        }
        catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
