package com.bornaapp.borna2d.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;
import com.bornaapp.borna2d.game.levels.Engine;

import box2dLight.PointLight;

/**
 * Created by Mehdi on 11/18/2016.
 */

//<-----should rayHandler(currently in LevelBase) become a system?or go inside rendering system?
    //Right now, light doesn't need a system & thus,it's not even necessary that be a component!!!!

public class PointLightComponent extends Component {

    public PointLight pointLight;

    //region Methods

    //private constructor, as components must be created
    //using Ashley Engine and initialize afterwards.
    private PointLightComponent(){};

    public void Init(Color color, int rays, float distance,float x,float y){
        pointLight = new PointLight(Engine.getInstance().getCurrentLevel().getRayHandler(), rays, color, distance, PixeltoMeters(x), PixeltoMeters(y));
    }

    //Box2D units are different from LibGdx rendering units
    //
    private float PixeltoMeters(float distanceInPixels){
        int ppm = Engine.getInstance().getConfig().ppm;
        return distanceInPixels/ppm;
    }

    private float MetertoPixels(float distanceInMeters){
        int ppm = Engine.getInstance().getConfig().ppm;
        return distanceInMeters*ppm;
    }
    //endregion
}
