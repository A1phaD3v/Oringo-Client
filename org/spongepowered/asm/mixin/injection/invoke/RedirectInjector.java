// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.injection.invoke;

import com.google.common.collect.ObjectArrays;
import org.spongepowered.asm.lib.tree.JumpInsnNode;
import org.spongepowered.asm.lib.tree.LabelNode;
import com.google.common.base.Joiner;
import org.spongepowered.asm.lib.tree.InsnNode;
import org.spongepowered.asm.lib.tree.VarInsnNode;
import org.spongepowered.asm.lib.tree.AnnotationNode;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.injection.Coerce;
import com.google.common.primitives.Ints;
import org.spongepowered.asm.util.Bytecode;
import org.spongepowered.asm.lib.tree.InsnList;
import org.spongepowered.asm.lib.tree.FieldInsnNode;
import org.spongepowered.asm.lib.tree.MethodInsnNode;
import java.util.Iterator;
import org.spongepowered.asm.lib.tree.TypeInsnNode;
import org.spongepowered.asm.mixin.injection.points.BeforeFieldAccess;
import org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionException;
import org.spongepowered.asm.mixin.injection.code.Injector;
import org.spongepowered.asm.mixin.injection.InjectionPoint;
import java.util.Set;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import org.spongepowered.asm.mixin.injection.struct.InjectionNodes;
import java.util.List;
import org.spongepowered.asm.mixin.injection.struct.Target;
import java.lang.annotation.Annotation;
import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.mixin.Final;
import java.util.HashMap;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.mixin.injection.points.BeforeNew;
import java.util.Map;

public class RedirectInjector extends InvokeInjector
{
    private static final String KEY_NOMINATORS = "nominators";
    private static final String KEY_FUZZ = "fuzz";
    private static final String KEY_OPCODE = "opcode";
    protected Meta meta;
    private Map<BeforeNew, ConstructorRedirectData> ctorRedirectors;
    
    public RedirectInjector(final InjectionInfo info) {
        this(info, "@Redirect");
    }
    
    protected RedirectInjector(final InjectionInfo info, final String annotationType) {
        super(info, annotationType);
        this.ctorRedirectors = new HashMap<BeforeNew, ConstructorRedirectData>();
        final int priority = info.getContext().getPriority();
        final boolean isFinal = Annotations.getVisible(this.methodNode, Final.class) != null;
        this.meta = new Meta(priority, isFinal, this.info.toString(), this.methodNode.desc);
    }
    
    @Override
    protected void checkTarget(final Target target) {
    }
    
    @Override
    protected void addTargetNode(final Target target, final List<InjectionNodes.InjectionNode> myNodes, final AbstractInsnNode insn, final Set<InjectionPoint> nominators) {
        final InjectionNodes.InjectionNode node = target.getInjectionNode(insn);
        ConstructorRedirectData ctorData = null;
        int fuzz = 8;
        int opcode = 0;
        if (node != null) {
            final Meta other = node.getDecoration("redirector");
            if (other != null && other.getOwner() != this) {
                if (other.priority >= this.meta.priority) {
                    Injector.logger.warn("{} conflict. Skipping {} with priority {}, already redirected by {} with priority {}", new Object[] { this.annotationType, this.info, this.meta.priority, other.name, other.priority });
                    return;
                }
                if (other.isFinal) {
                    throw new InvalidInjectionException(this.info, String.format("%s conflict: %s failed because target was already remapped by %s", this.annotationType, this, other.name));
                }
            }
        }
        for (final InjectionPoint ip : nominators) {
            if (ip instanceof BeforeNew) {
                ctorData = this.getCtorRedirect((BeforeNew)ip);
                ctorData.wildcard = !((BeforeNew)ip).hasDescriptor();
            }
            else {
                if (!(ip instanceof BeforeFieldAccess)) {
                    continue;
                }
                final BeforeFieldAccess bfa = (BeforeFieldAccess)ip;
                fuzz = bfa.getFuzzFactor();
                opcode = bfa.getArrayOpcode();
            }
        }
        final InjectionNodes.InjectionNode targetNode = target.addInjectionNode(insn);
        targetNode.decorate("redirector", this.meta);
        targetNode.decorate("nominators", nominators);
        if (insn instanceof TypeInsnNode && insn.getOpcode() == 187) {
            targetNode.decorate("ctor", ctorData);
        }
        else {
            targetNode.decorate("fuzz", fuzz);
            targetNode.decorate("opcode", opcode);
        }
        myNodes.add(targetNode);
    }
    
