package banditlike;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

/**
 * Logic for making lines. Used for field of view.
 * @author Andrew Kim
 */

public class Line implements Iterable<Point>{
	
	private List<Point> points;
	public List<Point> getPoints() { return points; }
	
	/**
	 * Creates an array of points that represent a line.
	 * We use Bresenham's line algorithm to find the points within a line.
	 * @param x0 Initial x value
	 * @param y0 Initial y value
	 * @param x1 Final x value
	 * @param y1 Final y value
	 */
	public Line(int x0, int y0, int x1, int y1){
		points = new ArrayList<Point>();
		
		int dx = Math.abs(x1-x0);
		int dy = Math.abs(y1-y0);
		
		int sx = x0 < x1 ? 1 : -1;
		int sy = y0 < y1 ? 1 : -1;
		int err = dx - dy;
		
		while(true){
			points.add(new Point(x0,y0,0));
			
			if(x0==x1 && y0==y1){
				break;
			}
			
			int e2 = err*2;
			if(e2 > -dx){
				err -= dy;
				x0 += sx;
			}
            if (e2 < dx){
                err += dx;
                y0 += sy;
            }
		}
	}
	
	/**
	 * Iterates all the points within the line.
	 */
	public Iterator<Point> iterator() {
        return points.iterator();
    }

}
