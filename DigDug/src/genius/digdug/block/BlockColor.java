package genius.digdug.block;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class BlockColor extends BlockScaleable {
	public Color color;
	
	public BlockColor(final Color color) {
		this(color, 0);
	}
	
	public BlockColor(final Color color, final int zindex) {
		this.color = color;
		this.zindex = zindex;
	}
	
	@Override
	public void render(final Graphics g, final float x, final float y) {
		g.setColor(this.color);
		g.fillRect(x, y, 32 * this.scale, 32 * this.scale);
	}
}
