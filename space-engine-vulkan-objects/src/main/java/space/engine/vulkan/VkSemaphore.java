package space.engine.vulkan;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.vulkan.VkSemaphoreCreateInfo;
import space.engine.buffer.Allocator;
import space.engine.buffer.AllocatorStack.AllocatorFrame;
import space.engine.buffer.pointer.PointerBufferPointer;
import space.engine.freeableStorage.Freeable;
import space.engine.freeableStorage.Freeable.FreeableWrapper;
import space.engine.freeableStorage.FreeableStorage;
import space.engine.sync.barrier.Barrier;

import java.util.function.BiFunction;

import static org.lwjgl.vulkan.VK10.*;
import static space.engine.freeableStorage.Freeable.addIfNotContained;
import static space.engine.lwjgl.LwjglStructAllocator.mallocStruct;
import static space.engine.vulkan.VkException.assertVk;

public class VkSemaphore implements FreeableWrapper {
	
	public static final VkSemaphore[] EMPTY_SEMAPHORE_ARRAY = new VkSemaphore[0];
	
	//alloc
	public static @NotNull VkSemaphore alloc(@NotNull VkDevice device, @NotNull Object[] parents) {
		try (AllocatorFrame frame = Allocator.frame()) {
			return alloc(mallocStruct(frame, VkSemaphoreCreateInfo::create, VkSemaphoreCreateInfo.SIZEOF).set(
					VK_STRUCTURE_TYPE_SEMAPHORE_CREATE_INFO,
					0,
					0
			), device, parents);
		}
	}
	
	public static @NotNull VkSemaphore alloc(VkSemaphoreCreateInfo info, @NotNull VkDevice device, @NotNull Object[] parents) {
		try (AllocatorFrame frame = Allocator.frame()) {
			PointerBufferPointer ptr = PointerBufferPointer.malloc(frame);
			assertVk(nvkCreateSemaphore(device, info.address(), 0, ptr.address()));
			return create(ptr.getPointer(), device, parents);
		}
	}
	
	//create
	public static @NotNull VkSemaphore create(long address, @NotNull VkDevice device, @NotNull Object[] parents) {
		return new VkSemaphore(address, device, Storage::new, parents);
	}
	
	public static @NotNull VkSemaphore wrap(long address, @NotNull VkDevice device, @NotNull Object[] parents) {
		return new VkSemaphore(address, device, Freeable::createDummy, parents);
	}
	
	//const
	public VkSemaphore(long address, @NotNull VkDevice device, @NotNull BiFunction<VkSemaphore, Object[], Freeable> storageCreator, @NotNull Object[] parents) {
		this.address = address;
		this.device = device;
		this.storage = storageCreator.apply(this, addIfNotContained(parents, device));
	}
	
	//parents
	private final @NotNull VkDevice device;
	
	public VkDevice device() {
		return device;
	}
	
	public VkInstance instance() {
		return device.instance();
	}
	
	//address
	private final long address;
	
	public long address() {
		return address;
	}
	
	//storage
	private final @NotNull Freeable storage;
	
	@Override
	public @NotNull Freeable getStorage() {
		return storage;
	}
	
	public static class Storage extends FreeableStorage {
		
		private final @NotNull VkDevice device;
		private final long address;
		
		public Storage(@NotNull VkSemaphore o, @NotNull Object[] parents) {
			super(o, parents);
			this.device = o.device;
			this.address = o.address;
		}
		
		@Override
		protected @NotNull Barrier handleFree() {
			vkDestroySemaphore(device, address, null);
			return Barrier.ALWAYS_TRIGGERED_BARRIER;
		}
	}
}
