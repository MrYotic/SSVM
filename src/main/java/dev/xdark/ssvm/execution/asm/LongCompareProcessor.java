package dev.xdark.ssvm.execution.asm;

import dev.xdark.ssvm.execution.ExecutionContext;
import dev.xdark.ssvm.execution.InstructionProcessor;
import dev.xdark.ssvm.execution.Result;
import dev.xdark.ssvm.value.IntValue;
import lombok.val;
import org.objectweb.asm.tree.AbstractInsnNode;

/**
 * Compares two long values.
 *
 * @author xDark
 * @see Long#compare(long, long)
 */
public final class LongCompareProcessor implements InstructionProcessor<AbstractInsnNode> {

	@Override
	public Result execute(AbstractInsnNode insn, ExecutionContext ctx) {
		val stack = ctx.getStack();
		val v2 = stack.popWide().asLong();
		val v1 = stack.popWide().asLong();
		stack.push(new IntValue(Long.compare(v1, v2)));
		return Result.CONTINUE;
	}
}
