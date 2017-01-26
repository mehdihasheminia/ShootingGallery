package com.bornaapp.shootingGallery;

import com.bornaapp.borna2d.game.application.AppListener;
import com.bornaapp.borna2d.game.application.PlatformSpecific;
import com.bornaapp.borna2d.game.levels.Engine;
import com.bornaapp.shootingGallery.Levels.EntryMenu;
import com.bornaapp.shootingGallery.Levels.L1;

public class game extends AppListener {

    public game(PlatformSpecific _platformSpecific){
        super(_platformSpecific);
        //nothing in constructor!!! as Gdx is not initialized yet.
    }

    @Override
    protected void setLevel() {
        Engine.getInstance().setLevel(new EntryMenu());
    }
}
