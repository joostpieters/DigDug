package genius.digdug;

import java.util.List;

/**
 * @deprecated
 * @see genius.queue.Queue
 * @author dyslabs
 * iterator, but without CocurrentModificationException
 * @param <T>
 */
public class CheatIterator<T> {
	private int index = 0;
	private final List<T> list;
	
	public CheatIterator(final List<T> list) {
		this.list = list;
	}
	
	public void setIndex(final int index) {
		this.index = index;
	}
	
	public boolean hasNext() {
		return this.index < this.list.size();
	}
	
	public T next() {
		return this.list.get(this.index++);
	}
}
