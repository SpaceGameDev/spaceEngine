package space.util.delegate.indexmap;

import space.util.baseobject.Cache;
import space.util.baseobject.ToString;
import space.util.delegate.map.CachingMap;
import space.util.delegate.util.CacheUtil;
import space.util.indexmap.IndexMap;
import space.util.string.toStringHelper.ToStringHelper;
import space.util.string.toStringHelper.ToStringHelper.ToStringHelperObjectsInstance;

import java.util.Collection;
import java.util.function.IntFunction;
import java.util.function.Supplier;

/**
 * The {@link CachingMap} tries to get a value from the {@link CachingMap#map}, and when no value has been found, it will get the value from the {@link CachingMap#def}, write it into the local map and return it;
 * <p>
 * {@link CachingMap} is threadsafe, if the internal {@link CachingMap#map} is threadsafe.
 */
public class CachingIndexMap<VALUE> extends ConvertingIndexMap.BiDirectional<VALUE, VALUE> implements Cache, ToString {
	
	public IntFunction<VALUE> def;
	
	public CachingIndexMap(IndexMap<VALUE> indexMap, IntFunction<VALUE> def) {
		super(indexMap, CacheUtil::fromCache, CacheUtil::toCache);
		this.def = def;
	}
	
	@Override
	public boolean contains(VALUE v) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public int indexOf(VALUE v) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean replace(int index, VALUE oldValue, VALUE newValue) {
		boolean[] chg = new boolean[1];
		super.putIfAbsent(index, () -> {
			chg[0] = true;
			VALUE defValue = def.apply(index);
			return defValue == oldValue ? newValue : defValue;
		});
		return chg[0] || super.replace(index, oldValue, newValue);
	}
	
	@Override
	public boolean replace(int index, VALUE oldValue, Supplier<? extends VALUE> newValue) {
		boolean[] chg = new boolean[1];
		super.putIfAbsent(index, () -> {
			chg[0] = true;
			VALUE defValue = def.apply(index);
			return defValue == oldValue ? newValue.get() : defValue;
		});
		return chg[0] || super.replace(index, oldValue, newValue);
	}
	
	@Override
	public boolean remove(VALUE v) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean remove(int index, VALUE v) {
		boolean[] chg = new boolean[1];
		super.putIfAbsent(index, () -> {
			chg[0] = true;
			VALUE defValue = def.apply(index);
			return defValue == v ? null : defValue;
		});
		return chg[0] || super.remove(index, v);
	}
	
	@Override
	public Collection<VALUE> values() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean contains(int index) {
		return super.putIfAbsent(index, () -> this.def.apply(index)) != null;
	}
	
	@Override
	public VALUE get(int index) {
		return super.putIfAbsent(index, () -> this.def.apply(index));
	}
	
	@Override
	public VALUE[] toArray() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public VALUE[] toArray(VALUE[] a) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public VALUE getOrDefault(int index, VALUE def) {
		VALUE value = super.putIfAbsent(index, () -> this.def.apply(index));
		return value == null ? def : value;
	}
	
	@Override
	public <TSHTYPE> TSHTYPE toTSH(ToStringHelper<TSHTYPE> api) {
		ToStringHelperObjectsInstance<TSHTYPE> tsh = api.createObjectInstance(this);
		tsh.add("indexMap", this.indexMap);
		tsh.add("def", this.def);
		return tsh.build();
	}
	
	@Override
	public IndexMapEntry<VALUE> getEntry(int index) {
		IndexMapEntry<VALUE> entry = super.getEntry(index);
		entry.setIfAbsent(() -> this.def.apply(index));
		return entry;
	}
	
	@Override
	public void putAllIfAbsent(IndexMap<VALUE> indexMap) {
		indexMap.table().forEach(entry -> super.putIfAbsent(entry.getIndex(), () -> {
			VALUE defValue = def.apply(entry.getIndex());
			return defValue != null ? defValue : entry.getValue();
		}));
	}
	
	@Override
	public VALUE putIfAbsent(int index, VALUE v) {
		return super.putIfAbsent(index, () -> {
			VALUE defValue = def.apply(index);
			return defValue != null ? defValue : v;
		});
	}
	
	@Override
	public VALUE putIfAbsent(int index, Supplier<? extends VALUE> v) {
		return super.putIfAbsent(index, () -> {
			VALUE defValue = def.apply(index);
			return defValue != null ? defValue : v.get();
		});
	}
	
	@Override
	public Collection<IndexMapEntry<VALUE>> table() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String toString() {
		return toString0();
	}
	
	@Override
	public void clearCache() {
		indexMap.clear();
	}
}
