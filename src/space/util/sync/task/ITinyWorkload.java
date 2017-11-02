package space.util.sync.task;

/**
 * classes "marked" with {@link ITinyWorkload} will be executed directly instead of being submitted to an executor.
 * This is useful if the AbstractTask is very tiny so it is faster to execute directly than having the overhead required to execute it in separate threads.
 */
public interface ITinyWorkload {
	
}
