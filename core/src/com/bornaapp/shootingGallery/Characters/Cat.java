package com.bornaapp.shootingGallery.Characters;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
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
 * Created by Mehdi on 12/1/2016.
 */
public class Cat {

    public int coinsCollected = 0;
    private float life = 100;
    boolean dead = false;

    private float fallingDistance = 0;
    private float latestAltitude = 0;
    private final float fallingDamage = 20;
    private final float fallingDamageThreshold = 12;

    private Entity entity;
    private TextureAtlasComponent texComp;
    private AnimationComponent animComp;
    private BodyComponent bodyComp;
    private SoundComponent soundComp_walk;
    private SoundComponent soundComp_jump;
    private SoundComponent soundComp_ouch;
    //public PathComponent pathComp;

    private final float idleSpeed = 0.15f;
    private final float fallingSpeed = 2.5f;

    private Animation anim_Idle;
    private Animation anim_Jump;
    private Animation anim_Dead;
    private Animation anim_Slide;
    private Animation anim_Fall;
    private Animation anim_Run;
    private Animation anim_Hurt;

    private com.bornaapp.shootingGallery.Characters.CHAR_STATUS characterStatus = com.bornaapp.shootingGallery.Characters.CHAR_STATUS.idle;
    private float charStatTimer = 0.0f;
    private final float resetTime = 0.3f;

