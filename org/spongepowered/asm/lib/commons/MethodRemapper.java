// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.lib.commons;

import org.spongepowered.asm.lib.Label;
import org.spongepowered.asm.lib.Handle;
import org.spongepowered.asm.lib.TypePath;
import org.spongepowered.asm.lib.AnnotationVisitor;
import org.spongepowered.asm.lib.MethodVisitor;

public class MethodRemapper extends MethodVisitor
{
    protected final Remapper remapper;
    
    public MethodRemapper(final MethodVisitor mv, final Remapper remapper) {
        this(327680, mv, remapper);
    }
    
    protected MethodRemapper(final int api, final MethodVisitor mv, final Remapper remapper) {
        super(api, mv);
        this.remapper = remapper;
    }
    
    @Override
    public AnnotationVisitor visitAnnotationDefault() {
        final AnnotationVisitor av = super.visitAnnotationDefault();
        return (av == null) ? av : new AnnotationRemapper(av, this.remapper);
    }
    
    @Override
    public AnnotationVisitor visitAnnotation(final String desc, final boolean visible) {
        final AnnotationVisitor av = super.visitAnnotation(this.remapper.mapDesc(desc), visible);
        return (av == null) ? av : new AnnotationRemapper(av, this.remapper);
    }
    
    @Override
    public AnnotationVisitor visitTypeAnnotation(final int typeRef, final TypePath typePath, final String desc, final boolean visible) {
        final AnnotationVisitor av = super.visitTypeAnnotation(typeRef, typePath, this.remapper.mapDesc(desc), visible);
        return (av == null) ? av : new AnnotationRemapper(av, this.remapper);
    }
    
    @Override
    public AnnotationVisitor visitParameterAnnotation(final int parameter, final String desc, final boolean visible) {
        final AnnotationVisitor av = super.visitParameterAnnotation(parameter, this.remapper.mapDesc(desc), visible);
        return (av == null) ? av : new AnnotationRemapper(av, this.remapper);
    }
    
    @Override
    public void visitFrame(final int type, final int nLocal, final Object[] local, final int nStack, final Object[] stack) {
        super.visitFrame(type, nLocal, this.remapEntries(nLocal, local), nStack, this.remapEntries(nStack, stack));
    }
    
    private Object[] remapEntries(final int n, final Object[] entries) {
        for (int i = 0; i < n; ++i) {
            if (entries[i] instanceof String) {
                final Object[] newEntries = new Object[n];
                if (i > 0) {
                    System.arraycopy(entries, 0, newEntries, 0, i);
                }
                do {
                    final Object t = entries[i];
                    newEntries[i++] = ((t instanceof String) ? this.remapper.mapType((String)t) : t);
                } while (i < n);
                return newEntries;
            }
        }
        return entries;
    }
    
    @Override
    public void visitFieldInsn(final int opcode, final String owner, final String name, final String desc) {
        super.visitFieldInsn(opcode, this.remapper.mapType(owner), this.remapper.mapFieldName(owner, name, desc), this.remapper.mapDesc(desc));
    }
    
    @Deprecated
    @Override
    public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc) {
        if (this.api >= 327680) {
            super.visitMethodInsn(opcode, owner, name, desc);
            return;
        }
        this.doVisitMethodInsn(opcode, owner, name, desc, opcode == 185);
    }
    
    @Override
    public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc, final boolean itf) {
        if (this.api < 327680) {
            super.visitMethodInsn(opcode, owner, name, desc, itf);
            return;
        }
        this.doVisitMethodInsn(opcode, owner, name, desc, itf);
    }
    
    private void doVisitMethodInsn(final int opcode, final String owner, final String name, final String desc, final boolean itf) {
        if (this.mv != null) {
            this.mv.visitMethodInsn(opcode, this.remapper.mapType(owner), this.remapper.mapMethodName(owner, name, desc), this.remapper.mapMethodDesc(desc), itf);
        }
    }
    
    @Override
    public void visitInvokeDynamicInsn(final String name, final String desc, final Handle bsm, final Object... bsmArgs) {
        for (int i = 0; i < bsmArgs.length; ++i) {
            bsmArgs[i] = this.remapper.mapValue(bsmArgs[i]);
        }
        super.visitInvokeDynamicInsn(this.remapper.mapInvokeDynamicMethodName(name, desc), this.remapper.mapMethodDesc(desc), (Handle)this.remapper.mapValue(bsm), bsmArgs);
    }
    
    @Override
    public void visitTypeInsn(final int opcode, final String type) {
        super.visitTypeInsn(opcode, this.remapper.mapType(type));
    }
    
    @Override
    public void visitLdcInsn(final Object cst) {
        super.visitLdcInsn(this.remapper.mapValue(cst));
    }
    
    @Override
    public void visitMultiANewArrayInsn(final String desc, final int dims) {
        super.visitMultiANewArrayInsn(this.remapper.mapDesc(desc), dims);
    }
    
    @Override
    public AnnotationVisitor visitInsnAnnotation(final int typeRef, final TypePath typePath, final String desc, final boolean visible) {
        final AnnotationVisitor av = super.visitInsnAnnotation(typeRef, typePath, this.remapper.mapDesc(desc), visible);
        return (av == null) ? av : new AnnotationRemapper(av, this.remapper);
    }
    
    @Override
    public void visitTryCatchBlock(final Label start, final Label end, final Label handler, final String type) {
        super.visitTryCatchBlock(start, end, handler, (type == null) ? null : this.remapper.mapType(type));
    }
    
    @Override
    public AnnotationVisitor visitTryCatchAnnotation(final int typeRef, final TypePath typePath, final String desc, final boolean visible) {
        final AnnotationVisitor av = super.visitTryCatchAnnotation(typeRef, typePath, this.remapper.mapDesc(desc), visible);
        return (av == null) ? av : new AnnotationRemapper(av, this.remapper);
    }
    
    @Override
    public void visitLocalVariable(final String name, final String desc, final String signature, final Label start, final Label end, final int index) {
        super.visitLocalVariable(name, this.remapper.mapDesc(desc), this.remapper.mapSignature(signature, true), start, end, index);
    }
    
    @Override
    public AnnotationVisitor visitLocalVariableAnnotation(final int typeRef, final TypePath typePath, final Label[] start, final Label[] end, final int[] index, final String desc, final boolean visible) {
        final AnnotationVisitor av = super.visitLocalVariableAnnotation(typeRef, typePath, start, end, index, this.remapper.mapDesc(desc), visible);
        return (av == null) ? av : new AnnotationRemapper(av, this.remapper);
    }
}
