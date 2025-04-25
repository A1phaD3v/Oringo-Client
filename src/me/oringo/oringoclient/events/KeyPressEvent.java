// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.events;

import net.minecraftforge.fml.common.eventhandler.Event;

public class KeyPressEvent extends Event
{
    public int key;
    public char aChar;
    
    public KeyPressEvent(final int key, final char aChar) {
        this.key = key;
        this.aChar = aChar;
    }
}
