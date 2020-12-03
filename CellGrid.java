
/**
 * Author: Marcos Antonios Charalambous 
 * Written: 26/11/2020
 * Last updated: 01/12/2020
 *
 * Compilation command: javac -classpath .:stdlib.jar AntColonies.java
 * Execution command: java -classpath .:stdlib.jar AntColonies 10 20 R 0
 *
 * Implementation of the CellGrid Object.
 *
 */
public class CellGrid {

	private int size; // Grid size.
	private Cell[][] grid; // Cells that constitute a grid.

	/**
	 * The CellGrid Object constructor. All of the class private variables are
	 * initialised to their default values. The grid in which the ants will be
	 * placed on is created here and it constitutes of a 2D array(NxN) of the Cell
	 * object.
	 */
	public CellGrid(int N) {
		size = N;
		grid = new Cell[N][N];
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++)
				grid[i][j] = new Cell();
	}

	/**
	 * Return the size of the grid.
	 * 
	 * @return Grid size.
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Returned cell located at row i and column j.
	 * 
	 * @param i
	 *            Row of desired cell.
	 * @param j
	 *            Column of desired cell.
	 * @return Cell at row i and column j.
	 */
	public Cell getCell(int i, int j) {
		return grid[i][j];
	}

	/**
	 * Places a nest at point (x,y) which is inhabited by the ant in given array
	 * antIds.
	 * 
	 * @param x
	 *            Row of nest to be placed.
	 * @param y
	 *            Column of nest to be placed.
	 * @param antIds
	 *            Array with the IDs of the ants that inhabit a nest.
	 */
	public void putNest(int x, int y, int[] antIds) {
		grid[x][y].putNest();
		for (int ant : antIds)
			grid[x][y].addAnt(ant);
	}

	/**
	 * Places a given number of seeds(food) at the point (x,y).
	 * 
	 * @param x
	 *            Row of cell in which food will be placed.
	 * @param y
	 *            Column of cell in which food will be placed.
	 * @param food
	 *            Number of food units to be placed in (x,y).
	 */
	public void putFood(int x, int y, int food) {
		grid[x][y].putFood(food);
	}

	/**
	 * Updates the scent units for all if its cells.
	 * 
	 * @param time
	 *            Current point in time.
	 * @param elapsed
	 *            Maximum life time of a scent.
	 */
	public void updateScent(int time, int elapsed) {
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
				grid[i][j].updateScent(time, elapsed);
	}

	/**
	 * Returns whether all the seeds have been collected. For doing this, we simply
	 * check that any non-nest cell has any food on it.
	 * 
	 * @return If all the seeds are at nest-cells only.
	 */
	public boolean allSeedsCollected() {
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
				if (!grid[i][j].withNest() && grid[i][j].hasFood())
					return false;
		return true;
	}

	/**
	 * The toString method gives the external presentation for the Object CellGrid
	 * as a string.
	 */
	public String toString() {
		String s = "\nA " + size + "x" + size + " Terain as follows:\n";
		for (int x = 0; x < size; x++)
			for (int y = 0; y < size; y++)
				if (grid[x][y].hasFood() || grid[x][y].withNest() || grid[x][y].getScent() > 0 || grid[x][y].hasAnts())
					s += "Cell(" + x + "," + y + "):\n" + grid[x][y];
		s += "\n\nAll other cells are empty.\n\n";
		return s;
	}

	/**
	 * Gives the graphic presentation of the CellGrid Object.
	 */
	public void draw() {
		StdDraw.setXscale(0.0, (double) size);
		StdDraw.setYscale(0.0, (double) size);
		StdDraw.clear(StdDraw.GRAY.darker().darker());
		StdDraw.setPenColor(StdDraw.GRAY.brighter().brighter());
		StdDraw.setPenRadius(0.05 / size);
		for (int d = 0; d <= size; d++)
			StdDraw.line((double) d, 0.0, (double) d, (double) size);
		for (int d = 0; d <= size; d++)
			StdDraw.line(0.0, (double) d, (double) size, (double) d);

		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++) {
				// Draws the white nest with the black food source on top.
				if (getCell(i, j).withNest() && getCell(i, j).hasFood()) {
					StdDraw.setPenRadius(0.8 / size);
					StdDraw.setPenColor(StdDraw.WHITE);
					StdDraw.point((double) i, (double) j);
					StdDraw.setPenRadius(0.6 / size);
					StdDraw.setPenColor(StdDraw.BLACK);
					StdDraw.point((double) i, (double) j);
				}
				// Draws the white nest.
				if (getCell(i, j).withNest() && !getCell(i, j).hasFood()) {
					StdDraw.setPenRadius(0.8 / size);
					StdDraw.setPenColor(StdDraw.WHITE);
					StdDraw.point((double) i, (double) j);
				}
				// Draws the black food source.
				if (getCell(i, j).hasFood() && !getCell(i, j).withNest()) {
					StdDraw.setPenRadius(0.6 / size);
					StdDraw.setPenColor(StdDraw.BLACK);
					StdDraw.point((double) i, (double) j);
				}
			}
	}

}