package com.bornaapp.borna2d.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.bornaapp.borna2d.game.levels.Engine;

/**
 * Created by Hashemi on 11/7/2016.
 */
public class TextureComponent extends Component {

    public float scale = 1.0f;
    public Texture texture;

    //region Methods

    //private constructor, as components must be created
    //using Ashley Engine and initialize afterwards.
    private TextureComponent(){};

    public void Init(String _Path, float _scale){
        texture = Engine.getInstance().getCurrentLevel().getAssetManager().getTexture(_Path);
        scale = _scale;
    }

    //endregion

}


