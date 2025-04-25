// 
// Decompiled by Procyon v0.5.36
// 

package me.oringodevmode;

import java.util.Map;
import me.oringo.oringoclient.OringoDevMode;
import org.spongepowered.asm.launch.MixinBootstrap;
import java.util.HashMap;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.MCVersion("1.8.9")
public class Transformer implements IFMLLoadingPlugin
{
    public static HashMap<String, byte[]> classes;
    
    public Transformer() {
        MixinBootstrap.init();
    }
    
    public String[] getASMTransformerClass() {
        return new String[] { OringoDevMode.class.getName() };
    }
    
    public String getModContainerClass() {
        return null;
    }
    
    public String getSetupClass() {
        return null;
    }
    
    public void injectData(final Map<String, Object> data) {
    }
    
    public String getAccessTransformerClass() {
        return null;
    }
    
    static {
        Transformer.classes = new HashMap<String, byte[]>();
    }
}
