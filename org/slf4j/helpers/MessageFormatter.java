// 
// Decompiled by Procyon v0.5.36
// 

package org.slf4j.helpers;

import java.util.Map;
import java.util.HashMap;

public final class MessageFormatter
{
    static final char DELIM_START = '{';
    static final char DELIM_STOP = '}';
    static final String DELIM_STR = "{}";
    private static final char ESCAPE_CHAR = '\\';
    
    public static final FormattingTuple format(final String messagePattern, final Object arg) {
        return arrayFormat(messagePattern, new Object[] { arg });
    }
    
    public static final FormattingTuple format(final String messagePattern, final Object arg1, final Object arg2) {
        return arrayFormat(messagePattern, new Object[] { arg1, arg2 });
    }
    
    static final Throwable getThrowableCandidate(final Object[] argArray) {
        if (argArray == null || argArray.length == 0) {
            return null;
        }
        final Object lastEntry = argArray[argArray.length - 1];
        if (lastEntry instanceof Throwable) {
            return (Throwable)lastEntry;
        }
        return null;
    }
    
    public static final FormattingTuple arrayFormat(final String messagePattern, final Object[] argArray) {
        final Throwable throwableCandidate = getThrowableCandidate(argArray);
        Object[] args = argArray;
        if (throwableCandidate != null) {
            args = trimmedCopy(argArray);
        }
        return arrayFormat(messagePattern, args, throwableCandidate);
    }
    
    private static Object[] trimmedCopy(final Object[] argArray) {
        if (argArray == null || argArray.length == 0) {
            throw new IllegalStateException("non-sensical empty or null argument array");
        }
        final int trimemdLen = argArray.length - 1;
        final Object[] trimmed = new Object[trimemdLen];
        System.arraycopy(argArray, 0, trimmed, 0, trimemdLen);
        return trimmed;
    }
    
    public static final FormattingTuple arrayFormat(final String messagePattern, final Object[] argArray, final Throwable throwable) {
        if (messagePattern == null) {
            return new FormattingTuple(null, argArray, throwable);
        }
        if (argArray == null) {
            return new FormattingTuple(messagePattern);
        }
        int i = 0;
        final StringBuilder sbuf = new StringBuilder(messagePattern.length() + 50);
        int L = 0;
        while (L < argArray.length) {
            final int j = messagePattern.indexOf("{}", i);
            if (j == -1) {
                if (i == 0) {
                    return new FormattingTuple(messagePattern, argArray, throwable);
                }
                sbuf.append(messagePattern, i, messagePattern.length());
                return new FormattingTuple(sbuf.toString(), argArray, throwable);
            }
            else {
                if (isEscapedDelimeter(messagePattern, j)) {
                    if (!isDoubleEscaped(messagePattern, j)) {
                        --L;
                        sbuf.append(messagePattern, i, j - 1);
                        sbuf.append('{');
                        i = j + 1;
                    }
                    else {
                        sbuf.append(messagePattern, i, j - 1);
                        deeplyAppendParameter(sbuf, argArray[L], new HashMap<Object[], Object>());
                        i = j + 2;
                    }
                }
                else {
                    sbuf.append(messagePattern, i, j);
                    deeplyAppendParameter(sbuf, argArray[L], new HashMap<Object[], Object>());
                    i = j + 2;
                }
                ++L;
            }
        }
        sbuf.append(messagePattern, i, messagePattern.length());
        return new FormattingTuple(sbuf.toString(), argArray, throwable);
    }
    
    static final boolean isEscapedDelimeter(final String messagePattern, final int delimeterStartIndex) {
        if (delimeterStartIndex == 0) {
            return false;
        }
        final char potentialEscape = messagePattern.charAt(delimeterStartIndex - 1);
        return potentialEscape == '\\';
    }
    
    static final boolean isDoubleEscaped(final String messagePattern, final int delimeterStartIndex) {
        return delimeterStartIndex >= 2 && messagePattern.charAt(delimeterStartIndex - 2) == '\\';
    }
    
