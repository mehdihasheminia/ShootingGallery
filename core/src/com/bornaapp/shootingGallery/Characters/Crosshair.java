package com.bornaapp.shootingGallery.Characters;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Timer;
import com.bornaapp.borna2d.PlayStatus;
import com.bornaapp.borna2d.components.AnimationComponent;
import com.bornaapp.borna2d.components.BodyComponent;
import com.bornaapp.borna2d.components.PositionComponent;
import com.bornaapp.borna2d.components.TextureAtlasComponent;
import com.bornaapp.borna2d.components.ZComponent;
import com.bornaapp.borna2d.game.levels.Engine;
import com.bornaapp.borna2d.physics.CircleDef;
import com.bornaapp.borna2d.physics.CollisionEvent;

/**
 * Created by Mehdi on 12/19/2016.
 */
public class Crosshair {
    final public int maxBullets = 5;
    public int remainingBullets = 5;
    public int reloadDuration = 3;
    public boolean isReloading = false;

    public boolean bulletIsTravelling = false;
    Timer shotTimer = new Timer();
    float shotDuration = 0.1f;

    private TextureAtlasComponent texComp;

    private AnimationComponent animComp;
    private Animation anim_shoot;

    public BodyComponent bodyComp;

    public ZComponent zComp;

    public Crosshair(float x, float y) {

        PooledEngine ashleyEngine = Engine.getInstance().getCurrentLevel().getAshleyEngine();
        //
        Entity entity = ashleyEngine.createEntity();
        ashleyEngine.addEntity(entity);
        //
        texComp = ashleyEngine.createComponent(TextureAtlasComponent.class);
        texComp.Init("crosshair.atlas", 0.8f);
        entity.add(texComp);
        //
        // Animation Sets
        //
        TextureRegion[] frames_shoot = new TextureRegion[2];
        frames_shoot[0] = (texComp.textureAtlas.findRegion("crosshair_large"));
        frames_shoot[1] = (texComp.textureAtlas.findRegion("crosshair_small"));
        anim_shoot = new Animation(1 / 20f, frames_shoot);
        anim_shoot.setPlayMode(Animation.PlayMode.NORMAL);
        //
        animComp = ashleyEngine.createComponent(AnimationComponent.class);
        animComp.Init(anim_shoot);
        animComp.setPlayStatus(PlayStatus.Stopped);
        entity.add(animComp);
        //
        bodyComp = ashleyEngine.createComponent(BodyComponent.class);
        bodyComp.Init(BodyDef.BodyType.DynamicBody, new CircleDef(5f), x, y, true, true, new CollisionEvent(this) {
            @Override
            public void onBeginContact(Object collidedObject, Body collidedBody, Fixture collidedFixture) {
            }
        });
        bodyComp.body.setGravityScale(0);
        entity.add(bodyComp);
        //
        zComp = ashleyEngine.createComponent(ZComponent.class);
        zComp.Init();
        entity.add(zComp);
    }

    public void update() {

    }

    public void Shoot() {
        bulletIsTravelling = true;
        animComp.setPlayStatus(PlayStatus.Playing);

        bulletIsTravelling = true;
        shotTimer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                bulletIsTravelling = false;
                animComp.setPlayStatus(PlayStatus.Stopped);
            }
        }, shotDuration);

        if (!isReloading)
            remainingBullets--;

        Engine.getInstance().getCurrentLevel().ExecuteDelegate("Shoot");
        if (remainingBullets < 1) {
            remainingBullets = 0;
            bulletIsTravelling = false;
            isReloading = true;
            Timer reloadTimer = new Timer();
            reloadTimer.scheduleTask(new Timer.Task() {

                @Override
                public void run() {
                    remainingBullets = maxBullets;
                    isReloading = false;
                }
            }, reloadDuration);
        }
    }
}
