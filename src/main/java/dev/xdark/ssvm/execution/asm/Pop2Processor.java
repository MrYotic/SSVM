package dev.xdark.ssvm.execution.asm;

import dev.xdark.ssvm.execution.ExecutionContext;
import dev.xdark.ssvm.execution.InstructionProcessor;
import dev.xdark.ssvm.execution.Result;
import lombok.val;
import org.objectweb.asm.tree.AbstractInsnNode;

/**
 * Pops two value off the stack.
 *
 * @author xDark
 */
public final class Pop2Processor implements InstructionProcessor<AbstractInsnNode> {

	@Override
	public Result execute(AbstractInsnNode insn, ExecutionContext ctx) {
		val stack = ctx.getStack();
		stack.pop();
		stack.pop();
		return Result.CONTINUE;
	}
}
