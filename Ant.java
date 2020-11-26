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

	public void move(int time) {
		if (!carriesFood)
			searchFood(time);
		else
			goBackNest(time);
	}

	private void searchFood(int time) {
		Cell current = terain.getCell(pos[0], pos[1]);
		if (!current.withNest() && current.hasFood()) {
			current.getFood();
			carriesFood = true;
			// return;
		}

		int up = -1, down = -1, left = -1, right = -1;

		if (terain.getCell(pos[0] - 1, pos[1]) != null && previousX != (pos[0] - 1))
			up = terain.getCell(pos[0] - 1, pos[1]).getScent();
		if (terain.getCell(pos[0] + 1, pos[1]) != null && previousX != (pos[0] + 1))
			down = terain.getCell(pos[0] + 1, pos[1]).getScent();
		if (terain.getCell(pos[0], pos[1] - 1) != null && previousY != (pos[1] - 1))
			left = terain.getCell(pos[0], pos[1] - 1).getScent();
		if (terain.getCell(pos[0], pos[1] + 1) != null && previousY != (pos[1] + 1))
			right = terain.getCell(pos[0], pos[1] + 1).getScent();

		String largest = findMax(up, down, left, right);

		terain.getCell(pos[0], pos[1]).removeAnt(id);
		previousX = pos[0];
		previousY = pos[1];
		if (largest.equals("up"))
			pos[0] = pos[0] - 1;
		else if (largest.equals("down"))
			pos[0] = pos[0] + 1;
		else if (largest.equals("left"))
			pos[1] = pos[1] - 1;
		else if (largest.equals("right"))
			pos[1] = pos[1] + 1;
//		System.out.println("up " + up + " down " + down + " left " + left + " right " + right + " direction: " + largest );
		current = terain.getCell(pos[0], pos[1]);
		current.addAnt(id);
	}

	private String findMax(int up, int down, int left, int right) {
		String largest = "";

		if (up == -1) {
			if (down >= left && down >= right)
				largest = "down";
			else if (left >= down && left >= right)
				largest = "left";
			else
				largest = "right";
		} else if (down == -1) {
			if (up >= left && up >= right)
				largest = "up";
			else if (left >= up && left >= right)
				largest = "left";
			else
				largest = "right";
		} else if (left == -1) {
			if (up >= down && up >= right)
				largest = "up";
			else if (down >= up && down >= right)
				largest = "down";
			else
				largest = "right";
		} else {
			if (up >= down && up >= left)
				largest = "up";
			else if (down >= up && down >= left)
				largest = "down";
			else
				largest = "left";
		}
		return largest;
	}

	
	private void goBackNest(int time) {
		Cell current = terain.getCell(pos[0], pos[1]);
		if (isAtNest()) { // Reached his nest.
			current.putFood();
			carriesFood = false;
			// return;
		}

		int[] distances = new int[4];

		// Manhattan Distance calculations
		distances[0] = Math.abs((pos[0] - 1) - nestPos[0]) + Math.abs(pos[1] - nestPos[1]); // Up
		distances[1] = Math.abs((pos[0] + 1) - nestPos[0]) + Math.abs(pos[1] - nestPos[1]); // Down
		distances[2] = Math.abs(pos[0] - nestPos[0]) + Math.abs((pos[1] - 1) - nestPos[1]); // Left
		distances[3] = Math.abs(pos[0] - nestPos[0]) + Math.abs((pos[1] + 1) - nestPos[1]); // Right

		int min = Integer.MAX_VALUE;
		int minIndex = 0;
		for (int i = 0; i < distances.length; i++)
			if (distances[i] < min) {
				min = distances[i];
				minIndex = i;
			}

		terain.getCell(pos[0], pos[1]).removeAnt(id);
		previousX = pos[0];
		previousY = pos[1];
		switch (minIndex) {
		case 0:
			pos[0] = pos[0] - 1;
			break;
		case 1:
			pos[0] = pos[0] + 1;
			break;
		case 2:
			pos[1] = pos[1] - 1;
			break;
		case 3:
			pos[1] = pos[1] + 1;
			break;
		}

		current = terain.getCell(pos[0], pos[1]);
		current.addScent(time);
		current.addAnt(id);
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