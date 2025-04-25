// 
// Decompiled by Procyon v0.5.36
// 

package com.jagrosh.discordipc.entities;

public enum DiscordBuild
{
    CANARY("//canary.discordapp.com/api"), 
    PTB("//ptb.discordapp.com/api"), 
    STABLE("//discordapp.com/api"), 
    ANY;
    
    private final String endpoint;
    
    private DiscordBuild(final String endpoint) {
        this.endpoint = endpoint;
    }
    
    private DiscordBuild() {
        this(null);
    }
    
    public static DiscordBuild from(final String endpoint) {
        for (final DiscordBuild value : values()) {
            if (value.endpoint != null && value.endpoint.equals(endpoint)) {
                return value;
            }
        }
        return DiscordBuild.ANY;
    }
}
