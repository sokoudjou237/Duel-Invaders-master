package com.hegyi.botond;

import javafx.geometry.Point2D;
import org.junit.Assert;
import org.junit.Test;

public class MovingGameObjectTest {
	@Test
	public void testVelocity() {
		MovingGameObject mgo = new MovingGameObject();
		mgo.setVelocity(15, 20);

		double expVelX = 15;
		double expVelY = 20;

		Assert.assertEquals(expVelX, mgo.getVelocityX(), 0.1);
		Assert.assertEquals(expVelY, mgo.getVelocityY(), 0.1);
	}

	@Test
	public void testMovingRight() {
		MovingGameObject mgo = new MovingGameObject();
		mgo.setSpeed(10);
		mgo.setAlive(true);

		mgo.setMovingRight(true);
		mgo.update();

		Point2D expPosition = new Point2D(10, 0);

		Assert.assertEquals(expPosition, mgo.getPosition());
		Assert.assertTrue(mgo.isMovingRight());
	}

	@Test
	public void testMovingLeft() {
		MovingGameObject mgo = new MovingGameObject();
		mgo.setSpeed(10);
		mgo.setAlive(true);

		mgo.setMovingLeft(true);
		mgo.update();

		Point2D expPosition = new Point2D(-10, 0);

		Assert.assertEquals(expPosition, mgo.getPosition());
		Assert.assertTrue(mgo.isMovingLeft());
	}

	@Test
	public void testMovingUp() {
		MovingGameObject mgo = new MovingGameObject();
		mgo.setSpeed(10);
		mgo.setAlive(true);

		mgo.setMovingUp(true);
		mgo.update();

		Point2D expPosition = new Point2D(0, -10);

		Assert.assertEquals(expPosition, mgo.getPosition());
		Assert.assertTrue(mgo.isMovingUp());
	}

	@Test
	public void testMovingDown() {
		MovingGameObject mgo = new MovingGameObject();
		mgo.setSpeed(10);
		mgo.setAlive(true);

		mgo.setMovingDown(true);
		mgo.update();

		Point2D expPosition = new Point2D(0, 10);

		Assert.assertEquals(expPosition, mgo.getPosition());
		Assert.assertTrue(mgo.isMovingDown());
	}
}