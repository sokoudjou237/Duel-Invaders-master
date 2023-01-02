package com.hegyi.botond;

import javafx.scene.media.AudioClip;
import com.hegyi.botond.interfaces.checker;
import com.hegyi.botond.interfaces.canShoot;

public class Ship extends MovingGameObject implements checker, canShoot {
	private MovingGameObject bullet;
	private MovingGameObject bullet2;
	private GameObject destroyedImage;
	private AudioClip shootSound;
	private AudioClip explosionSound;
	public Ship(String filename) {
		super(filename, 50, 50);
		init();
	}

	public MovingGameObject getBullet() {
		return bullet;
	}

	public MovingGameObject getBullet2() {
		return bullet2;
	}

	public GameObject getDestroyedImage() {
		return destroyedImage;
	}

	private void initPlayerShoot() {
		bullet = new MovingGameObject("images/ship_shot.png");
		bullet.setSpeed(10);

		bullet2 = new MovingGameObject("images/ship_shot.png");
		bullet2.setSpeed(10);
	}

	private void initShootSound() {
		shootSound = new AudioClip(getClass().getClassLoader().getResource("sounds/shoot.mp3").toString());
		shootSound.setVolume(0.15);
	}

	private void initExplosionSound() {
		explosionSound = new AudioClip(getClass().getClassLoader().getResource("sounds/explosion.mp3").toString());
		explosionSound.setVolume(0.15);
	}

	private void init() {
		initPlayerShoot();
		initShootSound();
		initExplosionSound();

		destroyedImage = new GameObject("images/playerDestroyedImage.png", 50, 50);

		setPosition((Game.WIDTH - getWidth())/2, Game.HEIGHT-getHeight() + 5);
		setSpeed(200);
		setAlive(true);
		if(bullet.getVelocityX()== 0 || bullet.getSpeed() == 1){
//			explosionSound.setVolume(0);
			shootSound.setVolume(0);
		}
		if(bullet2.getVelocityX()== 0 || bullet2.getSpeed() == 1){
//			explosionSound.setVolume(0);
			shootSound.setVolume(0);
		}
	}

	private void checkPlayerShoot() {
		if (bullet.getPositionY() < 0) {
			bullet.die();
		}

		if (bullet2.getPositionY() < 0) {
			bullet2.die();
		}
	}

	@Override
	public void die() {
		super.die();
		destroyedImage.setPosition(getPositionX(), getPositionY());
		destroyedImage.setAlive(true);
		explosionSound.play();
	}

	@Override
	public void check() {
		if ((getPositionX() > Game.WIDTH - getWidth() + 7) &&
				isMovingRight()) {
			setMovingRight(false);
		} else {
			if ((getPositionX() < 0 - 7) &&
					isMovingLeft()) {
				setMovingLeft(false);
			}
		}

		checkPlayerShoot();
	}

	@Override
	public void shoot() {
		if (!bullet.isAlive()) {
			shootSound.play();

			bullet.setAlive(true);
			bullet.setMovingUp(true);

			bullet.setPosition(this.getPositionX() + this.getWidth() / 2 - 3,
					this.getPositionY() - 5);
		}

		if (!bullet2.isAlive()) {
			shootSound.play();

			bullet2.setAlive(true);
			bullet2.setMovingDown(true);
			bullet2.setPosition(this.getPositionX() + this.getWidth() / 2 - 3,
					this.getPositionY() - 5);
		}
	}

}
