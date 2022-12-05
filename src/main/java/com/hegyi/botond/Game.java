package com.hegyi.botond;

import javafx.animation.AnimationTimer;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class Game extends Canvas {
	private GraphicsContext gc;

	private Ship player;
	private Fleet fleet;

	private Ship player2;

	private Fleet fleet2;

	private GameObject background;

	private myTimer timer = new myTimer();
	private long lastNanoTime;
	private boolean inGame;

	private int score = 0;

	private int score2 = 0;


	public static double WIDTH, HEIGHT;

	private Pane root;

	public Game(double width, double height, Pane root) {
		super(width, height);
		WIDTH = width;
		HEIGHT = height;
		this.root = root;
		initElements();
	}

	public myTimer getTimer() {
		return timer;
	}

	public Ship getPlayer() {
		return player;
	}

	public Ship getPlayer2() {
		return player2;
	}

	public boolean isInGame() {
		return inGame;
	}

	public void setInGame(boolean inGame) {
		this.inGame = inGame;
	}

	private void initBackground() {
		background = new GameObject("images/gameWallpaper.jpg");
		background.setPosition(0, (getHeight() - background.getHeight()));
		background.setAlive(true);
	}

	private void initElements() {
		gc = this.getGraphicsContext2D();
		gc.setFill(Color.WHITE);
		Font.loadFont(getClass().getClassLoader().getResource("arcadeclassic.ttf").toExternalForm(), 10);
		gc.setFont(new Font("ArcadeClassic", 17));
		inGame = true;

		player = new Ship("images/shipSkin.png");

		player2 = new Ship("images/shipSkin.png");


		fleet = new Fleet("images/invader.png");
		fleet2 = new Fleet("images/invader.png");

		initBackground();

		lastNanoTime = System.nanoTime();

		timer.start();
	}

	public void checkElements() {
		player.check();
		fleet.check();

		player2.check();
		fleet2.check();

	}

	private void updateElements(double elapsedTime) {
		if (inGame) {
			player.update(elapsedTime);
			player.getBullet().update();
			fleet.update(elapsedTime);

			player2.update(elapsedTime);
			player2.getBullet().update();
			fleet2.update(elapsedTime);
		}
	}

	private void renderScore() {
		if (player.isAlive()) {
			gc.fillText("Your score: " + Integer.toString(score), 3, 15);
		}
		if (player2.isAlive()) {
			gc.fillText("Your score2: " + Integer.toString(score2), 3, 15);
		}
	}

	private void renderElements() {
		background.render(gc);

		if (fleet.intersect(player.getBullet(), gc)) {
			score += 100;
		}
		if (fleet2.intersect(player2.getBullet(), gc)) {
			score2 += 100;
		}
		fleet.intersect(player, gc);

		fleet2.intersect(player2, gc);


		fleet.render(gc);
		fleet2.render(gc);


		player.render(gc);
		player.getBullet().render(gc);

		player2.render(gc);
		player2.getBullet().render(gc);

		renderScore();
	}

	private void gameOver(int scor) {
		gc.setFill(Color.WHITE);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setTextBaseline(VPos.CENTER);
		gc.setFont(new Font(gc.getFont().getName(), 30));

		gc.fillText("Game over!\nYour score: " + scor, WIDTH / 2.0, HEIGHT / 2.0);

		gc.fillText("Game over!\nYour score: " + scor, WIDTH / 2.0, HEIGHT / 2.0);
	}

	public class myTimer extends AnimationTimer {
		@Override
		public void handle(long currentNanoTime) {
			double elapsedTime = (currentNanoTime - lastNanoTime) / 1000000000.0;
			lastNanoTime = currentNanoTime;

			gc.clearRect(0, 0, getWidth(), getHeight());

			checkElements();

			updateElements(elapsedTime);

			renderElements();

			fleet.shoot();
			fleet2.shoot();

			if (!player.isAlive()) {
				inGame = false;
				gameOver(score);
				this.stop();
			}

			if (!player2.isAlive()) {
				inGame = false;
				gameOver(score2);
				this.stop();
			}

			if (fleet.isDestroyed()) {
				System.out.println("You win");
				inGame = false;
				this.stop();
			}

			if (fleet2.isDestroyed()) {
				System.out.println("You win");
				inGame = false;
				this.stop();
			}
		}
	}
}