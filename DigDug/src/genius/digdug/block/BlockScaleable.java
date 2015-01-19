package genius.digdug.block;

import org.newdawn.slick.Graphics;

public abstract class BlockScaleable extends Block {
	public int scale = 1;
	
	@Override
	public abstract void render(Graphics g, float x, float y);
}