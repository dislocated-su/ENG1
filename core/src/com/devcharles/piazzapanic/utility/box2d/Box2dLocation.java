/*******************************************************************************
 * Copyright 2014 davebaol https://github.com/davebaol
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.devcharles.piazzapanic.utility.box2d;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;

/**
 * {@link Vector2} wrapper that contains body orientation.
 */
public class Box2dLocation implements Location<Vector2> {

	Vector2 position;
	float orientation;

	public Box2dLocation () {
		this.position = new Vector2();
		this.orientation = 0;
	}

	/**
	 * Create a new location.
	 * @param position {@link com.badlogic.gdx.physics.box2d.World} position vector.
	 * @param orientation rotation in radians
	 */
    public Box2dLocation(Vector2 position, float orientation) {
        this.position = position;
        this.orientation = orientation;
    }

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.ai.utils.Location#getPosition()
	 */
	@Override
	public Vector2 getPosition () {
		return position;
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.ai.utils.Location#getOrientation()
	 */
	@Override
	public float getOrientation () {
		return orientation;
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.ai.utils.Location#setOrientation(float)
	 */
	@Override
	public void setOrientation (float orientation) {
		this.orientation = orientation;
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.ai.utils.Location#newLocation()
	 */
	@Override
	public Location<Vector2> newLocation () {
		return new Box2dLocation();
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.ai.utils.Location#vectorToAngle(com.badlogic.gdx.math.Vector)
	 */
	@Override
	public float vectorToAngle (Vector2 vector) {
        return (float)Math.atan2(-vector.x, vector.y);
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.ai.utils.Location#angleToVector(com.badlogic.gdx.math.Vector, float)
	 */
	@Override
	public Vector2 angleToVector (Vector2 outVector, float angle) {
        outVector.x = -(float)Math.sin(angle);
		outVector.y = (float)Math.cos(angle);
		return outVector;
	}

}