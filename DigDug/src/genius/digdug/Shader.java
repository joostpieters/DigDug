package genius.digdug;

import org.newdawn.slick.Graphics;

public abstract class Shader {
	/**
	 * order of rendering
	 */
	public int zindex = 0;
	
	/**
	 * renders this shader
	 * 
	 * @param g graphics
	 */
	public void render(final Graphics g) {
		this.render(g, -1, -1);
	}
	
	/**
	 * renders at coordinates
	 * 
	 * @param g graphics
	 * @param x
	 * @param y
	 */
	public abstract void render(Graphics g, float x, float y);
}
