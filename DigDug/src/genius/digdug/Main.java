package genius.digdug;

import org.newdawn.slick.AppGameContainer;

public class Main {
	public static void main(final String[] args) {
		System.out.println(System.getProperty("java.version"));
		try {
			final AppGameContainer agc = new AppGameContainer(new DigDug());
			agc.setDisplayMode(640, 480, false);
			agc.setVSync(true);
			agc.start();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
