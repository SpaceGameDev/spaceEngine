package space.util.keygen.attribute;

import space.util.baseobject.Copyable;
import space.util.baseobject.ToString;
import space.util.concurrent.event.IEvent;
import space.util.concurrent.event.SimpleEvent;
import space.util.delegate.iterator.Iteratorable;
import space.util.indexmap.IndexMap;
import space.util.indexmap.IndexMap.IndexMapEntry;
import space.util.indexmap.IndexMapArrayWithDefault;
import space.util.keygen.IKey;
import space.util.keygen.IKeyGenerator;
import space.util.keygen.IllegalKeyException;
import space.util.keygen.impl.DisposableKeyGenerator;
import space.util.string.toStringHelper.ToStringHelper;
import space.util.string.toStringHelper.ToStringHelper.ToStringHelperObjectsInstance;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class AttributeListCreator implements IAttributeListCreator, ToString {
	
	static {
		Copyable.manualEntry(AttributeList.class, AttributeList::copy);
		Copyable.manualEntry(AttributeListModification.class, AttributeListModification::copy);
	}
	
	public final IKeyGenerator gen;
	
	public AttributeListCreator() {
		this(new DisposableKeyGenerator(false));
	}
	
	public AttributeListCreator(IKeyGenerator gen) {
		this.gen = gen;
	}
	
	public void check(IKey<?> key) {
		if (!gen.isKeyOf(key))
			throw new IllegalKeyException(key);
	}
	
	public static <V> V correctDefault(V v, IKey<V> key) {
		return v != DEFAULT ? v : key.getDefaultValue();
	}
	
	//delegate to gen
	@Override
	public <T> IKey<T> generateKey() {
		return gen.generateKey();
	}
	
	@Override
	public <T> IKey<T> generateKey(T def) {
		return gen.generateKey(def);
	}
	
	@Override
	public <T> IKey<T> generateKey(Supplier<T> def) {
		return gen.generateKey(def);
	}
	
	@Override
	public IKey<?> getKey(int id) {
		return gen.getKey(id);
	}
	
	@Override
	public boolean isKeyOf(IKey<?> key) {
		return gen.isKeyOf(key);
	}
	
	//create
	@Override
	public AttributeList create() {
		return new AttributeList();
	}
	
	@Override
	public AttributeListModification createModify() {
		return new AttributeListModification();
	}
	
	//toString
	@Override
	public <T> T toTSH(ToStringHelper<T> api) {
		ToStringHelperObjectsInstance<T> tsh = api.createObjectInstance(this);
		tsh.add("gen", this.gen);
		return tsh.build();
	}
	
	@Override
	public String toString() {
		return toString0();
	}
	
	public abstract class AbstractAttributeList implements IAbstractAttributeList, ToString {
		
		public IndexMapArrayWithDefault<Object> indexMap;
		
		protected AbstractAttributeList(Object defaultObject) {
			this(gen instanceof DisposableKeyGenerator ? new IndexMapArrayWithDefault<>(((DisposableKeyGenerator) gen).counter, defaultObject) : new IndexMapArrayWithDefault<>(defaultObject));
		}
		
		private AbstractAttributeList(IndexMapArrayWithDefault<Object> indexMap) {
			this.indexMap = indexMap;
		}
		
		//get
		@Override
		@SuppressWarnings("unchecked")
		public <V> Object getDirect(IKey<V> key) {
			check(key);
			return indexMap.get(key.getID());
		}
		
		//other
		@Override
		public int size() {
			return indexMap.size();
		}
		
		@Override
		public IAttributeListCreator getCreator() {
			return AttributeListCreator.this;
		}
		
		@Override
		public Iteratorable<Object> iterator() {
			return indexMap.iterator();
		}
		
		//toString
		@Override
		public <T> T toTSH(ToStringHelper<T> api) {
			ToStringHelperObjectsInstance<T> tsh = api.createObjectInstance(this);
			tsh.add("indexMap", indexMap);
			tsh.add("creator", AttributeListCreator.this);
			return tsh.build();
		}
		
		@Override
		public String toString() {
			return toString0();
		}
		
		protected abstract class AbstractEntryIteratorImpl<T> implements Iteratorable<T> {
			
			protected Iteratorable<IndexMapEntry<Object>> iter = indexMap.tableIterator();
			
			@Override
			public boolean hasNext() {
				return iter.hasNext();
			}
			
			protected IKey<?> nextKey() {
				return gen.getKey(iter.next().getIndex());
			}
		}
		
		protected abstract class AbstractEntryImpl<V> implements AbstractEntry<V> {
			
			protected IKey<V> key;
			
			public AbstractEntryImpl(IKey<V> key) {
				this.key = key;
			}
			
			@Override
			public IKey<V> getKey() {
				return key;
			}
			
			@Override
			public Object getValueDirect() {
				return AbstractAttributeList.this.getDirect(key);
			}
		}
	}
	
	public class AttributeList extends AbstractAttributeList implements IAttributeList {
		
		public SimpleEvent<Consumer<IAttributeListChangeEvent>> changeEvent = new SimpleEvent<>();
		
		public AttributeList() {
			super(DEFAULT);
		}
		
		private AttributeList(IndexMapArrayWithDefault<Object> indexMap) {
			super(indexMap);
		}
		
		private AttributeList copy() {
			return new AttributeList(new IndexMapArrayWithDefault<>(indexMap.toArray(), DEFAULT));
		}
		
		//get
		@Override
		@SuppressWarnings("unchecked")
		public <V> V get(IKey<V> key) {
			check(key);
			return correctDefault((V) indexMap.get(key.getID()), key);
		}
		
		@Override
		@SuppressWarnings("unchecked")
		public <V> V getOrDefault(IKey<V> key, V def) {
			check(key);
			Object o = indexMap.get(key.getID());
			return o == DEFAULT ? def : (V) o;
		}
		
		//other
		@Override
		public IEvent<Consumer<IAttributeListChangeEvent>> getChangeEvent() {
			return changeEvent;
		}
		
		@Override
		public synchronized void apply(IAttributeListModification mod2) {
			//same check
			AttributeListModification mod = AttributeListCreator.this.createModify();
			mod2.tableIterator().forEach(entry -> {
				IKey<?> key = entry.getKey();
				Object value = entry.getValueDirect();
				mod.putDirect(key, Objects.equals(value, this.getDirect(key)) ? UNCHANGED : value);
			});
			
			//trigger events
			IAttributeListChangeEvent chEvent = new AttributeListChangeEvent(this, mod);
			changeEvent.run(attributeListChangeEventConsumer -> attributeListChangeEventConsumer.accept(chEvent));
			
			//apply values
			mod.tableIterator().forEach(entry -> {
				Object value = entry.getValueDirect();
				if (value != UNCHANGED)
					this.indexMap.put(entry.getKey().getID(), value);
			});
		}
		
		@Override
		public Iteratorable<? extends EntryList<?>> tableIterator() {
			return new AbstractEntryIteratorImpl<EntryList<?>>() {
				@Override
				public EntryList<?> next() {
					return new EntryListImpl<>(nextKey());
				}
			};
		}
		
		protected class EntryListImpl<V> extends AbstractEntryImpl<V> implements EntryList<V> {
			
			public EntryListImpl(IKey<V> key) {
				super(key);
			}
			
			@Override
			public V getValue() {
				return AttributeList.this.get(key);
			}
		}
	}
	
	public class AttributeListModification extends AbstractAttributeList implements IAttributeListModification {
		
		public AttributeListModification() {
			super(UNCHANGED);
		}
		
		private AttributeListModification(IndexMap<Object> indexMap) {
			super(indexMap);
		}
		
		private AttributeListModification copy() {
			return new AttributeListModification(new IndexMapArrayWithDefault<>(indexMap.toArray(), UNCHANGED));
		}
		
		//put
		@Override
		@SuppressWarnings("unchecked")
		public <V> void put(IKey<V> key, V v) {
			check(key);
			indexMap.put(key.getID(), v);
		}
		
		@Override
		public <V> void putDirect(IKey<V> key, Object v) {
			check(key);
			indexMap.put(key.getID(), v);
		}
		
		//set to UNCHANGED
		@Override
		@SuppressWarnings("unchecked")
		public <V> void reset(IKey<V> key) {
			check(key);
			indexMap.put(key.getID(), UNCHANGED);
		}
		
		@Override
		@SuppressWarnings("unchecked")
		public <V> boolean reset(IKey<V> key, V v) {
			check(key);
			return indexMap.replace(key.getID(), v, UNCHANGED);
		}
		
		//set to DEFAULT
		@Override
		public <V> void setDefault(IKey<V> key) {
			check(key);
			indexMap.put(key.getID(), DEFAULT);
		}
		
		@Override
		@SuppressWarnings("unchecked")
		public <V> boolean setDefault(IKey<V> key, V v) {
			check(key);
			return indexMap.replace(key.getID(), v, DEFAULT);
		}
		
		//putAndGet
		@Override
		@SuppressWarnings("unchecked")
		public <V> V putAndGet(IKey<V> key, V v) {
			check(key);
			return correctDefault((V) indexMap.put(key.getID(), v), key);
		}
		
		//replace
		@Override
		@SuppressWarnings("unchecked")
		public <V> boolean replace(IKey<V> key, V oldValue, V newValue) {
			check(key);
			return indexMap.replace(key.getID(), oldValue, newValue);
		}
		
		@Override
		@SuppressWarnings("unchecked")
		public <V> boolean replace(IKey<V> key, V oldValue, Supplier<? extends V> newValue) {
			check(key);
			return indexMap.replace(key.getID(), oldValue, newValue);
		}
		
		//clear
		@Override
		public void clear() {
			indexMap.clear();
		}
		
		@Override
		public Iteratorable<? extends EntryListModification<?>> tableIterator() {
			return new AbstractEntryIteratorImpl<EntryListModification<?>>() {
				@Override
				public EntryListModification<?> next() {
					return new EntryListModificationImpl<>(nextKey());
				}
			};
		}
		
		protected class EntryListModificationImpl<V> extends AbstractEntryImpl<V> implements EntryListModification<V> {
			
			public EntryListModificationImpl(IKey<V> key) {
				super(key);
			}
			
			@Override
			public void put(V v) {
				AttributeListModification.this.put(key, v);
			}
			
			@Override
			public void putDirect(Object v) {
				AttributeListModification.this.putDirect(key, v);
			}
			
			@Override
			public void setDefault() {
				AttributeListModification.this.setDefault(key);
			}
			
			@Override
			public void reset() {
				AttributeListModification.this.reset(key);
			}
		}
	}
	
	class AttributeListChangeEvent implements IAttributeListChangeEvent {
		
		public final IAttributeList oldList;
		public final IAttributeListModification mod;
		
		public AttributeListChangeEvent(IAttributeList oldList, IAttributeListModification mod) {
			this.oldList = oldList;
			this.mod = mod;
		}
		
		@Override
		public IAttributeList getOldList() {
			return oldList;
		}
		
		@Override
		public IAttributeListModification getMod() {
			return mod;
		}
		
		@Override
		public <V> IAttributeListChangeEventEntry<V> getEntry(IKey<V> key) {
			return new IAttributeListChangeEventEntry<V>() {
				
				//get
				@Override
				public IKey<V> getKey() {
					return key;
				}
				
				@Override
				public Object getOldDirect() {
					return oldList.getDirect(key);
				}
				
				@Override
				public V getOld() {
					return oldList.get(key);
				}
				
				@Override
				public Object getMod() {
					return mod.getDirect(key);
				}
				
				@Override
				public Object getNewDirect() {
					Object v = mod.getDirect(key);
					return v == UNCHANGED ? oldList.getDirect(key) : v;
				}
				
				@Override
				public V getNew() {
					Object v = mod.getDirect(key);
					if (v == UNCHANGED)
						return oldList.get(key);
					if (v == DEFAULT)
						return key.getDefaultValue();
					//noinspection unchecked
					return (V) v;
				}
				
				//set
				@Override
				public void setMod(Object newmod) {
					mod.putDirect(key, newmod);
				}
			};
		}
	}
}
