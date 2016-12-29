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
import com.bornaapp.shootingGallery.Levels.EntryMenu;

/**
 * Created by Mehdi on 12/7/2016.
 */
public class LevelCompleteMenu {
    Table table = new Table();

    public LevelCompleteMenu() {
    }

    public void Init() {

        //------------------------ Continue Button -----------------------------------
        Skin continueBtnSkin = new Skin();
        continueBtnSkin.addRegions(Engine.getInstance().getCurrentLevel().assets.getTextureAtlas("ContinueBtn.atlas"));

        ImageButton continueBtn = new ImageButton(continueBtnSkin.getDrawable("01Normal"), continueBtnSkin.getDrawable("02Clicked"));

        continueBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Engine.getInstance().getCurrentLevel().NextLevel();
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
                Engine.getInstance().setLevel(new EntryMenu());
            }
        });

        //---------------------------Layout ------------------------------
        table.setVisible(false);
        Engine.getInstance().getCurrentLevel().uiStage.addActor(table);
//        table.debugAll();

        final Drawable tblBackground = new TextureRegionDrawable(new TextureRegion(Engine.getInstance().getCurrentLevel().assets.getTexture("levelCompleteTable.png")));
        table.setBackground(tblBackground);

        table.setWidth(280);
        table.setHeight(400);
        table.setTransform(true);
        float x = (Engine.getInstance().ScreenWidth() - table.getWidth()) / 2;
        float y = (Engine.getInstance().ScreenHeight() - table.getHeight()) / 2;
        table.setPosition((x > 0 ? x : 0), (y > 0 ? y : 0));

        //Adding buttons to Table
        table.bottom();
        table.row().maxHeight(55).maxWidth(125).padBottom(-25);
        table.add(continueBtn).colspan(2);
        table.row().maxHeight(55).maxWidth(125).padBottom(-25);
        table.add(restartBtn).colspan(2);
        table.row().maxHeight(55).maxWidth(125).padBottom(20);
        table.add(exitBtn).colspan(2);

        //Finalizing Table visuals
        table.pack();
    }

    public void setVisible(boolean state) {
        table.setVisible(state);
    }
}

