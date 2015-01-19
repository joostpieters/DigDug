package genius.digdug.entity;

import genius.digdug.Coordinates;
import genius.digdug.DigDug;
import genius.digdug.Facing;
import genius.digdug.Map;
import genius.digdug.block.BlockRailGun;
import genius.digdug.block.BlockScaleable;

import java.util.HashMap;

import org.newdawn.slick.Image;

public class EntityMonster extends Entity {
	public EntityMonster() {
		DigDug.monsters.add(this);
		System.out.println("init monster");
	}
	
	public EntityMonster(final HashMap<Facing, Image> imgs) {
		this();
		this.facingImages = imgs;
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
		final Coordinates old = this.coords;
		super.moveToTarget(target);
		System.out.println("guessed facing from " + old + " to " + this.coords + " = " + old.guessFacing(this.coords));
	}
	
	@Override
	public void die() {
		System.out.println("dead");
		DigDug.monsters.remove(this);
		Map.getBlocks(this.coords).remove(this.binded);
		this.canMove = false;
		this.dead = true;
	}
}
