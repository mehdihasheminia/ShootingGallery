package com.bornaapp.borna2d.physics;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.bornaapp.borna2d.log;

public class CollisionListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {

        //Checking the validity of parameters and extracting them
        //
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        CollisionEvent eventA = null, eventB = null;
        boolean eventA_valid, eventB_valid;

        try {
            eventA = (CollisionEvent) fixtureA.getUserData();
            eventA_valid = (eventA != null);
        } catch (Exception e) {
            eventA_valid = false;
        }

        try {
            eventB = (CollisionEvent) fixtureB.getUserData();
            eventB_valid = (eventB != null);
        } catch (Exception e) {
            eventB_valid = false;
        }

        //processing data and dispatching callbacks
        //
        if (eventA_valid) {
            if (!fixtureA.isSensor())
                eventA.numCollisions++;
        }

        if (eventB_valid) {
            if (!fixtureA.isSensor())
                eventB.numCollisions++;
        }

        if (eventA_valid && eventB_valid) {
            eventA.onCollision(eventB.owner, fixtureB.getBody(), fixtureB);
            eventB.onCollision(eventA.owner, fixtureA.getBody(), fixtureA);
        }
    }

    @Override
    public void endContact(Contact contact) {

        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if (fixtureA.getUserData() != null) {
            try {
                CollisionEvent eventA = (CollisionEvent) fixtureA.getUserData();
                if (!fixtureA.isSensor())
                    eventA.numCollisions--;
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        if (fixtureB.getUserData() != null) {
            try {
                CollisionEvent eventB = (CollisionEvent) fixtureB.getUserData();
                if (!fixtureB.isSensor())
                    eventB.numCollisions--;
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

}