    private ConstructorRedirectData getCtorRedirect(final BeforeNew ip) {
        ConstructorRedirectData ctorRedirect = this.ctorRedirectors.get(ip);
        if (ctorRedirect == null) {
            ctorRedirect = new ConstructorRedirectData();
            this.ctorRedirectors.put(ip, ctorRedirect);
        }
        return ctorRedirect;
    }
    
    @Override
    protected void inject(final Target target, final InjectionNodes.InjectionNode node) {
        if (!this.preInject(node)) {
            return;
        }
        if (node.isReplaced()) {
            throw new UnsupportedOperationException("Redirector target failure for " + this.info);
        }
        if (node.getCurrentTarget() instanceof MethodInsnNode) {
            this.checkTargetForNode(target, node);
            this.injectAtInvoke(target, node);
            return;
        }
        if (node.getCurrentTarget() instanceof FieldInsnNode) {
            this.checkTargetForNode(target, node);
            this.injectAtFieldAccess(target, node);
            return;
        }
        if (!(node.getCurrentTarget() instanceof TypeInsnNode) || node.getCurrentTarget().getOpcode() != 187) {
            throw new InvalidInjectionException(this.info, String.format("%s annotation on is targetting an invalid insn in %s in %s", this.annotationType, target, this));
        }
        if (!this.isStatic && target.isStatic) {
            throw new InvalidInjectionException(this.info, String.format("non-static callback method %s has a static target which is not supported", this));
        }
        this.injectAtConstructor(target, node);
    }
    
    protected boolean preInject(final InjectionNodes.InjectionNode node) {
        final Meta other = node.getDecoration("redirector");
        if (other.getOwner() != this) {
            Injector.logger.warn("{} conflict. Skipping {} with priority {}, already redirected by {} with priority {}", new Object[] { this.annotationType, this.info, this.meta.priority, other.name, other.priority });
            return false;
        }
        return true;
    }
    
    @Override
    protected void postInject(final Target target, final InjectionNodes.InjectionNode node) {
        super.postInject(target, node);
        if (node.getOriginalTarget() instanceof TypeInsnNode && node.getOriginalTarget().getOpcode() == 187) {
            final ConstructorRedirectData meta = node.getDecoration("ctor");
            if (meta.wildcard && meta.injected == 0) {
                throw new InvalidInjectionException(this.info, String.format("%s ctor invocation was not found in %s", this.annotationType, target));
            }
        }
    }
    
    @Override
    protected void injectAtInvoke(final Target target, final InjectionNodes.InjectionNode node) {
        final RedirectedInvoke invoke = new RedirectedInvoke(target, (MethodInsnNode)node.getCurrentTarget());
        this.validateParams(invoke);
        final InsnList insns = new InsnList();
        int extraLocals = Bytecode.getArgsSize(invoke.locals) + 1;
        int extraStack = 1;
        int[] argMap = this.storeArgs(target, invoke.locals, insns, 0);
        if (invoke.captureTargetArgs) {
            final int argSize = Bytecode.getArgsSize(target.arguments);
            extraLocals += argSize;
            extraStack += argSize;
            argMap = Ints.concat(new int[][] { argMap, target.getArgIndices() });
        }
        final AbstractInsnNode insn = this.invokeHandlerWithArgs(this.methodArgs, insns, argMap);
        target.replaceNode(invoke.node, insn, insns);
        target.addToLocals(extraLocals);
        target.addToStack(extraStack);
    }
    
