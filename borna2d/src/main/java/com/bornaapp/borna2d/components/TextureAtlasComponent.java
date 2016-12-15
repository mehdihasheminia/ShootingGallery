package com.bornaapp.borna2d.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.bornaapp.borna2d.game.levels.Engine;

/**
 * Created by Mehdi on 09/02/2015.
 * ...
 */
public class TextureAtlasComponent extends Component {

    public float scale = 1.0f;
    public TextureAtlas textureAtlas;

    //region Methods

    //private constructor, as components must be created
    //using Ashley Engine and initialize afterwards.
    private TextureAtlasComponent(){};

    public void Init(String _Path, float _scale){
        {
            textureAtlas = Engine.getInstance().getCurrentLevel().getAssetManager().getTextureAtlas(_Path);
            scale = _scale;
        }
    }

    //endregion
}
