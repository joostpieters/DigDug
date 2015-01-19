package genius.digdug.block;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class BlockImage extends BlockScaleable {
	public Image img;
	
	public BlockImage(final Image img) {
		this.img = img;
		System.out.println("DEFINED: " + this.coords);
	}
	
	@Override
	public void render(final Graphics g, final float x, final float y) {
		if (this.img == null) {
			return;
		}
		g.drawImage(this.img.getScaledCopy(32 * this.scale, 32 * this.scale), x, y);
	}
	
	@Override
	public void coordsUpdated() {
		this.img = this.binded.facingImages.get(this.binded.facing);
	}
}
