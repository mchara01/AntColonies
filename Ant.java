
/**
 * Author: Marcos Antonios Charalambous 
 * Written: 26/11/2020 
 * Last updated: 01/12/2020
 *
 * Compilation command: javac -classpath .:stdlib.jar AntColonies.java 
 * Execution command: java -classpath .:stdlib.jar AntColonies 10 20 R 0
 *
 * Implementation of the Ant Object.
 *
 */
import java.util.Random;

public class Ant {

	private CellGrid terain; // The field in which it is located and operates.
	private int id; // Its ID.
	private int[] pos; // The coordinates of its current position.
	private int previousX; // The X coordinate of its preceding position.
	private int previousY; // The Y coordinate of its preceding position.
	private int[] nestPos; // Coordinates of its nest.
	private boolean carriesFood; // Whether it carries a seed.
	private String color; // The colour of its tribe.

	/**
	 * Ant Object constructor. All of the class private variables are initialised to
	 * their default values.
	 * 
	 * @param grid
	 *            NxN cell grid.
	 * @param Id
	 *            Ant's identity.
	 * @param col
	 *            Ant's tribe colour.
	 * @param NestPos
	 *            Position of ant's nest.
	 */
	public Ant(CellGrid grid, int Id, String col, int[] NestPos) {
		terain = grid;
		id = Id;
		color = new String(col);
		pos = new int[2];
		nestPos = new int[2];
		nestPos[0] = NestPos[0];
		nestPos[1] = NestPos[1];
		pos[0] = nestPos[0]; // Initially it is in its nest.
		pos[1] = nestPos[1];
		previousX = -1; // Initial fake values.
		previousY = -1;
		carriesFood = false; // It does not carry food at the start.
	}

	/**
	 * Moves an ant by one step at a given point in time with the aid of private
	 * methods searchFood() and goBackNest().
	 * 
	 * @param time
	 *            Point in time the method was called.
	 */
	public void move(int time) {
		if (!carriesFood)
			searchFood(time);
		else
			goBackNest(time);
	}

	/**
	 * This method is called only if the ant does not hold a seed, which indicates
	 * that it is looking for a food source. The search is mostly random, but the
	 * ant avoids returning to where it was immediately before (thus preventing
	 * circular steps back and forth). Also, among the points where it can proceed,
	 * if any of them has any scent on it, then priority is given to the one with
	 * the strongest scent. Otherwise, it chooses between them in a completely
	 * random way. The ant can enter a nest (his own or another ants) but it never
	 * takes a seed from a nest.
	 * 
	 * @param time
	 *            Point in time the method was called.
	 */
	private void searchFood(int time) {
		Cell current = terain.getCell(pos[0], pos[1]);
		if (!current.withNest() && current.hasFood()) { // Check if the ant reached a food source.
			current.getFood(); // Grab the food
			carriesFood = true; // Change boolean variable to signify ant carrying food.
			goBackNest(time); // Call goBackNest at the same time point.
			return;
		}

		int directions[][] = new int[4][2]; // 2D array that stores all available positions the ant can move in.
		int directionsScore[] = new int[4]; // Array that stores score for each available direction.
		directions[0][0] = pos[0] - 1;
		directions[0][1] = pos[1];
		directions[1][0] = pos[0] + 1;
		directions[1][1] = pos[1];
		directions[2][0] = pos[0];
		directions[2][1] = pos[1] - 1;
		directions[3][0] = pos[0];
		directions[3][1] = pos[1] + 1;
		for (int i = 0; i < directionsScore.length; i++) // Calculate the score for each direction.
			directionsScore[i] = calculateScore(directions[i][0], directions[i][1]);
		sortArrays(directions, directionsScore); // Sort directionsScore and directions in ascending order.

		// Calculations for the new position to move in.
		Random rand = new Random();
		int[] positionToGo;
		if (directionsScore[3] == directionsScore[2] && directionsScore[3] == directionsScore[1]) {
			// Case where there are three directions with the same score.
			int randomIndex = rand.nextInt(3) + 1; // Choose randomly from the three directions.
			positionToGo = directions[randomIndex];
		} else if (directionsScore[3] == directionsScore[2]) {
			// Case where there are two directions with the same score.
			int randomIndex = rand.nextInt(2) + 2; // Choose randomly from the two directions.
			positionToGo = directions[randomIndex];
		} else
			positionToGo = directions[3]; // The last cell has the largest amount of scent.

		previousX = pos[0]; // Previous coordinates changed.
		previousY = pos[1];

		pos[0] = positionToGo[0]; // Current coordinates changed.
		pos[1] = positionToGo[1];

		current.removeAnt(id); // Ant removal from current position.

		Cell next = terain.getCell(pos[0], pos[1]);
		next.addAnt(id); // Add ant on current cell.
	}

	/**
	 * This a helper function for assisting in the score calculation. If the point
	 * (x,y) is the same as the previous position or either X or Y are out of the
	 * terrain bounds, -1 is returned. Otherwise, the scent of that cell is
	 * returned.
	 * 
	 * @param x
	 *            Coordinates on the X axle.
	 * @param y
	 *            Coordinates on the Y axle.
	 * @return Score for a given point.
	 */
	private int calculateScore(int x, int y) {
		if (wasAt(x, y)) // Same as previous position.
			return -1;
		if (x == terain.getSize() || x == -1 || y == terain.getSize() || y == -1) // Out of bounds.
			return -1;
		else
			return terain.getCell(x, y).getScent(); // Returned the scent of a valid cell.
	}

