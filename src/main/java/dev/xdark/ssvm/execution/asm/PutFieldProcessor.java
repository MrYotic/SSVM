package dev.xdark.ssvm.execution.asm;

import dev.xdark.ssvm.execution.ExecutionContext;
import dev.xdark.ssvm.execution.InstructionProcessor;
import dev.xdark.ssvm.execution.Result;
import dev.xdark.ssvm.mirror.InstanceJavaClass;
import dev.xdark.ssvm.value.*;
import lombok.val;
import org.objectweb.asm.tree.FieldInsnNode;

/**
 * Stores object field value.
 *
 * @author xDark
 */
public final class PutFieldProcessor implements InstructionProcessor<FieldInsnNode> {

	@Override
	public Result execute(FieldInsnNode insn, ExecutionContext ctx) {
		val vm = ctx.getVM();
		val helper = vm.getHelper();
		val owner = helper.findClass(ctx.getOwner().getClassLoader(), insn.owner, true);
		if (owner == null) {
			helper.throwException(vm.getSymbols().java_lang_NoClassDefFoundError, insn.owner);
		}
		val stack = ctx.getStack();
		val value = stack.popGeneric();
		val $instance = stack.pop();
		helper.checkNotNull($instance);
		val instance = (InstanceValue) $instance;
		long offset = helper.getFieldOffset((InstanceJavaClass) owner, instance.getJavaClass(), insn.name, insn.desc);
		if (offset == -1L) {
			helper.throwException(vm.getSymbols().java_lang_NoSuchFieldError, insn.name);
		}
		offset += vm.getMemoryManager().valueBaseOffset(instance);
		val manager = vm.getMemoryManager();
		switch (insn.desc) {
			case "J":
				manager.writeLong(instance, offset, value.asLong());
				break;
			case "D":
				manager.writeDouble(instance, offset, value.asDouble());
				break;
			case "I":
				manager.writeInt(instance, offset, value.asInt());
				break;
			case "F":
				manager.writeFloat(instance, offset, value.asFloat());
				break;
			case "C":
				manager.writeChar(instance, offset, value.asChar());
				break;
			case "S":
				manager.writeShort(instance, offset, value.asShort());
				break;
			case "B":
				manager.writeByte(instance, offset, value.asByte());
				break;
			case "Z":
				manager.writeBoolean(instance, offset, value.asBoolean());
				break;
			default:
				manager.writeValue(instance, offset, (ObjectValue) value);
				break;
		}
		return Result.CONTINUE;
	}
}
