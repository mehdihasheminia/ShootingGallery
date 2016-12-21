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
import com.bornaapp.borna2d.physics.CircleDef;
import com.bornaapp.borna2d.physics.CollisionEvent;

/**
 * Created by Mehdi on 12/3/2016.
 */
public class Coin {

    private Entity entity;
    private TextureAtlasComponent texComp;
    private AnimationComponent animComp;
    private BodyComponent bodyComp;
    private SoundComponent soundComp_coin;

    public Coin(float posX, float posY) {
        PooledEngine ashleyEngine = Engine.getInstance().getCurrentLevel().getAshleyEngine();
        //
        entity = ashleyEngine.createEntity();
        ashleyEngine.addEntity(entity);
        //
        texComp = ashleyEngine.createComponent(TextureAtlasComponent.class);
        texComp.Init("coin.atlas", 0.5f);
        entity.add(texComp);
        //
        // Animation Sets
        //
        TextureRegion[] frames_rotate = new TextureRegion[8];
        frames_rotate[0] = (texComp.textureAtlas.findRegion("rotate_0"));
        frames_rotate[1] = (texComp.textureAtlas.findRegion("rotate_1"));
        frames_rotate[2] = (texComp.textureAtlas.findRegion("rotate_2"));
        frames_rotate[3] = (texComp.textureAtlas.findRegion("rotate_3"));
        frames_rotate[4] = (texComp.textureAtlas.findRegion("rotate_4"));
        frames_rotate[5] = (texComp.textureAtlas.findRegion("rotate_5"));
        frames_rotate[6] = (texComp.textureAtlas.findRegion("rotate_6"));
        frames_rotate[7] = (texComp.textureAtlas.findRegion("rotate_7"));
        Animation anim_rotate = new Animation(1 / 10f, frames_rotate);
        anim_rotate.setPlayMode(Animation.PlayMode.LOOP);
        //
        animComp = ashleyEngine.createComponent(AnimationComponent.class);
        animComp.Init(anim_rotate);
        entity.add(animComp);
        //
        CollisionEvent collEvent = new CollisionEvent(this) {
            @Override
            public void onBeginContact(Object collidedObject, Body collidedBody, Fixture collidedFixture) {
            }
        };

        bodyComp = ashleyEngine.createComponent(BodyComponent.class);
        bodyComp.Init(BodyDef.BodyType.DynamicBody, new CircleDef(10.0f), posX, posY, true, true, collEvent);
        bodyComp.body.setGravityScale(0f);
        entity.add(bodyComp);
        //
        soundComp_coin = ashleyEngine.createComponent(SoundComponent.class);
        soundComp_coin.Init("coin1.wav", false, false);
        soundComp_coin.volume = 0.2f;
        entity.add(soundComp_coin);
    }

    public void update() {
    }

    public void ring() {
        soundComp_coin.Play();
    }

    public void remove() {
        //do not release or dispose resources like sound and texture
        // bcs they're common with other instances
        Engine.getInstance().getCurrentLevel().getAshleyEngine().removeEntity(entity);
        Engine.getInstance().getCurrentLevel().AddToKillList(bodyComp.body);
    }
}
