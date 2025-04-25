// 
// Decompiled by Procyon v0.5.36
// 

package org.scijava.nativelib;

import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Level;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public abstract class BaseJniExtractor implements JniExtractor
{
    private static final Logger LOGGER;
    private static final String JAVA_TMPDIR = "java.io.tmpdir";
    private Class libraryJarClass;
    private String[] nativeResourcePaths;
    
    public BaseJniExtractor() throws IOException {
        this.init(null);
    }
    
    public BaseJniExtractor(final Class libraryJarClass) throws IOException {
        this.init(libraryJarClass);
    }
    
    private void init(final Class libraryJarClass) throws IOException {
        this.libraryJarClass = libraryJarClass;
        final String mxSysInfo = MxSysInfo.getMxSysInfo();
        if (mxSysInfo != null) {
            this.nativeResourcePaths = new String[] { "META-INF/lib/" + mxSysInfo + "/", "META-INF/lib/" };
        }
        else {
            this.nativeResourcePaths = new String[] { "META-INF/lib/" };
        }
    }
    
    public abstract File getNativeDir();
    
    public abstract File getJniDir();
    
    public File extractJni(final String libPath, final String libname) throws IOException {
        String mappedlibName = System.mapLibraryName(libname);
        BaseJniExtractor.LOGGER.log(Level.FINE, "mappedLib is " + mappedlibName);
        URL lib = null;
        if (null == this.libraryJarClass) {
            this.libraryJarClass = this.getClass();
        }
        lib = this.libraryJarClass.getClassLoader().getResource(libPath + mappedlibName);
        if (null == lib && mappedlibName.endsWith(".jnilib")) {
            lib = this.getClass().getClassLoader().getResource(libPath + mappedlibName.substring(0, mappedlibName.length() - 7) + ".dylib");
            if (null != lib) {
                mappedlibName = mappedlibName.substring(0, mappedlibName.length() - 7) + ".dylib";
            }
        }
        if (null != lib) {
            BaseJniExtractor.LOGGER.log(Level.FINE, "URL is " + lib.toString());
            BaseJniExtractor.LOGGER.log(Level.FINE, "URL path is " + lib.getPath());
            return this.extractResource(this.getJniDir(), lib, mappedlibName);
        }
        BaseJniExtractor.LOGGER.log(Level.INFO, "Couldn't find resource " + libPath + " " + mappedlibName);
        throw new IOException("Couldn't find resource " + libPath + " " + mappedlibName);
    }
    
    public void extractRegistered() throws IOException {
        BaseJniExtractor.LOGGER.log(Level.FINE, "Extracting libraries registered in classloader " + this.getClass().getClassLoader());
        for (int i = 0; i < this.nativeResourcePaths.length; ++i) {
            final Enumeration<URL> resources = this.getClass().getClassLoader().getResources(this.nativeResourcePaths[i] + "AUTOEXTRACT.LIST");
            while (resources.hasMoreElements()) {
                final URL res = resources.nextElement();
                BaseJniExtractor.LOGGER.log(Level.FINE, "Extracting libraries listed in " + res);
                final BufferedReader r = new BufferedReader(new InputStreamReader(res.openStream(), "UTF-8"));
                String line;
                while ((line = r.readLine()) != null) {
                    URL lib = null;
                    for (int j = 0; j < this.nativeResourcePaths.length; ++j) {
                        lib = this.getClass().getClassLoader().getResource(this.nativeResourcePaths[j] + line);
                        if (lib != null) {
                            break;
                        }
                    }
                    if (lib == null) {
                        throw new IOException("Couldn't find native library " + line + "on the classpath");
                    }
                    this.extractResource(this.getNativeDir(), lib, line);
                }
            }
        }
    }
    
    File extractResource(final File dir, final URL resource, final String outputName) throws IOException {
        final InputStream in = resource.openStream();
        String prefix = outputName;
        String suffix = null;
        final int lastDotIndex = outputName.lastIndexOf(46);
        if (-1 != lastDotIndex) {
            prefix = outputName.substring(0, lastDotIndex);
            suffix = outputName.substring(lastDotIndex);
        }
        this.deleteLeftoverFiles(prefix, suffix);
        final File outfile = File.createTempFile(prefix, suffix);
        BaseJniExtractor.LOGGER.log(Level.FINE, "Extracting '" + resource + "' to '" + outfile.getAbsolutePath() + "'");
        final FileOutputStream out = new FileOutputStream(outfile);
        copy(in, out);
        out.close();
        in.close();
        outfile.deleteOnExit();
        return outfile;
    }
    
    void deleteLeftoverFiles(final String prefix, final String suffix) {
        final File tmpDirectory = new File(System.getProperty("java.io.tmpdir"));
        final File[] files = tmpDirectory.listFiles(new FilenameFilter() {
            public boolean accept(final File dir, final String name) {
                return name.startsWith(prefix) && name.endsWith(suffix);
            }
        });
        if (files == null) {
            return;
        }
        for (final File file : files) {
            try {
                file.delete();
            }
            catch (SecurityException ex) {}
        }
    }
    
    static void copy(final InputStream in, final OutputStream out) throws IOException {
        final byte[] tmp = new byte[8192];
        int len = 0;
        while (true) {
            len = in.read(tmp);
            if (len <= 0) {
                break;
            }
            out.write(tmp, 0, len);
        }
    }
    
    static {
        LOGGER = Logger.getLogger("org.scijava.nativelib.BaseJniExtractor");
    }
}
