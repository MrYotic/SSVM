package dev.xdark.ssvm.execution.asm;

import dev.xdark.ssvm.execution.ExecutionContext;
import dev.xdark.ssvm.execution.InstructionProcessor;
import dev.xdark.ssvm.execution.Result;
import dev.xdark.ssvm.value.ObjectValue;
import org.objectweb.asm.tree.AbstractInsnNode;

/**
 * Releases object monitor.
 *
 * @author xDark
 */
public final class MonitorExitProcessor implements InstructionProcessor<AbstractInsnNode> {

	@Override
	public Result execute(AbstractInsnNode insn, ExecutionContext ctx) {
		ctx.getStack().<ObjectValue>pop().monitorExit();
		return Result.CONTINUE;
	}
}
