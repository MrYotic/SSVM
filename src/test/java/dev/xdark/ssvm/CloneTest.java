package dev.xdark.ssvm;

import dev.xdark.ssvm.value.ArrayValue;
import dev.xdark.ssvm.value.Value;
import lombok.val;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class CloneTest {

	private static VirtualMachine vm;

	@BeforeAll
	private static void setup() {
		vm = new VirtualMachine();
	}

	@Test
	public void testCloneObject() {
		val helper = vm.getHelper();
		val bytes = "Hello, World".getBytes(StandardCharsets.UTF_8);
		val array = helper.toVMBytes(bytes);
		val copy = (ArrayValue) helper.invokeVirtual("clone", "()Ljava/lang/Object;", new Value[0], new Value[]{array}).getResult();
		assertArrayEquals(bytes, helper.toJavaBytes(copy));
	}
}
