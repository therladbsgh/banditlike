package banditlike;

import java.awt.Color;
import java.util.List;
import java.util.ArrayList;

/**
 * The world class to store all tiles, creatures, and items.
 * @author Andrew Kim
 */

public class World {
	
	private Tile[][][] tiles;
	private Item[][][] items;
	private int width;
	private int height;
	private int depth;
	private List<Creature> creatures;
	
	/**
	 * @return width
	 */
	public int width() { return width; }
	
	/**
	 * @return height
	 */
	public int height() { return height; }
	
	/**
	 * @return depth
	 */
	public int depth() { return depth; }
	
	/**
	 * @return creatures
	 */
	public List<Creature> creatures() { return creatures; }
	
	/**
	 * Class constructor.
	 * Defines width, height, and depth, and any items or creatures in the world.
	 * @param tiles Tiles to fill in the world
	 */
	public World(Tile[][][] tiles){
		this.tiles = tiles;
		this.width = tiles.length;
		this.height = tiles[0].length;
		this.depth = tiles[0][0].length;
		this.items = new Item[width][height][depth];
		this.creatures = new ArrayList<Creature>();
	}
	
	/**
	 * @param x The x value
	 * @param y The y value
	 * @param z The z value
	 * @return The tile in the given coordinates
	 */
	public Tile tile(int x, int y, int z){
		if (x < 0 || x >= width 
				|| y < 0 || y >= height
				|| z < 0 || z >= depth){
			return Tile.BOUNDS;
		} else {
			return tiles[x][y][z];
		}
	}
	
	/**
	 * @param x The x value
	 * @param y The y value
	 * @param z The z value
	 * @return The item in the given coordinates, if any
	 */
	public Item item(int x, int y, int z){
		return items[x][y][z];
	}
	
	/**
	 * @param x The x value
	 * @param y The y value
	 * @param z The z value
	 * @return The glyph oh the given coordinates, either from a tile ,creature, or item
	 */
	public char glyph(int x, int y, int z){
		Creature creature = creature(x,y,z);
		if (creature != null)
			return creature.glyph();
		
		if (item(x,y,z) != null)
			return item(x,y,z).glyph();
		
		return tile(x,y,z).glyph();
	}
	
	/**
	 * @param x The x value
	 * @param y The y value
	 * @param z The z value
	 * @return The color oh the given coordinates, either from a tile ,creature, or item
	 */
	public Color color(int x, int y, int z){
		Creature creature = creature(x,y,z);
		if (creature != null)
			return creature.color();
		
		if (item(x,y,z) != null)
			return item(x,y,z).color();
		
		return tile(x,y,z).color();
	}
	
	//CREATURES, ITEMS, AND CREATURE ACTIONS------------------------------------------------------------------------
	
	/**
	 * Adds a creature at an empty location.
	 * @param creature The creature to add
	 * @param z The given depth
	 */
	public void addAtEmptyLocation(Creature creature, int z){
		int x;
		int y;
		
		do {
			x = (int)(Math.random() * width);
			y = (int)(Math.random() * height);
		} while(!tile(x,y,z).isGround() || creature(x,y,z) != null);
		
		creature.x = x;
		creature.y = y;
		creature.z = z;
		creatures.add(creature);
	}
	
	/**
	 * Adds an item at an empty location
	 * @param item The item to add
	 * @param depth The given depth
	 */
	public void addAtEmptyLocation(Item item, int depth){
		int x;
		int y;
		
		do {
	        x = (int)(Math.random() * width);
	        y = (int)(Math.random() * height);
	    } while (!tile(x,y,depth).isGround() || item(x,y,depth) != null);
	    
	    items[x][y][depth] = item;
	}
	
	/**
	 * @param x The x value
	 * @param y The y value
	 * @param z The z value
	 * @return The creature at the given location, if any
	 */
	public Creature creature(int x, int y, int z){
		for (Creature c : creatures){
			if(c.x == x && c.y == y && c.z == z){
				return c;
			}
		}
		return null;
	}
	
	/**
	 * Removes the creature from the world.
	 * @param other The creature to remove
	 */
	public void remove(Creature other){
		creatures.remove(other);
	}
	
	/**
	 * Removes the item form the world.
	 * @param x The x value
	 * @param y The y value
	 * @param z The z value
	 */
	public void remove(int x, int y, int z){
		items[x][y][z] = null;
	}
	
	/**
	 * Adds an item to the nearest space of the given coordinates
	 * If no space is present, the item may disappear. This may or may not be treated as a bug.
	 * @param item The item to add
	 * @param x The x value
	 * @param y The y value
	 * @param z The z value
	 */
	public void addAtEmptySpace(Item item, int x, int y, int z){
		if (item == null) {
			return;
		}
		
		List<Point> points = new ArrayList<Point>();
		List<Point> checked = new ArrayList<Point>();
		
		points.add(new Point(x,y,z));
		
		while (!points.isEmpty()) {
			Point p = points.remove(0);
			checked.add(p);
			
			if (!tile(p.x, p.y, p.z).isGround())
				continue;
			
			if (items[p.x][p.y][p.z] == null) {
				items[p.x][p.y][p.z] = item;
				Creature c = this.creature(p.x, p.y, p.z);
	            if (c != null)
	            	c.notify("A %s lands between your feet.", item.name());
	            return;
	        } else {
	        	List<Point> neighbors = p.neighbors8();
	            neighbors.removeAll(checked);
	            points.addAll(neighbors);
			}
		}
	}
	
	/**
	 * "Digs" a tile so it becomes a floor tile.
	 * @param x The x value
	 * @param y The y value
	 * @param z The z value
	 */
	public void dig(int x, int y, int z){
		if(tile(x,y,z).isDiggable()){
			tiles[x][y][z] = Tile.FLOOR;
		}
	}
	
	/**
	 * Updates the world.
	 */
	public void update(){
		List<Creature> toUpdate = new ArrayList<Creature>(creatures);
		for(Creature creature : toUpdate){
			creature.update();
		}
	}

}
