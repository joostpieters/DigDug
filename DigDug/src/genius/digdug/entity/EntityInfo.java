package genius.digdug.entity;

import genius.digdug.Coordinates;
import genius.digdug.block.Block;

public class EntityInfo {
	private Class<EntityMonster> clazz;
	private Block block;
	private Coordinates spawn;
	public EntityInfo(Class<EntityMonster> class1,Block b,Coordinates spawn) {
		this.clazz=class1;
		this.block=b;
		this.spawn=spawn;
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
}