    protected void validateParams(final RedirectedInvoke invoke) {
        int argc = this.methodArgs.length;
        final String description = String.format("%s handler method %s", this.annotationType, this);
        if (!invoke.returnType.equals(this.returnType)) {
            throw new InvalidInjectionException(this.info, String.format("%s has an invalid signature. Expected return type %s found %s", description, this.returnType, invoke.returnType));
        }
        for (int index = 0; index < argc; ++index) {
            Type toType = null;
            if (index >= this.methodArgs.length) {
                throw new InvalidInjectionException(this.info, String.format("%s has an invalid signature. Not enough arguments found for capture of target method args, expected %d but found %d", description, argc, this.methodArgs.length));
            }
            final Type fromType = this.methodArgs[index];
            if (index < invoke.locals.length) {
                toType = invoke.locals[index];
            }
            else {
                invoke.captureTargetArgs = true;
                argc = Math.max(argc, invoke.locals.length + invoke.target.arguments.length);
                final int arg = index - invoke.locals.length;
                if (arg >= invoke.target.arguments.length) {
                    throw new InvalidInjectionException(this.info, String.format("%s has an invalid signature. Found unexpected additional target argument with type %s at index %d", description, fromType, index));
                }
                toType = invoke.target.arguments[arg];
            }
            final AnnotationNode coerce = Annotations.getInvisibleParameter(this.methodNode, Coerce.class, index);
            if (fromType.equals(toType)) {
                if (coerce != null && this.info.getContext().getOption(MixinEnvironment.Option.DEBUG_VERBOSE)) {
                    Injector.logger.warn("Redundant @Coerce on {} argument {}, {} is identical to {}", new Object[] { description, index, toType, fromType });
                }
            }
            else {
                final boolean canCoerce = Injector.canCoerce(fromType, toType);
                if (coerce == null) {
                    throw new InvalidInjectionException(this.info, String.format("%s has an invalid signature. Found unexpected argument type %s at index %d, expected %s", description, fromType, index, toType));
                }
                if (!canCoerce) {
                    throw new InvalidInjectionException(this.info, String.format("%s has an invalid signature. Cannot @Coerce argument type %s at index %d to %s", description, toType, index, fromType));
                }
            }
        }
    }
    
    private void injectAtFieldAccess(final Target target, final InjectionNodes.InjectionNode node) {
        final FieldInsnNode fieldNode = (FieldInsnNode)node.getCurrentTarget();
        final int opCode = fieldNode.getOpcode();
        final Type ownerType = Type.getType("L" + fieldNode.owner + ";");
        final Type fieldType = Type.getType(fieldNode.desc);
        final int targetDimensions = (fieldType.getSort() == 9) ? fieldType.getDimensions() : 0;
        final int handlerDimensions = (this.returnType.getSort() == 9) ? this.returnType.getDimensions() : 0;
        if (handlerDimensions > targetDimensions) {
            throw new InvalidInjectionException(this.info, "Dimensionality of handler method is greater than target array on " + this);
        }
        if (handlerDimensions == 0 && targetDimensions > 0) {
            final int fuzz = node.getDecoration("fuzz");
            final int opcode = node.getDecoration("opcode");
            this.injectAtArrayField(target, fieldNode, opCode, ownerType, fieldType, fuzz, opcode);
        }
        else {
            this.injectAtScalarField(target, fieldNode, opCode, ownerType, fieldType);
        }
    }
    
    private void injectAtArrayField(final Target target, final FieldInsnNode fieldNode, final int opCode, final Type ownerType, final Type fieldType, final int fuzz, int opcode) {
        final Type elementType = fieldType.getElementType();
        if (opCode != 178 && opCode != 180) {
            throw new InvalidInjectionException(this.info, String.format("Unspported opcode %s for array access %s", Bytecode.getOpcodeName(opCode), this.info));
        }
        if (this.returnType.getSort() != 0) {
            if (opcode != 190) {
                opcode = elementType.getOpcode(46);
            }
            final AbstractInsnNode varNode = BeforeFieldAccess.findArrayNode(target.insns, fieldNode, opcode, fuzz);
            this.injectAtGetArray(target, fieldNode, varNode, ownerType, fieldType);
        }
        else {
            final AbstractInsnNode varNode = BeforeFieldAccess.findArrayNode(target.insns, fieldNode, elementType.getOpcode(79), fuzz);
            this.injectAtSetArray(target, fieldNode, varNode, ownerType, fieldType);
        }
    }
    
    private void injectAtGetArray(final Target target, final FieldInsnNode fieldNode, final AbstractInsnNode varNode, final Type ownerType, final Type fieldType) {
        final String handlerDesc = getGetArrayHandlerDescriptor(varNode, this.returnType, fieldType);
        final boolean withArgs = this.checkDescriptor(handlerDesc, target, "array getter");
        this.injectArrayRedirect(target, fieldNode, varNode, withArgs, "array getter");
    }
    
    private void injectAtSetArray(final Target target, final FieldInsnNode fieldNode, final AbstractInsnNode varNode, final Type ownerType, final Type fieldType) {
        final String handlerDesc = Bytecode.generateDescriptor(null, (Object[])getArrayArgs(fieldType, 1, fieldType.getElementType()));
        final boolean withArgs = this.checkDescriptor(handlerDesc, target, "array setter");
        this.injectArrayRedirect(target, fieldNode, varNode, withArgs, "array setter");
    }
    
