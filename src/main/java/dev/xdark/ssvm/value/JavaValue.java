package dev.xdark.ssvm.value;

import dev.xdark.ssvm.memory.Memory;

/**
 * Java value wrapper.
 *
 * @param <V>
 * 		Type of Java object.
 *
 * @author xDark
 */
public final class JavaValue<V> extends InstanceValue {

	private final V value;

	/**
	 * @param memory
	 * 		Object data.
	 * @param value
	 * 		Java value.
	 */
	public JavaValue(Memory memory, V value) {
		super(memory);
		this.value = value;
	}

	/**
	 * Returns Java value.
	 *
	 * @return Java value.
	 */
	public V getValue() {
		return value;
	}
}
