package genius.digdug.block;

import genius.digdug.Coordinates;
import genius.digdug.DigDug;
import genius.digdug.Facing;
import genius.digdug.Map;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class BlockRailGun extends Block {
	public BlockRailGun(final Coordinates coords) {
		super(coords);
		if (Map.getBlocks(coords).isEmpty()) {
			DigDug.railguns.add(this);
		}
	}
	
	@Override
	public void render(final Graphics g, final float x, final float y) {
		if ((DigDug.player.facing == Facing.left) || (DigDug.player.facing == Facing.right)) {
			g.setColor(Color.white);
			g.fillRect(x, y + (32 / 2), 32, 10);
		} else {
			g.setColor(Color.white);
			g.fillRect(x + (32 / 2), y, 10, 32);
		}
	}
}
