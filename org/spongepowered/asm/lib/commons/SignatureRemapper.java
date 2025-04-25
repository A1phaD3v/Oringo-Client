// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.lib.commons;

import java.util.Stack;
import org.spongepowered.asm.lib.signature.SignatureVisitor;

public class SignatureRemapper extends SignatureVisitor
{
    private final SignatureVisitor v;
    private final Remapper remapper;
    private Stack<String> classNames;
    
    public SignatureRemapper(final SignatureVisitor v, final Remapper remapper) {
        this(327680, v, remapper);
    }
    
    protected SignatureRemapper(final int api, final SignatureVisitor v, final Remapper remapper) {
        super(api);
        this.classNames = new Stack<String>();
        this.v = v;
        this.remapper = remapper;
    }
    
    @Override
    public void visitClassType(final String name) {
        this.classNames.push(name);
        this.v.visitClassType(this.remapper.mapType(name));
    }
    
    @Override
    public void visitInnerClassType(final String name) {
        final String outerClassName = this.classNames.pop();
        final String className = outerClassName + '$' + name;
        this.classNames.push(className);
        final String remappedOuter = this.remapper.mapType(outerClassName) + '$';
        final String remappedName = this.remapper.mapType(className);
        final int index = remappedName.startsWith(remappedOuter) ? remappedOuter.length() : (remappedName.lastIndexOf(36) + 1);
        this.v.visitInnerClassType(remappedName.substring(index));
    }
    
    @Override
    public void visitFormalTypeParameter(final String name) {
        this.v.visitFormalTypeParameter(name);
    }
    
    @Override
    public void visitTypeVariable(final String name) {
        this.v.visitTypeVariable(name);
    }
    
    @Override
    public SignatureVisitor visitArrayType() {
        this.v.visitArrayType();
        return this;
    }
    
    @Override
    public void visitBaseType(final char descriptor) {
        this.v.visitBaseType(descriptor);
    }
    
    @Override
    public SignatureVisitor visitClassBound() {
        this.v.visitClassBound();
        return this;
    }
    
    @Override
    public SignatureVisitor visitExceptionType() {
        this.v.visitExceptionType();
        return this;
    }
    
    @Override
    public SignatureVisitor visitInterface() {
        this.v.visitInterface();
        return this;
    }
    
    @Override
    public SignatureVisitor visitInterfaceBound() {
        this.v.visitInterfaceBound();
        return this;
    }
    
    @Override
    public SignatureVisitor visitParameterType() {
        this.v.visitParameterType();
        return this;
    }
    
    @Override
    public SignatureVisitor visitReturnType() {
        this.v.visitReturnType();
        return this;
    }
    
    @Override
    public SignatureVisitor visitSuperclass() {
        this.v.visitSuperclass();
        return this;
    }
    
    @Override
    public void visitTypeArgument() {
        this.v.visitTypeArgument();
    }
    
    @Override
    public SignatureVisitor visitTypeArgument(final char wildcard) {
        this.v.visitTypeArgument(wildcard);
        return this;
    }
    
    @Override
    public void visitEnd() {
        this.v.visitEnd();
        this.classNames.pop();
    }
}
