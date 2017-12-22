package space.util.delegate.list.listiterator;

import space.util.baseobject.Copyable;
import space.util.string.toStringHelper.ToStringHelper;

import java.util.ListIterator;

/**
 * The {@link UnmodifiableListIterator} makes the {@link ListIterator} unmodifiable.
 */
public class UnmodifiableListIterator<E> extends DelegatingListIterator<E> {
	
	static {
		//noinspection unchecked
		Copyable.manualEntry(UnmodifiableListIterator.class, d -> new UnmodifiableListIterator(Copyable.copy(d.iterator)));
	}
	
	public UnmodifiableListIterator(ListIterator<E> iterator) {
		super(iterator);
	}
	
	@Override
	public void remove() {
		throw new UnsupportedOperationException("Unmodifiable");
	}
	
	@Override
	public void set(E e) {
		throw new UnsupportedOperationException("Unmodifiable");
	}
	
	@Override
	public void add(E e) {
		throw new UnsupportedOperationException("Unmodifiable");
	}
	
	@Override
	public <T> T toTSH(ToStringHelper<T> api) {
		return api.createModifier("unmodifiable", iterator);
	}
}
