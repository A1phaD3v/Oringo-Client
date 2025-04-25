// 
// Decompiled by Procyon v0.5.36
// 

package org.scijava.nativelib;

import java.io.IOException;

public class NativeLoader
{
    private static JniExtractor jniExtractor;
    
    public static void loadLibrary(final String libname) throws IOException {
        System.load(NativeLoader.jniExtractor.extractJni("", libname).getAbsolutePath());
    }
    
    public static void extractRegistered() throws IOException {
        NativeLoader.jniExtractor.extractRegistered();
    }
    
    public static JniExtractor getJniExtractor() {
        return NativeLoader.jniExtractor;
    }
    
    public static void setJniExtractor(final JniExtractor jniExtractor) {
        NativeLoader.jniExtractor = jniExtractor;
    }
    
    static {
        NativeLoader.jniExtractor = null;
        try {
            if (NativeLoader.class.getClassLoader() == ClassLoader.getSystemClassLoader()) {
                NativeLoader.jniExtractor = new DefaultJniExtractor();
            }
            else {
                NativeLoader.jniExtractor = new WebappJniExtractor("Classloader");
            }
        }
        catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }
}
