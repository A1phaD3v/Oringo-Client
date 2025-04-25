// 
// Decompiled by Procyon v0.5.36
// 

package org.scijava.nativelib;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NativeLibraryUtil
{
    private static Architecture architecture;
    private static final String DELIM = "/";
    private static final String JAVA_TMPDIR = "java.io.tmpdir";
    private static final Logger LOGGER;
    
    public static Architecture getArchitecture() {
        if (Architecture.UNKNOWN == NativeLibraryUtil.architecture) {
            final Processor processor = getProcessor();
            if (Processor.UNKNOWN != processor) {
                final String name = System.getProperty("os.name").toLowerCase();
                if (name.indexOf("nix") >= 0 || name.indexOf("nux") >= 0) {
                    if (Processor.INTEL_32 == processor) {
                        NativeLibraryUtil.architecture = Architecture.LINUX_32;
                    }
                    else if (Processor.INTEL_64 == processor) {
                        NativeLibraryUtil.architecture = Architecture.LINUX_64;
                    }
                }
                else if (name.indexOf("win") >= 0) {
                    if (Processor.INTEL_32 == processor) {
                        NativeLibraryUtil.architecture = Architecture.WINDOWS_32;
                    }
                    else if (Processor.INTEL_64 == processor) {
                        NativeLibraryUtil.architecture = Architecture.WINDOWS_64;
                    }
                }
                else if (name.indexOf("mac") >= 0) {
                    if (Processor.INTEL_32 == processor) {
                        NativeLibraryUtil.architecture = Architecture.OSX_32;
                    }
                    else if (Processor.INTEL_64 == processor) {
                        NativeLibraryUtil.architecture = Architecture.OSX_64;
                    }
                    else if (Processor.PPC == processor) {
                        NativeLibraryUtil.architecture = Architecture.OSX_PPC;
                    }
                }
            }
        }
        NativeLibraryUtil.LOGGER.log(Level.FINE, "architecture is " + NativeLibraryUtil.architecture + " os.name is " + System.getProperty("os.name").toLowerCase());
        return NativeLibraryUtil.architecture;
    }
    
    private static Processor getProcessor() {
        Processor processor = Processor.UNKNOWN;
        final String arch = System.getProperty("os.arch").toLowerCase();
        if (arch.indexOf("ppc") >= 0) {
            processor = Processor.PPC;
        }
        else if (arch.indexOf("86") >= 0 || arch.indexOf("amd") >= 0) {
            int bits = 32;
            if (arch.indexOf("64") >= 0) {
                bits = 64;
            }
            processor = ((32 == bits) ? Processor.INTEL_32 : Processor.INTEL_64);
        }
        NativeLibraryUtil.LOGGER.log(Level.FINE, "processor is " + processor + " os.arch is " + System.getProperty("os.arch").toLowerCase());
        return processor;
    }
    
    public static String getPlatformLibraryPath() {
        String path = "META-INF/lib/";
        path = path + getArchitecture().name().toLowerCase() + "/";
        NativeLibraryUtil.LOGGER.log(Level.FINE, "platform specific path is " + path);
        return path;
    }
    
    public static String getPlatformLibraryName(final String libName) {
        String name = null;
        switch (getArchitecture()) {
            case LINUX_32:
            case LINUX_64: {
                name = libName + ".so";
                break;
            }
            case WINDOWS_32:
            case WINDOWS_64: {
                name = libName + ".dll";
                break;
            }
            case OSX_32:
            case OSX_64: {
                name = "lib" + libName + ".dylib";
                break;
            }
        }
        NativeLibraryUtil.LOGGER.log(Level.FINE, "native library name " + name);
        return name;
    }
    
    public static String getVersionedLibraryName(final Class libraryJarClass, String libName) {
        final String version = libraryJarClass.getPackage().getImplementationVersion();
        if (null != version && version.length() > 0) {
            libName = libName + "-" + version;
        }
        return libName;
    }
    
    public static boolean loadVersionedNativeLibrary(final Class libraryJarClass, String libName) {
        libName = getVersionedLibraryName(libraryJarClass, libName);
        return loadNativeLibrary(libraryJarClass, libName);
    }
    
    public static boolean loadNativeLibrary(final Class libraryJarClass, final String libName) {
        boolean success = false;
        if (Architecture.UNKNOWN == getArchitecture()) {
            NativeLibraryUtil.LOGGER.log(Level.WARNING, "No native library available for this platform.");
        }
        else {
            try {
                final String tmpDirectory = System.getProperty("java.io.tmpdir");
                final JniExtractor jniExtractor = new DefaultJniExtractor(libraryJarClass, tmpDirectory);
                final File extractedFile = jniExtractor.extractJni(getPlatformLibraryPath(), libName);
                System.load(extractedFile.getPath());
                success = true;
            }
            catch (IOException e) {
                NativeLibraryUtil.LOGGER.log(Level.WARNING, "IOException creating DefaultJniExtractor", e);
            }
            catch (SecurityException e2) {
                NativeLibraryUtil.LOGGER.log(Level.WARNING, "Can't load dynamic library", e2);
            }
            catch (UnsatisfiedLinkError e3) {
                NativeLibraryUtil.LOGGER.log(Level.WARNING, "Problem with library", e3);
            }
        }
        return success;
    }
    
    static {
        NativeLibraryUtil.architecture = Architecture.UNKNOWN;
        LOGGER = Logger.getLogger("org.scijava.nativelib.NativeLibraryUtil");
    }
    
    public enum Architecture
    {
        UNKNOWN, 
        LINUX_32, 
        LINUX_64, 
        WINDOWS_32, 
        WINDOWS_64, 
        OSX_32, 
        OSX_64, 
        OSX_PPC;
    }
    
    private enum Processor
    {
        UNKNOWN, 
        INTEL_32, 
        INTEL_64, 
        PPC;
    }
}
