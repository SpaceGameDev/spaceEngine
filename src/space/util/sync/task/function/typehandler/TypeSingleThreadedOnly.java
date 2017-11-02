package space.util.sync.task.function.typehandler;

public class TypeSingleThreadedOnly<FUNCTION> implements ITypeHandler<FUNCTION> {
	
	public ITypeHandler<FUNCTION> handler;
	
	public TypeSingleThreadedOnly(ITypeHandler<FUNCTION> handler) {
		this.handler = handler;
	}
	
	@Override
	public void accept(FUNCTION function) {
		handler.accept(function);
	}
	
	@Override
	public boolean allowMultithreading() {
		return false;
	}
}
