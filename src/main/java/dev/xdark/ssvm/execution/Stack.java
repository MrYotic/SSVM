package dev.xdark.ssvm.execution;

import dev.xdark.ssvm.value.TopValue;
import dev.xdark.ssvm.value.Value;
import lombok.val;

import java.util.Objects;

/**
 * Method execution stack.
 *
 * @author xDark
 */
public final class Stack {

	private final Value[] stack;
	private int cursor;

	/**
	 * @param maxSize
	 * 		The maximum size of the stack.
	 */
	public Stack(int maxSize) {
		stack = new Value[maxSize];
	}

	/**
	 * Pushes value onto the stack.
	 *
	 * @param value
	 * 		Value to push.
	 */
	public void push(Value value) {
		stack[cursor++] = Objects.requireNonNull(value, "value");
	}

	/**
	 * Pushes wide value onto the stack.
	 * Inserts TOP after.
	 *
	 * @param value
	 * 		Value to push.
	 */
	public void pushWide(Value value) {
		val stack = this.stack;
		int cursor = this.cursor;
		stack[cursor++] = Objects.requireNonNull(value, "value");
		stack[cursor++] = TopValue.INSTANCE;
		this.cursor = cursor;
	}

	/**
	 * Pushes generic value onto the stack.
	 * If the value is wide, TOP will also be pushed.
	 *
	 * @param value
	 * 		Value to push.
	 */
	public void pushGeneric(Value value) {
		if (value.isWide()) {
			pushWide(value);
		} else {
			push(value);
		}
	}

	/**
	 * Pops value off the stack.
	 *
	 * @param <V>
	 * 		Type of the value.
	 *
	 * @return value popped off the stack.
	 */
	public <V extends Value> V pop() {
		return (V) stack[--cursor];
	}

	/**
	 * Pops wide value off the stack.
	 *
	 * @param <V>
	 * 		Type of the value.
	 *
	 * @return wide value popped off the stack.
	 *
	 * @throws IllegalStateException
	 * 		If wide value does not occupy two slots.
	 */
	public <V extends Value> V popWide() {
		Value top = pop();
		if (top != TopValue.INSTANCE) {
			throw new IllegalStateException("Expected to pop TOP value, but got: " + top);
		}
		return pop();
	}

	/**
	 * Pops generic value off the stack.
	 *
	 * @param <V>
	 * 		Type of the value.
	 *
	 * @return generic value popped off the stack.
	 */
	public <V extends Value> V popGeneric() {
		val top = pop();
		if (top == TopValue.INSTANCE) {
			return pop();
		}
		return (V) top;
	}

	/**
	 * Peeks value from the stack.
	 *
	 * @param <V>
	 * 		Type of the value.
	 *
	 * @return value peeked from the stack.
	 */
	public <V extends Value> V peek() {
		return (V) stack[cursor - 1];
	}

	/**
	 * Duplicates value on the stack.
	 */
	public void dup() {
		push(peek());
	}

	/**
	 * Duplicate the top operand stack value and insert two values down.
	 */
	public void dupx1() {
		val v1 = pop();
		val v2 = pop();
		push(v1);
		push(v2);
		push(v1);
	}

	/**
	 * Duplicate the top operand stack value
	 * and insert two or three values down.
	 */
	public void dupx2() {
		val v1 = pop();
		val v2 = popGeneric();
		if (v2.isWide()) {
			push(v1);
			pushWide(v2);
			push(v1);
		} else {
			val v3 = pop();
			push(v1);
			push(v3);
			push(v2);
			push(v1);
		}
	}

	/**
	 * Duplicate the top one or two operand stack values.
	 */
	public void dup2() {
		val v = popGeneric();
		if (v.isWide()) {
			pushWide(v);
			pushWide(v);
		} else {
			val v2 = pop();
			push(v2);
			push(v);
			push(v2);
			push(v);
		}
	}

	/**
	 * Duplicate the top one or two operand stack values
	 * and insert two or three values down.
	 */
	public void dup2x1() {
		val v = popGeneric();
		if (v.isWide()) {
			val v2 = pop();
			pushWide(v);
			push(v2);
			pushWide(v);
		} else {
			val v2 = pop();
			val v3 = pop();
			push(v2);
			push(v);
			push(v3);
			push(v2);
			push(v);
		}
	}

	/**
	 * Duplicate the top one or two operand stack values
	 * and insert two, three, or four values down.
	 */
	public void dup2x2() {
		val v1 = popGeneric();
		val v2 = popGeneric();
		if (v1.isWide()) {
			if (v2.isWide()) {
				pushWide(v1);
				pushWide(v2);
				pushWide(v1);
			} else {
				val v3 = pop();
				pushWide(v1);
				push(v3);
				push(v2);
				pushWide(v1);
			}
		} else {
			val v3 = popGeneric();
			//noinspection IfStatementWithIdenticalBranches
			if (v3.isWide()) {
				push(v2);
				push(v1);
				pushWide(v3);
				push(v2);
				push(v1);
			} else {
				val v4 = pop();
				push(v2);
				push(v1);
				push(v4);
				push(v3);
				push(v2);
				push(v1);
			}
		}
	}

	/**
	 * Swap the top two operand stack values.
	 */
	public void swap() {
		val stack = this.stack;
		val cursor = this.cursor;
		val v1 = stack[cursor - 1];
		val v2 = stack[cursor - 2];
		stack[cursor - 1] = v2;
		stack[cursor - 2] = v1;
	}

	/**
	 * Returns whether the stack is empty.
	 *
	 * @return {@code true} if stack is empty,
	 * {@code false} otherwise.
	 */
	public boolean isEmpty() {
		return cursor == 0;
	}

	/**
	 * Resets stack.
	 */
	public void clear() {
		cursor = 0;
	}
}
