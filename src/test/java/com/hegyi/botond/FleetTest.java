package com.hegyi.botond;

import javafx.embed.swing.JFXPanel;
import javafx.scene.canvas.Canvas;
import org.junit.Assert;
import org.junit.Test;

public class FleetTest {
	@Test
	public void testCheckInvadersMoving() {
		JFXPanel jfxPanel = new JFXPanel();
		Fleet fleet = new Fleet("images/invader.png");
		fleet.getInvaders().get(3).setPosition(Game.WIDTH+200, 0);
		fleet.check();

		Assert.assertTrue(fleet.getInvaders().get(3).isMovingLeft());

		fleet.getInvaders().get(3).setPosition(-200, 0);
		fleet.check();

		Assert.assertTrue(fleet.getInvaders().get(3).isMovingRight());
	}

	@Test
	public void testCheckBulletMoving() {
		JFXPanel jfxPanel = new JFXPanel();
		Fleet fleet = new Fleet("images/invader.png");
		fleet.setCanAttack(true);
		fleet.shoot();
		fleet.getBullets().get(1).setPosition(0, Game.HEIGHT+200);
		fleet.check();

		Assert.assertFalse(fleet.getBullets().get(1).isAlive());
	}

	@Test
	public void testIntersect() {
		JFXPanel jfxPanel = new JFXPanel();
		Fleet fleet = new Fleet("images/invader.png");

		MovingGameObject mgo = fleet.getInvaders().get(3);

		fleet.intersect(mgo, (new Canvas()).getGraphicsContext2D());

		Assert.assertFalse(mgo.isAlive());
		Assert.assertTrue(fleet.canAttack());
	}
}