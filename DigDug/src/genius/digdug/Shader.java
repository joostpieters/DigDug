package genius.digdug;

import org.newdawn.slick.Graphics;

public abstract class Shader {
	public int zindex = 0;
	
	public void render(final Graphics g) {
		this.render(g, -1, -1);
	}
	
	public abstract void render(Graphics g, float x, float y);
}
