package genius.digdug.entity;

import java.util.HashMap;

import org.newdawn.slick.Image;

import genius.digdug.Coordinates;
import genius.digdug.Facing;
import genius.digdug.block.Block;

public class EntityInfo {
	private Class<EntityMonster> clazz;
	private Block block;
	private Coordinates spawn;
	private HashMap<Facing,Image> imgs;
	public EntityInfo(Class<EntityMonster> class1,Block b,Coordinates spawn,HashMap<Facing,Image> imgs) {
		this.clazz=class1;
		this.block=b;
		this.spawn=spawn;
		this.imgs=imgs;
	}
	
	public Class<EntityMonster> entity() {
		return clazz;
	}
	
	public Block block() {
		return block;
	}
	
	public Coordinates spawn() {
		return spawn;
	}
	
	public HashMap<Facing,Image> imgs() {
		return imgs;
	}
}
