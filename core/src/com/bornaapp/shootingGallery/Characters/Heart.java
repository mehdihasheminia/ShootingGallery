package com.bornaapp.shootingGallery.Characters;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.bornaapp.borna2d.components.AnimationComponent;
import com.bornaapp.borna2d.components.BodyComponent;
import com.bornaapp.borna2d.components.SoundComponent;
import com.bornaapp.borna2d.components.TextureAtlasComponent;
import com.bornaapp.borna2d.game.levels.Engine;
import com.bornaapp.borna2d.log;
import com.bornaapp.borna2d.physics.CollisionEvent;

/**
 * Created by Mehdi on 12/6/2016.
 */
public class Heart {
    private Entity entity;
    private TextureAtlasComponent texComp;
    private AnimationComponent animComp;
    private BodyComponent bodyComp;
    private SoundComponent soundComp_heart;

    public float healthValue = 2.5f;

    public Heart(float posX, float posY) {
        PooledEngine ashleyEngine = Engine.getInstance().getCurrentLevel().getAshleyEngine();
        //
        entity = ashleyEngine.createEntity();
        ashleyEngine.addEntity(entity);
        //
        texComp = ashleyEngine.createComponent(TextureAtlasComponent.class);
        texComp.Init("heart.atlas", 0.75f);
        entity.add(texComp);
        //
        // Animation Sets
        //
        TextureRegion[] frames_shimmer = new TextureRegion[4];
        frames_shimmer[0] = (texComp.textureAtlas.findRegion("frame-1"));
        frames_shimmer[1] = (texComp.textureAtlas.findRegion("frame-2"));
        frames_shimmer[2] = (texComp.textureAtlas.findRegion("frame-3"));
        frames_shimmer[3] = (texComp.textureAtlas.findRegion("frame-4"));
        Animation anim_shimmer = new Animation(1 / 7f, frames_shimmer);
        anim_shimmer.setPlayMode(Animation.PlayMode.LOOP);
        //
        animComp = ashleyEngine.createComponent(AnimationComponent.class);
        animComp.Init(anim_shimmer);
        entity.add(animComp);
        //
        CollisionEvent collEvent = new CollisionEvent(this) {
            @Override
            public void onCollision(Object collidedObject, Body collidedBody, Fixture collidedFixture) {
            }
        };

        bodyComp = ashleyEngine.createComponent(BodyComponent.class);
        bodyComp.Init_Circle(BodyDef.BodyType.DynamicBody, 15.0f, posX, posY, true, true, collEvent);
        bodyComp.body.setGravityScale(0f);
        entity.add(bodyComp);
        //
        soundComp_heart = ashleyEngine.createComponent(SoundComponent.class);
        soundComp_heart.Init("level-up-01.wav", false, false);
        soundComp_heart.volume = 0.2f;
//        entity.add(soundComp_heart);
    }

    public void update() {
    }

    public void ring(){
        soundComp_heart.Play();
    }

    public void remove(){
        //do not release or dispose resources like sound and texture
        // bcs they're common with other instances
        Engine.getInstance().getCurrentLevel().getAshleyEngine().removeEntity(entity);
        Engine.getInstance().getCurrentLevel().AddToKillList(bodyComp.body);
    }
}
