package com.bornaapp.shootingGallery.Characters;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.Array;
import com.bornaapp.borna2d.components.BodyComponent;
import com.bornaapp.borna2d.components.TextureComponent;
import com.bornaapp.borna2d.game.levels.Engine;
import com.bornaapp.borna2d.physics.BoxDef;


/**
 * Created by Hashemi on 11/7/2016.
 */
public class MovingPlatform {

    private Entity entity;
    private TextureComponent texComp;
    public BodyComponent bodyComp;

    private Array<Vector2> destinations = new Array<Vector2>();
    private int currentDestIndex = 0;
    private float waitTime = 0f;
    private float travelSpeed = 2.0f;

    float width = 140f;
    float height = 30f;

    public MovingPlatform(Vector2 position, float _speed) {
        PooledEngine ashleyEngine = Engine.getInstance().getCurrentLevel().getAshleyEngine();
        //
        entity = ashleyEngine.createEntity();
        ashleyEngine.addEntity(entity);
        //
        texComp = ashleyEngine.createComponent(TextureComponent.class);
        texComp.Init("MovingPlatform.png", 0.7f);
        entity.add(texComp);
        //
        bodyComp = ashleyEngine.createComponent(BodyComponent.class);
        bodyComp.Init(BodyDef.BodyType.KinematicBody, new BoxDef(width, height), position.x, position.y, false, false);
        bodyComp.setMaterial(1.0f, 0.0f, 1.0f);
        entity.add(bodyComp);

        travelSpeed = _speed;
    }

    public void update() {

        //moving between checkpoints
        if (destinations.size > 0) {
            //where is it right now?
            for (int i = 0; i < destinations.size; i++) {
                // As box2D returns position of Center of objects, it is
                // incompatible with other frameworks which use bot-left
                Vector2 centerOfBody = bodyComp.getPositionOfCenter_inPixels();
                Vector2 botLeft = new Vector2(centerOfBody.x - width / 2, centerOfBody.y - height / 2);
                if (botLeft.sub(destinations.get(i)).len() < 10.0f) {
                    currentDestIndex = i;
                    break;
                }
            }
            if (currentDestIndex + 1 <= destinations.size - 1) {
                Vector2 start = new Vector2(destinations.get(currentDestIndex).x, destinations.get(currentDestIndex).y);
                Vector2 stop = new Vector2(destinations.get(currentDestIndex + 1).x, destinations.get(currentDestIndex + 1).y);
                Vector2 vel = stop.sub(start);
                vel.scl(travelSpeed / vel.len());
                bodyComp.body.setLinearVelocity(vel);
            } else {
                currentDestIndex = 0;
                destinations.reverse();
            }
        }
    }

    public void addCheckPoint(Vector2 checkpoint) {
        destinations.add(checkpoint);
    }
}
