package banditlike;

/**
 * Basic inventory logic.
 * @author Andrew Kim
 */

public class Inventory {
	
	private Item[] items;
	
	/**
	 * Returns the inventory
	 * @return Item array representing the inventory
	 */
	public Item[] getItems() { return items; }
	
	/**
	 * Returns an item within the inventory based on the index
	 * @param i Index of inventory
	 * @return The item within the index
	 */
	public Item get(int i) { return items[i]; }
	
	/**
	 * Class constructor.
	 * @param max The max size of the inventory.
	 */
	public Inventory(int max){
		items = new Item[max];
	}
	
	/**
	 * Adds an item to the inventory, to the empty space with the lowest index.
	 * @param item Item to add
	 */
	public void add(Item item){
		for (int i = 0; i < items.length; i++) {
			if (items[i] == null) {
				items[i] = item;
				break;
			}
		}
	}
	
	/**
	 * Removes an item from the inventory.
	 * @param item Item to drop
	 */
	public void remove(Item item){
		for (int i = 0; i < items.length; i++){
			if (items[i] == item) {
				items[i] = null;
				break;
			}
		}
	}
	
	/**
	 * Checks if the inventory is full.
	 * @return true if the inventory is full
	 */
	public boolean isFull() {
		int size = 0;
		for (int i = 0; i < items.length; i++) {
			if (items[i] != null) {
				size++;
			}
		}
		return size == items.length;
	}

}
