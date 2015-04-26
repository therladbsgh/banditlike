package banditlike.screens;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import asciiPanel.AsciiPanel;
import banditlike.Creature;
import banditlike.Item;

/**
 * The screen to display inventory. 
 * In most cases, an action will be demanded (what to drop, eat, etc.)
 * @author Andrew Kim
 */

public abstract class InventoryBasedScreen implements Screen {
	
	protected Creature player;
	
	/**
	 * Letters that represent a character for each item in the inventory.
	 * A represents the first item, B represents the second item, and so on.
	 */
	private String letters;
	
	/**
	 * The "verb" that must be done in this screen (dropping, eating, etc.)
	 * @return verb
	 */
	protected abstract String getVerb();
	
	/**
	 * Check if the item is acceptable for the said verb
	 * @param item The item selected
	 * @return Whether the item is appropriate for the verb
	 */
	protected abstract boolean isAcceptable(Item item);
	
	/**
	 * The method to use (do the verb) for the selected item.
	 * @param item The item selected.
	 * @return The screen to display in the end (if a subscreen, null will return to PlayScreen)
	 */
	protected abstract Screen use(Item item);
	
	/**
	 * Class constructor.
	 * @param player The player to manipulate the inventory.
	 */
	public InventoryBasedScreen(Creature player) {
		this.player = player;
		this.letters = "abcdefghijklmopqrstuvwxyz";
	}
	
	
	/**
	 * Displays the items in the inventory, and suggests what item to select.
	 */
	@Override
	public void displayOutput(AsciiPanel terminal) {
		ArrayList<String> lines = getList();
		
		int y = 40 - lines.size();
		int x = 4;
		if (lines.size() > 0) {
			terminal.clear(' ', x, y, 20, lines.size());
		}
		
		for (String line : lines) {
			terminal.write(line, x, y++);
		}
		
		terminal.clear(' ', 0, 40, 80, 1);
		terminal.write("What would you like to " + getVerb() + "? (Press the corresponding character, " +
				"or press esc to close)", 4, 40);
		
		terminal.repaint();
	}
	
	/**
	 * Returns a list of formatted string that represents the inventory.
	 * @return
	 */
	private ArrayList<String> getList() {
		ArrayList<String> lines = new ArrayList<String>();
		Item[] inventory = player.inventory().getItems();
		
		for (int i = 0; i < inventory.length; i++) {
			Item item = inventory[i];
			if (item == null || !isAcceptable(item)) {
				continue;
			}
			
			String line = "(" + letters.charAt(i) + ") " + item.glyph() + " (" + item.name() + ")";
			
			lines.add(line);
		}
		return lines;
	}
	
	/**
	 * Response to key input.
	 * If any character in <code>letters</code> is pressed, the item associated with the letter is used.
	 * If escape is pressed, the subscreen goes back to PlayScreen.
	 * Otherwise nothing happens.
	 */
	public Screen respondToUserInput(KeyEvent key) {
		char c = key.getKeyChar();
		
		Item[] items = player.inventory().getItems();
		
		if (letters.indexOf(c) > -1 && items.length > letters.indexOf(c)
				&& items[letters.indexOf(c)] != null && isAcceptable(items[letters.indexOf(c)])) {
			return use(items[letters.indexOf(c)]);
		} else if (key.getKeyCode() == KeyEvent.VK_ESCAPE) {
			return null;
		} else {
			return this;
		}
	}

}
