package space.engine.event.typehandler;

import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public class TypeBiConsumer<T, U> implements TypeHandlerParallel<BiConsumer<T, U>> {
	
	public T t;
	public U u;
	
	public TypeBiConsumer(T t, U u) {
		this.t = t;
		this.u = u;
	}
	
	@Override
	public void accept(@NotNull BiConsumer<T, U> consumer) {
		consumer.accept(t, u);
	}
}
