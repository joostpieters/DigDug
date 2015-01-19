package genius.digdug.entity;

import genius.digdug.Coordinates;
import genius.digdug.Facing;
import genius.digdug.block.Block;

import java.util.HashMap;

import org.newdawn.slick.Image;

public class EntityInfo {
	private final Class<EntityMonster> clazz;
	private final Block block;
	private final Coordinates spawn;
	private final HashMap<Facing, Image> imgs;
	
	public EntityInfo(final Class<EntityMonster> class1, final Block b, final Coordinates spawn, final HashMap<Facing, Image> imgs) {
		this.clazz = class1;
		this.block = b;
		this.spawn = spawn;
		this.imgs = imgs;
	}
	
	public Class<EntityMonster> entity() {
		return this.clazz;
	}
	
	public Block block() {
		return this.block;
	}
	
	public Coordinates spawn() {
		return this.spawn;
	}
	
	public HashMap<Facing, Image> imgs() {
		return this.imgs;
	}
}
