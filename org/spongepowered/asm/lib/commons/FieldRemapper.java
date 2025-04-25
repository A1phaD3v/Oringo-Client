// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.lib.commons;

import org.spongepowered.asm.lib.TypePath;
import org.spongepowered.asm.lib.AnnotationVisitor;
import org.spongepowered.asm.lib.FieldVisitor;

public class FieldRemapper extends FieldVisitor
{
    private final Remapper remapper;
    
    public FieldRemapper(final FieldVisitor fv, final Remapper remapper) {
        this(327680, fv, remapper);
    }
    
    protected FieldRemapper(final int api, final FieldVisitor fv, final Remapper remapper) {
        super(api, fv);
        this.remapper = remapper;
    }
    
    @Override
    public AnnotationVisitor visitAnnotation(final String desc, final boolean visible) {
        final AnnotationVisitor av = this.fv.visitAnnotation(this.remapper.mapDesc(desc), visible);
        return (av == null) ? null : new AnnotationRemapper(av, this.remapper);
    }
    
    @Override
    public AnnotationVisitor visitTypeAnnotation(final int typeRef, final TypePath typePath, final String desc, final boolean visible) {
        final AnnotationVisitor av = super.visitTypeAnnotation(typeRef, typePath, this.remapper.mapDesc(desc), visible);
        return (av == null) ? null : new AnnotationRemapper(av, this.remapper);
    }
}
