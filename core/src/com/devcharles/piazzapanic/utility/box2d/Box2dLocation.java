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