package genius.digdug;

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
	
	public Coordinates normalize() {
		return new Coordinates(this.x * 32, this.y * 32);
	}
	
	public Coordinates cx(final float nx) {
		return new Coordinates(nx, this.y);
	}
	
	public Coordinates cy(final float ny) {
		return new Coordinates(this.x, ny);
	}
	
	public Coordinates ix(final float a) {
		return this.cx(this.x + a);
	}
	
	public Coordinates iy(final float a) {
		return this.cy(this.y + a);
	}
	
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
	
	public static Coordinates toCoordinates(String s) {
		s = s.replaceAll("\\)", "").replaceAll("\\(", "");
		final String[] data = s.split(",");
		final float x = Float.parseFloat(data[0]);
		final float y = Float.parseFloat(data[1]);
		return new Coordinates(x, y);
	}
	
	@Override
	public int hashCode() {
		return (int) (this.x * this.y);
	}
	
	public Coordinates[] getNeighbors() {
		final Facing[] values = Facing.values();
		final Coordinates[] neighbors = new Coordinates[values.length];
		for (int i = 0; i < values.length; i++) {
			neighbors[i] = this.facing(values[i]);
		}
		return neighbors;
	}
	
	public boolean outOfBounds() {
		return ((this.x > 18) || (this.x < 1) || (this.y < 1) || (this.y > 13));
	}
}