    public void injectArrayRedirect(final Target target, final FieldInsnNode fieldNode, final AbstractInsnNode varNode, final boolean withArgs, final String type) {
        if (varNode == null) {
            final String advice = "";
            throw new InvalidInjectionException(this.info, String.format("Array element %s on %s could not locate a matching %s instruction in %s. %s", this.annotationType, this, type, target, advice));
        }
        if (!this.isStatic) {
            target.insns.insertBefore(fieldNode, new VarInsnNode(25, 0));
            target.addToStack(1);
        }
        final InsnList invokeInsns = new InsnList();
        if (withArgs) {
            this.pushArgs(target.arguments, invokeInsns, target.getArgIndices(), 0, target.arguments.length);
            target.addToStack(Bytecode.getArgsSize(target.arguments));
        }
        target.replaceNode(varNode, this.invokeHandler(invokeInsns), invokeInsns);
    }
    
    public void injectAtScalarField(final Target target, final FieldInsnNode fieldNode, final int opCode, final Type ownerType, final Type fieldType) {
        AbstractInsnNode invoke = null;
        final InsnList insns = new InsnList();
        if (opCode == 178 || opCode == 180) {
            invoke = this.injectAtGetField(insns, target, fieldNode, opCode == 178, ownerType, fieldType);
        }
        else {
            if (opCode != 179 && opCode != 181) {
                throw new InvalidInjectionException(this.info, String.format("Unspported opcode %s for %s", Bytecode.getOpcodeName(opCode), this.info));
            }
            invoke = this.injectAtPutField(insns, target, fieldNode, opCode == 179, ownerType, fieldType);
        }
        target.replaceNode(fieldNode, invoke, insns);
    }
    
    private AbstractInsnNode injectAtGetField(final InsnList insns, final Target target, final FieldInsnNode node, final boolean staticField, final Type owner, final Type fieldType) {
        final String handlerDesc = staticField ? Bytecode.generateDescriptor(fieldType, new Object[0]) : Bytecode.generateDescriptor(fieldType, owner);
        final boolean withArgs = this.checkDescriptor(handlerDesc, target, "getter");
        if (!this.isStatic) {
            insns.add(new VarInsnNode(25, 0));
            if (!staticField) {
                insns.add(new InsnNode(95));
            }
        }
        if (withArgs) {
            this.pushArgs(target.arguments, insns, target.getArgIndices(), 0, target.arguments.length);
            target.addToStack(Bytecode.getArgsSize(target.arguments));
        }
        target.addToStack(this.isStatic ? 0 : 1);
        return this.invokeHandler(insns);
    }
    
    private AbstractInsnNode injectAtPutField(final InsnList insns, final Target target, final FieldInsnNode node, final boolean staticField, final Type owner, final Type fieldType) {
        final String handlerDesc = staticField ? Bytecode.generateDescriptor(null, fieldType) : Bytecode.generateDescriptor(null, owner, fieldType);
        final boolean withArgs = this.checkDescriptor(handlerDesc, target, "setter");
        if (!this.isStatic) {
            if (staticField) {
                insns.add(new VarInsnNode(25, 0));
                insns.add(new InsnNode(95));
            }
            else {
                final int marshallVar = target.allocateLocals(fieldType.getSize());
                insns.add(new VarInsnNode(fieldType.getOpcode(54), marshallVar));
                insns.add(new VarInsnNode(25, 0));
                insns.add(new InsnNode(95));
                insns.add(new VarInsnNode(fieldType.getOpcode(21), marshallVar));
            }
        }
        if (withArgs) {
            this.pushArgs(target.arguments, insns, target.getArgIndices(), 0, target.arguments.length);
            target.addToStack(Bytecode.getArgsSize(target.arguments));
        }
        target.addToStack((!this.isStatic && !staticField) ? 1 : 0);
        return this.invokeHandler(insns);
    }
    
    protected boolean checkDescriptor(final String desc, final Target target, final String type) {
        if (this.methodNode.desc.equals(desc)) {
            return false;
        }
        final int pos = desc.indexOf(41);
        final String alternateDesc = String.format("%s%s%s", desc.substring(0, pos), Joiner.on("").join((Object[])target.arguments), desc.substring(pos));
        if (this.methodNode.desc.equals(alternateDesc)) {
            return true;
        }
        throw new InvalidInjectionException(this.info, String.format("%s method %s %s has an invalid signature. Expected %s but found %s", this.annotationType, type, this, desc, this.methodNode.desc));
    }
    
