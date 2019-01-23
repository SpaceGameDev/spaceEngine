package space.util.event.typehandler;

import org.jetbrains.annotations.NotNull;

import java.util.function.UnaryOperator;

public class TypeUnaryOperator<T> implements TypeHandler<UnaryOperator<T>> {
	
	public T obj;
	
	public TypeUnaryOperator(T obj) {
		this.obj = obj;
	}
	
	@Override
	public void accept(@NotNull UnaryOperator<T> unary) {
		obj = unary.apply(obj);
	}
}
