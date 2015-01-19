package genius.digdug.block;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class BlockImage extends BlockScaleable {
	public Image img;
	
	public BlockImage(final Image img) {
		this.img = img;
	}
	
	@Override
	public void render(final Graphics g, final float x, final float y) {
		g.drawImage(this.img.getScaledCopy(32 * this.scale, 32 * this.scale), x, y);
	}
}
