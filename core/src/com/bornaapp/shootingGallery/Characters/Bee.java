package com.bornaapp.shootingGallery.Characters;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Array;
import com.bornaapp.borna2d.components.AnimationComponent;
import com.bornaapp.borna2d.components.BodyComponent;
import com.bornaapp.borna2d.components.PathComponent;
import com.bornaapp.borna2d.components.SoundComponent;
import com.bornaapp.borna2d.components.TextureAtlasComponent;
import com.bornaapp.borna2d.game.levels.Engine;
import com.bornaapp.borna2d.log;
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

    private SoundComponent soundComp_fly;
    private SoundComponent soundComp_dead;
    public PathComponent pathComp;

    private final float idleSpeed = 0.15f;

    private Animation anim_Idle;
    private Animation anim_Hurt;

    private float charStatTimer = 0.0f;
    private final float resetTime = 0.3f;

    private Array<Vector2> destinations = new Array<Vector2>();
    private int currentDestIndex = 0;
    private float waitTime = 0f;

    public Bee(Vector2 position) {
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
            public void onCollision(Object collidedObject, Body collidedBody, Fixture collidedFixture) {
            }
        };
        //
        bodyComp = ashleyEngine.createComponent(BodyComponent.class);
        bodyComp.Init_Circle(BodyDef.BodyType.DynamicBody, 15f, position.x, position.y, false, true, collEvent);
        bodyComp.body.setGravityScale(0);
        entity.add(bodyComp);
        //
        pathComp = ashleyEngine.createComponent(PathComponent.class);
        pathComp.Init(position, Engine.getInstance().getCurrentLevel().getMap().getEdgeGraph());
        pathComp.setTravelVelocity(7.5f);
        entity.add(pathComp);
        //
//        soundComp_fly = ashleyEngine.createComponent(SoundComponent.class);
//        soundComp_fly.Init("grass-walk-loop.wav", false, true);
//        entity.add(soundComp_fly);

//        soundComp_dead = ashleyEngine.createComponent(SoundComponent.class);
//        soundComp_dead.Init("ouch.wav", false, false);
//        soundComp_dead.volume = 0.2f;
    }

    public void update() {

        //------------------------
        //  animation
        //
//        if (isLifeFinished()) {
//            animComp.setAnimation(anim_Hurt);
//            if (anim_Hurt.isAnimationFinished(animComp.elapsedTime))
//                dead = true;
//            return;
//        }

        float v = bodyComp.body.getLinearVelocity().len();
        float angle = bodyComp.body.getLinearVelocity().angle();

        //Moving right
        if ((angle < 90f || angle > 270f)) {
            if (animComp.isFlippedX)
                animComp.isFlippedX = false;
        }
        //Moving left
        else if ((angle > 90f && angle < 270f)) {
            if (!animComp.isFlippedX)
                animComp.isFlippedX = true;
        }

        //------------------------
        //  Sound
        //
        //todo: we should not check each rendering frame, we need to do this in collision-callback
//        if (animComp.getAnimation() == anim_Idle)
//            soundComp_fly.Play();
//        else
//            soundComp_fly.Pause();
//
//        if (animComp.getAnimation() == anim_Hurt)
//            soundComp_dead.Play();
//        else
//            soundComp_dead.Stop();

        //
        //behaviour
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
}
