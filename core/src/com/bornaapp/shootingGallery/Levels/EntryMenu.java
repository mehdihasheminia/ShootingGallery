package com.bornaapp.shootingGallery.Levels;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.bornaapp.borna2d.game.levels.Engine;
import com.bornaapp.borna2d.game.levels.LevelBase;
import com.bornaapp.borna2d.game.levels.LevelFlags;
import com.bornaapp.borna2d.graphics.Background;

public class EntryMenu extends LevelBase {
    public EntryMenu() {
        super("assetManifest_entryMenu.json");
        flags.set(LevelFlags.LoadProgressively);
    }

    @Override
    public void onCreate() {

        background = new Background("EntryMenu-bg.png", true);

        //------------------------ Play Button ------------------------------
        Skin playBtnSkin = new Skin();
        playBtnSkin.addRegions(assets.getTextureAtlas("PlayBtn.atlas"));

        ImageButton playBtn = new ImageButton(playBtnSkin.getDrawable("01playNormal"), playBtnSkin.getDrawable("02playClicked"));

        playBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                NextLevel();
            }
        });

        //------------------------ Settings Button -----------------------------------
        Skin settingsBtnSkin = new Skin();
        settingsBtnSkin.addRegions(assets.getTextureAtlas("settingsBtn.atlas"));

        ImageButton settingsBtn = new ImageButton(settingsBtnSkin.getDrawable("01settingsNormal"), settingsBtnSkin.getDrawable("02settingsClicked"));

        settingsBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("settings Button Pressed");
            }
        });

        //---------------Share Button-----------------------------------
        Skin shareBtnSkin = new Skin();
        shareBtnSkin.addRegions(assets.getTextureAtlas("shareBtn.atlas"));

        ImageButton shareBtn = new ImageButton(shareBtnSkin.getDrawable("01shareNormal"), shareBtnSkin.getDrawable("02shareClicked"));

        shareBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("share Button Pressed");
            }
        });

        //---------------------------Layout ------------------------------
        Table table = new Table();
        table.bottom();
        table.setTransform(true);
        table.setPosition(400, 10);

        table.row().bottom();
        table.add(settingsBtn).maxHeight(75);
        table.add(playBtn).minHeight(75).prefHeight(120).padLeft(100).padRight(100);
        table.add(shareBtn).maxHeight(75);

        uiStage.addActor(table);
    }

    @Override
    public void onDispose() {
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public void onPause() {
    }

    @Override
    public void onResize(int width, int height) {
    }

    @Override
    public void onSystemPause() {
    }

    @Override
    public void onSystemResume() {
    }

    @Override
    public void NextLevel() {
        engine.setLevel(new L1());
    }

    @Override
    public void RestartLevel() {
        engine.setLevel(new EntryMenu());
    }

    //region Gesture Overrided methods
    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }
    //endregion
}
