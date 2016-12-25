package com.bornaapp.shootingGallery.Controls;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.bornaapp.borna2d.game.levels.Engine;

/**
 * Created by Hashemi on 12/6/2016.
 */
public class CoinCounter {

    Table table = new Table();
    Label label;

    public float margin = 10f;

    public CoinCounter() {
    }

    public void Init() {

        //------------------------ Text Label -----------------------------------
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = new BitmapFont();
        style.fontColor = Color.MAROON;

        label = new Label("0",style);

        //---------------------------Layout Table ------------------------------
        Engine.getInstance().getCurrentLevel().uiStage.addActor(table);

        final Drawable tblBackground = new TextureRegionDrawable(new TextureRegion(Engine.getInstance().getCurrentLevel().assets.getTexture("coinBox.png")));
        table.setBackground(tblBackground);

        table.setWidth(100);
        table.setHeight(36);
        table.setTransform(true);
        float x = margin;
        float y = Engine.getInstance().WindowHeight() - table.getHeight() - margin;
        table.setPosition((x > 0 ? x : 0), (y > 0 ? y : 0));

        table.add(label).padBottom(3);

        table.pack();
    }

    public void setCoinCount(int count){
        label.setText(Integer.toString(count));
    }
}
