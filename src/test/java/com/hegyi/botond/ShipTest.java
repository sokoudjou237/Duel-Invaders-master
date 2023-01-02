package com.hegyi.botond;

import javafx.embed.swing.JFXPanel;
import javafx.geometry.Point2D;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class ShipTest {
	@Test
	public void testInit() {
		JFXPanel jfxPanel = new JFXPanel();
		Ship ship = new Ship("images/shipSkin.png");

		Point2D expPos = new Point2D((Game.WIDTH - ship.getWidth())/2,
				Game.HEIGHT - ship.getHeight() + 5);

		Assert.assertEquals(expPos, ship.getPosition());
		Assert.assertEquals(200, ship.getSpeed());
		Assert.assertTrue(ship.isAlive());
	}

	@Test
	public void testShoot() {
		JFXPanel jfxPanel = new JFXPanel();
		Ship ship = new Ship("images/shipSkin.png");
		ship.shoot();
		ship.shoot_if_player1_dies();
		ship.shoot_if_player2_dies();

		Assert.assertTrue(ship.getBullet().isAlive());
		Assert.assertTrue(ship.getBullet().isMovingUp());

		Assert.assertTrue(ship.getBullet2().isAlive());
		Assert.assertTrue(ship.getBullet2().isMovingDown());
	}

	@Test
	public void testCheckPlayerShoot() {
		JFXPanel jfxPanel = new JFXPanel();
		Ship ship = new Ship("images/shipSkin.png");
		ship.shoot();
		ship.shoot_if_player1_dies();
		ship.shoot_if_player2_dies();
		ship.getBullet().setPosition(10, -10);
		ship.getBullet2().setPosition(10, -10);
		ship.check();

		Assert.assertFalse(ship.getBullet().isAlive());
		Assert.assertFalse(ship.getBullet2().isAlive());
	}

	@Test
	public void testCheckPlayerDirections() {
		JFXPanel jfxPanel = new JFXPanel();
		Ship ship = new Ship("images/shipSkin.png");
		ship.setMovingRight(true);
		ship.setPosition(Game.WIDTH+200, 0);
		ship.check();

		Assert.assertFalse(ship.isMovingRight());

		ship.setMovingLeft(true);
		ship.setPosition(-200, 0);
		ship.check();

		Assert.assertFalse(ship.isMovingLeft());
	}
}