    public Cat(Vector2 position) {
        PooledEngine ashleyEngine = Engine.getInstance().getCurrentLevel().getAshleyEngine();
        //
        entity = ashleyEngine.createEntity();
        ashleyEngine.addEntity(entity);
        //
        texComp = ashleyEngine.createComponent(TextureAtlasComponent.class);
        texComp.Init("cat.atlas", 0.4f);
        entity.add(texComp);
        //
        // Animation Sets
        //
        TextureRegion[] frames_idle = new TextureRegion[10];
        frames_idle[0] = (texComp.textureAtlas.findRegion("Idle_1"));
        frames_idle[1] = (texComp.textureAtlas.findRegion("Idle_2"));
        frames_idle[2] = (texComp.textureAtlas.findRegion("Idle_3"));
        frames_idle[3] = (texComp.textureAtlas.findRegion("Idle_4"));
        frames_idle[4] = (texComp.textureAtlas.findRegion("Idle_5"));
        frames_idle[5] = (texComp.textureAtlas.findRegion("Idle_6"));
        frames_idle[6] = (texComp.textureAtlas.findRegion("Idle_7"));
        frames_idle[7] = (texComp.textureAtlas.findRegion("Idle_8"));
        frames_idle[8] = (texComp.textureAtlas.findRegion("Idle_9"));
        frames_idle[9] = (texComp.textureAtlas.findRegion("Idle_10"));
        anim_Idle = new Animation(1 / 10f, frames_idle);
        anim_Idle.setPlayMode(Animation.PlayMode.LOOP);
        //
        TextureRegion[] frames_jump = new TextureRegion[8];
        frames_jump[0] = (texComp.textureAtlas.findRegion("Jump_1"));
        frames_jump[1] = (texComp.textureAtlas.findRegion("Jump_2"));
        frames_jump[2] = (texComp.textureAtlas.findRegion("Jump_3"));
        frames_jump[3] = (texComp.textureAtlas.findRegion("Jump_4"));
        frames_jump[4] = (texComp.textureAtlas.findRegion("Jump_5"));
        frames_jump[5] = (texComp.textureAtlas.findRegion("Jump_6"));
        frames_jump[6] = (texComp.textureAtlas.findRegion("Jump_7"));
        frames_jump[7] = (texComp.textureAtlas.findRegion("Jump_8"));
        anim_Jump = new Animation(1 / 7f, frames_jump);
        anim_Jump.setPlayMode(Animation.PlayMode.NORMAL);
        //
        TextureRegion[] frames_dead = new TextureRegion[10];
        frames_dead[0] = (texComp.textureAtlas.findRegion("Dead_1"));
        frames_dead[1] = (texComp.textureAtlas.findRegion("Dead_2"));
        frames_dead[2] = (texComp.textureAtlas.findRegion("Dead_3"));
        frames_dead[3] = (texComp.textureAtlas.findRegion("Dead_4"));
        frames_dead[4] = (texComp.textureAtlas.findRegion("Dead_5"));
        frames_dead[5] = (texComp.textureAtlas.findRegion("Dead_6"));
        frames_dead[6] = (texComp.textureAtlas.findRegion("Dead_7"));
        frames_dead[7] = (texComp.textureAtlas.findRegion("Dead_8"));
        frames_dead[8] = (texComp.textureAtlas.findRegion("Dead_9"));
        frames_dead[9] = (texComp.textureAtlas.findRegion("Dead_10"));
        anim_Dead = new Animation(1 / 15f, frames_dead);
        anim_Dead.setPlayMode(Animation.PlayMode.NORMAL);
        //
        TextureRegion[] frames_slide = new TextureRegion[10];
        frames_slide[0] = (texComp.textureAtlas.findRegion("Slide_1"));
        frames_slide[1] = (texComp.textureAtlas.findRegion("Slide_2"));
        frames_slide[2] = (texComp.textureAtlas.findRegion("Slide_3"));
        frames_slide[3] = (texComp.textureAtlas.findRegion("Slide_4"));
        frames_slide[4] = (texComp.textureAtlas.findRegion("Slide_5"));
        frames_slide[5] = (texComp.textureAtlas.findRegion("Slide_6"));
        frames_slide[6] = (texComp.textureAtlas.findRegion("Slide_7"));
        frames_slide[7] = (texComp.textureAtlas.findRegion("Slide_8"));
        frames_slide[8] = (texComp.textureAtlas.findRegion("Slide_9"));
        frames_slide[9] = (texComp.textureAtlas.findRegion("Slide_10"));
        anim_Slide = new Animation(1 / 10f, frames_slide);
        anim_Slide.setPlayMode(Animation.PlayMode.LOOP);
        //
        TextureRegion[] frames_Fall = new TextureRegion[8];
        frames_Fall[0] = (texComp.textureAtlas.findRegion("Fall_1"));
        frames_Fall[1] = (texComp.textureAtlas.findRegion("Fall_2"));
        frames_Fall[2] = (texComp.textureAtlas.findRegion("Fall_3"));
        frames_Fall[3] = (texComp.textureAtlas.findRegion("Fall_4"));
        frames_Fall[4] = (texComp.textureAtlas.findRegion("Fall_5"));
        frames_Fall[5] = (texComp.textureAtlas.findRegion("Fall_6"));
        frames_Fall[6] = (texComp.textureAtlas.findRegion("Fall_7"));
        frames_Fall[7] = (texComp.textureAtlas.findRegion("Fall_8"));
        anim_Fall = new Animation(1 / 7f, frames_Fall);
        anim_Fall.setPlayMode(Animation.PlayMode.NORMAL);
        //
        TextureRegion[] frames_run = new TextureRegion[8];
        frames_run[0] = (texComp.textureAtlas.findRegion("Run_1"));
        frames_run[1] = (texComp.textureAtlas.findRegion("Run_2"));
        frames_run[2] = (texComp.textureAtlas.findRegion("Run_3"));
        frames_run[3] = (texComp.textureAtlas.findRegion("Run_4"));
        frames_run[4] = (texComp.textureAtlas.findRegion("Run_5"));
        frames_run[5] = (texComp.textureAtlas.findRegion("Run_6"));
        frames_run[6] = (texComp.textureAtlas.findRegion("Run_7"));
        frames_run[7] = (texComp.textureAtlas.findRegion("Run_8"));
        anim_Run = new Animation(1 / 15f, frames_run);
        anim_Run.setPlayMode(Animation.PlayMode.LOOP);
        //
        TextureRegion[] frames_hurt = new TextureRegion[10];
        frames_hurt[0] = (texComp.textureAtlas.findRegion("Hurt_1"));
        frames_hurt[1] = (texComp.textureAtlas.findRegion("Hurt_2"));
        frames_hurt[2] = (texComp.textureAtlas.findRegion("Hurt_3"));
        frames_hurt[3] = (texComp.textureAtlas.findRegion("Hurt_4"));
        frames_hurt[4] = (texComp.textureAtlas.findRegion("Hurt_5"));
        frames_hurt[5] = (texComp.textureAtlas.findRegion("Hurt_6"));
        frames_hurt[6] = (texComp.textureAtlas.findRegion("Hurt_7"));
        frames_hurt[7] = (texComp.textureAtlas.findRegion("Hurt_8"));
        frames_hurt[8] = (texComp.textureAtlas.findRegion("Hurt_9"));
        frames_hurt[9] = (texComp.textureAtlas.findRegion("Hurt_10"));
        anim_Hurt = new Animation(1 / 50f, frames_hurt);
        anim_Hurt.setPlayMode(Animation.PlayMode.NORMAL);
        //
        animComp = ashleyEngine.createComponent(AnimationComponent.class);
        animComp.Init(anim_Idle);
        entity.add(animComp);
        //
        final CollisionEvent collEvent = new CollisionEvent(this) {
            @Override
            public void onCollision(Object collidedObject, Body collidedBody, Fixture collidedFixture) {

                //Coin
                if (collidedObject.getClass().getName().equals(Coin.class.getName())) {
                    try {
                        Coin coin = (Coin) collidedObject;
                        coin.ring();
                        coin.remove();
                        coinsCollected++;
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                }

                //Heart
                if (collidedObject.getClass().getName().equals(com.bornaapp.shootingGallery.Characters.Heart.class.getName())) {
                    try {
                        com.bornaapp.shootingGallery.Characters.Heart heart = (com.bornaapp.shootingGallery.Characters.Heart) collidedObject;
                        heart.ring();
                        heart.remove();
                        IncreaseLife(heart.healthValue);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                }
            }
        };
        //
        bodyComp = ashleyEngine.createComponent(BodyComponent.class);
        bodyComp.Init_Capsule(BodyDef.BodyType.DynamicBody, 21.0f, 35.0f, position.x, position.y, false, true, collEvent);
        entity.add(bodyComp);
        //
        //pathComp = Build.AshleyComponent.PathFinder();
        //entity.add(pathComp);
        //
        soundComp_walk = ashleyEngine.createComponent(SoundComponent.class);
        soundComp_walk.Init("grass-walk-loop.wav", false, true);
        entity.add(soundComp_walk);

        soundComp_jump = ashleyEngine.createComponent(SoundComponent.class);
        soundComp_jump.Init("fins__jumping.wav", false, false);
        soundComp_jump.volume = 0.2f;

        soundComp_ouch = ashleyEngine.createComponent(SoundComponent.class);
        soundComp_ouch.Init("ouch.wav", false, false);
        soundComp_ouch.volume = 0.2f;
    }

    public void update() {

        //------------------------
        //  animation
        //
        if (isLifeFinished()) {
            animComp.setAnimation(anim_Dead);
            if (anim_Dead.isAnimationFinished(animComp.elapsedTime))
                dead = true;
            return;
        }

        float v = bodyComp.body.getLinearVelocity().len();
        float angle = bodyComp.body.getLinearVelocity().angle();

        if (isOnFoot()) {

            if (fallingDistance > fallingDamageThreshold) {
                DecreaseLife(fallingDamage);
                animComp.setAnimation(anim_Hurt);
                soundComp_ouch.Play();
            }
            fallingDistance = 0f;

            if (v < idleSpeed || characterStatus == com.bornaapp.shootingGallery.Characters.CHAR_STATUS.free) {
                if (animComp.getAnimation() != anim_Hurt) {
                    animComp.setAnimation(anim_Idle);
                    soundComp_ouch.Stop();
                } else if (anim_Hurt.isAnimationFinished(animComp.elapsedTime)) {
                    animComp.setAnimation(anim_Idle);
                    soundComp_ouch.Stop();
                }
            } else {
                //Moving right
                if ((angle < 90f || angle > 270f)) {
                    animComp.setAnimation(anim_Run);
                    //float frameDuration = v<10f ? (1.0f/v) : 0.1f;
                    //anim_Run.setFrameDuration(frameDuration);
                    if (animComp.isFlippedX)
                        animComp.isFlippedX = false;
                }
                //Moving left
                else if ((angle > 90f && angle < 270f)) {
                    animComp.setAnimation(anim_Run);
                    if (!animComp.isFlippedX)
                        animComp.isFlippedX = true;
                }
            }
        } else {
            //Jumping up
            if ((angle > 15f && angle <= 165f) && characterStatus != com.bornaapp.shootingGallery.Characters.CHAR_STATUS.free) {
                animComp.setAnimation(anim_Jump);
            }
            //falling down
            else if ((angle > 195f && angle < 345f) && v > fallingSpeed && characterStatus == com.bornaapp.shootingGallery.Characters.CHAR_STATUS.free) {
                animComp.setAnimation(anim_Fall);

                float currentY = bodyComp.body.getPosition().y;
                if (currentY < latestAltitude)
                    fallingDistance += (latestAltitude - currentY);

                latestAltitude = currentY;
            }
        }

        //------------------------
        //  Sound
        //
        //todo: we should not check each rendering frame, we need to do this in collision-callback
        if (animComp.getAnimation() == anim_Run)
            soundComp_walk.Play();
        else
            soundComp_walk.Pause();

        if (animComp.getAnimation() == anim_Jump)
            soundComp_jump.Play();
        else
            soundComp_jump.Stop();

        //------------------------
        //  Info
        //
        charStatTimer += Engine.getInstance().getCurrentLevel().deltaTime();
        if (charStatTimer >= resetTime) {
            characterStatus = com.bornaapp.shootingGallery.Characters.CHAR_STATUS.free;
            charStatTimer = 0.0f;
        }
    }

    public boolean isOnFoot() {
        //we have added userData to Fixture2(foot) before, to track its collision
        if (bodyComp.body.getFixtureList().get(2).getUserData() != null) {
            CollisionEvent colStat = (CollisionEvent) bodyComp.body.getFixtureList().get(2).getUserData();
            return (colStat.numCollisions > 0);
        }
        return false;
    }

    public void setCharacterStatus(com.bornaapp.shootingGallery.Characters.CHAR_STATUS stat) {
        characterStatus = stat;
        charStatTimer = 0.0f;
    }

    public Vector2 getPosition() {
        return bodyComp.getPositionOfCenter_inPixels();
    }

    public Vector2 getLinearVelocity() {
        return bodyComp.body.getLinearVelocity();
    }

    public void ApplyLinearImpulse(Vector2 _impulse) {
        bodyComp.body.applyLinearImpulse(_impulse, bodyComp.body.getWorldCenter(), true);
    }

    public void setLife(float _life) {
        life = _life;

        if (life > 100)
            life = 100;
        else if (life < 0)
            life = 0;
    }

    public float getLife() {
        return life;
    }

    public void IncreaseLife(float value) {
        if (value > 100)
            value = 100;
        else if (value < 0)
            value = 0;

        life += value;
    }

    public void DecreaseLife(float value) {
        if (value > 100)
            value = 100;
        else if (value < 0)
            value = 0;

        life -= value;
    }


    private boolean isLifeFinished() {
        return (life == 0);
    }

    public boolean isDead() {
        return dead;
    }
}
