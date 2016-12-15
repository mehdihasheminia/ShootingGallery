package com.bornaapp.borna2d.game.levels;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Mehdi on 08/25/2015.
 * EngineConfig class that represents the configuration of Engine.
 */
public class EngineConfig {

    /**
     * 0. mutes all logging.<p>
     * 1. logs only error messages.<p>
     * 2. logs error & info messages(non debug messages).<p>
     * 3. logs all messages.<p>
     */
    public int logLevel;

    /**
     * the factor which determines how many pixels(rendering Gdx unit) represents one meters(Box2D physics unit)
     */
    public int     ppm;

    public Vector2 gravity = new Vector2();
}