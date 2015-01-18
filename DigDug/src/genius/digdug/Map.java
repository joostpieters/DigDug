package genius.digdug;

import genius.digdug.block.Block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Map {
	private static HashMap<Coordinates, ArrayList<Block>> map = new HashMap<Coordinates, ArrayList<Block>>();
	
	/**
	 * clears all blocks at the coordinates
	 * @param coords coordinates
	 */
	public static void removeAll(final Coordinates coords) {
		Map.getBlocks(coords).clear();
	}
	
	/**
	 * adds the block to the coordinates
	 * @param coords the coordinates
	 * @param b the block
	 */
	public static void add(final Coordinates coords, final Block b) {
		if ((b != null) && (b.coords == null)) {
			b.coords = coords;
			Map.addBlock(coords, b);
		} else if ((b != null) && (b.coords != null)) {
			Map.addBlock(b.coords, b);
		}
	}
	
	/**
	 * @see Map#add(Coordinates, Block)
	 * adds if empty
	 * @param coords the coordinates
	 * @param b the block
	 */
	public static void addIfEmpty(final Coordinates coords, final Block b) {
		if (Map.getBlocks(coords).isEmpty()) {
			Map.add(coords, b);
		}
	}
	
	/**
	 * gets the first block at the coordinates
	 * @param coords the coordinates
	 * @return index 0 block
	 */
	public static Block getBaseBlock(final Coordinates coords) {
		if (Map.getBlocks(coords).size() == 0) {
			return null;
		} else {
			return Map.getBlocks(coords).get(0);
		}
	}
	
	/**
	 * triggers events, used internally by {@link Map#add(Coordinates, Block)}
	 * @param coords
	 * @param b
	 */
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
	
	/**
	 * gets the blocks
	 * @param coords
	 * @return
	 */
	public static ArrayList<Block> getBlocks(final Coordinates coords) {
		if (map.get(coords) == null) {
			map.put(coords, new ArrayList<Block>());
		}
		return map.get(coords);
	}
	
	/**
	 * @see Map#removeAll(Coordinates)
	 * @param coords
	 */
	public static void delete(final Coordinates coords) {
		map.get(coords).clear();
	}
	
	/**
	 * @return the hashmap used internally
	 */
	@SuppressWarnings("unchecked")
	public static HashMap<Coordinates, ArrayList<Block>> getMap() {
		return (HashMap<Coordinates, ArrayList<Block>>) map.clone();
	}
}
