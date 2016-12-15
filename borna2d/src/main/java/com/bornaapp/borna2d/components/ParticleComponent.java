package com.bornaapp.borna2d.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.math.Vector2;
import com.bornaapp.borna2d.game.levels.Engine;

/**
 *
 */
public class ParticleComponent extends Component {

    public ParticleEffect particleEffect;
    public boolean looped = true;

    //region Methods

    //private constructor, as components must be created
    //using Ashley Engine and initialize afterwards.
    private ParticleComponent(){};

    public void Init(String filePath, float x, float y){
        particleEffect = new ParticleEffect(Engine.getInstance().getCurrentLevel().getAssetManager().getParticleEffect(filePath));//<---todo: assets get cleaned on exit? I mean because of 'new' !
        particleEffect.start();
        particleEffect.setPosition(x, y);
    }
    // endregion

    //region Properties

    public Vector2 getPosition() {
        ParticleEmitter firstEmitter = particleEffect.getEmitters().get(0);
        return new Vector2(firstEmitter.getX(), firstEmitter.getY());
    }

    public void setPosition(Vector2 pos) {
        particleEffect.setPosition(pos.x, pos.y);
    }
    //endregion
}
