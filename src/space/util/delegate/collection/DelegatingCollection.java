package space.util.delegate.collection;

import space.util.baseobject.Copyable;
import space.util.baseobject.ToString;
import space.util.string.toStringHelper.ToStringHelper;

import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * A {@link Collection} delegating all calls to it's Field {@link DelegatingCollection#coll}, provided by Constructor or set directly.
 */
public class DelegatingCollection<E> implements ToString, Collection<E> {
	
	static {
		//noinspection unchecked
		Copyable.manualEntry(DelegatingCollection.class, d -> new DelegatingCollection<>(Copyable.copy(d.coll)));
	}
	
	public Collection<E> coll;
	
	public DelegatingCollection(Collection<E> coll) {
		this.coll = coll;
	}
	
	//delegate
	@Override
	public int size() {
		return coll.size();
	}
	
	@Override
	public boolean isEmpty() {
		return coll.isEmpty();
	}
	
	@Override
	public boolean contains(Object o) {
		return coll.contains(o);
	}
	
	@Override
	public Iterator<E> iterator() {
		return coll.iterator();
	}
	
	@Override
	public Object[] toArray() {
		return coll.toArray();
	}
	
	@Override
	@SuppressWarnings("SuspiciousToArrayCall")
	public <T> T[] toArray(T[] a) {
		return coll.toArray(a);
	}
	
	@Override
	public boolean add(E e) {
		return coll.add(e);
	}
	
	@Override
	public boolean remove(Object o) {
		return coll.remove(o);
	}
	
	@Override
	public boolean containsAll(Collection<?> c) {
		return coll.containsAll(c);
	}
	
	@Override
	public boolean addAll(Collection<? extends E> c) {
		return coll.addAll(c);
	}
	
	@Override
	public boolean removeAll(Collection<?> c) {
		return coll.removeAll(c);
	}
	
	@Override
	public boolean retainAll(Collection<?> c) {
		return coll.retainAll(c);
	}
	
	@Override
	public void clear() {
		coll.clear();
	}
	
	@Override
	public Spliterator<E> spliterator() {
		return coll.spliterator();
	}
	
	@Override
	public boolean removeIf(Predicate<? super E> filter) {
		return coll.removeIf(filter);
	}
	
	@Override
	public Stream<E> stream() {
		return coll.stream();
	}
	
	@Override
	public Stream<E> parallelStream() {
		return coll.parallelStream();
	}
	
	@Override
	public void forEach(Consumer<? super E> action) {
		coll.forEach(action);
	}
	
	//Object
	@Override
	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	public boolean equals(Object o) {
		return coll.equals(o);
	}
	
	@Override
	public int hashCode() {
		return coll.hashCode();
	}
	
	//super
	protected boolean removeIfSuper(Predicate<? super E> filter) {
		return Collection.super.removeIf(filter);
	}
	
	protected Spliterator<E> spliteratorSuper() {
		return Collection.super.spliterator();
	}
	
	protected Stream<E> streamSuper() {
		return Collection.super.stream();
	}
	
	protected Stream<E> parallelStreamSuper() {
		return Collection.super.parallelStream();
	}
	
	protected void forEachSuper(Consumer<? super E> action) {
		Collection.super.forEach(action);
	}
	
	@Override
	public <T> T toTSH(ToStringHelper<T> api) {
		return api.createModifier("delegate", coll);
	}
	
	@Override
	public String toString() {
		return toString0();
	}
}
