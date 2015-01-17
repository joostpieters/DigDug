package genius.digdug.octopus;

import genius.digdug.Coordinates;

import java.util.Iterator;

public class OctopusDebug {
	public static void showPath(final Coordinates start, final Coordinates target) {
		OctopusDebug.showPath("Octo", start, target);
	}
	
	public static void showPath(final String prefix, final Coordinates start, final Coordinates target) {
		final Octopus octopus = new Octopus();
		final Iterator<Coordinates> itr = octopus.construct(start, target).iterator();
		while (itr.hasNext()) {
			System.out.println(itr.next());
		}
	}
}