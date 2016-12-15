package com.bornaapp.shootingGallery.Characters;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.bornaapp.borna2d.components.AnimationComponent;
import com.bornaapp.borna2d.components.BodyComponent;
import com.bornaapp.borna2d.components.SoundComponent;
import com.bornaapp.borna2d.components.TextureAtlasComponent;
import com.bornaapp.borna2d.game.levels.Engine;

/**
 * Created by Hashemi on 12/15/2016.
 */
public class Rifle {

    private TextureAtlasComponent texComp;

    private AnimationComponent animComp;
    private Animation anim_reload;

    private SoundComponent soundComp_shoot;
    private SoundComponent soundComp_reload;

    public BodyComponent bodyComp;

    public Rifle(Vector2 position) {

        PooledEngine ashleyEngine = Engine.getInstance().getCurrentLevel().getAshleyEngine();
        //
        Entity entity = ashleyEngine.createEntity();
        ashleyEngine.addEntity(entity);
        //
        texComp = ashleyEngine.createComponent(TextureAtlasComponent.class);
        texComp.Init("rifle.atlas", 0.5f);
        entity.add(texComp);
        //
        // Animation Sets
        //
        TextureRegion[] frames_reload = new TextureRegion[2];
        frames_reload[0] = (texComp.textureAtlas.findRegion("rifle_1"));
        frames_reload[1] = (texComp.textureAtlas.findRegion("rifle_2"));
        anim_reload = new Animation(1 / 5f, frames_reload);
        anim_reload.setPlayMode(Animation.PlayMode.LOOP);
        //
        animComp = ashleyEngine.createComponent(AnimationComponent.class);
        animComp.Init(anim_reload);
        entity.add(animComp);
        //
        bodyComp = ashleyEngine.createComponent(BodyComponent.class);
        bodyComp.Init_Box(BodyDef.BodyType.DynamicBody, 80, 170, position.x, position.y, true, true);
        bodyComp.body.setGravityScale(0);
        entity.add(bodyComp);

//        soundComp_shoot = ashleyEngine.createComponent(SoundComponent.class);
//        soundComp_shoot.Init("grass-walk-loop.wav", false, true);
//        entity.add(soundComp_shoot);

//        soundComp_reload = ashleyEngine.createComponent(SoundComponent.class);
//        soundComp_reload.Init("ouch.wav", false, false);
//        soundComp_reload.volume = 0.2f;
    }

    public void update() {

        //  animation
        //
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
//        if (animComp.getAnimation() == anim_reload)
//            soundComp_shoot.Play();
//        else
//            soundComp_shoot.Pause();
//
//        if (animComp.getAnimation() == anim_Hurt)
//            soundComp_reload.Play();
//        else
//            soundComp_reload.Stop();
    }
}

