package banditlike.screens;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import banditlike.Creature;
import banditlike.Item;
import banditlike.StuffFactory;
import banditlike.FieldOfView;
import banditlike.Tile;
import banditlike.World;
import banditlike.WorldBuilder;
import asciiPanel.AsciiPanel;

/**
 * The screen of the main game. 
 * The screen displays the map, creatures, items, messages, and player stats.
 * Most screens in this game are to be shown as subscreens of PlayScreen.
 * @author Andrew Kim
 */

public class PlayScreen implements Screen {
	
	private World world;
	private int screenHeight;
	private int screenWidth;
	
	/**
	 * The subscreen for PlayScreen.
	 * If subscreen is null, the PlayScreen displays output and accepts key presses.
	 * Otherwise, the subscreen displays output and accepts key presses.
	 */
	private Screen subscreen;
	
	private Creature player;
	private List<String> messages;
	private FieldOfView fov;
	
	/**
	 * Class constructor.
	 * Screen width and height is set smaller than the actual terminal to leave blank space for messages and stats.
	 * Initializes the world, messages, field of view, creatures, and items in the world.
	 */
	public PlayScreen(){
		screenWidth = 100;
		screenHeight = 40;
		messages = new ArrayList<String>();
		createWorld();
		fov = new FieldOfView(world);
		
		StuffFactory stuffFactory = new StuffFactory(world);
		createCreatures(stuffFactory);
		createItems(stuffFactory);
	}
	
	/**
	 * Creates creatures in the world.
	 * @param creatureFactory The factory used to create creatures.
	 */
	private void createCreatures(StuffFactory creatureFactory){
		player = creatureFactory.newPlayer(messages, fov);
		
		for(int z = 0; z < world.depth(); z++){
			for(int i = 0; i < 8; i++){
				creatureFactory.newFungus(z);
			}
			for(int i = 0; i < 20; i++){
				creatureFactory.newBat(z);
			}
		}
	}
	
	/**
	 * Creates items in the world.
	 * @param factory The factory used to create items.
	 */
	private void createItems(StuffFactory factory){
		for (int z = 0; z < world.depth(); z++) {
			for (int i = 0; i < world.width() * world.height() / 20; i++) {
				factory.newRock(z);
			}
		}
		factory.newVictoryItem(world.depth() - 1);
	}

	/**
	 * Displays the output (world, creatures, items, stats, messages).
	 * The world view is automatically scrolled based on the player location.
	 */
	@Override
	public void displayOutput(AsciiPanel terminal) {
		int left = getScrollX();
		int top = getScrollY();
		
		displayTiles(terminal, left, top);
		displayMessages(terminal, messages);
		
		terminal.write(player.glyph(), player.x - left, player.y - top, player.color());
		
		String stats = String.format(" %3d/%3d hp", player.hp(), player.maxHp());
		terminal.write(" Stats", screenWidth, 1);
		terminal.write(stats, screenWidth, 3);
	}

	/**
	 * Lists responses to key input.
	 * If a subscreen is present, the subscreen will respond to key input.
	 * Use arrow keys to move around in the four cardinal directions.
	 * Use enter / escape to automatically win or lose respectively.
	 * Use "g" or "," to pick up items.
	 * Use "d" to open the drop subscreen.
	 * Use "<" or ">" to go upstairs or downstairs, if a stair is present.
	 * The world updates every time a key is pressed.
	 * @param key The key pressed.
	 */
	@Override
	public Screen respondToUserInput(KeyEvent key) {
		if (subscreen != null) {
	         subscreen = subscreen.respondToUserInput(key);
	     } else {
	         switch (key.getKeyCode()){
	         case KeyEvent.VK_ESCAPE: 
	        	 return new LoseScreen();
	         case KeyEvent.VK_ENTER: 
	        	 return new WinScreen();
	         case KeyEvent.VK_LEFT:
	        	 player.moveBy(-1, 0, 0); 
	        	 break;
	         case KeyEvent.VK_RIGHT:
	        	 player.moveBy( 1, 0, 0); 
	        	 break;
	         case KeyEvent.VK_UP:
	        	 player.moveBy( 0,-1, 0); 
	        	 break;
	         case KeyEvent.VK_DOWN:
	        	 player.moveBy( 0, 1, 0); 
	        	 break;
	         case KeyEvent.VK_D: 
	        	 subscreen = new DropScreen(player); 
	        	 break;
	         }
	        
	         switch (key.getKeyChar()){
	         case 'g':
	         case ',': 
	        	 player.pickup(); 
	        	 break;
	         case '<': 
	        	 if (userIsTryingToExit()) {
	        		 return userExits();
	        	 } else {
	        		 player.moveBy(0,0,-1);
	        	 }
	        	 break;
	         case '>': 
	        	 player.moveBy( 0, 0, 1); 
	        	 break;
	         }
	     }
	    
	     if (subscreen == null)
	         world.update();
	    
	     if (player.hp() < 1)
	         return new LoseScreen();
	    
	     return this;
	}
	
