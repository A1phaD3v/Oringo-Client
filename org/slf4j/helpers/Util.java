// 
// Decompiled by Procyon v0.5.36
// 

package org.slf4j.helpers;

public final class Util
{
    private static ClassContextSecurityManager SECURITY_MANAGER;
    private static boolean SECURITY_MANAGER_CREATION_ALREADY_ATTEMPTED;
    
    private Util() {
    }
    
    public static String safeGetSystemProperty(final String key) {
        if (key == null) {
            throw new IllegalArgumentException("null input");
        }
        String result = null;
        try {
            result = System.getProperty(key);
        }
        catch (SecurityException ex) {}
        return result;
    }
    
    public static boolean safeGetBooleanSystemProperty(final String key) {
        final String value = safeGetSystemProperty(key);
        return value != null && value.equalsIgnoreCase("true");
    }
    
    private static ClassContextSecurityManager getSecurityManager() {
        if (Util.SECURITY_MANAGER != null) {
            return Util.SECURITY_MANAGER;
        }
        if (Util.SECURITY_MANAGER_CREATION_ALREADY_ATTEMPTED) {
            return null;
        }
        Util.SECURITY_MANAGER = safeCreateSecurityManager();
        Util.SECURITY_MANAGER_CREATION_ALREADY_ATTEMPTED = true;
        return Util.SECURITY_MANAGER;
    }
    
    private static ClassContextSecurityManager safeCreateSecurityManager() {
        try {
            return new ClassContextSecurityManager();
        }
        catch (SecurityException sm) {
            return null;
        }
    }
    
    public static Class<?> getCallingClass() {
        final ClassContextSecurityManager securityManager = getSecurityManager();
        if (securityManager == null) {
            return null;
        }
        Class<?>[] trace;
        String thisClassName;
        int i;
        for (trace = securityManager.getClassContext(), thisClassName = Util.class.getName(), i = 0; i < trace.length && !thisClassName.equals(trace[i].getName()); ++i) {}
        if (i >= trace.length || i + 2 >= trace.length) {
            throw new IllegalStateException("Failed to find org.slf4j.helpers.Util or its caller in the stack; this should not happen");
        }
        return trace[i + 2];
    }
    
    public static final void report(final String msg, final Throwable t) {
        System.err.println(msg);
        System.err.println("Reported exception:");
        t.printStackTrace();
    }
    
    public static final void report(final String msg) {
        System.err.println("SLF4J: " + msg);
    }
    
    static {
        Util.SECURITY_MANAGER_CREATION_ALREADY_ATTEMPTED = false;
    }
    
    private static final class ClassContextSecurityManager extends SecurityManager
    {
        @Override
        protected Class<?>[] getClassContext() {
            return super.getClassContext();
        }
    }
}
