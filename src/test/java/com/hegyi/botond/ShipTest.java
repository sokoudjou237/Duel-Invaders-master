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

		Assert.assertTrue(ship.getBullet().isAlive());
		Assert.assertTrue(ship.getBullet().isMovingUp());
	}

	@Test
	public void testCheckPlayerShoot() {
		JFXPanel jfxPanel = new JFXPanel();
		Ship ship = new Ship("images/shipSkin.png");
		ship.shoot();
		ship.getBullet().setPosition(10, -10);
		ship.check();

		Assert.assertFalse(ship.getBullet().isAlive());
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