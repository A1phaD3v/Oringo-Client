// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.lib.commons;

import org.spongepowered.asm.lib.AnnotationVisitor;

public class AnnotationRemapper extends AnnotationVisitor
{
    protected final Remapper remapper;
    
    public AnnotationRemapper(final AnnotationVisitor av, final Remapper remapper) {
        this(327680, av, remapper);
    }
    
    protected AnnotationRemapper(final int api, final AnnotationVisitor av, final Remapper remapper) {
        super(api, av);
        this.remapper = remapper;
    }
    
    @Override
    public void visit(final String name, final Object value) {
        this.av.visit(name, this.remapper.mapValue(value));
    }
    
    @Override
    public void visitEnum(final String name, final String desc, final String value) {
        this.av.visitEnum(name, this.remapper.mapDesc(desc), value);
    }
    
    @Override
    public AnnotationVisitor visitAnnotation(final String name, final String desc) {
        final AnnotationVisitor v = this.av.visitAnnotation(name, this.remapper.mapDesc(desc));
        return (v == null) ? null : ((v == this.av) ? this : new AnnotationRemapper(v, this.remapper));
    }
    
    @Override
    public AnnotationVisitor visitArray(final String name) {
        final AnnotationVisitor v = this.av.visitArray(name);
        return (v == null) ? null : ((v == this.av) ? this : new AnnotationRemapper(v, this.remapper));
    }
}
