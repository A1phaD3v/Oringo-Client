// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.injection.points;

import org.spongepowered.asm.lib.tree.MethodInsnNode;
import java.util.ListIterator;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import java.util.Collection;
import org.spongepowered.asm.lib.tree.InsnList;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.mixin.injection.struct.InjectionPointData;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.refmap.IMixinContext;
import org.spongepowered.asm.mixin.injection.struct.MemberInfo;
import org.spongepowered.asm.mixin.injection.InjectionPoint;

@AtCode("INVOKE")
public class BeforeInvoke extends InjectionPoint
{
    protected final MemberInfo target;
    protected final boolean allowPermissive;
    protected final int ordinal;
    protected final String className;
    protected final IMixinContext context;
    protected final Logger logger;
    private boolean log;
    
    public BeforeInvoke(final InjectionPointData data) {
        super(data);
        this.logger = LogManager.getLogger("mixin");
        this.log = false;
        this.target = data.getTarget();
        this.ordinal = data.getOrdinal();
        this.log = data.get("log", false);
        this.className = this.getClassName();
        this.context = data.getContext();
        this.allowPermissive = (this.context.getOption(MixinEnvironment.Option.REFMAP_REMAP) && this.context.getOption(MixinEnvironment.Option.REFMAP_REMAP_ALLOW_PERMISSIVE) && !this.context.getReferenceMapper().isDefault());
    }
    
    private String getClassName() {
        final AtCode atCode = this.getClass().getAnnotation(AtCode.class);
        return String.format("@At(%s)", (atCode != null) ? atCode.value() : this.getClass().getSimpleName().toUpperCase());
    }
    
    public BeforeInvoke setLogging(final boolean logging) {
        this.log = logging;
        return this;
    }
    
    @Override
    public boolean find(final String desc, final InsnList insns, final Collection<AbstractInsnNode> nodes) {
        this.log("{} is searching for an injection point in method with descriptor {}", this.className, desc);
        if (!this.find(desc, insns, nodes, this.target, SearchType.STRICT) && this.target.desc != null && this.allowPermissive) {
            this.logger.warn("STRICT match for {} using \"{}\" in {} returned 0 results, attempting permissive search. To inhibit permissive search set mixin.env.allowPermissiveMatch=false", new Object[] { this.className, this.target, this.context });
            return this.find(desc, insns, nodes, this.target, SearchType.PERMISSIVE);
        }
        return true;
    }
    
    protected boolean find(final String desc, final InsnList insns, final Collection<AbstractInsnNode> nodes, final MemberInfo member, final SearchType searchType) {
        if (member == null) {
            return false;
        }
        final MemberInfo target = (searchType == SearchType.PERMISSIVE) ? member.transform(null) : member;
        int ordinal = 0;
        int found = 0;
        for (final AbstractInsnNode insn : insns) {
            if (this.matchesInsn(insn)) {
                final MemberInfo nodeInfo = new MemberInfo(insn);
                this.log("{} is considering insn {}", this.className, nodeInfo);
                if (target.matches(nodeInfo.owner, nodeInfo.name, nodeInfo.desc)) {
                    this.log("{} > found a matching insn, checking preconditions...", this.className);
                    if (this.matchesInsn(nodeInfo, ordinal)) {
                        this.log("{} > > > found a matching insn at ordinal {}", this.className, ordinal);
                        if (this.addInsn(insns, nodes, insn)) {
                            ++found;
                        }
                        if (this.ordinal == ordinal) {
                            break;
                        }
                    }
                    ++ordinal;
                }
            }
            this.inspectInsn(desc, insns, insn);
        }
        if (searchType == SearchType.PERMISSIVE && found > 1) {
            this.logger.warn("A permissive match for {} using \"{}\" in {} matched {} instructions, this may cause unexpected behaviour. To inhibit permissive search set mixin.env.allowPermissiveMatch=false", new Object[] { this.className, member, this.context, found });
        }
        return found > 0;
    }
    
    protected boolean addInsn(final InsnList insns, final Collection<AbstractInsnNode> nodes, final AbstractInsnNode insn) {
        nodes.add(insn);
        return true;
    }
    
    protected boolean matchesInsn(final AbstractInsnNode insn) {
        return insn instanceof MethodInsnNode;
    }
    
    protected void inspectInsn(final String desc, final InsnList insns, final AbstractInsnNode insn) {
    }
    
    protected boolean matchesInsn(final MemberInfo nodeInfo, final int ordinal) {
        this.log("{} > > comparing target ordinal {} with current ordinal {}", this.className, this.ordinal, ordinal);
        return this.ordinal == -1 || this.ordinal == ordinal;
    }
    
    protected void log(final String message, final Object... params) {
        if (this.log) {
            this.logger.info(message, params);
        }
    }
    
    public enum SearchType
    {
        STRICT, 
        PERMISSIVE;
    }
}
