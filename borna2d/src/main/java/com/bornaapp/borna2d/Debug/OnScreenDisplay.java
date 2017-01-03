package com.bornaapp.borna2d.Debug;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.bornaapp.borna2d.Flags;
import com.bornaapp.borna2d.game.levels.Engine;
import com.bornaapp.borna2d.iDispose;

import java.util.ArrayList;
import java.util.List;

public class OnScreenDisplay implements iDispose {

    public Flags<osdFlag> flags = new Flags<osdFlag>(osdFlag.class);

    private Engine engine = Engine.getInstance();
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private Camera camera;

    private List<LogData> logList = new ArrayList<LogData>();

    private BitmapFont font = new BitmapFont();
    private float posCorrectionX = 0f;

    private class LogData {
        String title;
        String value;

        LogData(String _title, String _value) {
            title = _title;
            value = _value;
        }
    }

    public OnScreenDisplay() {
    }

    public void Init() {
        //Use LibGDX's default Arial font.
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        batch = engine.getCurrentLevel().getBatch();
        shapeRenderer = engine.getCurrentLevel().getShapeRenderer();
        camera = Engine.getInstance().getCurrentLevel().getCamera();
    }

    public void log(String title, int value) {
        log(title, Integer.toString(value));
    }

    public void log(String title, float value) {
        log(title, Float.toString(value));
    }

    public void log(String title, Vector2 value) {
        log(title, "[" + Float.toString(value.x) + ", " + Float.toString(value.y) + "]");
    }

    public void log(String title) {
        log(title, "");
    }

    public void log(String title, String value) {
        //prevents adding repetitive logs
        //instead updates existing logs
        for (LogData data : logList) {
            if (data.title.equals(title)) {
                data.value = value;
                return;
            }
        }
        //saves all requested logs in an array
        logList.add(new LogData(title, value));
    }

    private void DrawLogs() {
        try {
            Vector2 linePos = new Vector2(5 * font.getSpaceWidth(), font.getLineHeight());
            font.setColor(Color.CYAN);

            if (!batch.isDrawing())
                batch.begin();
            for (LogData data : logList) {
                font.draw(batch, data.title + (data.value.isEmpty() ? "" : " : " + data.value), linePos.x, linePos.y);
                linePos.y += font.getLineHeight();
            }
            batch.end();

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void DrawGrids(int pixel) {

        //Draw viewport boundary Rect
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.rect(0, 0, camera.viewportWidth - 1, camera.viewportHeight - 1);
        shapeRenderer.end();

        //Draw grids
        shapeRenderer.setColor(Color.GRAY);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        for (int x = 0; x < camera.viewportWidth; x += pixel) {
            shapeRenderer.line(x, 0, x, camera.viewportHeight);
        }

        for (int y = 0; y < camera.viewportHeight; y += pixel) {
            shapeRenderer.line(0, y, camera.viewportWidth, y);
        }

        shapeRenderer.end();
    }

    private void DrawMouseLocation() {
        //convert mouse pointer coordinates from screen-space to world-space
        Vector3 mouseInWorldCoord = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        Vector3 screenInWorldCoord = camera.unproject(new Vector3(engine.ScreenWidth(), engine.ScreenHeight(), 0));

        //Draw Mouse position lines
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.line(mouseInWorldCoord.x + 10, mouseInWorldCoord.y, 0, mouseInWorldCoord.y);
        shapeRenderer.line(mouseInWorldCoord.x, mouseInWorldCoord.y + 10, mouseInWorldCoord.x, 0);
        shapeRenderer.end();

        //Draw Text
        font.setColor(Color.BLACK);
        if (!batch.isDrawing())
            batch.begin();
        String txt = "( " + Integer.toString((int) mouseInWorldCoord.x) + ", " + Integer.toString((int) mouseInWorldCoord.y) + " )";
        com.badlogic.gdx.graphics.g2d.GlyphLayout glyphLayout = font.draw(batch, txt, mouseInWorldCoord.x - posCorrectionX, mouseInWorldCoord.y);
        batch.end();

        // Calculate text bounding rectangle and
        // correct text position when clipped
        Rectangle rect = new Rectangle(mouseInWorldCoord.x, mouseInWorldCoord.y - glyphLayout.height, glyphLayout.width, glyphLayout.height);

        float newOffest = rect.x + rect.width - screenInWorldCoord.x;
        posCorrectionX = Math.max(newOffest, posCorrectionX);
        if (newOffest < 0)
            posCorrectionX = 0;
    }

    public void render() {
        try {
            if (engine.getConfig().logLevel == LogLevel.NONE)
                return;

            if (flags.contains(osdFlag.ShowGrids))
                DrawGrids(engine.getConfig().ppm);

            if (flags.contains(osdFlag.ShowMousePosition))
                DrawMouseLocation();

            if (flags.contains(osdFlag.ShowLogs))
                DrawLogs();

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void dispose() {
        font.dispose();
        logList.clear();
    }

    public void clearLogList() {
        logList.clear();
    }

    public void setFontScale(float scale) {
        font.getData().setScale(scale);
    }
}
