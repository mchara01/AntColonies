public class Cell {

	private boolean hasNest; // Whether it has a nest on it.
	private int foodAmount; // Number of seeds it currently has.
	private int scentAmount; // Amount of scent units that have been anointed and not evaporated.
	private int[] times; // Points in time at which it was anointed with the above scent units.
	private int[] antIds; // IDs of ants on this cell.

	/**
	 * The Cell Object constructor. All of the class private variables are
	 * initialised to their default values.
	 */
	public Cell() {
		hasNest = false;
		foodAmount = 0;
		scentAmount = 0;
		times = null;
		antIds = null;
	}

	/**
	 * Returns whether cell has currently seeds on it.
	 * 
	 * @return Cell contains food or not.
	 */
	public boolean hasFood() {
		if (foodAmount > 0)
			return true;
		return false;
	}

	/**
	 * Reduce one seed from the total amount.
	 */
	public void getFood() {
		foodAmount--;
	}

	/**
	 * Add one seed to the total amount.
	 */
	public void putFood() {
		foodAmount++;
	}

	/**
	 * Add a given number of seeds to the total amount.
	 * 
	 * @param food
	 *            Number of seeds to be added.
	 */
	public void putFood(int food) {
		foodAmount += food;
	}

	/**
	 * Returns a boolean value whether the cell currently has any ants on it.
	 * 
	 * @return If cell has ants or not.
	 */
	public boolean hasAnts() {
		if (!(antIds == null) && antIds.length > 0)
			return true;
		return false;
	}

	/**
	 * Point out that the cell has a nest on it.
	 */
	public void putNest() {
		hasNest = true;
	}

	/**
	 * Returns a boolean value whether the cell has a nest on it.
	 * 
	 * @return If cell has a nest on it or not.
	 */
	public boolean withNest() {
		return hasNest;
	}

	/**
	 * Gives the number of scent units that are currently anointed to the cell.
	 * 
	 * @return Number of scent units on cell.
	 */
	public int getScent() {
		return scentAmount;
	}

	/**
	 * Add the ant with antID on it which means the ant in question proceeded to
	 * this position.
	 * 
	 * @param antID
	 *            ID of ant that move to the cell.
	 */
	public void addAnt(int antID) {
		if (antIds == null) {
			antIds = new int[1];
			antIds[0] = antID;
		} else {
			antIds = expand(antIds);
			antIds[antIds.length - 1] = antID;
		}
	}

	private int[] expand(int[] antIds) {
		int[] newArray = new int[antIds.length + 1];
		System.arraycopy(antIds, 0, newArray, 0, antIds.length);
		return newArray;
	}

	/**
	 * Remove the ant with antID identity which means that the ant left this
	 * position. To remove an ant with ID antID from the array antIds, we simply
	 * "shift" all elements after it. This means that we're going to iterate through
	 * all the elements after antID and simply "move" them one place to the left in
	 * order to replace the ID of the ant we want to remove.
	 * 
	 * @param antID
	 *            ID of ant that left the cell.
	 */
	public void removeAnt(int antID) {
		if (antIds.length == 0)
			return;
		int[] newArray = new int[antIds.length - 1];
		int index = 0;
		for (int i = 0; i < antIds.length; i++)
			if (antIds[i] == antID)
				index = i;

		for (int i = 0, j = 0; i < antIds.length; i++)
			if (i != index)
				newArray[j++] = antIds[i];

		antIds = newArray;
	}

	/**
	 * Add one scent unit at a given time point.
	 * 
	 * @param time
	 *            Time in which the scent will be added.
	 */
	public void addScent(int time) {
		if (times == null) {
			times = new int[100];
			times[time - 1]++;
		} else {
			scentAmount++;
			times[time - 1]++;
		}
	}

	/**
	 * Subtract all the scent units that have evaporated. For a scent unit to be
	 * evaporated, this means that the time between the current time (curTime) and
	 * the time that the anointing was done is greater than the elapsed time.
	 * 
	 * @param curTime
	 *            Current point in time.
	 * @param elapsed
	 *            Max life time of a seed.
	 */
	public void updateScent(int curTime, int elapsed) {
		if (times == null)
			times = new int[100];
		for (int i = 0; i < times.length; i++)
			if (curTime - i > elapsed) {
				scentAmount -= times[i];
				times[i] = 0;
			}
	}

	/**
	 * The toString method gives the external presentation for the Object Cell as a
	 * string.
	 */
	public String toString() {
		String s = "\tCell that ";
		if (hasNest)
			s += "has a nest.\n";
		else
			s += "does not have a nest.\n";
		if (hasNest && foodAmount > 0)
			s += "\t- The nest has " + foodAmount + " stored seeds.\n";
		if (!hasNest && foodAmount > 0)
			s += "\t- The cell has " + foodAmount + " seeds for collection.\n";
		if (foodAmount == 0)
			s += "\t- There are no seeds.\n";
		if (scentAmount > 0)
			s += "\t- Currently it is annotated with " + scentAmount + " units of scent.\n";
		if (antIds == null)
			s += "\t- At present there are no ants on it.\n";
		else
			s += "\t- At present there are " + antIds.length + " ants on it.\n";
		return s;
	}

}