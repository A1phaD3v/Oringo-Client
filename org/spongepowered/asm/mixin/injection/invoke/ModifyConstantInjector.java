// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.injection.invoke;

import org.spongepowered.asm.mixin.injection.code.Injector;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.lib.tree.LocalVariableNode;
import org.spongepowered.asm.util.Locals;
import org.spongepowered.asm.util.SignaturePrinter;
import org.spongepowered.asm.lib.tree.FieldInsnNode;
import org.spongepowered.asm.mixin.injection.invoke.util.InsnFinder;
import org.spongepowered.asm.lib.tree.VarInsnNode;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.lib.tree.InsnNode;
import org.spongepowered.asm.lib.tree.InsnList;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionException;
import org.spongepowered.asm.util.Bytecode;
import org.spongepowered.asm.lib.tree.JumpInsnNode;
import org.spongepowered.asm.mixin.injection.struct.InjectionNodes;
import org.spongepowered.asm.mixin.injection.struct.Target;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;

public class ModifyConstantInjector extends RedirectInjector
{
    private static final int OPCODE_OFFSET = 6;
    
    public ModifyConstantInjector(final InjectionInfo info) {
        super(info, "@ModifyConstant");
    }
    
    @Override
    protected void inject(final Target target, final InjectionNodes.InjectionNode node) {
        if (!this.preInject(node)) {
            return;
        }
        if (node.isReplaced()) {
            throw new UnsupportedOperationException("Target failure for " + this.info);
        }
        final AbstractInsnNode targetNode = node.getCurrentTarget();
        if (targetNode instanceof JumpInsnNode) {
            this.checkTargetModifiers(target, false);
            this.injectExpandedConstantModifier(target, (JumpInsnNode)targetNode);
            return;
        }
        if (Bytecode.isConstant(targetNode)) {
            this.checkTargetModifiers(target, false);
            this.injectConstantModifier(target, targetNode);
            return;
        }
        throw new InvalidInjectionException(this.info, this.annotationType + " annotation is targetting an invalid insn in " + target + " in " + this);
    }
    
    private void injectExpandedConstantModifier(final Target target, final JumpInsnNode jumpNode) {
        final int opcode = jumpNode.getOpcode();
        if (opcode < 155 || opcode > 158) {
            throw new InvalidInjectionException(this.info, this.annotationType + " annotation selected an invalid opcode " + Bytecode.getOpcodeName(opcode) + " in " + target + " in " + this);
        }
        final InsnList insns = new InsnList();
        insns.add(new InsnNode(3));
        final AbstractInsnNode invoke = this.invokeConstantHandler(Type.getType("I"), target, insns, insns);
        insns.add(new JumpInsnNode(opcode + 6, jumpNode.label));
        target.replaceNode(jumpNode, invoke, insns);
        target.addToStack(1);
    }
    
    private void injectConstantModifier(final Target target, final AbstractInsnNode constNode) {
        final Type constantType = Bytecode.getConstantType(constNode);
        if (constantType.getSort() <= 5 && this.info.getContext().getOption(MixinEnvironment.Option.DEBUG_VERBOSE)) {
            this.checkNarrowing(target, constNode, constantType);
        }
        final InsnList before = new InsnList();
        final InsnList after = new InsnList();
        final AbstractInsnNode invoke = this.invokeConstantHandler(constantType, target, before, after);
        target.wrapNode(constNode, invoke, before, after);
    }
    
    private AbstractInsnNode invokeConstantHandler(final Type constantType, final Target target, final InsnList before, final InsnList after) {
        final String handlerDesc = Bytecode.generateDescriptor(constantType, constantType);
        final boolean withArgs = this.checkDescriptor(handlerDesc, target, "getter");
        if (!this.isStatic) {
            before.insert(new VarInsnNode(25, 0));
            target.addToStack(1);
        }
        if (withArgs) {
            this.pushArgs(target.arguments, after, target.getArgIndices(), 0, target.arguments.length);
            target.addToStack(Bytecode.getArgsSize(target.arguments));
        }
        return this.invokeHandler(after);
    }
    
    private void checkNarrowing(final Target target, final AbstractInsnNode constNode, final Type constantType) {
        final AbstractInsnNode pop = new InsnFinder().findPopInsn(target, constNode);
        if (pop == null) {
            return;
        }
        if (pop instanceof FieldInsnNode) {
            final FieldInsnNode fieldNode = (FieldInsnNode)pop;
            final Type fieldType = Type.getType(fieldNode.desc);
            this.checkNarrowing(target, constNode, constantType, fieldType, target.indexOf(pop), String.format("%s %s %s.%s", Bytecode.getOpcodeName(pop), SignaturePrinter.getTypeName(fieldType, false), fieldNode.owner.replace('/', '.'), fieldNode.name));
        }
        else if (pop.getOpcode() == 172) {
            this.checkNarrowing(target, constNode, constantType, target.returnType, target.indexOf(pop), "RETURN " + SignaturePrinter.getTypeName(target.returnType, false));
        }
        else if (pop.getOpcode() == 54) {
            final int var = ((VarInsnNode)pop).var;
            final LocalVariableNode localVar = Locals.getLocalVariableAt(target.classNode, target.method, pop, var);
            if (localVar != null && localVar.desc != null) {
                final String name = (localVar.name != null) ? localVar.name : "unnamed";
                final Type localType = Type.getType(localVar.desc);
                this.checkNarrowing(target, constNode, constantType, localType, target.indexOf(pop), String.format("ISTORE[var=%d] %s %s", var, SignaturePrinter.getTypeName(localType, false), name));
            }
        }
    }
    
    private void checkNarrowing(final Target target, final AbstractInsnNode constNode, final Type constantType, final Type type, final int index, final String description) {
        final int fromSort = constantType.getSort();
        final int toSort = type.getSort();
        if (toSort < fromSort) {
            final String fromType = SignaturePrinter.getTypeName(constantType, false);
            final String toType = SignaturePrinter.getTypeName(type, false);
            final String message = (toSort == 1) ? ". Implicit conversion to <boolean> can cause nondeterministic (JVM-specific) behaviour!" : "";
            final Level level = (toSort == 1) ? Level.ERROR : Level.WARN;
            Injector.logger.log(level, "Narrowing conversion of <{}> to <{}> in {} target {} at opcode {} ({}){}", new Object[] { fromType, toType, this.info, target, index, description, message });
        }
    }
}
