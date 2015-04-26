package banditlike;

/**
 * The bat AI.
 * @author Andrew Kim
 */

public class BatAi extends CreatureAi{
	
	/**
	 * Class constructor.
	 * @param creature The given bat in the world.
	 */
	public BatAi(Creature creature){
		super(creature);
	}
	
	/**
	 * Wanders twice every time it is updated.
	 */
	public void onUpdate(){
		wander();
		wander();
	}

}
