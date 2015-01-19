package genius.digdug;

public abstract class Task {
	public int poll = 0;
	public int maxPolls = 0;
	public boolean noDelete = false;
	public boolean canRun = true;
	public boolean repressWarnings = false;
	
	public abstract void run(Object... vars);
	
	public void run() {
		this.poll();
	}
	
	public final boolean poll(final Object... vars) {
		if (!this.canRun) {
			return false;
		}
		
		this.poll++;
		if (this.poll >= this.maxPolls) {
			this.poll = 0;
			this.run(vars);
			return !this.noDelete;
		}
		return false;
	}
}
