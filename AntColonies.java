import java.awt.Color;

public class AntColonies {

	private static final int scentDuration = 50;

	private static boolean someAntsCarryFood(Ant[] ants) {
		for (Ant a : ants)
			if (a.carries_food())
				return true;
		return false;
	}

	private static Picture scaleToHalf(Picture pic) {
		Picture picH = new Picture(pic.width() / 2, pic.height() / 2);
		for (int tx = 0; tx < picH.width(); tx++) {
			for (int ty = 0; ty < picH.height(); ty++) {
				int sx = tx * pic.width() / picH.width();
				int sy = ty * pic.height() / picH.height();
				picH.set(tx, ty, pic.get(sx, sy));
			}
		}
		return picH;
	}

	private static Picture bottomRight(Picture pic, Color c) {
		int w = pic.width(), h = pic.height();
		Picture npic = new Picture(w * 2, h * 2);
		for (int i = 0; i < npic.width(); i++)
			for (int j = 0; j < npic.height(); j++)
				npic.set(i, j, c);
		for (int i = 0; i < pic.width(); i++)
			for (int j = 0; j < pic.height(); j++)
				npic.set(i + w, j + h, pic.get(i, j));
		return npic;
	}

	public static void main(String[] args) {
		CellGrid terain = new CellGrid(5);
		Ant[] redAnts = new Ant[10];
		Ant[] blackAnts = new Ant[10];
		int[] redIds = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		int[] blackIds = { 10, 11, 12, 13, 14, 15, 16, 17, 18, 19 };

		int nx1 = 1, ny1 = 4, nx2 = 4, ny2 = 1;
		for (int i = 0; i < 10; i++) {
			int[] nestPos = { nx1, ny1 };
			redAnts[i] = new Ant(terain, redIds[i], "Red", nestPos);
		}
		terain.putNest(nx1, ny1, redIds);

		for (int i = 0; i < 10; i++) {
			int[] nestPos = { nx2, ny2 };
			blackAnts[i] = new Ant(terain, blackIds[i], "Black", nestPos);
		}
		terain.putNest(nx2, ny2, blackIds);

		int fx1 = 2, fy1 = 2, fx2 = 3, fy2 = 3;
		terain.getCell(fx1, fy1).putFood(Integer.parseInt(args[0]));
		terain.getCell(fx2, fy2).putFood(Integer.parseInt(args[1]));

		boolean showRed;
		if (args[2].equals("R"))
			showRed = true;
		else
			showRed = false;
		int antNo = Integer.parseInt(args[3]);

		int time = 1;

		System.out.println("INITIAL");
		System.out.println(terain);
		terain.draw();
		StdDraw.save("terainStart.jpg");
		while (!terain.allSeedsCollected() || someAntsCarryFood(redAnts) || someAntsCarryFood(blackAnts)) {
			if (showRed) {
				System.out.println("Time " + time + ": " + redAnts[antNo]);
			} else {
				System.out.println("Time " + time + ": " + blackAnts[antNo]);
			}
			for (int i = 0; i < 10; i++) {
				redAnts[i].move(time);
				blackAnts[i].move(time);
			}
			terain.updateScent(time, scentDuration);
			time++;
		}
		time--;

		System.out.println("\nFINAL");
		System.out.println(terain);
		System.out.println("\nTIME = " + time + "\n");
		terain.draw();
		StdDraw.save("terainFinish.jpg");
		Picture picS = new Picture("terainStart.jpg");
		Picture picSH = scaleToHalf(picS);
		Picture picF = new Picture("terainFinish.jpg");
		Picture picFH = bottomRight(scaleToHalf(picF), StdDraw.BLACK);
		picFH.show();
		picSH.show();

	}

}