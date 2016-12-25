package com.bornaapp.shootingGallery.Controls;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.bornaapp.borna2d.game.levels.Engine;

/**
 * Created by Mehdi on 11/28/2016.
 */
public class PauseMenu {

    Table table = new Table();

    public PauseMenu() {
    }

    public void Init() {

        //------------------------ Settings Button -----------------------------------
        Skin settingsBtnSkin = new Skin();
        settingsBtnSkin.addRegions(Engine.getInstance().getCurrentLevel().assets.getTextureAtlas("settingsBtn.atlas"));

        ImageButton settingsBtn = new ImageButton(settingsBtnSkin.getDrawable("01settingsNormal"), settingsBtnSkin.getDrawable("02settingsClicked"));

        settingsBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("settings Button Pressed");
            }
        });

        //------------------------ Volume Button -----------------------------------
        Skin volumeBtnSkin = new Skin();
        volumeBtnSkin.addRegions(Engine.getInstance().getCurrentLevel().assets.getTextureAtlas("volumeBtn.atlas"));

        ImageButton volumeBtn = new ImageButton(volumeBtnSkin.getDrawable("01Volume_on"), volumeBtnSkin.getDrawable("02Volume_mute"), volumeBtnSkin.getDrawable("02Volume_mute"));

        volumeBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Engine.getInstance().mute = !Engine.getInstance().mute;
            }
        });

        //------------------------ Resume Button -----------------------------------
        Skin resumeBtnSkin = new Skin();
        resumeBtnSkin.addRegions(Engine.getInstance().getCurrentLevel().assets.getTextureAtlas("resumeBtn.atlas"));

        ImageButton resumeBtn = new ImageButton(resumeBtnSkin.getDrawable("01ResumeNormal"), resumeBtnSkin.getDrawable("02ResumeClicked"));

        resumeBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Engine.getInstance().getCurrentLevel().paused = false;
            }
        });

        //------------------------ Restart Button -----------------------------------
        Skin restartBtnSkin = new Skin();
        restartBtnSkin.addRegions(Engine.getInstance().getCurrentLevel().assets.getTextureAtlas("restartBtn.atlas"));

        ImageButton restartBtn = new ImageButton(restartBtnSkin.getDrawable("01RestartNormal"), restartBtnSkin.getDrawable("02RestartClicked"));

        restartBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Engine.getInstance().getCurrentLevel().RestartLevel();
            }
        });

        //------------------------ Exit Button -----------------------------------
        Skin exitBtnSkin = new Skin();
        exitBtnSkin.addRegions(Engine.getInstance().getCurrentLevel().assets.getTextureAtlas("exitBtn.atlas"));

        ImageButton exitBtn = new ImageButton(exitBtnSkin.getDrawable("01exitNormal"), exitBtnSkin.getDrawable("02exitClicked"));

        exitBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Engine.getInstance().setLevel(new com.bornaapp.shootingGallery.Levels.EntryMenu());
            }
        });

        //---------------------------Layout ------------------------------
        table.setVisible(false);
        Engine.getInstance().getCurrentLevel().uiStage.addActor(table);
//        table.debugAll();

        final Drawable tblBackground = new TextureRegionDrawable(new TextureRegion(Engine.getInstance().getCurrentLevel().assets.getTexture("pauseTable.png")));
        table.setBackground(tblBackground);

        table.setWidth(280);
        table.setHeight(400);
        table.setTransform(true);
        float x = (Engine.getInstance().WindowWidth() - table.getWidth()) / 2;
        float y = (Engine.getInstance().WindowHeight() - table.getHeight()) / 2;
        table.setPosition((x > 0 ? x : 0), (y > 0 ? y : 0));

        //Adding buttons to Table
        table.row().maxHeight(55).maxWidth(55).uniform().padTop(+40);
        table.add(settingsBtn).padRight(-25);
        table.add(volumeBtn).padLeft(-25);
        table.row().maxHeight(55).maxWidth(125).padTop(-25);
        table.add(resumeBtn).colspan(2);
        table.row().maxHeight(55).maxWidth(125).padTop(-25);
        table.add(restartBtn).colspan(2);
        table.row().maxHeight(55).maxWidth(125).padTop(-25);
        table.add(exitBtn).colspan(2);

        //Finalizing Table visuals
        table.pack();
    }

    public void setVisible(boolean state) {
        table.setVisible(state);
    }
}
