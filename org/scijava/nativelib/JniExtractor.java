// 
// Decompiled by Procyon v0.5.36
// 

package org.scijava.nativelib;

import java.io.IOException;
import java.io.File;

public interface JniExtractor
{
    File extractJni(final String p0, final String p1) throws IOException;
    
    void extractRegistered() throws IOException;
}
