package genius.digdug;

import java.util.List;

/*
* does not throw CocurrentModificationException on modifying elements while iterating
@Deceperated use Queue
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
