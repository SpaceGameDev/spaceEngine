package space.util.delegate.collection;

import space.util.baseobject.BaseObject;
import space.util.baseobject.Copyable;
import space.util.delegate.iterator.UnmodifiableIterator;
import space.util.string.toStringHelper.ToStringHelperCollection;
import space.util.string.toStringHelper.ToStringHelperInstance;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Predicate;

public class UnmodifiableCollection<E> extends DelegatingCollection<E> {
	
	static {
		//noinspection unchecked
		BaseObject.initClass(UnmodifiableCollection.class, d -> new UnmodifiableCollection(Copyable.copy(d.coll)));
	}
	
	public UnmodifiableCollection(Collection<E> coll) {
		super(coll);
	}
	
	@Override
	public Iterator<E> iterator() {
		return new UnmodifiableIterator<>(coll.iterator());
	}
	
	@Override
	public boolean add(E e) {
		throw new UnsupportedOperationException("Unmodifiable");
	}
	
	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException("Unmodifiable");
	}
	
	@Override
	public boolean addAll(Collection<? extends E> c) {
		throw new UnsupportedOperationException("Unmodifiable");
	}
	
	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException("Unmodifiable");
	}
	
	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException("Unmodifiable");
	}
	
	@Override
	public void clear() {
		throw new UnsupportedOperationException("Unmodifiable");
	}
	
	@Override
	public boolean removeIf(Predicate<? super E> filter) {
		throw new UnsupportedOperationException("Unmodifiable");
	}
	
	@Override
	public ToStringHelperInstance toTSH(ToStringHelperCollection api) {
		return api.getModifier().getInstance("unmodifiable", coll);
	}
}
