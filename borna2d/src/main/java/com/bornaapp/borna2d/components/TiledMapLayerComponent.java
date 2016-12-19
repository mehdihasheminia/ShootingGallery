package com.bornaapp.borna2d.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

/**
 * Created by Hashemi on 12/18/2016.
 */
public class TiledMapLayerComponent extends Component {

    public TiledMapTileLayer tileLayer;

    public void Init(TiledMapTileLayer _tileLayer) {
        tileLayer = _tileLayer;
    }
}