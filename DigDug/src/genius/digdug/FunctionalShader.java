package genius.digdug;

import org.newdawn.slick.Graphics;

@FunctionalInterface
public interface FunctionalShader {
	public void render(Graphics g, float x, float y);
}
