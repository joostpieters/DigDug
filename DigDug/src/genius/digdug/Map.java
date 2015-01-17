package genius.digdug;

import genius.digdug.block.Block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Map {
	private static HashMap<Coordinates, ArrayList<Block>> map = new HashMap<Coordinates, ArrayList<Block>>();
	
	public static void removeAll(final Coordinates coords) {
		Map.getBlocks(coords).clear();
	}
	
	public static void add(final Coordinates coords, final Block b) {
		if ((b != null) && (b.coords == null)) {
			b.coords = coords;
			Map.addBlock(coords, b);
		} else if ((b != null) && (b.coords != null)) {
			Map.addBlock(b.coords, b);
		}
	}
	
	public static void addIfEmpty(final Coordinates coords, final Block b) {
		if (Map.getBlocks(coords).isEmpty()) {
			Map.add(coords, b);
		}
	}
	
	public static Block getBaseBlock(final Coordinates coords) {
		if (Map.getBlocks(coords).size() == 0) {
			return null;
		} else {
			return Map.getBlocks(coords).get(0);
		}
	}
	
	private static void addBlock(final Coordinates coords, final Block b) {
		Map.getBlocks(coords).add(b);
		final Iterator<Block> itr = Map.getBlocks(coords).iterator();
		while (itr.hasNext()) {
			final Block est = itr.next();
			est.blockAdded(b);
			if (b.binded != null) {
				est.entityWalkedOn(b.binded);
			}
		}
	}
	
	public static ArrayList<Block> getBlocks(final Coordinates coords) {
		if (map.get(coords) == null) {
			map.put(coords, new ArrayList<Block>());
		}
		return map.get(coords);
	}
	
	public static void delete(final Coordinates coords) {
		map.get(coords).clear();
	}
	
	@SuppressWarnings("unchecked")
	public static HashMap<Coordinates, ArrayList<Block>> getMap() {
		return (HashMap<Coordinates, ArrayList<Block>>) map.clone();
	}
}
