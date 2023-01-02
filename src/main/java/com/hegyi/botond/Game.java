package com.hegyi.botond;

import javafx.animation.AnimationTimer;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class Game extends Canvas {
	/*
	 Classe de jeu qui étend la classe Canvas de JavaFX
	 Canvas est utilisé pour dessiner des éléments graphiques sur une fenêtre
	 implémente les fonctionnalités de jeu, telles que l'initialisation des éléments du jeu,
	 la mise à jour et le rendu des éléments du jeu, et la gestion de la fin de jeu
	 */

	private GraphicsContext gc;
	// Contexte graphique utilisé pour dessiner des éléments sur le Canvas
	private Ship player;
	// Objet de type Ship qui représente le joueur
	private Ship player2;
	// Objet de type Ship qui représente le deuxième joueur
	private Fleet fleet;
	// Objet de type Fleet qui représente une flotte d'invaders
	private GameObject background;
	// Objet de type GameObject qui représente l'arrière-plan du jeu
	private double yy;
	private myTimer timer = new myTimer();
	// Objet de type myTimer qui gère le temps de jeu
	private long lastNanoTime;
	// Variable qui stocke le temps en nanosecondes de la dernière frame de jeu
	private boolean inGame;
	// Booléen qui indique si le jeu est en cours ou non
	private int score = 0;
	// Variable qui stocke le score du premier joueur
	private int score2 = 0;
	// Variable qui stocke le score du deuxième joueur

	public static double WIDTH, HEIGHT;

	private Pane root;

	public Game(double width, double height, Pane root) {
		/*
		 Constructeur de la classe Game
         width:largeur de la fenêtre
         height : la hauteur fenêtre
         Pane : le Pane racine du Canvas
		 */
		super(width, height);
		WIDTH = width;
		HEIGHT = height;
		this.root = root;
		initElements();
	}

	public myTimer getTimer() {
		// Retourne l'objet de type myTimer qui gère le temps de jeu
		return timer;
	}

	public Ship getPlayer() {
		// Retourne l'objet de type Ship qui représente le premier joueur
		return player;
	}

	public Ship getPlayer2() {
		// Retourne l'objet de type Ship qui représente le deuxième joueur
		return player2;
	}


	public boolean isInGame() {
		// Retourne la valeur de la variable booléenne inGame
		return inGame;
	}

	public void setInGame(boolean inGame) {
		// Affecte la valeur en entrée à la variable booléenne inGame
		this.inGame = inGame;
	}

	private void initBackground() {
		/*
		Initialisation de l'arrière-plan du jeu
		 */

		background = new GameObject("images/gameWallpaper.jpg");
		background.setPosition(0, (getHeight() - background.getHeight()));
		background.setAlive(true);
	}

	private void initElements() {
		/*
		Initialisation des éléments du jeu
		 */

		gc = this.getGraphicsContext2D();
		gc.setFill(Color.WHITE);
		Font.loadFont(getClass().getClassLoader().getResource("arcadeclassic.ttf").toExternalForm(), 10);
		gc.setFont(new Font("ArcadeClassic", 17));
		inGame = true;

		player = new Ship("images/shipSkin.png");
		player2 = new Ship("images/shipSkin2.png");
		player2.setPosition(512,5);

		fleet = new Fleet("images/invader.png");

		initBackground();

		lastNanoTime = System.nanoTime();

		timer.start();
	}

	public void checkElements() {
		/*
		vérifie l'état des éléments du jeu
		 */

		player.check();
		player2.check();
		fleet.check();
	}

	private void updateElements(double elapsedTime) {
		/*
		Met à jour les éléments du jeu
		 */
		if (inGame) {
			player.update(elapsedTime);
			player2.update(elapsedTime);

			player.getBullet().update();
			player2.getBullet2().update();

			fleet.update(elapsedTime);
		}
	}

	private void renderScore() {
		// affiche le score à l'écran en cour de jeu
		if (player.isAlive()) {
			gc.fillText("Your score: " + Integer.toString(score), 3, 1000);
		}

		if (player2.isAlive()) {
			gc.fillText("Your score: " + Integer.toString(score2), 3, 15);
		}
	}

	private void renderElements() {
		//Affiche les éléments du jeu à l'écran
		background.render(gc);
		if (fleet.intersect(player.getBullet(), gc)) {
			score += 100;
		}
		if (fleet.intersect(player2.getBullet2(), gc)) {
			score2 += 100;
		}

		fleet.intersect(player,player2, gc);
		fleet.render(gc);

		player.render(gc);
		player.getBullet().render(gc);

		player2.render(gc);
		player2.getBullet2().render(gc);

		renderScore();
	}


	public void gameOver(int score) {
		/*
		Affiche un message de fin de jeu et le score final
		 */
		gc.setFill(Color.WHITE);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setTextBaseline(VPos.CENTER);
		gc.setFont(new Font(gc.getFont().getName(), 30));

		if(fleet.getInvaders().isEmpty() && fleet.isDestroyed()){
			gc.fillText("You won!\n",WIDTH / 2.0, HEIGHT / 2.5);
		}else {
			gc.fillText("Game over!\n",WIDTH / 2.0, HEIGHT / 2.5);
		}
		gc.fillText("Player2 score:  " + score2, WIDTH / 1.5, HEIGHT / 2.0);
		gc.fillText("Player1 score:  " + score, WIDTH / 4.0, HEIGHT / 2.0);
		if (score2 > score) {
			gc.fillText("Player2  win\n",WIDTH / 2.0, HEIGHT / 1.5);
		}else if(score2 < score){
			gc.fillText("Player1  win\n",WIDTH / 2.0, HEIGHT / 1.5);
		}else {
			gc.fillText("You won\n",WIDTH / 2.0, HEIGHT / 1.5);
		}
	}

	public class myTimer extends AnimationTimer {
		/*
		Classe qui gère la logique du jeu, elle hérite de
		la classe AnimationTimer de JavaFX
		 */
		@Override
		public void handle(long currentNanoTime) {
			double elapsedTime = (currentNanoTime - lastNanoTime) / 1000000000.0;
			lastNanoTime = currentNanoTime;

			gc.clearRect(0, 0, getWidth(), getHeight());

			checkElements();

			updateElements(elapsedTime);

			renderElements();

			if (!player.isAlive()) {
				fleet.shoot_if_player1_dies();

			}

			else if (!player2.isAlive()) {
				fleet.shoot_if_player2_dies();
			}
			else {fleet.shoot();}
			/*
			si les deux joueurs sont mort,
			si les invaders sont plus près des joueurs
			alors le jeu est terminer
			 */
			if ((!player.isAlive() && !player2.isAlive()) || (fleet.CheckGameOver() && fleet.CheckGameOver2())
			|| (!player.isAlive() && fleet.CheckGameOver2()) || (!player2.isAlive() && fleet.CheckGameOver()) ) {
				inGame = false;
				gameOver(score);
				this.stop();
			}

			if (fleet.CheckGameOver()){
				player.setAlive(false);
				player.getBullet().setAlive(false);
				player.getBullet().setSpeed(0);
				player.getBullet().setVelocity(0, 0);
				player.setPosition(1050, 1050);
			}

			if (fleet.CheckGameOver2()){
				player2.setAlive(false);
				player2.getBullet().setAlive(false);
				player2.getBullet().setSpeed(0);
				player2.getBullet().setVelocity(0, 0);
				player2.setPosition(1050, 1050);

			}
			if (!player.isAlive()) {
				player.getBullet().setAlive(false);
				player.getBullet().setSpeed(0);
				player.getBullet().setVelocity(0, 0);
				player.setPosition(1050, 1050);

				for (MovingGameObject invader : fleet.getInvaders()) {
					if (invader.getPositionX() > Game.WIDTH - invader.getWidth() &&
							invader.isMovingRight()) {
						yy = invader.getPositionY();
						for (MovingGameObject v : fleet.getInvaders()) {
							v.setMovingUp(true);
						}
						break;
					} else if (invader.getPositionX() > Game.WIDTH - invader.getWidth() &&
							invader.isMovingUp()) {
						if (yy - invader.getPositionY() > 0) {
							for (MovingGameObject v : fleet.getInvaders()) {
								v.setMovingLeft(true);
							}
							break;
						}
					} else {
						if (invader.getPositionX() < 0 &&
								invader.isMovingLeft()) {
							yy = invader.getPositionY();
							for (MovingGameObject v : fleet.getInvaders()) {
								v.setMovingUp(true);
							}
							break;
						} else if (invader.getPositionX() < 0 &&
								invader.isMovingUp()) {
							if (yy - invader.getPositionY() > 0) {
								for (MovingGameObject v : fleet.getInvaders()) {
									v.setMovingRight(true);
								}
								break;
							}
						}
					}
				}
			}

			if (!player2.isAlive()) {
				player2.getBullet2().setAlive(false);
				player2.getBullet2().setSpeed(1);
				player2.getBullet2().setVelocity(0, 0);
				player2.setPosition(1050, 1050);

				for (MovingGameObject invader2 : fleet.getInvaders2()) {
					if (invader2.getPositionX() > Game.WIDTH - invader2.getWidth() &&
							invader2.isMovingRight()) {
						yy = invader2.getPositionY();
						for (MovingGameObject v : fleet.getInvaders2()) {
							v.setMovingDown(true);
						}
						break;
					} else if (invader2.getPositionX() > Game.WIDTH - invader2.getWidth() &&
							invader2.isMovingDown()) {
						if (invader2.getPositionY() - yy > 150) {
							for (MovingGameObject v : fleet.getInvaders2()) {
								v.setMovingLeft(true);
							}
							break;
						}

					} else {
						if (invader2.getPositionX() < 0 &&
								invader2.isMovingLeft()) {
							yy = invader2.getPositionY();
							for (MovingGameObject v : fleet.getInvaders2()) {
								v.setMovingDown(true);
							}
							break;
						} else if (invader2.getPositionX() < 0 &&
								invader2.isMovingDown()) {
							if (invader2.getPositionY() - yy > 150) {
								for (MovingGameObject v : fleet.getInvaders2()) {
									v.setMovingRight(true);
								}
								break;
							}
						}
					}

				}
			}

			if (fleet.isDestroyed() && fleet.isDestroyed2() ){
				inGame = false;
//				this.stop();
			}

//			if (fleet.isDestroyed()) {
//				inGame = false;
//				this.stop();
//			}
//
//			if (fleet.isDestroyed2()) {
//				inGame = false;
//				this.stop();
//			}
		}
	}
}