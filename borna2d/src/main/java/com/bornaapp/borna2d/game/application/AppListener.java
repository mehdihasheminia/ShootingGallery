package com.bornaapp.borna2d.game.application;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.bornaapp.borna2d.game.levels.Engine;

public abstract class AppListener implements ApplicationListener {

    Engine engine = Engine.getInstance();

    public AppListener(){
        //nothing in constructor!!! as Gdx is not initialized yet.
    }

    protected abstract void setLevel();

    /**
     * gets called by application automatically when application window is started for the first time.
     */
    @Override
    public void create() {
        engine.create();
        setLevel();
    }

    /**
     * gets called by application automatically when application window is getting closed, after pause().
     */
    @Override
    public void dispose() {
        engine.dispose();
        Gdx.app.exit();
    }

    /**
     * gets called by application automatically and periodically after application window is created.
     */
    @Override
    public void render() {
        engine.render();
    }

    /**
     * gets called by application automatically when application window is resized.
     * consequently, it also runs after application window is created for the first time.
     *
     * @param width
     * @param height
     */
    @Override
    public void resize(int width, int height) {
        engine.resize(width, height);
    }

    /**
     * gets called by application automatically when application window is paused.
     * tconsequently, it also runs when application window loses focus, minimized, or closed.
     */
    @Override
    public void pause() {
        engine.pause();
    }

    /**
     * gets called by application automatically when application window comes out of a paused state.
     */
    @Override
    public void resume() {
        engine.resume();
    }
}