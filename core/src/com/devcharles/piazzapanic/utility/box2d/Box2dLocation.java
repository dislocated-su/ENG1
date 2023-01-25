package com.devcharles.piazzapanic.utility.box2d;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;

public class Box2dLocation implements Location<Vector2> {

	Vector2 position;
	float orientation;

	public Box2dLocation () {
		this.position = new Vector2();
		this.orientation = 0;
	}

    public Box2dLocation(Vector2 position, float orientation) {
        this.position = position;
        this.orientation = orientation;
    }

	@Override
	public Vector2 getPosition () {
		return position;
	}

	@Override
	public float getOrientation () {
		return orientation;
	}

	@Override
	public void setOrientation (float orientation) {
		this.orientation = orientation;
	}

	@Override
	public Location<Vector2> newLocation () {
		return new Box2dLocation();
	}

	@Override
	public float vectorToAngle (Vector2 vector) {
        return (float)Math.atan2(-vector.x, vector.y);
	}

	@Override
	public Vector2 angleToVector (Vector2 outVector, float angle) {
        outVector.x = -(float)Math.sin(angle);
		outVector.y = (float)Math.cos(angle);
		return outVector;
	}

}