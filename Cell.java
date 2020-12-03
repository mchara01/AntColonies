
/**
 * Author: Marcos Antonios Charalambous 
 * Written: 26/11/2020
 * Last updated: 01/12/2020
 *
 * Compilation command: javac -classpath .:stdlib.jar AntColonies.java 
 * Execution command: java -classpath .:stdlib.jar AntColonies 10 20 R 0
 *
 * Implementation of the Cell Object.
 *
 */
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
	 * Returns whether cell has currently any seeds on it.
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
	 * Adds one seed to the total amount.
	 */
	public void putFood() {
		foodAmount++;
	}

	/**
	 * Adds a given number of seeds to the total amount.
	 * 
	 * @param food
	 *            Number of seeds to be added.
	 */
	public void putFood(int food) {
		foodAmount += food;
	}

	/**
	 * Returns a boolean value whether the cell currently has any ants on it or not.
	 * 
	 * @return If cell has ants or not.
	 */
	public boolean hasAnts() {
		if (antIds == null)
			return false;
		return antIds.length > 0;
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
	 * Add the ant with antID on the current Cell, which means that the ant in
	 * question proceeded to this position.
	 * 
	 * @param antID
	 *            ID of ant that move to the cell.
	 */
	public void addAnt(int antID) {
		if (antIds == null) { // Check if antIds array has not been initialised yet.
			antIds = new int[1]; // Initialise it with only one index.
			antIds[0] = antID; // Place antID on first index.
		} else {
			int[] tempArray = new int[antIds.length + 1]; // New temporary array.
			for (int i = 0; i < antIds.length; i++)
				tempArray[i] = antIds[i]; // Copy ants into temporary array.
			tempArray[antIds.length] = antID; // Place new ant on last index of array.
			antIds = new int[tempArray.length]; // New antIds array, expanded.
			for (int i = 0; i < tempArray.length; i++)
				antIds[i] = tempArray[i]; // Copy temporary array back in antIds.
		}
	}

	/**
	 * Remove the ant with antID identity which means that the ant left this
	 * position. To remove an ant with ID antID from the array antIds, we simply
	 * "shift" all elements after it. This means that we are going to iterate
	 * through all the elements after antID and simply "move" them one place to the
	 * left in order to replace the ID of the ant we want to remove.
	 * 
	 * @param antID
	 *            ID of ant that left the cell.
	 */
	public void removeAnt(int antID) {
		if (antIds == null) // If array is empty, exit to avoid a crash.
			return;

		int newSize = antIds.length - 1;
		if (newSize == 0) { // If new array size after deletion is 0, make antIds equal to null.
			antIds = null;
			return;
		}

		int[] tempArray = new int[newSize];
		int index = 0;
		for (int i = 0; i < antIds.length; i++)
			if (antIds[i] == antID) { // Store index of antID.
				index = i;
				break;
			}

		for (int i = 0, j = 0; i < antIds.length; i++)
			if (i != index)
				tempArray[j++] = antIds[i]; // Save all ants in a temporary array, except from antID.

		antIds = new int[newSize]; // New antIds with one space less.
		for (int i = 0; i < tempArray.length; i++)
			antIds[i] = tempArray[i]; // Copy back ants from temporary array.
	}

	/**
	 * Add one scent unit at a given time point. All necessary checks are made to
	 * the times array to ensure that it does not crash and it is expanded
	 * appropriately.
	 * 
	 * @param time
	 *            Time in which the scent will be added.
	 */
	public void addScent(int time) {
		scentAmount++;
		if (times == null) { // Check if times array has not been initialised yet.
			times = new int[1];
			times[0] = time; // Place new time point at first index.
			return;
		}

		int[] tempArray = new int[times.length + 1]; // New temporary array with same size plus one.
		for (int i = 0; i < times.length; i++)
			tempArray[i] = times[i]; // Copy times into a temporary array.
		tempArray[times.length] = time; // Add new time point at last index.

		times = new int[tempArray.length];
		for (int i = 0; i < times.length; i++)
			times[i] = tempArray[i]; // Copy temporary array into new times array.
	}

	/**
	 * Remove all the scent units from times array that have evaporated. For a scent
	 * unit to be evaporated, the time between the current time (curTime) and the
	 * time that the anointing was done is greater than the elapsed time.
	 * 
	 * @param curTime
	 *            Current point in time.
	 * @param elapsed
	 *            Max life time of a seed.
	 */
	public void updateScent(int curTime, int elapsed) {
		if (times == null) // Check if times array has not been initialised yet.
			return;

		int removalCount = 0; // Counter for scents that have been evaporated.
		for (int i = 0; i < times.length; i++)
			if (curTime - times[i] > elapsed) { // Find evaporated scents.
				scentAmount--;
				removalCount++;
			}

		if (removalCount == times.length) { // Check if all scents have been evaporated.
			times = null;
			return;
		}

		int[] tempArray = new int[times.length - removalCount]; // Temporary array to save only valid scents.
		for (int i = removalCount; i < times.length; i++) // Times are sorted in the array, so start from removal count.
			tempArray[i - removalCount] = times[i]; // Copy valid scent values into temporary array.
		times = new int[tempArray.length]; // New times array declared.
		for (int i = 0; i < tempArray.length; i++)
			times[i] = tempArray[i]; // Copy valid scent values into times array.
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