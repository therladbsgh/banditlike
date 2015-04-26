package banditlike;

import asciiPanel.AsciiPanel;
import java.util.List;

/**
 * The class to generate creatures and items.
 * Only this class should instantiate creatures and items.
 * @author Andrew Kim
 */

public class StuffFactory {
	
	public World world;
	
	/**
	 * Class constructor.
	 * @param world The world to generate creatures and items in.
	 */
	public StuffFactory(World world){
		this.world = world;
	}
	
	/**
	 * Generates a new player, and assigns messages and a field of view.
	 * Each player has a '@' glyph, is white, has 100 HP, 20 attack, and 0 defense.
	 * @param messages List of messages, probably defined in PlayScreen
	 * @param fov Field of view of player, probably defined in PlayScreen
	 * @return The player object
	 */
	public Creature newPlayer(List<String> messages, FieldOfView fov){
		Creature player = new Creature(world, '@', AsciiPanel.brightWhite, "player", 100, 20, 0);
		world.addAtEmptyLocation(player,0);
		new PlayerAi(player, messages, fov);
		return player;
	}
	
	/**
	 * Generates a new fungus.
	 * Each fungus has a 'f' glyph, is green, has 10 HP, 0 attack, and 0 defense.
	 * @param depth The level of depth the fungus should be in
	 * @return The fungus object
	 */
	public Creature newFungus(int depth){
		Creature fungus = new Creature(world, 'f', AsciiPanel.green, "fungus", 10, 0, 0);
		world.addAtEmptyLocation(fungus, depth);
		new FungusAi(fungus, this);
		return fungus;
	}
	
	/**
	 * Generates a new bat.
	 * Each bat has a 'b' glyph, is yellow, has 15 HP, 5 attack, and 0 defense.
	 * @param depth The level of depth the bat should be in
	 * @return The bat object
	 */
	public Creature newBat(int depth){
		Creature bat = new Creature(world, 'b', AsciiPanel.yellow, "bat", 15, 5, 0);
		world.addAtEmptyLocation(bat, depth);
		new BatAi(bat);
		return bat;
	}
	
	/**
	 * Generates a new rock.
	 * Each rock has a ',' glyph, and is yellow.
	 * @param depth The level of depth the rock should be in
	 * @return The rock object
	 */
	public Item newRock(int depth){
		Item rock = new Item(',', AsciiPanel.yellow, "rock");
		world.addAtEmptyLocation(rock, depth);
		return rock;
	}
	
	/**
	 * Generates a new victory item.
	 * The victory item has a '*' glyph, and is white.
	 * @param depth The level of depth the item should be in
	 * @return The object
	 */
	public Item newVictoryItem(int depth){
        Item item = new Item('*', AsciiPanel.brightWhite, "teddy bear");
        world.addAtEmptyLocation(item, depth);
        return item;
    }

}
