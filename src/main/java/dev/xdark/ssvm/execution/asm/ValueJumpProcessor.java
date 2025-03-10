package dev.xdark.ssvm.execution.asm;

import dev.xdark.ssvm.execution.ExecutionContext;
import dev.xdark.ssvm.execution.InstructionProcessor;
import dev.xdark.ssvm.execution.Result;
import dev.xdark.ssvm.util.AsmUtil;
import dev.xdark.ssvm.util.BiIntPredicate;
import dev.xdark.ssvm.value.Value;
import org.objectweb.asm.tree.JumpInsnNode;

import java.util.function.Predicate;

/**
 * Jumps if predicate on an object succeeds.
 *
 * @author xDark
 */
public final class ValueJumpProcessor implements InstructionProcessor<JumpInsnNode> {

	private final Predicate<Value> condition;

	/**
	 * @param condition
	 * 		Predicate to check.
	 */
	public ValueJumpProcessor(Predicate<Value> condition) {
		this.condition = condition;
	}

	@Override
	public Result execute(JumpInsnNode insn, ExecutionContext ctx) {
		if (condition.test(ctx.getStack().pop())) {
			ctx.setInsnPosition(AsmUtil.getIndex(insn.label));
		}
		return Result.CONTINUE;
	}
}
