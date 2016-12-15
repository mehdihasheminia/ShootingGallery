package com.bornaapp.borna2d.graphics;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bornaapp.borna2d.game.levels.Engine;

/**
 * Created by Mehdi on 11/27/2016.
 */
public class Background {

    private Texture texture;
    private boolean wrap;

    public Background(String _Path, boolean _wrap) {
        texture = Engine.getInstance().getCurrentLevel().getAssetManager().getTexture(_Path);
        texture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
        wrap = _wrap;
    }

    public void render() {

        SpriteBatch batch = Engine.getInstance().getCurrentLevel().getBatch();
        Camera camera = Engine.getInstance().getCurrentLevel().getCamera();

        int texW, texH;
        if (wrap) {
            texW = (int) camera.viewportWidth;
            texH = (int) camera.viewportHeight;
        }
        else{
            texW = texture.getWidth();
            texH = texture.getHeight();
        }

        batch.draw(texture, 0, 0, 0, 0, texW, texH);
    }
}
