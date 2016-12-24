package com.bornaapp.shootingGallery;

import com.bornaapp.borna2d.game.application.AppListener;
import com.bornaapp.borna2d.game.levels.Engine;
import com.bornaapp.shootingGallery.Levels.L1;

public class game extends AppListener {

    public game(){
        //nothing in constructor!!! as Gdx is not initialized yet.
    }

    @Override
    protected void setLevel() {
        Engine.getInstance().setLevel(new L1());
    }
}
