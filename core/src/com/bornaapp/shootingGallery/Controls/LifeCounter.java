package com.bornaapp.shootingGallery.Controls;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.bornaapp.borna2d.game.levels.Engine;

/**
 * Created by Mehdi on 12/6/2016.
 */
public class LifeCounter {

    Table table = new Table();
    Image image;

    public LifeCounter() {
    }

    public void Init() {

        //------------------------ Image -----------------------------------
        image = new Image(new TextureRegion(Engine.getInstance().getCurrentLevel().assets.getTexture("LifeBar.png")));

        //---------------------------Layout Table ------------------------------
        Engine.getInstance().getCurrentLevel().baseUIStage.addActor(table);

        final Drawable tblBackground = new TextureRegionDrawable(new TextureRegion(Engine.getInstance().getCurrentLevel().assets.getTexture("LifeBox.png")));
        table.setBackground(tblBackground);

        table.setWidth(120);
        table.setHeight(28);
        table.setTransform(true);
        float margin = 10f;
        float x = Engine.getInstance().ScreenWidth() - table.getWidth() - margin;
        float y = Engine.getInstance().ScreenHeight() - table.getHeight() - margin;
        table.setPosition((x > 0 ? x : 0), (y > 0 ? y : 0));

        table.left();
        table.add(image).padBottom(2).padLeft(28);

        table.pack();
    }

    public void setLifeCount(float count) {
        if (count > 100)
            count = 100;
        if (count < 0)
            count = 0;

        // -visualScale- determines by size and padding of table
        // and image to match the bar to its value and table
        float visualScale = 0.85f;

        image.setScaleX( count * visualScale);
    }
}
