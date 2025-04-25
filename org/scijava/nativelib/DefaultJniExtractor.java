// 
// Decompiled by Procyon v0.5.36
// 

package org.scijava.nativelib;

import java.io.IOException;
import java.io.File;

public class DefaultJniExtractor extends BaseJniExtractor
{
    private File nativeDir;
    
    public DefaultJniExtractor() throws IOException {
        super(null);
        this.init("tmplib");
    }
    
    public DefaultJniExtractor(final Class libraryJarClass, final String tmplib) throws IOException {
        super(libraryJarClass);
        this.init(tmplib);
    }
    
    void init(final String tmplib) throws IOException {
        (this.nativeDir = new File(System.getProperty("java.library.tmpdir", tmplib))).mkdirs();
        if (!this.nativeDir.isDirectory()) {
            throw new IOException("Unable to create native library working directory " + this.nativeDir);
        }
    }
    
    @Override
    public File getJniDir() {
        return this.nativeDir;
    }
    
    @Override
    public File getNativeDir() {
        return this.nativeDir;
    }
}
