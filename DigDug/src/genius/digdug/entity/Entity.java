package genius.digdug.entity;

import genius.digdug.Coordinates;
import genius.digdug.Facing;
import genius.digdug.block.Block;
import genius.digdug.octopus.Octopus;

import java.util.Iterator;

public class Entity {
	public Coordinates coords = new Coordinates(9, 7);
	public Block binded;
	public Facing facing = Facing.down;
	public boolean isActioning = false;
	public Octopus octopus = new Octopus();
	int octoTries = 0;
	public boolean followingPath = false;
	public Iterator<Coordinates> apth;
	private int polls = 0;
	public boolean canMove = true;
	public boolean dead = false;
	
	public int maxX = 19;
	public int minX = 0;
	public int maxY = 14;
	public int minY = 2;
	
	public boolean move(final Coordinates coords) {
		//System.out.println("attempted move to "+coords);
		final float x = coords.x;
		final float y = coords.y;
		if ((x > this.maxX) || (x < this.minX) || (y > this.maxY) || (y < this.minY)) {
			return false;
		}
		if (!this.canMove) {
			return false;
		}
		this.coords = coords;
		if (this.binded != null) {
			this.binded.updateCoords();
		}
		return true;
	}
	
	public boolean move(final Facing facing) {
		this.facing = facing;
		return this.move(this.coords.facing(facing));
	}
	
	public void moveToTarget(final Coordinates target) {
		try {
			final Octopus octopus = new Octopus();
			if (this.polls <= 0) {
				try {
					this.move(octopus.construct(this.coords, target).get(0));
				} catch (final IndexOutOfBoundsException ioobe) {
					System.err.println("warning: nowhere to go");
				}
			} else {
				this.polls--;
				if ((this.polls % 5) == 0) {
					System.out.println(this.polls + " polls remaining");
				}
				this.move(octopus.constructToDest(this.coords, null).get(0));
			}
		} catch (final NullPointerException npe) {
		}
	}
	
	public boolean canMoveTo(final Coordinates coords) {
		final float x = coords.x;
		final float y = coords.y;
		return !((x > this.maxX) || (x < this.minX) || (y > this.maxY) || (y < this.minY)) || (!this.canMove || this.dead);
	}
	
	public void die() {
	}
}
