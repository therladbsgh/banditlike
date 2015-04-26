package banditlike;

import java.awt.Color;

/**
 * The creature class. It should list all properties and actions for a creature.
 * @author Andrew Kim
 */

public class Creature {
	
	private World world;
	
	//Coordinates of Creature
	public int x;
	public int y;
	public int z;
	
	//Characteristics defined in Factory
	private char glyph;
	private Color color;
	private CreatureAi ai;
	private int visionRadius;
	private String name;
	
	//Character stats
	private int maxHp;
	private int hp;
	private int attackValue;
	private int defenseValue;
	private Inventory inventory;
	
	/**
	 * Returns the glyph of the creature.
	 * @return glyph
	 */
	public char glyph() { return glyph; }
	
	/**
	 * Returns the color of the creature.
	 * @return color
	 */
	public Color color() { return color; }
	
	/**
	 * Sets the ai of the creature.
	 * @param ai
	 */
	public void setCreatureAi(CreatureAi ai) { this.ai = ai; }
	
	/**
	 * Returns the radius of the field of view.
	 * @return radius
	 */
	public int visionRadius() { return visionRadius; }
	
	/**
	 * Sets the radius of the field of view
	 * @param radius
	 */
	public void setVisionRadius(int radius) { this.visionRadius = radius; }
	
	/**
	 * Returns the name of the creature.
	 * @return name
	 */
	public String name() { return name; }
	
	/**
	 * Returns the inventory of the creature.
	 * @return inventory
	 */
	public Inventory inventory() { return inventory; }
	
	/**
	 * Returns the max HP of a given creature.
	 * @return maxHp
	 */
	public int maxHp() { return maxHp; }
	
	/**
	 * Returns the current HP of a given creature.
	 * @return hp
	 */
	public int hp() { return hp; }
	
	/**
	 * Returns the attack value of a given creature.
	 * @return attackValue
	 */
	public int attackValue() { return attackValue; }
	
	/**
	 * Returns the defense value of a given creature.
	 * @return defenseValue
	 */
	public int defenseValue() { return defenseValue; }
	
	/**
	 * Class constructor. This should only be called in the CreatureFactory / StuffFactory.
	 * All creatures have a set field of view radius of 9 and an inventory limit of 20 items.
	 * @param world The world the creature is in
	 * @param glyph The glyph representing the creature
	 * @param color The color representing the creature
	 * @param name The name of the creature
	 * @param maxHp The max HP of the creature
	 * @param attack The attack value of the creature
	 * @param defense The defense value of the creature
	 */
	public Creature(World world, char glyph, Color color, String name, int maxHp, int attack, int defense){
		this.world = world;
		this.glyph = glyph;
		this.color = color;
		this.maxHp = maxHp;
		this.hp = maxHp;
		this.attackValue = attack;
		this.defenseValue = defense;
		this.name = name;
		this.visionRadius = 9;
		this.inventory = new Inventory(20);
	}
	
	//ACTIONS----------------------------------------------------------------------------------------
	
	/**
	 * Digs a certain diggable tile.
	 * @param wx The x value of the tile
	 * @param wy The y value of the tile
	 * @param wz The z value of the tile
	 */
	public void dig(int wx, int wy, int wz){
		world.dig(wx, wy, wz);
	}
	
	/**
	 * The amount to move by.
	 * @param mx Change in x
	 * @param my Change in y
	 * @param mz Change in z
	 */
	public void moveBy(int mx, int my, int mz){
		if(mx == 0 && my == 0 && mz == 0){
			return;
		}
		
		Tile tile = world.tile(x+mx, y+my, z+mz);
		
		if(mz == -1){
			if(tile == Tile.STAIRS_DOWN){
				doAction("walk up the stairs to level %d", z+mz+1);
			} else {
				doAction("try to go up but are stopped by the cave ceiling");
				return;
			}
		} else if(mz==1){
			if(tile == Tile.STAIRS_UP){
				doAction("walk down the stairs to level %d", z+mz+1);
            } else {
                doAction("try to go down but are stopped by the cave floor");
                return;
            }
		}
		
		Creature other = world.creature(x+mx, y+my, z+mz);
		
		if(other == null){
			ai.onEnter(x+mx, y+my, z+mz, world.tile(x+mx, y+my, z+mz));
		}else{
			attack(other);
		}
	}
	
