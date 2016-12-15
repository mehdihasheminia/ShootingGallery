package com.bornaapp.shootingGallery.Characters;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Vector2;
import com.bornaapp.borna2d.components.ParticleComponent;
import com.bornaapp.borna2d.game.levels.Engine;

/**
 * Created by Mehdi on 11/18/2016.
 */
public class Fire {
    private Entity entity;
    private ParticleComponent partComp;

    public Fire(String _path, Vector2 position) {
        PooledEngine ashleyEngine = Engine.getInstance().getCurrentLevel().getAshleyEngine();
        //
        entity = ashleyEngine.createEntity();
        ashleyEngine.addEntity(entity);
        //
        partComp = ashleyEngine.createComponent(ParticleComponent.class);
        partComp.Init(_path, position.x, position.y);
        entity.add(partComp);
    }

    public void update() {
    }
}
