package com.bornaapp.shootingGallery.Controls;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.bornaapp.borna2d.game.levels.Engine;
import com.bornaapp.borna2d.game.levels.LevelBase;

/**
 * Created by Hashemi on 12/6/2016.
 */
public class CoinCounter {

    LevelBase currentLevel = Engine.getInstance().getCurrentLevel();
    Table table = new Table();
    Label label;

    public float marginX = 10f;
    public float marginY = 10f;

    public CoinCounter() {
    }

    public void Init() {

        //------------------------ Text Label -----------------------------------
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = new BitmapFont();
        style.fontColor = Color.MAROON;

        label = new Label("0",style);

        //---------------------------Layout Table ------------------------------
        currentLevel.baseUIStage.addActor(table);

        final Drawable tblBackground = new TextureRegionDrawable(new TextureRegion(currentLevel.assets.getTexture("coinBox.png")));
        table.setBackground(tblBackground);

        table.setWidth(100);
        table.setHeight(36);
        table.setTransform(true);
        float x = marginX;
        float y = currentLevel.getCamera().viewportHeight - table.getHeight() - marginY;
        table.setPosition((x > 0 ? x : 0), (y > 0 ? y : 0));

        table.add(label).padBottom(3);

        table.pack();
    }

    public void setCoinCount(int count){
        label.setText(Integer.toString(count));
    }
}
