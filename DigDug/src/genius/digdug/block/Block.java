package genius.digdug.block;

import genius.digdug.Coordinates;
import genius.digdug.Map;
import genius.digdug.Shader;
import genius.digdug.entity.Entity;

public abstract class Block extends Shader {
	public Entity binded;
	public Coordinates coords;
	
	public Block(final Coordinates coords) {
		this.coords = coords;
	}
	
	public Block(final int zindex) {
		this.zindex = zindex;
	}
	
	public Block() {
	}
	
	public final void updateCoords() {
		Map.getBlocks(this.coords).remove(this);
		this.coords = this.binded.coords;
		Map.add(this.coords, this);
		this.coordsUpdated();
	}
	
	public void coordsUpdated() {}
	
	public void update() {
	}
	
	public void blockAdded(final Block b) {
	}
	
	public void entityWalkedOn(final Entity entity) {
	}
	
	@Override
	public final String toString() {
		return "[" + this.getClass().toString() + "] at " + this.coords;
	}
}
