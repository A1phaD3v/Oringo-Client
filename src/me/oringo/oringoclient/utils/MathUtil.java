// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.utils;

import java.util.Random;

public class MathUtil
{
    private static final Random rand;
    
    private boolean isEven(final double num) {
        return !this.isOdd(num);
    }
    
    private boolean isOdd(final double num) {
        return !this.isEven(num);
    }
    
    public static double getRandomInRange(final double max, final double min) {
        return min + (max - min) * MathUtil.rand.nextFloat();
    }
    
    public static double clamp(final double num, double max, double min) {
        if (max < min) {
            final double temp = max;
            max = min;
            min = temp;
        }
        return Math.max(Math.min(max, num), min);
    }
    
    public static int clamp(final int num, int max, int min) {
        if (max < min) {
            final int temp = max;
            max = min;
            min = temp;
        }
        return Math.max(Math.min(max, num), min);
    }
    
    public static float clamp(final float num, float max, float min) {
        if (max < min) {
            final float temp = max;
            max = min;
            min = temp;
        }
        return Math.max(Math.min(max, num), min);
    }
    
    public static double hypot(final double a, final double b) {
        return Math.sqrt(a * a + b * b);
    }
    
    static {
        rand = new Random();
    }
}
