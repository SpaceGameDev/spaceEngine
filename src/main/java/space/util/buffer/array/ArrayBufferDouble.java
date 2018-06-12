package space.util.buffer.array;

import space.util.buffer.Allocator;
import space.util.buffer.direct.DirectBuffer;
import space.util.primitive.Primitive;

import static space.util.primitive.Primitives.FP64;

public class ArrayBufferDouble extends AbstractArrayBuffer<ArrayBufferDouble> {
	
	public static final Primitive<?> TYPE = FP64;
	
	public static ArrayAllocator<ArrayBufferDouble> createAlloc(Allocator<DirectBuffer> alloc) {
		return new ArrayAllocator<>(alloc, TYPE, ArrayBufferDouble::new);
	}
	
	public ArrayBufferDouble(DirectBuffer buffer) {
		super(buffer, TYPE);
	}
	
	protected ArrayBufferDouble(DirectBuffer buffer, long length) {
		super(buffer, TYPE, length);
	}
	
	//get / put
	public byte getByte(long index) {
		return buffer.getByte(getOffset(index));
	}
	
	public void putByte(long index, byte b) {
		buffer.putByte(getOffset(index), b);
	}
	
	//array
	public void copyInto(byte[] dest) {
		buffer.copyInto(dest);
	}
	
	public void copyInto(long index, byte[] dest) {
		buffer.copyInto(getOffset(index), dest);
	}
	
	public void copyInto(long index, byte[] dest, int destPos, int length) {
		buffer.copyInto(getOffset(index), dest, destPos, length);
	}
	
	public void copyFrom(byte[] src) {
		buffer.copyFrom(src);
	}
	
	public void copyFrom(byte[] src, long index) {
		buffer.copyFrom(src, getOffset(index));
	}
	
	public void copyFrom(byte[] src, int srcPos, int length, long index) {
		buffer.copyFrom(src, srcPos, length, getOffset(index));
	}
}