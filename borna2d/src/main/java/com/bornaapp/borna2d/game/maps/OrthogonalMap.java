package com.bornaapp.borna2d.game.maps;

import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.bornaapp.borna2d.game.levels.*;

/**
 * Created by Hashemi on 11/15/2016.
 */
public class OrthogonalMap extends Map {

    @Override
    protected void Init(String filePath) {
        tiledMap = Engine.getInstance().getCurrentLevel().assets.getTiledMap(filePath);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, Engine.getInstance().getCurrentLevel().getBatch());
    }
}
