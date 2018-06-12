package space.util.buffer.pointer;

import space.util.buffer.Allocator;
import space.util.buffer.direct.DirectBuffer;
import space.util.primitive.Primitive;

import static space.util.primitive.Primitives.INT32;

//single
public class PointerBufferInt extends AbstractPointerBuffer<PointerBufferInt> {
	
	public static final Primitive<?> TYPE = INT32;
	
	public static PointerAllocator<PointerBufferInt> createAlloc(Allocator<DirectBuffer> alloc) {
		return new PointerAllocator<>(alloc, TYPE, PointerBufferInt::new);
	}
	
	public PointerBufferInt(DirectBuffer buffer) {
		super(buffer, TYPE);
	}
	
	//get / put
	public byte getByte() {
		return buffer.getByte(0);
	}
	
	public void putByte(byte b) {
		buffer.putByte(0, b);
	}
}