	/**
	 * Attacks another creature.
	 * Amount is any value between 0 and the creature's attack value minus the other's defense value.
	 * @param other
	 */
	public void attack(Creature other){
		int amount = Math.max(0, attackValue() - other.defenseValue());
		amount = (int)(Math.random() * amount) + 1;
		other.modifyHp(-amount);
		
		doAction("attack the %s for %d damage", other.name, amount);
		if(other.hp < 1){
			other.doAction("die");
		} else {
			notify("HP of %s: %d/%d", other.name, other.hp, other.maxHp);
		}
	}
	
	/**
	 * Modifies the creature's HP.
	 * @param amount Amount to modify by (negative equals damaging)
	 */
	public void modifyHp(int amount){
		hp += amount;
		if(hp < 1){
			hp = 0;
			world.remove(this);
		}
	}
	
	/**
	 * Updates the creature.
	 */
	public void update(){
		ai.onUpdate();
	}
	
	/**
	 * Picks up an item.
	 */
	public void pickup(){
		Item item = world.item(x,y,z);
		
		if (inventory.isFull() || item == null) {
			doAction("grab at the ground");
		} else {
			doAction("pick up a %s", item.name());
			world.remove(x, y, z);
			inventory.add(item);
		}
	}
	
	/**
	 * Drops an item.
	 * @param item Item to drop
	 */
	public void drop(Item item){
		doAction("drop a " + item.name());
		inventory.remove(item);
		world.addAtEmptySpace(item, x, y, z);
		
	}
	
	//MESSAGES ----------------------------------------------------------------------------------
	
	/**
	 * Notifies the creature of a given message.
	 * @param message Message to notify
	 * @param params Any necessary parameters for a formatted string
	 */
	public void notify(String message, Object ... params){
		ai.onNotify(String.format(message, params));
	}
	
	/**
	 * Displays a message to itself and to others within a radius of 9.
	 * @param message The message to broadcast
	 * @param params Any necessary parameters for a formatted string
	 */
	public void doAction(String message, Object ... params){
		int r = 9;
		for(int ox = -r; ox <= r; ox++){
			for(int oy = -r; oy <= r; oy++){
				if(ox*ox + oy*oy > r*r){
					continue;
				}
				Creature other = world.creature(x+ox,y+oy, z);
				if(other == null){
					continue;
				} else if (other == this){
					other.notify("You " + message + ".", params);
				} else {
					other.notify(String.format("The %s %s.", name, makeSecondPerson(message)), params);
				}
				
			}
		}
	}
	
	/**
	 * Formats a string to make it second person. Grammar may not be always correct.
	 * @param text The text to format
	 * @return Formatted second-person text
	 */
	private String makeSecondPerson(String text){
		String[] words = text.split(" ");
		words[0] = words[0] + "s";
		
		StringBuilder builder = new StringBuilder();
		for(String word : words){
			builder.append(" ");
			builder.append(word);
		}
		
		return builder.toString().trim();
	}
	
	//CHECKS --------------------------------------------------------------------------------
	
	/**
	 * Checks if a creature can enter a tile
	 * @param wx The x value of tile
	 * @param wy The y value of tile
	 * @param wz The z value of tile
	 * @return Whether a creature can enter the tile
	 */
	public boolean canEnter(int wx, int wy, int wz){
		return world.tile(wx,wy,wz).isGround() && world.creature(wx,wy,wz) == null;
	}
	
	/**
	 * Checks if a creature can see a given tile
	 * @param wx The x value of tile
	 * @param wy The y value of tile
	 * @param wz The z value of tile 
	 * @return Whether the creature can see the tile
	 */
	public boolean canSee(int wx, int wy, int wz){
		return ai.canSee(wx, wy, wz);
	}
	
	/**
	 * Returns the tile within the given coordinates
	 * @param wx The x value
	 * @param wy The y value
	 * @param wz The z value
	 * @return The world tile
	 */
	public Tile tile(int wx, int wy, int wz){
		return world.tile(wx, wy, wz);
	}
	
	/**
	 * Returns the creature within the given coordinates
	 * @param wx The x value
	 * @param wy The y value
	 * @param wz The z value
	 * @return The world creature
	 */
	public Creature creature(int wx, int wy, int wz) {
	    return world.creature(wx, wy, wz);
	}
	
}
