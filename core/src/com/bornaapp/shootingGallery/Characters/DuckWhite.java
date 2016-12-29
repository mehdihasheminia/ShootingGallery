package com.bornaapp.shootingGallery.Characters;

import com.badlogic.gdx.math.Vector2;
import com.bornaapp.borna2d.game.levels.Engine;

/**
 * Created by Hashemi on 12/22/2016.
 */
public class DuckWhite extends Duck {

    public DuckWhite(float _lineY, boolean _flipped) {
        super(_lineY, _flipped);
    }

    @Override
    public void Init() {

        textPath = "duck_outline_white.atlas";
        scale = 1.0f;
        targetR = 20.0f;
        wingVertices = new Vector2[6];
        oscillationRadius = 40.0f;
        oscillationSpeed = 0.5f;

        if (flipped) {
            texOffsetX = +3.0f;
            texOffsetY = -19.0f;
            //vertices in clockwise order
            wingVertices[0] = new Vector2(-34.0f, 22.0f);
            wingVertices[1] = new Vector2(-40.0f, 0.0f);
            wingVertices[2] = new Vector2(-26.0f, -23.0f);
            wingVertices[3] = new Vector2(+30.0f, -22.0f);
            wingVertices[4] = new Vector2(+47.0f, 22.0f);
            wingVertices[5] = new Vector2(+30.0f, 30.0f);
            //
            headR = 22.0f;
            headOffsetX = -22.0f;
            headOffsetY = 45.0f;
            //
            linearVelocity = -1.0f;
            startPosition = new Vector2(Engine.getInstance().ScreenWidth(), LineY);
            missX = -100f;
        } else {
            texOffsetX = -3.0f; //negative of flipped offset about x-axis
            texOffsetY = -19.0f;
            //vertices in clockwise order
            wingVertices[0] = new Vector2(34.0f, 22.0f); // negative of flipped vertices about x-axis
            wingVertices[1] = new Vector2(40.0f, 0.0f);
            wingVertices[2] = new Vector2(26.0f, -23.0f);
            wingVertices[3] = new Vector2(-30.0f, -22.0f);
            wingVertices[4] = new Vector2(-47.0f, 22.0f);
            wingVertices[5] = new Vector2(-30.0f, 30.0f);
            //
            headR = 22.0f;
            headOffsetX = 22.0f; //negative of flipped offset about x-axis
            headOffsetY = 45.0f;
            //
            linearVelocity = 1.0f; //negative of flipped speed about x-axis
            startPosition = new Vector2(0.0f, LineY);
            missX = Engine.getInstance().ScreenWidth()+100f;
        }
    }
}
