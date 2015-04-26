package banditlike;

import java.util.List;

/**
 * The player AI, lists actions for what the player controls.
 * @author Andrew Kim
 */

public class PlayerAi extends CreatureAi {
	
	private List<String> messages;
	private FieldOfView fov;
	
	/**
	 * Class constructor.
	 * Each player should receive messages and have a field of view.
	 * @param creature The creature in the world to assign the AI.
	 * @param messages The list of messages the player receives.
	 * @param fov The field of view of the player.
	 */
	public PlayerAi(Creature creature, List<String> messages, FieldOfView fov){
		super(creature);
		this.messages = messages;
		this.fov = fov;
	}
	
	
	/**
	 * Allows the player to move in a given tile, or dig if the tile is diggable.
	 */
	@Override
	public void onEnter(int x, int y, int z, Tile tile){
		if(tile.isGround()){
			creature.x = x;
			creature.y = y;
			creature.z = z;
		} else if(tile.isDiggable()){
			creature.dig(x,y,z);
		}
	}
	
	
	/**
	 * Sends the message to the player (and then to the screen) when it receives a message.
	 */
	public void onNotify(String message){
		messages.add(message);
	}
	
	
	/**
	 * Returns whether the tile is within the player's field of view.
	 */
	public boolean canSee(int wx, int wy, int wz){
		return fov.isVisible(wx,wy,wz);
	}

}
