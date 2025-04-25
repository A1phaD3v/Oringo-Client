// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.lib.commons;

import org.spongepowered.asm.lib.signature.SignatureVisitor;
import org.spongepowered.asm.lib.signature.SignatureWriter;
import org.spongepowered.asm.lib.signature.SignatureReader;
import org.spongepowered.asm.lib.Handle;
import org.spongepowered.asm.lib.Type;

public abstract class Remapper
{
    public String mapDesc(final String desc) {
        final Type t = Type.getType(desc);
        switch (t.getSort()) {
            case 9: {
                String s = this.mapDesc(t.getElementType().getDescriptor());
                for (int i = 0; i < t.getDimensions(); ++i) {
                    s = '[' + s;
                }
                return s;
            }
            case 10: {
                final String newType = this.map(t.getInternalName());
                if (newType != null) {
                    return 'L' + newType + ';';
                }
                break;
            }
        }
        return desc;
    }
    
    private Type mapType(final Type t) {
        switch (t.getSort()) {
            case 9: {
                String s = this.mapDesc(t.getElementType().getDescriptor());
                for (int i = 0; i < t.getDimensions(); ++i) {
                    s = '[' + s;
                }
                return Type.getType(s);
            }
            case 10: {
                final String s = this.map(t.getInternalName());
                return (s != null) ? Type.getObjectType(s) : t;
            }
            case 11: {
                return Type.getMethodType(this.mapMethodDesc(t.getDescriptor()));
            }
            default: {
                return t;
            }
        }
    }
    
    public String mapType(final String type) {
        if (type == null) {
            return null;
        }
        return this.mapType(Type.getObjectType(type)).getInternalName();
    }
    
    public String[] mapTypes(final String[] types) {
        String[] newTypes = null;
        boolean needMapping = false;
        for (int i = 0; i < types.length; ++i) {
            final String type = types[i];
            final String newType = this.map(type);
            if (newType != null && newTypes == null) {
                newTypes = new String[types.length];
                if (i > 0) {
                    System.arraycopy(types, 0, newTypes, 0, i);
                }
                needMapping = true;
            }
            if (needMapping) {
                newTypes[i] = ((newType == null) ? type : newType);
            }
        }
        return needMapping ? newTypes : types;
    }
    
    public String mapMethodDesc(final String desc) {
        if ("()V".equals(desc)) {
            return desc;
        }
        final Type[] args = Type.getArgumentTypes(desc);
        final StringBuilder sb = new StringBuilder("(");
        for (int i = 0; i < args.length; ++i) {
            sb.append(this.mapDesc(args[i].getDescriptor()));
        }
        final Type returnType = Type.getReturnType(desc);
        if (returnType == Type.VOID_TYPE) {
            sb.append(")V");
            return sb.toString();
        }
        sb.append(')').append(this.mapDesc(returnType.getDescriptor()));
        return sb.toString();
    }
    
    public Object mapValue(final Object value) {
        if (value instanceof Type) {
            return this.mapType((Type)value);
        }
        if (value instanceof Handle) {
            final Handle h = (Handle)value;
            return new Handle(h.getTag(), this.mapType(h.getOwner()), this.mapMethodName(h.getOwner(), h.getName(), h.getDesc()), this.mapMethodDesc(h.getDesc()), h.isInterface());
        }
        return value;
    }
    
    public String mapSignature(final String signature, final boolean typeSignature) {
        if (signature == null) {
            return null;
        }
        final SignatureReader r = new SignatureReader(signature);
        final SignatureWriter w = new SignatureWriter();
        final SignatureVisitor a = this.createSignatureRemapper(w);
        if (typeSignature) {
            r.acceptType(a);
        }
        else {
            r.accept(a);
        }
        return w.toString();
    }
    
    @Deprecated
    protected SignatureVisitor createRemappingSignatureAdapter(final SignatureVisitor v) {
        return new SignatureRemapper(v, this);
    }
    
    protected SignatureVisitor createSignatureRemapper(final SignatureVisitor v) {
        return this.createRemappingSignatureAdapter(v);
    }
    
    public String mapMethodName(final String owner, final String name, final String desc) {
        return name;
    }
    
    public String mapInvokeDynamicMethodName(final String name, final String desc) {
        return name;
    }
    
    public String mapFieldName(final String owner, final String name, final String desc) {
        return name;
    }
    
    public String map(final String typeName) {
        return typeName;
    }
}
