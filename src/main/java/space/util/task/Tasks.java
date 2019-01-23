package space.util.task;

import space.util.sync.barrier.Barrier;
import space.util.sync.barrier.BarrierImpl;
import space.util.sync.lock.SyncLock;
import space.util.task.impl.FutureTask;
import space.util.task.impl.MultiTask;
import space.util.task.impl.RunnableTask;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

import static space.util.ArrayUtils.mergeIfNeeded;
import static space.util.Global.GLOBAL_EXECUTOR;
import static space.util.sync.barrier.Barrier.EMPTY_BARRIER_ARRAY;
import static space.util.sync.lock.SyncLock.EMPTY_SYNCLOCK_ARRAY;

public class Tasks {
	
	private Tasks() {
	}
	
	//Runnable
	public static TaskCreator<? extends RunnableTask> runnable(Runnable run) {
		return runnable(GLOBAL_EXECUTOR, EMPTY_SYNCLOCK_ARRAY, EMPTY_BARRIER_ARRAY, run);
	}
	
	public static TaskCreator<? extends RunnableTask> runnable(Executor exec, Runnable run) {
		return runnable(exec, EMPTY_SYNCLOCK_ARRAY, EMPTY_BARRIER_ARRAY, run);
	}
	
	public static TaskCreator<? extends RunnableTask> runnable(Barrier[] staticBarriers, Runnable run) {
		return runnable(GLOBAL_EXECUTOR, EMPTY_SYNCLOCK_ARRAY, staticBarriers, run);
	}
	
	public static TaskCreator<? extends RunnableTask> runnable(Executor exec, Barrier[] staticBarriers, Runnable run) {
		return runnable(exec, EMPTY_SYNCLOCK_ARRAY, staticBarriers, run);
	}
	
	public static TaskCreator<? extends RunnableTask> runnable(SyncLock[] staticLocks, Barrier[] staticBarriers, Runnable run) {
		return runnable(GLOBAL_EXECUTOR, staticLocks, staticBarriers, run);
	}
	
	public static TaskCreator<? extends RunnableTask> runnable(Executor exec, SyncLock[] staticLocks, Barrier[] staticBarriers, Runnable run) {
		return (locks, barriers) -> new RunnableTask(mergeIfNeeded(SyncLock[]::new, staticLocks, locks), mergeIfNeeded(Barrier[]::new, staticBarriers, barriers)) {
			@Override
			protected synchronized void submit1(Runnable toRun) {
				exec.execute(toRun);
			}
			
			@Override
			protected void execute() {
				run.run();
			}
		};
	}
	
	//Future
	public static <R> TaskCreator<? extends FutureTask<R>> future(Supplier<R> run) {
		return future(GLOBAL_EXECUTOR, EMPTY_SYNCLOCK_ARRAY, EMPTY_BARRIER_ARRAY, run);
	}
	
	public static <R> TaskCreator<? extends FutureTask<R>> future(Executor exec, Supplier<R> run) {
		return future(exec, EMPTY_SYNCLOCK_ARRAY, EMPTY_BARRIER_ARRAY, run);
	}
	
	public static <R> TaskCreator<? extends FutureTask<R>> future(Barrier[] staticBarriers, Supplier<R> run) {
		return future(GLOBAL_EXECUTOR, EMPTY_SYNCLOCK_ARRAY, staticBarriers, run);
	}
	
	public static <R> TaskCreator<? extends FutureTask<R>> future(Executor exec, Barrier[] staticBarriers, Supplier<R> run) {
		return future(exec, EMPTY_SYNCLOCK_ARRAY, staticBarriers, run);
	}
	
	public static <R> TaskCreator<? extends FutureTask<R>> future(SyncLock[] staticLocks, Barrier[] staticBarriers, Supplier<R> run) {
		return future(GLOBAL_EXECUTOR, staticLocks, staticBarriers, run);
	}
	
	public static <R> TaskCreator<? extends FutureTask<R>> future(Executor exec, SyncLock[] staticLocks, Barrier[] staticBarriers, Supplier<R> run) {
		return (locks, barriers) -> new FutureTask<>(mergeIfNeeded(SyncLock[]::new, staticLocks, locks), mergeIfNeeded(Barrier[]::new, staticBarriers, barriers)) {
			@Override
			protected synchronized void submit1(Runnable toRun) {
				exec.execute(toRun);
			}
			
			@Override
			protected R execute0() {
				return run.get();
			}
		};
	}
	
	//MultiTask sequential
	public static TaskCreator<? extends MultiTask> sequential(Collection<? extends TaskCreator> tasks) {
		return sequential(EMPTY_SYNCLOCK_ARRAY, EMPTY_BARRIER_ARRAY, tasks);
	}
	
	public static TaskCreator<? extends MultiTask> sequential(Barrier[] staticBarriers, Collection<? extends TaskCreator> tasks) {
		return sequential(EMPTY_SYNCLOCK_ARRAY, staticBarriers, tasks);
	}
	
	public static TaskCreator<? extends MultiTask> sequential(SyncLock[] staticLocks, Barrier[] staticBarriers, Collection<? extends TaskCreator> tasks) {
		return (locks, barriers) -> new MultiTask(mergeIfNeeded(SyncLock[]::new, staticLocks, locks), mergeIfNeeded(Barrier[]::new, staticBarriers, barriers)) {
			@Override
			protected Barrier setup(Barrier start) {
				Iterator<? extends TaskCreator> iter = tasks.iterator();
				if (!iter.hasNext()) {
					return start;
				}
				
				Barrier current = iter.next().submit(start);
				while (iter.hasNext()) {
					current = iter.next().submit(current);
				}
				return current;
			}
		};
	}
	
	//MultiTask parallel
	public static TaskCreator<? extends MultiTask> parallel(Collection<? extends TaskCreator> tasks) {
		return parallel(EMPTY_SYNCLOCK_ARRAY, EMPTY_BARRIER_ARRAY, tasks);
	}
	
	public static TaskCreator<? extends MultiTask> parallel(Barrier[] staticBarriers, Collection<? extends TaskCreator> tasks) {
		return parallel(EMPTY_SYNCLOCK_ARRAY, staticBarriers, tasks);
	}
	
	public static TaskCreator<? extends MultiTask> parallel(SyncLock[] staticLocks, Barrier[] staticBarriers, Collection<? extends TaskCreator> tasks) {
		return (locks, barriers) -> new MultiTask(mergeIfNeeded(SyncLock[]::new, staticLocks, locks), mergeIfNeeded(Barrier[]::new, staticBarriers, barriers)) {
			@Override
			protected Barrier setup(Barrier start) {
				if (tasks.size() == 0) {
					return start;
				}
				
				//custom handler for fever object allocation
				BarrierImpl end = new BarrierImpl();
				//noinspection SuspiciousToArrayCall
				start.addHook(() -> Barrier.awaitAll(end::triggerNow, tasks.stream().map(TaskCreator::submit).toArray(Barrier[]::new)));
				return end;
			}
		};
	}
}
