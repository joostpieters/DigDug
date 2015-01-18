package genius.digdug.octopus;

import genius.digdug.Coordinates;
import genius.digdug.Map;
import genius.queue.PriotoryQueue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class Octopus {
	private final PriotoryQueue<Coordinates> frontier = new PriotoryQueue<Coordinates>();
	private final HashMap<Coordinates, Coordinates> cameFrom = new HashMap<Coordinates, Coordinates>();
	private final HashMap<Coordinates, Integer> costThusFar = new HashMap<Coordinates, Integer>();
	private static HashMap<Class<?>, Integer> costs = new HashMap<Class<?>, Integer>();
	
	/**
	 * finds a path from start to target
	 * @param start
	 * @param target
	 * @return arraylist
	 */
	public ArrayList<Coordinates> construct(final Coordinates start, final Coordinates target) {
		this.frontier.push(start, 0);
		this.cameFrom.put(start, null);
		this.costThusFar.put(start, 0);
		while (!this.frontier.empty()) {
			final Coordinates current = this.frontier.poll();
			if (current.equals(target)) {
				return this.reconstruct(start, target);
			}
			if (current.outOfBounds()) {
				continue;
			}
			//System.out.println("considering "+current);
			for (final Coordinates next : current.getNeighbors()) {
				final int newCost = this.costThusFar.get(current) + this.distance(current, next);
				if (!this.costThusFar.containsKey(next) || (newCost < this.costThusFar.get(next))) {
					this.costThusFar.put(next, newCost);
					final int priotory = newCost + this.huestic(next);
					this.frontier.push(next, priotory);
					this.cameFrom.put(next, current);
				}
			}
		}
		return null;
	}
	
	/**
	 * finds a path from start to the nearest instance of destination
	 * @param start
	 * @param destination
	 * @return
	 */
	public ArrayList<Coordinates> constructToDest(final Coordinates start, final Class<?> destination) {
		this.frontier.push(start, 0);
		this.cameFrom.put(start, null);
		this.costThusFar.put(start, 0);
		while (!this.frontier.empty()) {
			final Coordinates current = this.frontier.poll();
			if (destination != null) {
				if (destination.isInstance(Map.getBaseBlock(current))) {
					return this.reconstruct(start, current);
				}
			} else {
				if (Map.getBaseBlock(current) == null) {
					return this.reconstruct(start, current);
				}
			}
			if (current.outOfBounds()) {
				continue;
			}
			//System.out.println("considering "+current);
			for (final Coordinates next : current.getNeighbors()) {
				final int newCost = this.costThusFar.get(current) + this.distance(current, next);
				if (!this.costThusFar.containsKey(next) || (newCost < this.costThusFar.get(next))) {
					this.costThusFar.put(next, newCost);
					final int priotory = newCost + this.huestic(next);
					this.frontier.push(next, priotory);
					this.cameFrom.put(next, current);
				}
			}
		}
		return null;
	}
	
	/**
	 * reconstructs the path from start to goal
	 * @param start
	 * @param goal
	 * @return
	 */
	private ArrayList<Coordinates> reconstruct(final Coordinates start, final Coordinates goal) {
		final ArrayList<Coordinates> path = new ArrayList<Coordinates>();
		Coordinates current = goal;
		path.add(current);
		while (!current.equals(start)) {
			current = this.cameFrom.get(current);
			path.add(current);
		}
		Collections.reverse(path);
		path.remove(0);
		return path;
	}
	
	/**
	 * calculates the distance between two points
	 * @param current
	 * @param next
	 * @return manhattan distance
	 */
	private int distance(final Coordinates current, final Coordinates next) {
		return (int) (Math.abs(current.x - next.x) + Math.abs(current.y - next.y));
	}
	
	/**
	 * finds the cost of the block
	 * @param next
	 * @return
	 */
	private int huestic(final Coordinates next) {
		int cost = 0;
		final Iterator<Class<?>> itr = costs.keySet().iterator();
		while (itr.hasNext()) {
			final Class<?> clazz = itr.next();
			if (clazz.isInstance(Map.getBaseBlock(next))) {
				cost = +costs.get(clazz);
			}
		}
		return cost;
	}
	
	/**
	 * adds cost to block
	 * @param b
	 * @param cost
	 */
	public static void filter(final Class<?> b, final int cost) {
		System.out.println("Octopus: filtering " + b);
		costs.put(b, cost);
	}
}
