package banditlike.screens;

import banditlike.Creature;
import banditlike.Item;

public class DropScreen extends InventoryBasedScreen {
	
	public DropScreen(Creature player) {
		super(player);
	}
	
	protected String getVerb() {
		return "drop";
	}
	
	protected boolean isAcceptable(Item item) {
		return true;
	}
	
	protected Screen use(Item item) {
		player.drop(item);
		return null;
	}

}
