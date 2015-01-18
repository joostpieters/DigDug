package genius.queue;

import java.util.ArrayList;

public class Queue<T> {
	private final ArrayList<T>	list	= new ArrayList<T>();
	
	public void push(final T node) {
		this.list.add(node);
	}
	
	public T poll() {
		final T node = this.peek();
		this.list.remove(node);
		return node;
	}
	
	public T peek() {
		return this.list.get(0);
	}
	
	public boolean empty() {
		return this.list.isEmpty();
	}
}