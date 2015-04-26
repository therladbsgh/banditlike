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

public class PlayScreen implements Screen {
	
	private World world;
	private int screenHeight;
	private int screenWidth;
	private Screen subscreen;
	
	private Creature player;
	private List<String> messages;
	private FieldOfView fov;
	
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
	
	private void createItems(StuffFactory factory){
		for (int z = 0; z < world.depth(); z++) {
			for (int i = 0; i < world.width() * world.height() / 20; i++) {
				factory.newRock(z);
			}
		}
		factory.newVictoryItem(world.depth() - 1);
	}

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
	
	private void createWorld(){
		world = new WorldBuilder(200,50,2).makeCaves().build();
	}
	
	private void displayMessages(AsciiPanel terminal, List<String> messages) {
	    int top = screenHeight + 3 - messages.size();
	    for (int i = 0; i < messages.size(); i++){
	        terminal.writeCenter(messages.get(i), top + i);
	    }
	    messages.clear();
	}
	
	public int getScrollX(){
		return Math.max(0, Math.min(player.x - screenWidth / 2, world.width() - screenWidth));
	}
	
	public int getScrollY(){
		return Math.max(0, Math.min(player.y - screenHeight / 2, world.height() - screenHeight));
	}
	
	private void displayTiles(AsciiPanel terminal, int left, int top){
		fov.update(player.x, player.y, player.z, player.visionRadius());
		
		//Better code. Places tiles first, and then creatures. Loops (Width * Height) + creatures times.
		//For FOV.
//		for(int x = 0; x < screenWidth; x++){
//			for(int y = 0; y < screenHeight; y++){
//				int wx = x + left;
//				int wy = y + top;
//				
//				if(player.canSee(wx,wy,player.z)){
//					terminal.write(world.glyph(wx,wy,player.z), x, y, world.color(wx, wy, player.z));
//				}else{
//					terminal.write(fov.tile(wx, wy, player.z).glyph(), x, y, Color.darkGray);
//				}
//			}
//		}
//		
//		if (subscreen != null) {
//			subscreen.displayOutput(terminal);
//		}
		
//		Old, inefficient code. Loops (Width * Height * Creatures) times. Also no FOV. Use for debug.
		for (int x = 0; x < screenWidth; x++){
			for (int y = 0; y < screenHeight; y++){
				int wx = x + left;
				int wy = y + top;
				
				terminal.write(world.glyph(wx, wy, player.z), x, y, world.color(wx, wy, player.z));
			}
		}
		if (subscreen != null) {
			subscreen.displayOutput(terminal);
		}
	}
	
	private boolean userIsTryingToExit() {
		return player.z == 0 && world.tile(player.x, player.y, player.z) == Tile.STAIRS_UP;
	}
	
	private Screen userExits() {
		for (Item item : player.inventory().getItems()){
	        if (item != null && item.name().equals("teddy bear"))
	            return new WinScreen();
	    }
	    return new LoseScreen();
	}
	
}
