package com.bornaapp.borna2d.game.maps;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Mehdi on 8/26/2015.
 * ...
 */

public class MapParameters {
    public int     ppm;                  //the factor which determines how many pixels(rendering Gdx unit) represents one meters(Box2D physics unit)
    public String  collisionLayerName;   //each object: property x & y & width & height(Float)
    public String  portalLayerName;      //each object: property x & y & width & height(Float)
    public String  lightsLayerName;      //each object: property x & y(Float), custom property"distance"(String) & "color"(String[4])
    public String  particlesLayerName;   //each object: property x & y(Float)
    public String  checkpointsLayerName; //each object: custom property"name"(String), property x & y(Float)
    public String  pathLayerName;
    public String  propertyKey_Color;
    public String  propertyKey_Distance;
}
