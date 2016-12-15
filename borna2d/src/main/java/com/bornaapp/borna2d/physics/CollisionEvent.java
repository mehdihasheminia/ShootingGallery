package com.bornaapp.borna2d.physics;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.bornaapp.borna2d.log;

public abstract class CollisionEvent {

    //number of collisions with non-sensor objects
    public int numCollisions = 0;
    public Object owner = null;

    public CollisionEvent(Object _owner) {
        numCollisions = 0;
        owner = _owner;
    }

    public abstract void onCollision(Object collidedObject, Body collidedBody, Fixture collidedFixture);
}
