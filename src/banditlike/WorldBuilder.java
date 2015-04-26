package banditlike;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Logic for generating a new world.
 * Although hard to deal with, the generator is only run once and therefore requires little maintenance.
 * @author Andrew Kim
 */

public class WorldBuilder {
	
	private int width;
	private int height;
	private int depth;
	private Tile[][][] tiles;
	private int[][][] regions;
	private int nextRegion;
	
	/** 
	 * Class constructor.
	 * @param width Width of the world
	 * @param height Height of the world
	 * @param depth Depth of the world
	 */
	public WorldBuilder(int width, int height, int depth){
		this.width = width;
		this.height = height;
		this.depth = depth;
		this.tiles = new Tile[width][height][depth];
		this.regions = new int[width][height][depth];
		this.nextRegion = 1;
	}
	
	/**
	 * Create a new world with the given generated tiles.
	 * @return New world
	 */
	public World build(){
		return new World(tiles);
	}
	
	/**
	 * Randomize every tile so it can become a floor or wall at a 50% chance respectively.
	 * @return
	 */
	private WorldBuilder randomizeTiles(){
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				for(int z = 0; z < depth; z++){
					tiles[x][y][z] = Math.random() < 0.5 ? Tile.FLOOR : Tile.WALL;
				} 
			}
		}
		return this;
	}
	
	/**
	 * Smoothens the map.
	 * The tile becomes a floor or wall tile depending on what the majority of the nearby tiles are.
	 * @param times The number of times to smooth the map
	 * @return This, with tiles updated
	 */
	private WorldBuilder smooth(int times){
		Tile[][][] tempTiles = new Tile[width][height][depth];
		for(int time = 0 ; time < times; time++){
			for(int x = 0; x < width; x++){
				for(int y = 0; y < height; y++){
					for(int z = 0; z < depth; z++){
						int floors = 0;
						int rocks = 0;
						
						for(int ox = -1; ox <= 1; ox++){
							for(int oy = -1; oy <= 1; oy++){
								if(x + ox < 0 || x + ox >= width 
										|| y + oy < 0 || y + oy >= height){
									continue;
								}
								if(tiles[x+ox][y+oy][z] == Tile.FLOOR){
									floors++;
								} else {
									rocks++;
								}
							}
						}
						tempTiles[x][y][z] = floors >= rocks ? Tile.FLOOR : Tile.WALL;
					}
				}
			}
			tiles = tempTiles;
		}
		return this;
	}
	
	/**
	 * Generates caves for the map
	 * @return This, with tiles updated
	 */
	public WorldBuilder makeCaves(){
		return randomizeTiles().smooth(8).createRegions().connectRegions().addExitStairs();
	}
	
	//CAVES AND REGIONS ----------------------------------------------------------------------------------
	
	/**
	 * Creates a region map.
	 * Each location has a number that identifies what region / open space it belongs to.
	 * If the region is too small it gets removed.
	 * @return This, with updated tiles.
	 */
	private WorldBuilder createRegions(){
		regions = new int[width][height][depth];
		
		for (int z = 0; z < depth; z++){
			for (int x = 0; x < width; x++){
				for (int y = 0; y < height; y++){
					if (tiles[x][y][z] != Tile.WALL && regions[x][y][z] == 0){
						int size = fillRegion(nextRegion++, x, y, z);
						
						if (size < 25)
							removeRegion(nextRegion - 1, z);
					}
				}
			}
		}
		return this;
	}
	
	/**
	 * Zeroes the region and fills it so it is no longer a cave.
	 * @param region Region to remove
	 * @param z Depth
	 */
	private void removeRegion(int region, int z){
		for (int x = 0; x < width; x++){
			for (int y = 0; y < height; y++){
				if (regions[x][y][z] == region){
					regions[x][y][z] = 0;
					tiles[x][y][z] = Tile.WALL;
				}
			}
		}
	}
	
	/**
	 * Fills the region with a given number.
	 * Any tile it is connected to gets the same region number.
	 * @param region Region number
	 * @param x Initial x coordinate
	 * @param y Initial y coordinate
	 * @param z Initial z coordinate
	 * @return The size of the region
	 */
	private int fillRegion(int region, int x, int y, int z) {
		int size = 1;
		ArrayList<Point> open = new ArrayList<Point>();
		open.add(new Point(x,y,z));
		regions[x][y][z] = region;
		
		while (!open.isEmpty()){
			Point p = open.remove(0);

			for (Point neighbor : p.neighbors8()){
				if (neighbor.x < 0 || neighbor.y < 0 || neighbor.x >= width || neighbor.y >= height)
					continue;
				
				if (regions[neighbor.x][neighbor.y][neighbor.z] > 0
						|| tiles[neighbor.x][neighbor.y][neighbor.z] == Tile.WALL)
					continue;

				size++;
				regions[neighbor.x][neighbor.y][neighbor.z] = region;
				open.add(neighbor);
			}
		}
		return size;
	}
	
	/**
	 * Connect the regions with stairs
	 * @return This, with updated tiles
	 */
	public WorldBuilder connectRegions(){
		for (int z = 0; z < depth-1; z++){
			connectRegionsDown(z);
		}
		return this;
	}
	
	/**
	 * Connects two adjacent regions.
	 * Checks each region above another region; if they are not connected, a stair is made.
	 * @param z The depth to check
	 */
	private void connectRegionsDown(int z){
		List<String> connected = new ArrayList<String>();
		
		for (int x = 0; x < width; x++){
			for (int y = 0; y < height; y++){
				String region = regions[x][y][z] + "," + regions[x][y][z+1];
				if (tiles[x][y][z] == Tile.FLOOR
						&& tiles[x][y][z+1] == Tile.FLOOR
						&& !connected.contains(region)){
					connected.add(region);
					connectRegionsDown(z, regions[x][y][z], regions[x][y][z+1]);
				}
			}
		}
	}
	
	/**
	 * Gets a list of all overlapping areas.
	 * Based on how much area overlaps, stairs that goes up and down are made.
	 * @param z Depth
	 * @param r1 First region
	 * @param r2 Second region
	 */
	private void connectRegionsDown(int z, int r1, int r2){
		List<Point> candidates = findRegionOverlaps(z, r1, r2);
		
		int stairs = 0;
		do{
			Point p = candidates.remove(0);
			tiles[p.x][p.y][z] = Tile.STAIRS_DOWN;
			tiles[p.x][p.y][z+1] = Tile.STAIRS_UP;
			stairs++;
		}
		while (candidates.size() / stairs > 250);
	}

	/**
	 * Find which areas of the two regions overlap.
	 * @param z Depth
	 * @param r1 First region
	 * @param r2 Second region
	 * @return List of overlapping points
	 */
	public List<Point> findRegionOverlaps(int z, int r1, int r2) {
		ArrayList<Point> candidates = new ArrayList<Point>();
		
		for (int x = 0; x < width; x++){
			for (int y = 0; y < height; y++){
				if (tiles[x][y][z] == Tile.FLOOR
						&& tiles[x][y][z+1] == Tile.FLOOR
						&& regions[x][y][z] == r1 
						&& regions[x][y][z+1] == r2){
					candidates.add(new Point(x,y,z));
				}
			}
		}
		
		Collections.shuffle(candidates);
		return candidates;
	}
	
	/**
	 * Add exit stairs to the topmost layer of the caves.
	 * @return This, with updated tiles.
	 */
	private WorldBuilder addExitStairs() {
		int x= -1;
		int y = -1;
		
		do {
			x = (int)(Math.random() * width);
			y = (int)(Math.random() * height);
		} while (tiles[x][y][0] != Tile.FLOOR);
		
		tiles[x][y][0] = Tile.STAIRS_UP;
		return this;
	}

}
