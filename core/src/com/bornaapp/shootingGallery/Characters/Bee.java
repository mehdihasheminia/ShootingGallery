package com.bornaapp.shootingGallery.Characters;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Array;
import com.bornaapp.borna2d.ai.AStarGraph;
import com.bornaapp.borna2d.components.AnimationComponent;
import com.bornaapp.borna2d.components.BodyComponent;
import com.bornaapp.borna2d.components.PathComponent;
import com.bornaapp.borna2d.components.TextureAtlasComponent;
import com.bornaapp.borna2d.components.ZComponent;
import com.bornaapp.borna2d.game.levels.Engine;
import com.bornaapp.borna2d.physics.CircleDef;
import com.bornaapp.borna2d.physics.CollisionEvent;

/**
 * Created by Hashemi on 12/11/2016.
 * ...
 */
public class Bee {

    private Entity entity;

    private TextureAtlasComponent texComp;
    private AnimationComponent animComp;

    private BodyComponent bodyComp;

    public PathComponent pathComp;

    private Animation anim_Idle;
    private Animation anim_Hurt;

    public ZComponent zComp;

    private Array<Vector2> destinations = new Array<Vector2>();
    private int currentDestIndex = 0;

    public Bee(Vector2 position, AStarGraph aStarGraph) {
        PooledEngine ashleyEngine = Engine.getInstance().getCurrentLevel().getAshleyEngine();
        //
        entity = ashleyEngine.createEntity();
        ashleyEngine.addEntity(entity);
        //
        texComp = ashleyEngine.createComponent(TextureAtlasComponent.class);
        texComp.Init("bee.atlas", 0.6f);
        entity.add(texComp);
        //
        // Animation Sets
        //
        TextureRegion[] frames_idle = new TextureRegion[5];
        frames_idle[0] = (texComp.textureAtlas.findRegion("Idle_1"));
        frames_idle[1] = (texComp.textureAtlas.findRegion("Idle_2"));
        frames_idle[2] = (texComp.textureAtlas.findRegion("Idle_3"));
        frames_idle[3] = (texComp.textureAtlas.findRegion("Idle_4"));
        frames_idle[4] = (texComp.textureAtlas.findRegion("Idle_5"));
        anim_Idle = new Animation(1 / 20f, frames_idle);
        anim_Idle.setPlayMode(Animation.PlayMode.LOOP);
        //
        TextureRegion[] frames_hurt = new TextureRegion[10];
        frames_hurt[0] = (texComp.textureAtlas.findRegion("Hurt_1"));
        frames_hurt[1] = (texComp.textureAtlas.findRegion("Hurt_2"));
        frames_hurt[2] = (texComp.textureAtlas.findRegion("Hurt_3"));
        frames_hurt[3] = (texComp.textureAtlas.findRegion("Hurt_4"));
        frames_hurt[4] = (texComp.textureAtlas.findRegion("Hurt_5"));
        anim_Hurt = new Animation(1 / 50f, frames_hurt);
        anim_Hurt.setPlayMode(Animation.PlayMode.LOOP);
        //
        animComp = ashleyEngine.createComponent(AnimationComponent.class);
        animComp.Init(anim_Idle);
        entity.add(animComp);
        //
        final CollisionEvent collEvent = new CollisionEvent(this) {
            @Override
            public void onBeginContact(Object collidedObject, Body collidedBody, Fixture collidedFixture) {
            }
        };
        //
        zComp = ashleyEngine.createComponent(ZComponent.class);
        zComp.Init();
        entity.add(zComp);
        //
        bodyComp = ashleyEngine.createComponent(BodyComponent.class);
        bodyComp.Init(BodyDef.BodyType.DynamicBody, new CircleDef(15f), position.x, position.y, false, true, collEvent);
        bodyComp.body.setGravityScale(0);
        entity.add(bodyComp);
        //
        pathComp = ashleyEngine.createComponent(PathComponent.class);
        pathComp.Init(position, aStarGraph);
        pathComp.setTravelVelocity(7.5f);
        entity.add(pathComp);
    }

    public void update() {

        //  animation
        //
        float angle = bodyComp.body.getLinearVelocity().angle();

        if ((angle < 90f || angle > 270f)) { //Moving right
            if (texComp.flipX)
                texComp.flipX = false;
        } else if ((angle > 90f && angle < 270f)) {//Moving left
            if (!texComp.flipX)
                texComp.flipX = true;
        }

        //Movement
        //
        if (pathComp.reachedDestination()) {

            currentDestIndex++;
            if (currentDestIndex < destinations.size) {
                pathComp.setDestination(destinations.get(currentDestIndex));
            } else {
                currentDestIndex = 0;
                destinations.reverse();
            }
        }
    }

    public void addCheckPoint(Vector2 checkpoint) {
        destinations.add(checkpoint);
    }

    public void setDebgColor(Color debgColor) {

    }
}
