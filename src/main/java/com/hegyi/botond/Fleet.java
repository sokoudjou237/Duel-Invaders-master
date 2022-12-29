package com.hegyi.botond;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.media.AudioClip;
import com.hegyi.botond.interfaces.canShoot;
import com.hegyi.botond.interfaces.checker;
import com.hegyi.botond.interfaces.moveable;
import com.hegyi.botond.interfaces.renderable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Fleet implements checker, moveable, renderable, canShoot {
	private double yy;
	private List<MovingGameObject> invaders;

	private List<MovingGameObject> invaders2;
	private List<MovingGameObject> bullets;

	private List<MovingGameObject> bullets2;
	private GameObject destroyedImage;
	private AudioClip invaderDieSound;
	private AudioClip bulletDestroyedSound;
	private boolean canAttack = false;
	private int numberOfBullet = 2;

	public Fleet(String filename) {
		init(filename);
	}

	public List<MovingGameObject> getInvaders() {
		return invaders;
	}

	public List<MovingGameObject> getInvaders2() {
		return invaders2;
	}

	public List<MovingGameObject> getBullets() {
		return bullets;
	}

	public List<MovingGameObject> getBullets2() {
		return bullets2;
	}

	public void setCanAttack(boolean value) {
		canAttack = value;
	}

	public boolean canAttack() {
		return canAttack;
	}

	private void initInvaderBullets() {
		bullets = new ArrayList<>();
		for (int i = 0; i < 3; ++i) {
			MovingGameObject temp = new MovingGameObject("images/alien_shot.png");
			temp.setAlive(false);
			temp.setSpeed(7);

			bullets.add(temp);
		}

		bullets2 = new ArrayList<>();
		for (int i = 0; i < 3; ++i) {
			MovingGameObject temp2 = new MovingGameObject("images/alien_shot.png");
			temp2.setAlive(false);
			temp2.setSpeed(7);

			bullets2.add(temp2);
		}
	}

	private void initBulletDestroyedSound() {
		bulletDestroyedSound = new AudioClip(getClass().getClassLoader().getResource("sounds/hitmarkerSound.mp3").toString());
		bulletDestroyedSound.setVolume(0.15);
	}

	private void initInvaderDestroyedSound() {
		invaderDieSound = new AudioClip(getClass().getClassLoader().getResource("sounds/invaderkilled.mp3").toString());
		invaderDieSound.setVolume(0.15);
	}

	private void initInvaders(String filename) {
		invaders = new ArrayList<>();
		for (int i = 0; i < 10; ++i) {
			for (int j = 0; j < 4; ++j) {
				MovingGameObject temp = new MovingGameObject(filename, 30, 30);
				temp.setPosition(2 + ((temp.getWidth() + 15) * i), 500 + ((temp.getHeight() + 15) * j));
				temp.setAlive(true);
				temp.setSpeed(500);
				temp.setMovingRight(true);

				invaders.add(temp);
			}
		}
		invaders2 = new ArrayList<>();
		for (int i = 0; i < 10; ++i) {
			for (int j = 0; j < 4; ++j) {
				MovingGameObject temp2 = new MovingGameObject(filename, 30, 30);
				temp2.setPosition(2 + ((temp2.getWidth() + 15) * i), 300 + ((temp2.getHeight() + 15) * j));
				temp2.setAlive(true);
				temp2.setSpeed(500);
				temp2.setMovingRight(true);

				invaders2.add(temp2);
			}
		}
	}

	private void init(String filename) {
		destroyedImage = new MovingGameObject("images/destroyed.png");

		initInvaderDestroyedSound();
		initBulletDestroyedSound();

		initInvaderBullets();

		initInvaders(filename);
	}

	public boolean intersectWithInvader(MovingGameObject bullet, GraphicsContext gc) {
		for (MovingGameObject invader : invaders) {
			if (invader.intersects(bullet) && invader.isAlive() && bullet.isAlive()) {
				invader.die();
				bullet.die();

				destroyedImage.setPosition(invader.getPositionX(), invader.getPositionY());
				destroyedImage.setAlive(true);
				destroyedImage.render(gc);

				invaderDieSound.play();

				return true;
			}
		}

		for (MovingGameObject invader2 : invaders2) {
			if (invader2.intersects(bullet) && invader2.isAlive() && bullet.isAlive()) {
				invader2.die();
				bullet.die();

				destroyedImage.setPosition(invader2.getPositionX(), invader2.getPositionY());
				destroyedImage.setAlive(true);
				destroyedImage.render(gc);

				invaderDieSound.play();

				return true;
			}
		}
		return false;
	}

	public void intersectWithBullet(MovingGameObject bullet, GraphicsContext gc) {
		for (MovingGameObject v : bullets) {
			if (v.intersects(bullet) && v.isAlive() && bullet.isAlive()) {
				v.die();
				bullet.die();
				bulletDestroyedSound.play();
				return;
			}
		}

		for (MovingGameObject v : bullets2) {
			if (v.intersects(bullet) && v.isAlive() && bullet.isAlive()) {
				v.die();
				bullet.die();
				bulletDestroyedSound.play();
				return;
			}
		}
	}

	public boolean intersect(MovingGameObject bullet, GraphicsContext gc) {
		boolean result = intersectWithInvader(bullet, gc);
		intersectWithBullet(bullet, gc);
		if (result) {
			canAttack = true;
			newFleet();
		}

		return result;
	}

	private void newFleet() {
		invaders = invaders.stream()
				.filter(MovingGameObject::isAlive)
				.collect(Collectors.toList());
		if (invaders.size() < numberOfBullet) {
			numberOfBullet = invaders.size();
		}
	}

	public void intersect(Ship player,Ship player2, GraphicsContext gc) {
		for (int i = 0; i < 3; i++) {
			if (player.intersects(bullets.get(i))) {
				bullets.get(i).die();
				player.die();
				player.getDestroyedImage().render(gc);
			}
		}

		for (int i = 0; i < 3; i++) {
			if (player2.intersects(bullets2.get(i))) {
				bullets2.get(i).die();
				player2.die();
				player2.getDestroyedImage().render(gc);
			}
		}
	}

	public boolean isDestroyed() {
		return invaders.isEmpty();
	}

	public boolean isDestroyed2() {
		return invaders2.isEmpty();
	}

	@Override
	public void render(GraphicsContext gc) {
		for (MovingGameObject v : invaders) {
			v.render(gc);
		}

		for (MovingGameObject v : bullets) {
			v.render(gc);
		}

		for (MovingGameObject v : invaders2) {
			v.render(gc);
		}

		for (MovingGameObject v : bullets2) {
			v.render(gc);
		}

	}



	public boolean CheckGameOver() {
		for (MovingGameObject invader : invaders) {
			if (invader.getPositionY() >= 915) {
				for (MovingGameObject v : invaders) {
					v.setMovingDown(false);
					v.setMovingLeft(false);
					v.setMovingRight(false);
					v.setMovingUp(false);
				}
				return true;
			}
		}
		return false;
	}
	public boolean CheckGameOver2() {
		for (MovingGameObject invader2 : invaders2) {
			if (invader2.getPositionY() < 50) {
				for (MovingGameObject v : invaders2) {
					v.setMovingDown(false);
					v.setMovingLeft(false);
					v.setMovingRight(false);
					v.setMovingUp(false);
				}
				return true;
			}
		}

		return false;
	}


	@Override
	public void check() {

		for (MovingGameObject invader : invaders) {
			if (invader.getPositionX() > Game.WIDTH - invader.getWidth() &&
					invader.isMovingRight()) {
				yy = invader.getPositionY();
				for (MovingGameObject v : invaders) {
					v.setMovingDown(true);
				}
				break;
			} else if (invader.getPositionX() > Game.WIDTH - invader.getWidth() &&
					invader.isMovingDown()) {
				if (invader.getPositionY() - yy > 150) {
					for (MovingGameObject v : invaders) {
						v.setMovingLeft(true);
					}
					break;
				}

			} else {
				if (invader.getPositionX() < 0 &&
						invader.isMovingLeft()) {
					yy = invader.getPositionY();
					for (MovingGameObject v : invaders) {
						v.setMovingDown(true);
					}
					break;
				} else if (invader.getPositionX() < 0 &&
						invader.isMovingDown()) {
					if (invader.getPositionY() - yy > 150) {
						for (MovingGameObject v : invaders) {
							v.setMovingRight(true);
						}
						break;
					}
				}
			}
		}
		for (int i = 0; i < numberOfBullet; i++) {
			if (bullets.get(i).getPositionY() > Game.HEIGHT) {
				bullets.get(i).setMovingDown(true);
				bullets.get(i).die();
			}
		}

		for (MovingGameObject invader2 : invaders2) {
			if (invader2.getPositionX() > Game.WIDTH - invader2.getWidth() &&
					invader2.isMovingRight()) {
				yy = invader2.getPositionY();
				for (MovingGameObject v : invaders2) {
					v.setMovingUp(true);
				}
				break;
			} else if (invader2.getPositionX() > Game.WIDTH - invader2.getWidth() &&
					invader2.isMovingUp()) {
				if (yy - invader2.getPositionY() > 0) {
					for (MovingGameObject v : invaders2) {
						v.setMovingLeft(true);
					}
					break;
				}
			} else {
				if (invader2.getPositionX() < 0 &&
						invader2.isMovingLeft()) {
					yy = invader2.getPositionY();
					for (MovingGameObject v : invaders2) {
						v.setMovingUp(true);
					}
					break;
				} else if (invader2.getPositionX() < 0 &&
						invader2.isMovingUp()) {
					if (yy - invader2.getPositionY() > 0) {
						for (MovingGameObject v : invaders2) {
							v.setMovingRight(true);
						}
						break;
					}
				}
			}
		}

		for (int i = 0; i < numberOfBullet; i++) {
			if (bullets2.get(i).getPositionY() < 0) {
				bullets2.get(i).setMovingUp(true);
				bullets2.get(i).die();
			}
		}
	}



	@Override
	public void update() {
		for (MovingGameObject v : invaders) {
			v.update();
		}
		for (MovingGameObject v : bullets) {
			v.update();
		}


		for (MovingGameObject v : invaders2) {
			v.update();
		}
		for (MovingGameObject v : bullets2) {
			v.update();
		}
	}

	@Override
	public void update(double time) {
		for (MovingGameObject v : invaders) {
			v.update(time);
		}
		for (MovingGameObject v : bullets) {
			v.update();
		}


		for (MovingGameObject v : invaders2) {
			v.update(time);
		}
		for (MovingGameObject v : bullets2) {
			v.update();
		}
	}

	@Override
	public void shoot() {

		if (canAttack) {
			Random rand = new Random();
			for (int i = 0; i < numberOfBullet; ++i) {
				if (!bullets.get(i).isAlive() ) {
					int x = rand.nextInt(invaders.size());
					while (!invaders.get(x).isAlive()) {
						x = rand.nextInt(invaders.size());
					}
					bullets.get(i).setAlive(true);
					bullets.get(i).setPosition(invaders.get(x).getPositionX() + invaders.get(x).getWidth() / 2,
							invaders.get(x).getPositionY());
					bullets.get(i).setMovingDown(true);

				}
			}

			Random rand2 = new Random();
			for (int i = 0; i < numberOfBullet; ++i) {
				if (!bullets2.get(i).isAlive()) {
					int y = rand2.nextInt(invaders2.size());
					while (!invaders2.get(y).isAlive()) {
						y = rand2.nextInt(invaders2.size());
					}
					bullets2.get(i).setAlive(true);
					bullets2.get(i).setPosition(invaders2.get(y).getPositionX() + invaders2.get(y).getWidth() / 2,
							invaders2.get(y).getPositionY());
					bullets2.get(i).setMovingUp(true);
				}

			}
		}
	}
}