    private static void deeplyAppendParameter(final StringBuilder sbuf, final Object o, final Map<Object[], Object> seenMap) {
        if (o == null) {
            sbuf.append("null");
            return;
        }
        if (!o.getClass().isArray()) {
            safeObjectAppend(sbuf, o);
        }
        else if (o instanceof boolean[]) {
            booleanArrayAppend(sbuf, (boolean[])o);
        }
        else if (o instanceof byte[]) {
            byteArrayAppend(sbuf, (byte[])o);
        }
        else if (o instanceof char[]) {
            charArrayAppend(sbuf, (char[])o);
        }
        else if (o instanceof short[]) {
            shortArrayAppend(sbuf, (short[])o);
        }
        else if (o instanceof int[]) {
            intArrayAppend(sbuf, (int[])o);
        }
        else if (o instanceof long[]) {
            longArrayAppend(sbuf, (long[])o);
        }
        else if (o instanceof float[]) {
            floatArrayAppend(sbuf, (float[])o);
        }
        else if (o instanceof double[]) {
            doubleArrayAppend(sbuf, (double[])o);
        }
        else {
            objectArrayAppend(sbuf, (Object[])o, seenMap);
        }
    }
    
    private static void safeObjectAppend(final StringBuilder sbuf, final Object o) {
        try {
            final String oAsString = o.toString();
            sbuf.append(oAsString);
        }
        catch (Throwable t) {
            Util.report("SLF4J: Failed toString() invocation on an object of type [" + o.getClass().getName() + "]", t);
            sbuf.append("[FAILED toString()]");
        }
    }
    
    private static void objectArrayAppend(final StringBuilder sbuf, final Object[] a, final Map<Object[], Object> seenMap) {
        sbuf.append('[');
        if (!seenMap.containsKey(a)) {
            seenMap.put(a, null);
            for (int len = a.length, i = 0; i < len; ++i) {
                deeplyAppendParameter(sbuf, a[i], seenMap);
                if (i != len - 1) {
                    sbuf.append(", ");
                }
            }
            seenMap.remove(a);
        }
        else {
            sbuf.append("...");
        }
        sbuf.append(']');
    }
    
    private static void booleanArrayAppend(final StringBuilder sbuf, final boolean[] a) {
        sbuf.append('[');
        for (int len = a.length, i = 0; i < len; ++i) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }
        sbuf.append(']');
    }
    
    private static void byteArrayAppend(final StringBuilder sbuf, final byte[] a) {
        sbuf.append('[');
        for (int len = a.length, i = 0; i < len; ++i) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }
        sbuf.append(']');
    }
    
    private static void charArrayAppend(final StringBuilder sbuf, final char[] a) {
        sbuf.append('[');
        for (int len = a.length, i = 0; i < len; ++i) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }
        sbuf.append(']');
    }
    
    private static void shortArrayAppend(final StringBuilder sbuf, final short[] a) {
        sbuf.append('[');
        for (int len = a.length, i = 0; i < len; ++i) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }
        sbuf.append(']');
    }
    
    private static void intArrayAppend(final StringBuilder sbuf, final int[] a) {
        sbuf.append('[');
        for (int len = a.length, i = 0; i < len; ++i) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }
        sbuf.append(']');
    }
    
    private static void longArrayAppend(final StringBuilder sbuf, final long[] a) {
        sbuf.append('[');
        for (int len = a.length, i = 0; i < len; ++i) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }
        sbuf.append(']');
    }
    
    private static void floatArrayAppend(final StringBuilder sbuf, final float[] a) {
        sbuf.append('[');
        for (int len = a.length, i = 0; i < len; ++i) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }
        sbuf.append(']');
    }
    
    private static void doubleArrayAppend(final StringBuilder sbuf, final double[] a) {
        sbuf.append('[');
        for (int len = a.length, i = 0; i < len; ++i) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }
        sbuf.append(']');
    }
}
