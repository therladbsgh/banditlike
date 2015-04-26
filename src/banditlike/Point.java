package banditlike;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Basic properties for a point. Used to make a line, and ultimately the field of view.
 * @author Andrew Kim
 */

public class Point {
	public int x;
	public int y;
	public int z;
	
	/**
	 * Class constructor.
	 * @param x
	 * @param y
	 * @param z
	 */
	public Point(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Overrides the hashCode() method from Object.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		result = prime * result + z;
		return result;
	}

	/**
	 * Checks whether it equals another point based on its x, y, and z coordinates.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Point))
			return false;
		Point other = (Point) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		if (z != other.z)
			return false;
		return true;
	}

	/**
	 * Lists the eight neighboring points of the given point.
	 * The points are shuffled so no priority is given to a certain direction.
	 * @return Neighboring points from the eight cardinal directions.
	 */
	public List<Point> neighbors8(){
		List<Point> points = new ArrayList<Point>();
		
		for (int ox = -1; ox < 2; ox++){
			for (int oy = -1; oy < 2; oy++){
				if (ox == 0 && oy == 0)
					continue;
				
				int nx = x+ox;
				int ny = y+oy;
				
				points.add(new Point(nx, ny, z));
			}
		}

		Collections.shuffle(points);
		return points;
	}
}
