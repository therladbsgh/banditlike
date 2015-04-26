package banditlike.screens;

import banditlike.Creature;
import banditlike.Item;

/**
 * The dropping screen, a child of the InventoryBasedScreen.
 * @author Andrew Kim
 */

public class DropScreen extends InventoryBasedScreen {
	
	/**
	 * Class constructor.
	 * @param player The player and its inventory to manipulate.
	 */
	public DropScreen(Creature player) {
		super(player);
	}
	
	@Override
	protected String getVerb() {
		return "drop";
	}
	
	/**
	 * Returns what items are acceptable to drop.
	 * All items are acceptable to drop.
	 */
	@Override
	protected boolean isAcceptable(Item item) {
		return true;
	}
	
	/**
	 * Method to drop the item.
	 */
	@Override
	protected Screen use(Item item) {
		player.drop(item);
		return null;
	}

}
