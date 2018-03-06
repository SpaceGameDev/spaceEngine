package space.util.indexmap;

import space.util.Empties;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Supplier;

public interface IndexMap<VALUE> {
	
	int[] EMPTYINT = Empties.EMPTYINTARRAY;
	
	//capacity
	boolean isExpandable();
	
	int size();
	
	//access
	default boolean contains(int index) {
		return get(index) != null;
	}
	
	default boolean contains(VALUE index) {
		return indexOf(index) != -1;
	}
	
	default void add(VALUE v) {
		put(size(), v);
	}
	
	VALUE get(int index);
	
	VALUE put(int index, VALUE v);
	
	int indexOf(VALUE v);
	
	VALUE remove(int index);
	
	VALUE[] toArray();
	
	VALUE[] toArray(VALUE[] array);
	
	//addAll
	default void addAll(Collection<VALUE> coll) {
		coll.forEach(this::add);
	}
	
	default void putAll(IndexMap<VALUE> indexMap) {
		indexMap.table().forEach(entry -> put(entry.getIndex(), entry.getValue()));
	}
	
	default void putAllIfAbsent(IndexMap<VALUE> indexMap) {
		indexMap.table().forEach(entry -> putIfAbsent(entry.getIndex(), entry::getValue));
	}
	
	//advanced access
	default VALUE getOrDefault(int index, VALUE def) {
		VALUE v = get(index);
		return v == null ? def : v;
	}
	
	default VALUE putIfAbsent(int index, VALUE v) {
		VALUE c = get(index);
		if (c != null)
			return c;
		
		put(index, c = v);
		return c;
	}
	
	default VALUE putIfAbsent(int index, Supplier<? extends VALUE> v) {
		VALUE c = get(index);
		if (c != null)
			return c;
		
		put(index, c = v.get());
		return c;
	}
	
	default boolean replace(int index, VALUE oldValue, VALUE newValue) {
		if (Objects.equals(get(index), oldValue)) {
			put(index, newValue);
			return true;
		}
		return false;
	}
	
	default boolean replace(int index, VALUE oldValue, Supplier<? extends VALUE> newValue) {
		if (Objects.equals(get(index), oldValue)) {
			put(index, newValue.get());
			return true;
		}
		return false;
	}
	
	default boolean remove(VALUE v) {
		return remove(indexOf(v)) != null;
	}
	
	default boolean remove(int index, VALUE v) {
		VALUE c = get(index);
		if (c == v) {
			remove(index);
			return true;
		}
		return false;
	}
	
	//other
	void clear();
	
	Collection<VALUE> values();
	
	Collection<IndexMapEntry<VALUE>> table();
	
	interface IndexMapEntry<VALUE> {
		
		int getIndex();
		
		VALUE getValue();
		
		void setValue(VALUE v);
	}
}