    protected void injectAtConstructor(final Target target, final InjectionNodes.InjectionNode node) {
        final ConstructorRedirectData meta = node.getDecoration("ctor");
        if (meta == null) {
            throw new InvalidInjectionException(this.info, String.format("%s ctor redirector has no metadata, the injector failed a preprocessing phase", this.annotationType));
        }
        final TypeInsnNode newNode = (TypeInsnNode)node.getCurrentTarget();
        final AbstractInsnNode dupNode = target.get(target.indexOf(newNode) + 1);
        final MethodInsnNode initNode = target.findInitNodeFor(newNode);
        if (initNode != null) {
            final boolean isAssigned = dupNode.getOpcode() == 89;
            final String desc = initNode.desc.replace(")V", ")L" + newNode.desc + ";");
            boolean withArgs = false;
            try {
                withArgs = this.checkDescriptor(desc, target, "constructor");
            }
            catch (InvalidInjectionException ex) {
                if (!meta.wildcard) {
                    throw ex;
                }
                return;
            }
            if (isAssigned) {
                target.removeNode(dupNode);
            }
            if (this.isStatic) {
                target.removeNode(newNode);
            }
            else {
                target.replaceNode(newNode, new VarInsnNode(25, 0));
            }
            final InsnList insns = new InsnList();
            if (withArgs) {
                this.pushArgs(target.arguments, insns, target.getArgIndices(), 0, target.arguments.length);
                target.addToStack(Bytecode.getArgsSize(target.arguments));
            }
            this.invokeHandler(insns);
            if (isAssigned) {
                final LabelNode nullCheckSucceeded = new LabelNode();
                insns.add(new InsnNode(89));
                insns.add(new JumpInsnNode(199, nullCheckSucceeded));
                this.throwException(insns, "java/lang/NullPointerException", String.format("%s constructor handler %s returned null for %s", this.annotationType, this, newNode.desc.replace('/', '.')));
                insns.add(nullCheckSucceeded);
                target.addToStack(1);
            }
            else {
                insns.add(new InsnNode(87));
            }
            target.replaceNode(initNode, insns);
            final ConstructorRedirectData constructorRedirectData = meta;
            ++constructorRedirectData.injected;
            return;
        }
        if (!meta.wildcard) {
            throw new InvalidInjectionException(this.info, String.format("%s ctor invocation was not found in %s", this.annotationType, target));
        }
    }
    
    private static String getGetArrayHandlerDescriptor(final AbstractInsnNode varNode, final Type returnType, final Type fieldType) {
        if (varNode != null && varNode.getOpcode() == 190) {
            return Bytecode.generateDescriptor(Type.INT_TYPE, (Object[])getArrayArgs(fieldType, 0, new Type[0]));
        }
        return Bytecode.generateDescriptor(returnType, (Object[])getArrayArgs(fieldType, 1, new Type[0]));
    }
    
    private static Type[] getArrayArgs(final Type fieldType, final int extraDimensions, final Type... extra) {
        final int dimensions = fieldType.getDimensions() + extraDimensions;
        final Type[] args = new Type[dimensions + extra.length];
        for (int i = 0; i < args.length; ++i) {
            args[i] = ((i == 0) ? fieldType : ((i < dimensions) ? Type.INT_TYPE : extra[dimensions - i]));
        }
        return args;
    }
    
    class Meta
    {
        public static final String KEY = "redirector";
        final int priority;
        final boolean isFinal;
        final String name;
        final String desc;
        
        public Meta(final int priority, final boolean isFinal, final String name, final String desc) {
            this.priority = priority;
            this.isFinal = isFinal;
            this.name = name;
            this.desc = desc;
        }
        
        RedirectInjector getOwner() {
            return RedirectInjector.this;
        }
    }
    
    static class ConstructorRedirectData
    {
        public static final String KEY = "ctor";
        public boolean wildcard;
        public int injected;
        
        ConstructorRedirectData() {
            this.wildcard = false;
            this.injected = 0;
        }
    }
    
    static class RedirectedInvoke
    {
        final Target target;
        final MethodInsnNode node;
        final Type returnType;
        final Type[] args;
        final Type[] locals;
        boolean captureTargetArgs;
        
        RedirectedInvoke(final Target target, final MethodInsnNode node) {
            this.captureTargetArgs = false;
            this.target = target;
            this.node = node;
            this.returnType = Type.getReturnType(node.desc);
            this.args = Type.getArgumentTypes(node.desc);
            this.locals = (Type[])((node.getOpcode() == 184) ? this.args : ObjectArrays.concat((Object)Type.getType("L" + node.owner + ";"), (Object[])this.args));
        }
    }
}
