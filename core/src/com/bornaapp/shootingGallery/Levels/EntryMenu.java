package com.bornaapp.shootingGallery.Levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.bornaapp.borna2d.Debug.log;
import com.bornaapp.borna2d.Debug.osdFlag;
import com.bornaapp.borna2d.game.levels.Engine;
import com.bornaapp.borna2d.game.levels.LevelBase;
import com.bornaapp.borna2d.game.levels.LevelFlags;
import com.bornaapp.borna2d.graphics.Background;
import com.bornaapp.shootingGallery.Controls.AboutDialog;

import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;


public class EntryMenu extends LevelBase {
    public EntryMenu() {
        super("assetManifest_entryMenu.json");
    }

    Table table;
    AboutDialog aboutDialog;

    @Override
    public void onCreate() {

        background = new Background("EntryMenu-bg.png", false);

        //---------------------------- Play Button -----------------------------------
        Skin playBtnSkin = new Skin();
        playBtnSkin.addRegions(assets.getTextureAtlas("PlayBtn.atlas"));

        ImageButton playBtn = new ImageButton(playBtnSkin.getDrawable("01playNormal"), playBtnSkin.getDrawable("02playClicked"));

        playBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                NextLevel();
            }
        });

        //------------------------ Mute Button -----------------------------------
        Skin muteBtnSkin = new Skin();
        muteBtnSkin.addRegions(assets.getTextureAtlas("volumeBtn.atlas"));

        ImageButton muteBtn = new ImageButton(muteBtnSkin.getDrawable("01Volume_on"), muteBtnSkin.getDrawable("02Volume_mute"), muteBtnSkin.getDrawable("02Volume_mute"));

        muteBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Engine.getInstance().mute = !Engine.getInstance().mute;
            }
        });

        //------------------------ About Button -----------------------------------
        Skin aboutBtnSkin = new Skin();
        aboutBtnSkin.addRegions(assets.getTextureAtlas("aboutBtn.atlas"));

        ImageButton aboutBtn = new ImageButton(aboutBtnSkin.getDrawable("0about_normal"), aboutBtnSkin.getDrawable("1about-clicked"));

        aboutBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                aboutDialog.setVisible(true);
            }
        });

        //------------------------- Share Button -----------------------------------
        Skin shareBtnSkin = new Skin();
        shareBtnSkin.addRegions(assets.getTextureAtlas("shareBtn.atlas"));

        ImageButton shareBtn = new ImageButton(shareBtnSkin.getDrawable("01shareNormal"), shareBtnSkin.getDrawable("02shareClicked"));

        shareBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                engine.platformSpecific.share();
            }
        });

        //------------------------- Support Button -----------------------------------
        Skin supportBtnSkin = new Skin();
        supportBtnSkin.addRegions(assets.getTextureAtlas("emailBtn.atlas"));

        ImageButton supportBtn = new ImageButton(supportBtnSkin.getDrawable("0email_normal"), supportBtnSkin.getDrawable("1email_clicked"));

        supportBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                engine.platformSpecific.support();
            }
        });

        //----------------------------- Layout ------------------------------------
        table = new Table();
        baseUIStage.addActor(table);

        table.setFillParent(true);

        // We use empty cells that expand through all available spaces for
        // alignment. This may not provide similar results in different screen
        // resolutions, but it is an extremely readable and clear piece of code

        //top row
        table.row().padBottom(25);
        table.add().expand();
        table.add(muteBtn).bottom().left();
        table.add().expand();
        table.add(playBtn).bottom();
        table.add().expand();
        table.add(shareBtn).bottom().right();
        table.add().expand();
        //bot row
        table.row();
        table.add();
        table.add(aboutBtn).bottom().left();
        table.add();
        table.add();
        table.add();
        table.add(supportBtn).bottom().right();

        //-------------- About Dialog ----------------------
        aboutDialog = new AboutDialog();
        aboutDialog.Init();
    }

    @Override
    public void onDispose() {
    }

    @Override
    public void onUpdate() {
        //-------------------------- On-Screen Display --------------------
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_0))
            flags.toggle(LevelFlags.DrawPhysicsDebug);

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_9))
            flags.toggle(LevelFlags.DrawPathDebug);

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_8))
            flags.toggle(LevelFlags.DrawUIDebug);

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_7))
            flags.toggle(LevelFlags.EnableLighting);

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1))
            osd.flags.toggle(osdFlag.ShowLogs);

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2))
            osd.flags.toggle(osdFlag.ShowGrids);

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3))
            osd.flags.toggle(osdFlag.ShowMousePosition);
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
