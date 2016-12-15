package com.bornaapp.borna2d.game.levels;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Mehdi on 08/25/2015.
 * EngineConfig class that represents the configuration of Engine.
 */
public class EngineConfig {

    /**
     * 0. mutes all logging.
     * 1. logs only error messages.
     * 2. logs error & info messages(non debug messages).
     * 3. logs all messages.
     */
    public int logLevel;

    public Vector2 gravity = new Vector2();
}