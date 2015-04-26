package banditlike;

/**
 * The fungus AI.
 * @author Andrew Kim
 */

public class FungusAi extends CreatureAi {
	private StuffFactory factory;
	private int spreadCount;
	
	/**
	 * The class constructor. 
	 * @param creature The given fungus on the world.
	 * @param factory The factory to create more fungus.
	 */
	public FungusAi(Creature creature, StuffFactory factory){
		super(creature);
		this.factory = factory;
	}
	
	/**
	 * Spawns a child occasionally every time it is updated.
	 */
	public void onUpdate(){
		if(spreadCount < 5 && Math.random() < 0.01){
			spread();
			creature.doAction("spawn a child");
		}
	}
	
	/**
	 * Creates another fungus entity within a 5x5 square of the original fungus.
	 * Increases the spread count of the original fungus by 1.
	 */
	private void spread(){
		int x = creature.x + (int)(Math.random() * 11) - 5;
		int y = creature.y + (int)(Math.random() * 11) - 5;
		
		if(!creature.canEnter(x,y,creature.z)){
			return;
		}
		
		Creature child = factory.newFungus(creature.z);
		child.x = x;
		child.y = y;
		child.z = creature.z;
		spreadCount++;
	}

}
