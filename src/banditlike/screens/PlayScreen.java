package banditlike.screens;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import banditlike.Creature;
import banditlike.CreatureFactory;
import banditlike.World;
import banditlike.WorldBuilder;
import asciiPanel.AsciiPanel;

public class PlayScreen implements Screen {
	
	private World world;
	private int screenHeight;
	private int screenWidth;
	
	private Creature player;
	private List<String> messages;
	
	public PlayScreen(){
		screenWidth = 100;
		screenHeight = 40;
		messages = new ArrayList<String>();
		createWorld();
		
		CreatureFactory creatureFactory = new CreatureFactory(world);
		createCreatures(creatureFactory);
	}
	
	private void createCreatures(CreatureFactory creatureFactory){
		player = creatureFactory.newPlayer(messages);
		
		for(int z = 0; z < world.depth(); z++){
			for(int i = 0; i < 8; i++){
				creatureFactory.newFungus(z);
			}
		}
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
		switch (key.getKeyCode()){
		case KeyEvent.VK_ESCAPE: 
			return new LoseScreen();
		case KeyEvent.VK_ENTER: 
			return new WinScreen();
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_H: 
			player.moveBy(-1, 0, 0);
			break;
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_L: 
			player.moveBy( 1, 0, 0); 
			break;
		case KeyEvent.VK_UP:
		case KeyEvent.VK_K: 
			player.moveBy( 0,-1, 0); 
			break;
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_J: 
			player.moveBy( 0, 1, 0); 
			break;
		case KeyEvent.VK_Y: 
			player.moveBy(-1,-1, 0); 
			break;
		case KeyEvent.VK_U: 
			player.moveBy( 1,-1, 0); 
			break;
		case KeyEvent.VK_B: 
			player.moveBy(-1, 1, 0); 
			break;
		case KeyEvent.VK_N: 
			player.moveBy( 1, 1, 0); 
			break;
		}
		switch (key.getKeyChar()){
        case '<': 
        	player.moveBy( 0, 0, -1); 
        	break;
        case '>': 
        	player.moveBy( 0, 0, 1); 
        	break;
        }
		world.update();
		
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
		//Better code. Places tiles first, and then creatures. Loops (Width * Height) + creatures times.
		for(int x = 0; x < screenWidth; x++){
			for(int y = 0; y < screenHeight; y++){
				int wx = x + left;
				int wy = y + top;
				terminal.write(world.glyph(wx,wy,player.z), x, y, world.color(wx, wy, player.z));
			}
		}
		for(Creature c: world.creatures()){
			if((c.x >= left && c.x < left + screenWidth) 
					&& (c.y >= top && c.y < top + screenHeight)
					&& (c.z == player.z)){
				terminal.write(c.glyph(), c.x - left, c.y - top, c.color());
			}
		}
		
//		Old, inefficient code. Loops (Width * Height * Creatures) times.
//		for(int x = 0; x < screenWidth; x++){
//			for(int y = 0; y < screenHeight; y++){
//				int wx = x + left;
//				int wy = y + top;
//				
//				Creature creature = world.creature(wx,wy);
//				if(creature != null){
//					terminal.write(creature.glyph(), creature.x - left, creature.y - top, creature.color());
//				} else {
//					terminal.write(world.glyph(wx,wy), x, y, world.color(wx,wy));
//				}
//			}
//		}
	}
	
}
