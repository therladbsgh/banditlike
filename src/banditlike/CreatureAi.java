package banditlike;

/**
 * The AI for a general creature.
 * Lists methods and decisions a creature will make.
 * @author Andrew Kim
 */

public class CreatureAi {
	
	protected Creature creature;
	
	/**
	 * Class constructor.
	 * Sets the AI's creature to the parameter, and sets the creature's AI to this.
	 * @param creature The creature to have this AI.
	 */
	public CreatureAi(Creature creature){
		this.creature = creature;
		this.creature.setCreatureAi(this);
	}
	
	/**
	 * Method for when a creature enters a tile.
	 * If the tile is empty, the creature should be able to move into it.
	 * @param x The x value of the tile.
	 * @param y The y value of the tile.
	 * @param z The z value of the tile.
	 * @param tile The tile in the located coordinate.
	 */
	public void onEnter(int x, int y, int z, Tile tile){
	    if (tile.isGround()){
	         creature.x = x;
	         creature.y = y;
	         creature.z = z;
	    } else {
	         creature.doAction("bump into a wall");
	    }
	}
	
	/**
	 * Method for what a creature should do whenever it is updated.
	 */
	public void onUpdate(){
		
	}
	
	/**
	 * Method for what a creature should do whenever it receives a message.
	 * @param message The message the creature receives.
	 */
	public void onNotify(String message){
		
	}
	
	/**
	 * Checks whether the creature can see a certain tile.
	 * @param wx The x value of the tile.
	 * @param wy The y value of the tile.
	 * @param wz The z value of the tile.
	 * @return true if the creature can see the tile, false otherwise.
	 */
	public boolean canSee(int wx, int wy, int wz){
		if(creature.z != wz){
			return false;
		}
		if((creature.x - wx) * (creature.x - wx) + (creature.y - wy) * (creature.y - wy)
				> creature.visionRadius() * creature.visionRadius()){
			return false;
		}
		for(Point p : new Line(creature.x, creature.y, wx, wy)){
			if(creature.tile(wx,wy,wz).isGround() || p.x == wx && p.y == wy){
				continue;
			}
			return false;
		}
		return true;
	}
	
	/**
	 * Moves in a random direction.
	 */
	public void wander(){
		int mx = (int)(Math.random() * 3) -1;
		int my = (int)(Math.random() * 3) -1;
		
		Creature other = creature.creature(creature.x + mx, creature.y + my, creature.z);
		
		if(other != null && other.glyph() == creature.glyph()){
			return;
		}else{
			creature.moveBy(mx,my,0);
		}
	}

}
