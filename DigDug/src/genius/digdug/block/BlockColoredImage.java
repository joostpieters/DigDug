package genius.digdug.block;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class BlockColoredImage extends BlockImage {
	public Color color;
	
	public BlockColoredImage(final Image img, final Color color) {
		super(img);
		this.color = color;
	}
	
	@Override
	public void render(final Graphics g, final float x, final float y) {
		g.drawImage(this.img.getScaledCopy(32 * this.scale, 32 * this.scale), x, y, this.color);
	}
}
