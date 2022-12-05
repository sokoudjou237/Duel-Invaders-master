package com.hegyi.botond;

import javafx.embed.swing.JFXPanel;
import javafx.geometry.Rectangle2D;
import org.junit.Assert;
import org.junit.Test;

public class GameObjectTest {
	@Test
	public void testPosition() {
		GameObject gameObject = new GameObject();

		gameObject.setPosition(1, 1);

		double expX = 1;
		double expY = 1;

		Assert.assertEquals(expX, gameObject.getPositionX(), 0.1);
		Assert.assertEquals(expY, gameObject.getPositionY(), 0.1);

	}

	@Test
	public void testSize() {
		JFXPanel jfxPanel = new JFXPanel();
		GameObject gameObject = new GameObject("images/playerDestroyedImage.png");
		gameObject.setWidth(50);
		gameObject.setHeight(60);

		double expWidth = 50;
		double expHeight = 60;

		Assert.assertEquals(expWidth, gameObject.getWidth(), 0.1);
		Assert.assertEquals(expHeight, gameObject.getHeight(), 0.1 );
	}

	@Test
	public void testLife() {
		GameObject gameObject = new GameObject();
		gameObject.setAlive(true);

		Assert.assertTrue(gameObject.isAlive());

		gameObject.die();

		Assert.assertFalse(gameObject.isAlive());
	}

	@Test
	public void testBoundary() {
		JFXPanel jfxPanel = new JFXPanel();
		GameObject gameObject = new GameObject("images/playerDestroyedImage.png", 50, 60);
		gameObject.setPosition(10, 25);

		Rectangle2D expResult = new Rectangle2D(10, 25, 50, 60);

		Assert.assertEquals(expResult, gameObject.getBoundary());
	}

	@Test
	public void testIntersect() {
		JFXPanel jfxPanel = new JFXPanel();
		GameObject gameObject1 = new GameObject("images/playerDestroyedImage.png", 50, 60);
		GameObject gameObject2 = new GameObject("images/playerDestroyedImage.png", 50, 60);
		GameObject gameObject3 = new GameObject("images/playerDestroyedImage.png", 50, 60);

		gameObject2.setPosition(10, 10);
		gameObject3.setPosition(100, 100);

		Assert.assertTrue(gameObject1.intersects(gameObject2));
		Assert.assertFalse(gameObject1.intersects(gameObject3));
	}
}