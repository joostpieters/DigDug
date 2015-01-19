package genius.digdug.entity;

import genius.digdug.Coordinates;
import genius.digdug.DigDug;
import genius.digdug.Map;
import genius.digdug.block.BlockRailGun;
import genius.digdug.block.BlockScaleable;

public class EntityMonster extends Entity {
	public EntityMonster() {
		DigDug.monstesrs.add(this);
	}
	
	@Override
	public void moveToTarget(final Coordinates target) {
		if (Map.getBaseBlock(this.coords) instanceof BlockRailGun) {
			this.canMove = false;
		} else {
			this.canMove = true;
		}
		final BlockScaleable img = (BlockScaleable) this.binded;
		if (!this.canMove) {
			img.zindex = Integer.MIN_VALUE;
			img.scale *= 2;
		} else {
			img.scale = 1;
		}
		if (img.scale > 10) {
			this.die();
		}
		super.moveToTarget(target);
	}
	
	@Override
	public void die() {
		System.out.println("dead");
		DigDug.monstesrs.remove(this);
		Map.getBlocks(this.coords).remove(this.binded);
		this.canMove = false;
		this.dead = true;
	}
}
