package genius.digdug;

import org.newdawn.slick.Graphics;

public abstract class Shader implements FunctionalShader {
	/**
	 * order of rendering
	 */
	public int zindex = 0;
	
	/**
	 * renders at coordinates
	 * 
	 * @param g graphics
	 * @param x
	 * @param y
	 */
	@Override
	public abstract void render(Graphics g, float x, float y);
}
