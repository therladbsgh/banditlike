package banditlike;

/**
 * The field of view object, currently only assocaited to the player and PlayScreen.
 * Updates the world accoridng to the field of view.
 * @author Andrew Kim
 */

public class FieldOfView {
	
	private World world;
	private int depth;
	
	/**
	 * The tiles visible at the given moment.
	 */
	private boolean[][] visible;
	
	/**
	 * All the tiles in the world, with the influence of the field of view.
	 */
	private Tile[][][] tiles;
	
	/**
	 * Checks if the tile is visible to the creature.
	 * The tile must be in the same depth of the creature, and within boundaries of the map.
	 * @param x The x value of the tile
	 * @param y The y value of the tile
	 * @param z The z value of the tile
	 * @return Visibility
	 */
	public boolean isVisible(int x, int y, int z){
		return z == depth && x >= 0 && y >= 0
				&& x < visible.length && y < visible[0].length
				&& visible[x][y];
	}
	
	/**
	 * Returns the tile influenced by the field of view
	 * @param x The x value
	 * @param y The y value
	 * @param z The z value
	 * @return The tile
	 */
	public Tile tile(int x, int y, int z){
		return tiles[x][y][z];
	}
	
	/**
	 * Class constructor.
	 * Sets the world, the visible array, and the tiles array for the class.
	 * All tiles in the beginning are unknown.
	 * @param world
	 */
	public FieldOfView(World world){
		this.world = world;
		this.visible = new boolean[world.width()][world.height()];
		this.tiles = new Tile[world.width()][world.height()][world.depth()];
		
		for(int x = 0; x < world.width(); x++){
			for(int y = 0; y < world.height(); y++){
				for(int z = 0; z < world.depth(); z++){
					tiles[x][y][z] = Tile.UNKNOWN;
				}
			}
		}
	}
	
	/**
	 * Updates the field of view.
	 * All tiles within the field of view become visible, and never become invisible again.
	 * @param wx The x value of the creature
	 * @param wy The y value of the creature
	 * @param wz The z value of the creature
	 * @param r The radius of the field of view
	 */
	public void update(int wx, int wy, int wz, int r){
		depth = wz;
		visible = new boolean[world.width()][world.height()];
		
		for (int x = -r; x < r; x++){
            for (int y = -r; y < r; y++){
                if (x*x + y*y > r*r){
                	continue;
                }
         
                if (wx + x < 0 || wx + x >= world.width() 
                 || wy + y < 0 || wy + y >= world.height()){
                	continue;
                }
         
                for (Point p : new Line(wx, wy, wx + x, wy + y)){
                    Tile tile = world.tile(p.x, p.y, wz);
                    visible[p.x][p.y] = true;
                    tiles[p.x][p.y][wz] = tile;
             
                    if (!tile.isGround()){
                    	break;
                    }
                }
            }
        }
	}

}
