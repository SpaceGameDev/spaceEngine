package space.util.lock.simplelock;

import space.util.lock.keylock.BlockingKeyLockImpl;

/**
 * a simple wrapper class from {@link BlockingLock} to {@link BlockingKeyLockImpl} with 'Thread' as the Key
 */
public class BlockingThreadLock extends AbstractBlockingLock<Thread> {
	
	public BlockingThreadLock() {
		super(Thread::currentThread);
	}
	
	public BlockingThreadLock(BlockingKeyLockImpl<? super Thread> lock) {
		super(Thread::currentThread, lock);
	}
}
