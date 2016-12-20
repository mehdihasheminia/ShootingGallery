package com.bornaapp.shootingGallery.Characters;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Timer;
import com.bornaapp.borna2d.PlayStatus;
import com.bornaapp.borna2d.components.AnimationComponent;
import com.bornaapp.borna2d.components.PositionComponent;
import com.bornaapp.borna2d.components.TextureAtlasComponent;
import com.bornaapp.borna2d.components.ZComponent;
import com.bornaapp.borna2d.game.levels.Engine;

/**
 * Created by Mehdi on 12/19/2016.
 */
public class Crosshair {
    private TextureAtlasComponent texComp;

    private AnimationComponent animComp;
    private Animation anim_shoot;

    public PositionComponent posComp;

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
        posComp = ashleyEngine.createComponent(PositionComponent.class);
        posComp.Init(x, y);
        entity.add(posComp);
        //
        zComp = ashleyEngine.createComponent(ZComponent.class);
        zComp.Init();
        entity.add(zComp);
    }

    public void update() {

    }

    public void Shoot() {
        animComp.setPlayStatus(PlayStatus.Playing);

        Timer timer = new Timer();
        timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                animComp.setPlayStatus(PlayStatus.Stopped);
            }
        }, 1/10f);
    }
}
