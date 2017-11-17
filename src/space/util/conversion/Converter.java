package space.util.conversion;

import space.util.baseobjectOld.BaseObject;
import space.util.baseobjectOld.Copyable;
import space.util.string.toStringHelper.ToStringHelper;
import space.util.string.toStringHelper.ToStringHelper.ToStringHelperObjectsInstance;

/**
 * A {@link Converter} converts something FROM to something TO.
 *
 * @param <FROM> from type
 * @param <TO>   to type
 * @see Converter#convertNew(Object)
 * @see Converter#convertInstance(Object, Object)
 */
public interface Converter<FROM, TO> {
	
	/**
	 * converts FROM to TO, making a new Instance of FROM
	 *
	 * @param from the object to be converted FROM
	 * @return the new Object TO
	 */
	TO convertNew(FROM from);
	
	/**
	 * converts FROM to LTO, using the supplied instance and returning it.
	 *
	 * @param from the object to be converted FROM
	 * @param ret  the supplied instance
	 * @return the supplied instance TO
	 */
	<LTO extends TO> LTO convertInstance(FROM from, LTO ret);
	
	static <TYPE> Converter<TYPE, TYPE> identity() {
		return new Converter<TYPE, TYPE>() {
			@Override
			public TYPE convertNew(TYPE type) {
				return Copyable.copy(type);
			}
			
			@Override
			public <LTO extends TYPE> LTO convertInstance(TYPE type, LTO ret) {
				return null;
			}
		};
	}
	
	default <LTO> Converter<FROM, LTO> andThen(Converter<TO, LTO> next) {
		return new ConverterAndThen<>(this, next);
	}
	
	default <LFROM> Converter<LFROM, TO> before(Converter<LFROM, FROM> before) {
		return new ConverterBefore<>(before, this);
	}
	
	class ConverterAndThen<FROM, MIDDLE, TO> implements Converter<FROM, TO>, BaseObject {
		
		static {
			//noinspection unchecked
			BaseObject.initClass(ConverterAndThen.class, d -> new ConverterAndThen(Copyable.copy(d.th), Copyable.copy(d.next)));
		}
		
		public Converter<FROM, MIDDLE> th;
		public Converter<MIDDLE, TO> next;
		
		public ConverterAndThen(Converter<FROM, MIDDLE> th, Converter<MIDDLE, TO> next) {
			this.th = th;
			this.next = next;
		}
		
		@Override
		public TO convertNew(FROM from) throws UnsupportedOperationException {
			return next.convertNew(th.convertNew(from));
		}
		
		@Override
		public <LTO extends TO> LTO convertInstance(FROM from, LTO ret) {
			return next.convertInstance(th.convertNew(from), ret);
		}
		
		@Override
		public <T> T toTSH(ToStringHelper<T> api) {
			ToStringHelperObjectsInstance<T> tsh = api.createObjectInstance(this);
			tsh.add("th", this.th);
			tsh.add("next", this.next);
			return tsh.build();
		}
		
		@Override
		public String toString() {
			return toString0();
		}
	}
	
	class ConverterBefore<FROM, MIDDLE, TO> implements Converter<FROM, TO>, BaseObject {
		
		static {
			//noinspection unchecked
			BaseObject.initClass(ConverterBefore.class, d -> new ConverterBefore(Copyable.copy(d.before), Copyable.copy(d.th)));
		}
		
		public Converter<FROM, MIDDLE> before;
		public Converter<MIDDLE, TO> th;
		
		public ConverterBefore(Converter<FROM, MIDDLE> before, Converter<MIDDLE, TO> th) {
			this.before = before;
			this.th = th;
		}
		
		@Override
		public TO convertNew(FROM from) throws UnsupportedOperationException {
			return th.convertNew(before.convertNew(from));
		}
		
		@Override
		public <LTO extends TO> LTO convertInstance(FROM from, LTO ret) {
			return th.convertInstance(before.convertNew(from), ret);
		}
		
		@Override
		public <T> T toTSH(ToStringHelper<T> api) {
			ToStringHelperObjectsInstance<T> tsh = api.createObjectInstance(this);
			tsh.add("before", this.before);
			tsh.add("th", this.th);
			return tsh.build();
		}
		
		@Override
		public String toString() {
			return toString0();
		}
	}
}
