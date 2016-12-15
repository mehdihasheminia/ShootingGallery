package com.bornaapp.shootingGallery.Characters;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.bornaapp.borna2d.components.BodyComponent;
import com.bornaapp.borna2d.components.TextureComponent;
import com.bornaapp.borna2d.game.levels.Engine;

/**
 * Created by Hashemi on 11/8/2016.
 */
public class Box {
    private Entity entity;
    private TextureComponent texComp;
    public BodyComponent bodyComp;

    public Box(String _boxTexture, float scl, Vector2 position) {
        PooledEngine ashleyEngine = Engine.getInstance().getCurrentLevel().getAshleyEngine();
        //
        entity = ashleyEngine.createEntity();
        ashleyEngine.addEntity(entity);
        //
        texComp = ashleyEngine.createComponent(TextureComponent.class);
        texComp.Init(_boxTexture, scl);
        entity.add(texComp);
        //
        bodyComp = ashleyEngine.createComponent(BodyComponent.class);
        bodyComp.Init_Box(BodyDef.BodyType.DynamicBody, 65.0f, 65.0f, position.x, position.y, false, false);
        bodyComp.setMaterial(25.0f, 0.5f, 0.5f);
        entity.add(bodyComp);
    }

    public void update() {

    }
}