	//AUXILIARY METHODS------------------------------------------------------------------------------------------
	
	/**
	 * The method for creating a world.
	 */
	private void createWorld(){
		world = new WorldBuilder(200,50,2).makeCaves().build();
	}
	
	/**
	 * Displays messages to the players or other creatures.
	 * The messages are written on the bottom center of the screen
	 * @param terminal The AsciiPanel terminal to write the messages in.
	 * @param messages The messages to display
	 */
	private void displayMessages(AsciiPanel terminal, List<String> messages) {
	    int top = screenHeight + 3 - messages.size();
	    for (int i = 0; i < messages.size(); i++){
	        terminal.writeCenter(messages.get(i), top + i);
	    }
	    messages.clear();
	}
	
	/**
	 * Scroll formula to adjust the world display (in the x-axis) respective to player location
	 * @return The leftmost coordinate to display in the terminal.
	 */
	public int getScrollX(){
		return Math.max(0, Math.min(player.x - screenWidth / 2, world.width() - screenWidth));
	}
	
	/**
	 * Scroll formula to adjust the world display (in the y-axis) respective to player location
	 * @return The topmost coordinate to display in the terminal.
	 */
	public int getScrollY(){
		return Math.max(0, Math.min(player.y - screenHeight / 2, world.height() - screenHeight));
	}
	
	/**
	 * Display the world tiles in the terminal.
	 * The world tiles to display should be adjusted respective to player location.
	 * If a tile is beyond the field of view, the tile is darkened.
	 * @param terminal The AsciiPanel terminal to write the world tiles in.
	 * @param left The leftmost coordinate of the world to display in the terminal.
	 * @param top The topmost coordinate of the world to display in the terminal.
	 */
	private void displayTiles(AsciiPanel terminal, int left, int top){
		fov.update(player.x, player.y, player.z, player.visionRadius());
		
		for(int x = 0; x < screenWidth; x++){
			for(int y = 0; y < screenHeight; y++){
				int wx = x + left;
				int wy = y + top;
				
				if(player.canSee(wx,wy,player.z)){
					terminal.write(world.glyph(wx,wy,player.z), x, y, world.color(wx, wy, player.z));
				}else{
					terminal.write(fov.tile(wx, wy, player.z).glyph(), x, y, Color.darkGray);
				}
				
				//If you do not want FOV (for debug purposes), uncomment below and comment out the top. 
				//terminal.write(world.glyph(wx, wy, player.z), x, y, world.color(wx, wy, player.z));
			}
		}
		
		if (subscreen != null) {
			subscreen.displayOutput(terminal);
		}
	}
	
	
	/**
	 * Check if the user is trying to exit.
	 * Check is done when the user is trying to go upstairs on topmost floor.
	 * @return If user is trying to go upstairs on topmost floor, or "exiting" the caves
	 */
	private boolean userIsTryingToExit() {
		return player.z == 0 && world.tile(player.x, player.y, player.z) == Tile.STAIRS_UP;
	}
	
	/**
	 * Method in making the player "exit" the caves
	 * If the player has the victory item, the WinScreen is returned.
	 * If the player does not have the victory item, the LoseScreen is returned.
	 * @return The winning or losing screen based on victory conditions
	 */
	private Screen userExits() {
		for (Item item : player.inventory().getItems()){
	        if (item != null && item.name().equals("teddy bear"))
	            return new WinScreen();
	    }
	    return new LoseScreen();
	}
	
}
