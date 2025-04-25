// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.ui.animation.impl;

import me.oringo.oringoclient.ui.animation.Animation;

public class PopOutAnimation extends Animation
{
    public PopOutAnimation(final long time, final double startingSize, final float smoothing) {
        super(time);
    }
    
    @Override
    public void doAnimation(final double x, final double y) {
        if (!this.isFinished()) {}
    }
}