	/**
	 * Bubble Sort simultaneously directionsScore and directions in ascending order.
	 * 
	 * @param directions
	 *            2D array that stores all available positions the ant can move in.
	 * @param directionsScore
	 *            Array that stores score for each available direction.
	 */
	private void sortArrays(int directions[][], int directionsScore[]) {
		int n = directionsScore.length;
		for (int i = 0; i < n - 1; i++)
			for (int j = 0; j < n - i - 1; j++)
				if (directionsScore[j] > directionsScore[j + 1]) {
					int temp = directionsScore[j];
					int[] tempArray = directions[j];
					directionsScore[j] = directionsScore[j + 1];
					directions[j] = directions[j + 1];
					directionsScore[j + 1] = temp;
					directions[j + 1] = tempArray;
				}
	}

	/**
	 * If the ant holds a seed in its mouth and consequently has discovered a food
	 * source, then it chooses its next step based on one of the shortest routes
	 * back to its nest. For this purpose, Manhattan Distance is used. In addition,
	 * it adds to each point it passes while it returns back to its nest a unit of
	 * scent, to help it return to the same food source after first storing the seed
	 * it holds in its nest. Moreover, the scent unit evaporates over a given period
	 * of time.
	 * 
	 * @param time
	 *            Point in time the method was called.
	 */
	private void goBackNest(int time) {
		Cell current = terain.getCell(pos[0], pos[1]);
		if (isAtNest()) { // Reached his nest.
			current.putFood(); // Unload the food.
			carriesFood = false; // Food does not carry food anymore.
			return;
		}
		current.removeAnt(id); // Ant removal from current cell.
		current.addScent(time); // Scent is added to the cell it is about to leave.

		// Manhattan Distance calculations
		int[] distances = new int[4]; // Store Manhattan Distance score for each direction.
		distances[0] = Math.abs((pos[0] - 1) - nestPos[0]) + Math.abs(pos[1] - nestPos[1]); // Up
		distances[1] = Math.abs((pos[0] + 1) - nestPos[0]) + Math.abs(pos[1] - nestPos[1]); // Down
		distances[2] = Math.abs(pos[0] - nestPos[0]) + Math.abs((pos[1] - 1) - nestPos[1]); // Left
		distances[3] = Math.abs(pos[0] - nestPos[0]) + Math.abs((pos[1] + 1) - nestPos[1]); // Right

		// Calculate the minimum distance.
		int min = Integer.MAX_VALUE;
		int minIndex = 0;
		for (int i = 0; i < distances.length; i++)
			if (distances[i] < min) {
				min = distances[i];
				minIndex = i;
			}

		previousX = pos[0]; // Change previous position to point to current one.
		previousY = pos[1];
		// Calculation of the new position in terrain.
		switch (minIndex) { // Find direction with shorter distance.
		case 0: // Up
			pos[0] = pos[0] - 1;
			break;
		case 1: // Down
			pos[0] = pos[0] + 1;
			break;
		case 2: // Left
			pos[1] = pos[1] - 1;
			break;
		case 3: // Right
			pos[1] = pos[1] + 1;
			break;
		}

		Cell next = terain.getCell(pos[0], pos[1]);
		next.addAnt(id); // Move ant to new cell.
	}

	/**
	 * Checks if the ant in question is at its nest.
	 * 
	 * @return Whether ant is in its nest.
	 */
	public boolean isAtNest() {
		if (pos[0] == nestPos[0] && pos[1] == nestPos[1])
			return true;
		return false;
	}

	/**
	 * Checks if ant in question previous position was at point (x,y).
	 * 
	 * @param x
	 *            Coordinate of row in the terrain.
	 * @param y
	 *            Coordinate of column in the terrain.
	 * @return Whether ant's previous position was (x,y).
	 */
	public boolean wasAt(int x, int y) {
		if (previousX == x && previousY == y)
			return true;
		return false;
	}

	/**
	 * Checks if ant is currently on cell (x,y).
	 * 
	 * @param x
	 *            Coordinate of row in the terrain.
	 * @param y
	 *            Coordinate of column in the terrain.
	 * @return Whether ant's current position is (x,y).
	 */
	public boolean isAt(int x, int y) {
		if (pos[0] == x && pos[1] == y)
			return true;
		return false;
	}

	/**
	 * Return the ant's ID.
	 * 
	 * @return Ant's ID
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gives the coordinates of the ant's current position.
	 * 
	 * @return Ant's current position.
	 */
	public int[] getPos() {
		return pos;
	}

	/**
	 * Gives the coordinates of the ant's nest.
	 * 
	 * @return Ant's nest position.
	 */
	public int[] getNestPos() {
		return nestPos;
	}

	/**
	 * Returns whether ant in question carries seed.
	 * 
	 * @return Whether ant is carrying food.
	 */
	public boolean carries_food() {
		return carriesFood;
	}

	/**
	 * The toString method gives the external presentation for the Object Ant as a
	 * string.
	 */
	public String toString() {
		String s = color + " Ant-" + id + " at (" + pos[0] + "," + pos[1] + ").";
		if (terain.getCell(pos[0], pos[1]).hasFood() && !isAtNest() && !terain.getCell(pos[0], pos[1]).withNest()) {
			s += " It has found food!";
			return s;
		}
		if (isAtNest() && carriesFood)
			s += " It has returned to its nest and has food to deliver.";
		if (isAtNest() && !carriesFood)
			s += " It is at its nest and does not hold any food.";
		if (!isAtNest() && carriesFood)
			s += " It is going back to its nest with food.";
		if (!isAtNest() && !carriesFood)
			s += " It is out looking for food.";
		return s;
	}

}