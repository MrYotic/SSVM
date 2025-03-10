package dev.xdark.ssvm.mirror;

import dev.xdark.ssvm.VirtualMachine;
import dev.xdark.ssvm.value.InstanceValue;
import dev.xdark.ssvm.value.NullValue;
import dev.xdark.ssvm.value.Value;
import lombok.val;
import org.objectweb.asm.Opcodes;

/**
 * Java class implementation for long, int, double, float, etc.
 *
 * @author xDark
 */
public final class PrimitiveClass implements JavaClass {

	private final VirtualMachine vm;
	private final String name;
	private final String descriptor;
	private final InstanceValue oop;
	private final JavaClass objectClass;
	private ArrayJavaClass arrayClass;

	/**
	 * @param vm
	 * 		VM instance.
	 * @param name
	 * 		Name of the class.
	 * @param descriptor
	 * 		Descriptor of the class.
	 */
	public PrimitiveClass(VirtualMachine vm, String name, String descriptor) {
		this.vm = vm;
		this.name = name;
		this.descriptor = descriptor;
		oop = vm.getMemoryManager().setOopForClass(this);
		objectClass = vm.getSymbols().java_lang_Object;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getInternalName() {
		return name;
	}

	@Override
	public String getDescriptor() {
		return descriptor;
	}

	@Override
	public int getModifiers() {
		return Opcodes.ACC_PUBLIC;
	}

	@Override
	public Value getClassLoader() {
		return NullValue.INSTANCE;
	}

	@Override
	public InstanceValue getOop() {
		return oop;
	}

	@Override
	public FieldLayout getVirtualFieldLayout() {
		return objectClass.getVirtualFieldLayout();
	}

	@Override
	public FieldLayout getStaticFieldLayout() {
		return FieldLayout.EMPTY;
	}

	@Override
	public JavaClass getSuperClass() {
		return objectClass;
	}

	@Override
	public JavaClass[] getInterfaces() {
		return new JavaClass[0];
	}

	@Override
	public ArrayJavaClass newArrayClass() {
		ArrayJavaClass arrayClass = this.arrayClass;
		if (arrayClass == null) {
			val vm = this.vm;
			arrayClass = this.arrayClass = new ArrayJavaClass(vm, '[' + descriptor, 1, this);
			vm.getHelper().setComponentType(arrayClass, this);
		}
		return arrayClass;
	}

	@Override
	public void initialize() {
	}

	@Override
	public boolean isAssignableFrom(JavaClass other) {
		return this == other;
	}

	@Override
	public boolean isPrimitive() {
		return true;
	}

	@Override
	public boolean isArray() {
		return false;
	}

	@Override
	public boolean isInterface() {
		return false;
	}

	@Override
	public JavaClass getComponentType() {
		return null;
	}
}
