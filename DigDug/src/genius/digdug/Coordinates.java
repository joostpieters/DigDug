package genius.digdug;

/**
 * identifies a point
 * @author dyslabs
 *
 */
public class Coordinates {
	public final float x;
	public final float y;
	
	public Coordinates(final float x, final float y) {
		this.x = x;
		this.y = y;
	}
	
	public static float x(final long pofloat) {
		return pofloat >> 32;
	}
	
	public static float y(final long pofloat) {
		return pofloat;
	}
	
	@Override
	public String toString() {
		return String.format("(%d,%d)", this.x, this.y);
	}
	
	/**
	 * converts to pixels 
	 * @return the corrdinates in pixel coordinates
	 */
	public Coordinates normalize() {
		return new Coordinates(this.x * 32, this.y * 32);
	}
	
	/**
	 * changes the x value
	 * @param nx the new x value
	 * @return coordinates with x value changed
	 */
	public Coordinates cx(final float nx) {
		return new Coordinates(nx, this.y);
	}
	
	/**
	 * changes the y value
	 * @param ny the new y value
	 * @return updated coordinates
	 */
	public Coordinates cy(final float ny) {
		return new Coordinates(this.x, ny);
	}
	
	/**
	 * adds A to the x value
	 * @param a how much to add
	 * @return updated coordinates
	 */
	public Coordinates ix(final float a) {
		return this.cx(this.x + a);
	}
	
	/**
	 * @see Coordinates#ix(float)
	 */
	public Coordinates iy(final float a) {
		return this.cy(this.y + a);
	}
	
	/**
	 * gets gets the coordinates in the specified direction
	 * @param facing the direction
	 * @return updated coordinates
	 */
	public Coordinates facing(final Facing facing) {
		switch (facing) {
			case up:
				return this.iy(-1f);
			case down:
				return this.iy(1f);
			case left:
				return this.ix(-1f);
			case right:
				return this.ix(1f);
			default:
				return new Coordinates(-1, -1);
		}
	}
	
	@Override
	public boolean equals(final Object o) {
		if (o == null) {
			return false;
		}
		if (o instanceof Coordinates) {
			final Coordinates other = (Coordinates) o;
			return (other.x == this.x) && (other.y == this.y);
		} else {
			return false;
		}
	}
	
	/**
	 * converts a string in format of (x,y) to a coordinate object
	 * @param s the string to decode
	 * @return coordinates
	 */
	public static Coordinates toCoordinates(String s) {
		s = s.replaceAll("\\)", "").replaceAll("\\(", "");
		final String[] data = s.split(",");
		final float x = Float.parseFloat(data[0]);
		final float y = Float.parseFloat(data[1]);
		return new Coordinates(x, y);
	}
	
	/**
	 * @see Coordinates#toCoordinates(String)
	 */
	public static Coordinates decode(String nm) {
		return Coordinates.toCoordinates(nm);
	}
	
	@Override
	public int hashCode() {
		return (int) (this.x * this.y);
	}
	
	/**
	 * gets all the neighbors of these coordinates
	 * @see Coordinates#facing(Facing)
	 * @return
	 */
	public Coordinates[] getNeighbors() {
		final Facing[] values = Facing.values();
		final Coordinates[] neighbors = new Coordinates[values.length];
		for (int i = 0; i < values.length; i++) {
			neighbors[i] = this.facing(values[i]);
		}
		return neighbors;
	}
	
	/**
	 * returns true if these coordinates are out of bounds
	 * @return boolean
	 */
	public boolean outOfBounds() {
		return ((this.x > 18) || (this.x < 1) || (this.y < 1) || (this.y > 13));
	}
}