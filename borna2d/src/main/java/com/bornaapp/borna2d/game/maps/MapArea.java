package com.bornaapp.borna2d.game.maps;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.physics.box2d.Body;
import com.bornaapp.borna2d.components.BodyComponent;
import com.bornaapp.borna2d.game.levels.Engine;
import com.bornaapp.borna2d.physics.CollisionEvent;

/**
 * Created by Hashemi on 12/8/2016.
 */
public class MapArea {

    public String name;
    private Body body;

    public MapArea(String _name, BodyComponent bodyComponent){
        name = _name;
        body = bodyComponent.body;
        //
        PooledEngine ashleyEngine = Engine.getInstance().getCurrentLevel().getAshleyEngine();

        Entity entity = ashleyEngine.createEntity();
        entity.add(bodyComponent);

        ashleyEngine.addEntity(entity);
    }

    public void SetCollisionEvent(CollisionEvent collisionEvent){
        body.getFixtureList().get(0).setUserData(collisionEvent);
    }

}
