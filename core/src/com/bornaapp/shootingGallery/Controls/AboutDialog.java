/*
 * Copyright (c) 2017.
 *  s. Mehdi HashemiNia
 *  All Rights Reserved.
 */

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
import com.bornaapp.borna2d.game.levels.LevelBase;

/**
 * Created by s. Mehdi HashemiNia on 1/25/2017.
 */
public class AboutDialog {

    Table table = new Table();
    LevelBase currentLevel;

    public void Init() {
        currentLevel = Engine.getInstance().getCurrentLevel();

        //------------------------------- OK Button -----------------------------------
        Skin okBtnSkin = new Skin();
        okBtnSkin.addRegions(Engine.getInstance().getCurrentLevel().assets.getTextureAtlas("okBtn.atlas"));

        ImageButton okBtn = new ImageButton(okBtnSkin.getDrawable("0ok_normal"), okBtnSkin.getDrawable("1ok_clicked"));

        okBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                table.setVisible(false);
            }
        });

        //-------------------------------- Layout -------------------------------------
        table.setVisible(false);
        currentLevel.dialogUIStage.addActor(table);
        table.setZIndex(100);

        final Drawable tblBackground = new TextureRegionDrawable(new TextureRegion(Engine.getInstance().getCurrentLevel().assets.getTexture("aboutTable.png")));
        table.setBackground(tblBackground);

        table.setWidth(400);
        table.setHeight(322);
        table.setTransform(true);
        float x = (Engine.getInstance().ScreenWidth() - table.getWidth()) / 2;
        float y = (Engine.getInstance().ScreenHeight() - table.getHeight()) / 2;
        table.setPosition((x > 0 ? x : 0), (y > 0 ? y : 0));

        //Adding buttons to Table
        table.row().maxHeight(75).maxWidth(75);
        table.add(okBtn).expand().bottom();

        //Finalizing Table visuals
        table.pack();
    }

    public void setVisible(boolean state) {
        table.setVisible(state);
    }
}
