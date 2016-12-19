package com.bornaapp.shootingGallery.Characters;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.Timer;
import com.bornaapp.borna2d.PlayStatus;
import com.bornaapp.borna2d.components.AnimationComponent;
import com.bornaapp.borna2d.components.BodyComponent;
import com.bornaapp.borna2d.components.SoundComponent;
import com.bornaapp.borna2d.components.TextureAtlasComponent;
import com.bornaapp.borna2d.components.ZComponent;
import com.bornaapp.borna2d.game.levels.Engine;
import com.bornaapp.borna2d.physics.BoxDef;

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

    public ZComponent zComp;

    public Rifle() {

        PooledEngine ashleyEngine = Engine.getInstance().getCurrentLevel().getAshleyEngine();
        //
        Entity entity = ashleyEngine.createEntity();
        ashleyEngine.addEntity(entity);
        //
        texComp = ashleyEngine.createComponent(TextureAtlasComponent.class);
        texComp.Init("rifle.atlas", 0.8f);
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
        animComp.setPlayStatus(PlayStatus.Stopped);
        entity.add(animComp);
        //
        zComp = ashleyEngine.createComponent(ZComponent.class);
        zComp.Init();
        entity.add(zComp);
        //
        bodyComp = ashleyEngine.createComponent(BodyComponent.class);
        float x, y, w, h;
        w = 120f;
        h = 250f;
        x = (Engine.getInstance().WindowWidth()-w)/2f;
        y = 0;
        bodyComp.Init(BodyDef.BodyType.DynamicBody, new BoxDef(w, h), x, y, true, true);
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

    public void Reload() {
        animComp.setPlayStatus(PlayStatus.Playing);

        Timer timer = new Timer();
        timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                animComp.setPlayStatus(PlayStatus.Stopped);
            }
        }, 2);
    }
}

