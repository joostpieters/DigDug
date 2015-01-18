package genius.queue;

import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeMap;

public class PriotoryQueue<T> {
	private final HashMap<T, Integer>	map			= new HashMap<T, Integer>();
	private TreeMap<T, Integer>			sortedMap;
	private Comparator<?>				comparator	= new Comparator<T>() {
														@Override
														public int compare(final T o1, final T o2) {
															final int i1 = PriotoryQueue.this.map.get(o1);
															final int i2 = PriotoryQueue.this.map.get(o2);
															return (i1 > i2) ? i2 : i1;
														}
													};
	
	public void setComparator(final Comparator<T> comparator) {
		this.comparator = comparator;
	}
	
	@SuppressWarnings("unchecked")
	private void sort() {
		this.sortedMap = new TreeMap<T, Integer>((Comparator<? super T>) this.comparator);
		this.sortedMap.putAll(this.map);
	}
	
	public void push(final T node, final int priotory) {
		this.map.put(node, priotory);
	}
	
	public T poll() {
		final T node = this.peek();
		this.map.remove(node);
		return node;
	}
	
	public T peek() {
		this.sort();
		return this.sortedMap.firstKey();
	}
	
	public boolean empty() {
		return this.map.isEmpty();
	}
}
