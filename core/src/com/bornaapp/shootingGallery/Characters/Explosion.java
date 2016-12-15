package com.bornaapp.shootingGallery.Characters;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.bornaapp.borna2d.components.AnimationComponent;
import com.bornaapp.borna2d.components.PositionComponent;
import com.bornaapp.borna2d.components.TextureAtlasComponent;
import com.bornaapp.borna2d.game.levels.Engine;

/**
 * Created by Hashemi on 11/8/2016.
 */
public class Explosion {

    private Entity entity;
    private TextureAtlasComponent texComp;
    private AnimationComponent animComp;
    private PositionComponent posComp;
    private Animation anim_explosion;

    public Explosion(Vector2 position) {
        PooledEngine ashleyEngine = Engine.getInstance().getCurrentLevel().getAshleyEngine();
        //
        entity = ashleyEngine.createEntity();
        ashleyEngine.addEntity(entity);
        //
        texComp = ashleyEngine.createComponent(TextureAtlasComponent.class);
        texComp.Init("explosion.atlas", 0.4f);
        entity.add(texComp);
        //
        // Animation Sets
        //
        TextureRegion[] frames_explosion = new TextureRegion[16];
        frames_explosion[0] = (texComp.textureAtlas.findRegion("explos_0"));
        frames_explosion[1] = (texComp.textureAtlas.findRegion("explos_1"));
        frames_explosion[2] = (texComp.textureAtlas.findRegion("explos_2"));
        frames_explosion[3] = (texComp.textureAtlas.findRegion("explos_3"));
        frames_explosion[4] = (texComp.textureAtlas.findRegion("explos_4"));
        frames_explosion[5] = (texComp.textureAtlas.findRegion("explos_5"));
        frames_explosion[6] = (texComp.textureAtlas.findRegion("explos_6"));
        frames_explosion[7] = (texComp.textureAtlas.findRegion("explos_7"));
        frames_explosion[8] = (texComp.textureAtlas.findRegion("explos_8"));
        frames_explosion[9] = (texComp.textureAtlas.findRegion("explos_9"));
        frames_explosion[10] = (texComp.textureAtlas.findRegion("explos_10"));
        frames_explosion[11] = (texComp.textureAtlas.findRegion("explos_11"));
        frames_explosion[12] = (texComp.textureAtlas.findRegion("explos_12"));
        frames_explosion[13] = (texComp.textureAtlas.findRegion("explos_13"));
        frames_explosion[14] = (texComp.textureAtlas.findRegion("explos_14"));
        frames_explosion[15] = (texComp.textureAtlas.findRegion("explos_15"));
        anim_explosion = new Animation(1 / 30f, frames_explosion);
        anim_explosion.setPlayMode(Animation.PlayMode.LOOP);
        animComp = ashleyEngine.createComponent(AnimationComponent.class);
        animComp.Init(anim_explosion);
        entity.add(animComp);
        //
        posComp = ashleyEngine.createComponent(PositionComponent.class);
        posComp.Init(position.x,  position.y);
        entity.add(posComp);
    }

    public void update() {

    }
}
