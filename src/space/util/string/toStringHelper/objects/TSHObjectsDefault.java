package space.util.string.toStringHelper.objects;

import space.util.delegate.iterator.Iteratorable;
import space.util.string.StringUtil;
import space.util.string.builder.CharBufferBuilder1D;

import java.util.ListIterator;

public class TSHObjectsDefault extends AbstractTSHObjects {
	
	public static final TSHObjectsDefault INSTANCE = new TSHObjectsDefault();
	
	@Override
	public TSHObjectsInstance getInstance(Object obj) {
		return new AbstractTSHObjectsInstance() {
			@Override
			public String toString() {
				CharBufferBuilder1D b = new CharBufferBuilder1D();
				ListIterator<Entry> iter = list.listIterator();
				for (Entry s : Iteratorable.toIteratorable(iter)) {
					b.append(s.name + ": " + s.value);
					if (iter.previousIndex() < list.size())
						b.append(StringUtil.nextLine);
				}
				return b.toString();
			}
		};
	}
}
