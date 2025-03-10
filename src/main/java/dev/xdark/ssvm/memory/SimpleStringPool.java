package dev.xdark.ssvm.memory;

import dev.xdark.ssvm.VirtualMachine;
import dev.xdark.ssvm.value.InstanceValue;
import dev.xdark.ssvm.value.NullValue;
import dev.xdark.ssvm.value.ObjectValue;
import lombok.val;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Basic string pool implementation.
 *
 * @author xDark
 */
public class SimpleStringPool implements StringPool {

	private final Map<String, ObjectValue> pool = new HashMap<>();
	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	private final VirtualMachine vm;

	/**
	 * @param vm
	 * 		VM instance.
	 */
	public SimpleStringPool(VirtualMachine vm) {
		this.vm = vm;
	}

	@Override
	public ObjectValue intern(String value) {
		if (value == null) {
			return NullValue.INSTANCE;
		}
		val lock = this.lock.writeLock();
		lock.lock();
		try {
			return pool.computeIfAbsent(value, k -> vm.getHelper().newUtf8(value, false));
		} finally {
			lock.unlock();
		}
	}

	@Override
	public InstanceValue intern(InstanceValue value) {
		val lock = this.lock.writeLock();
		lock.lock();
		val pool = this.pool;
		val key = vm.getHelper().readUtf8(value);
		InstanceValue existing = (InstanceValue) pool.putIfAbsent(key, value);
		if (existing == null) existing = value;
		lock.unlock();
		return existing;
	}

	@Override
	public InstanceValue getIfPresent(String str) {
		val lock = this.lock.readLock();
		lock.lock();
		try {
			return (InstanceValue) pool.get(str);
		} finally {
			lock.unlock();
		}
	}
}
