package space.util.delegate.list.listiterator;

import space.util.baseobject.BaseObject;
import space.util.baseobject.Copyable;
import space.util.string.toStringHelper.ToStringHelperCollection;
import space.util.string.toStringHelper.ToStringHelperInstance;

import java.util.ListIterator;
import java.util.function.Consumer;

public class DelegatingListIterator<E> implements BaseObject, ListIterator<E> {
	
	static {
		//noinspection unchecked
		BaseObject.initClass(DelegatingListIterator.class, d -> new DelegatingListIterator(Copyable.copy(d.iterator)));
	}
	
	public ListIterator<E> iterator;
	
	public DelegatingListIterator(ListIterator<E> iterator) {
		this.iterator = iterator;
	}
	
	//methods
	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}
	
	@Override
	public E next() {
		return iterator.next();
	}
	
	@Override
	public boolean hasPrevious() {
		return iterator.hasPrevious();
	}
	
	@Override
	public E previous() {
		return iterator.previous();
	}
	
	@Override
	public int nextIndex() {
		return iterator.nextIndex();
	}
	
	@Override
	public int previousIndex() {
		return iterator.previousIndex();
	}
	
	@Override
	public void remove() {
		iterator.remove();
	}
	
	@Override
	public void set(E e) {
		iterator.set(e);
	}
	
	@Override
	public void add(E e) {
		iterator.add(e);
	}
	
	@Override
	public void forEachRemaining(Consumer<? super E> action) {
		iterator.forEachRemaining(action);
	}
	
	@Override
	public int hashCode() {
		return iterator.hashCode();
	}
	
	@Override
	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	public boolean equals(Object obj) {
		return iterator.equals(obj);
	}
	
	//super
	protected void superforEachRemaining(Consumer<? super E> action) {
		ListIterator.super.forEachRemaining(action);
	}
	
	@Override
	public ToStringHelperInstance toTSH(ToStringHelperCollection api) {
		return api.getModifier().getInstance("delegate", iterator);
	}
	
	@Override
	public String toString() {
		return toString0();
	}
}
