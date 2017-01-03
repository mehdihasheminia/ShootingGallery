package com.bornaapp.borna2d.game.levels;

import com.badlogic.gdx.math.Vector2;
import com.bornaapp.borna2d.Debug.LogLevel;

/**
 * Created by Mehdi on 08/25/2015.
 * EngineConfig class that represents the configuration of Engine.
 */
public class EngineConfig {

    public LogLevel logLevel;

    /**
     * the factor which determines how many pixels(rendering Gdx unit) represents one meters(Box2D physics unit)
     */
    public int ppm;

    public Vector2 gravity = new Vector2();
}