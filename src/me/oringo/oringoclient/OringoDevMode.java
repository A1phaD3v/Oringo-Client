// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient;

import me.oringodevmode.Transformer;
import java.io.File;
import net.minecraft.launchwrapper.IClassTransformer;

public class OringoDevMode implements IClassTransformer
{
    private boolean enabled;
    
    public OringoDevMode() {
        this.enabled = new File("OringoDev").exists();
    }
    
    public byte[] transform(final String name, final String transformedName, final byte[] basicClass) {
        if (this.enabled && !transformedName.startsWith("java") && !transformedName.startsWith("sun") && !transformedName.startsWith("org.lwjgl") && !transformedName.startsWith("org.apache") && !transformedName.startsWith("org.objectweb")) {
            Transformer.classes.remove(transformedName);
            Transformer.classes.put(transformedName, basicClass);
        }
        return basicClass;
    }
}
