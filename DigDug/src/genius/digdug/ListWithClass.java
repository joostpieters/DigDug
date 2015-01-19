package genius.digdug;

import java.util.List;

public class ListWithClass {
	private boolean yes = false;
	
	private boolean listContainsClass(final List<?> list, final Class<?> clazz) {
		list.stream().forEach(e -> {
			System.out.print(e + " is of " + clazz + "?");
			if (clazz.isInstance(e)) {
				this.yes = true;
			}
			System.out.print(this.yes + "\n");
		});
		return this.yes;
	}
	
	/**
	 * returns true if the specified list contains an instance of clazz
	 * 
	 * @param list a list
	 * @param clazz a class
	 * @return boolean
	 */
	public static boolean containsClass(final List<?> list, final Class<?> clazz) {
		return new ListWithClass().listContainsClass(list, clazz);
	}
}
