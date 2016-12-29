package com.bornaapp.borna2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.bornaapp.borna2d.game.levels.Engine;

import java.util.ArrayList;
import java.util.List;

public class OnScreenDisplay implements iDispose {

    Engine engine = Engine.getInstance();
    SpriteBatch batch;
    ShapeRenderer shapeRenderer;
    Camera camera;

    private boolean f1 = false;

    private List<LogData> logList = new ArrayList<LogData>();
    private List<String> f1List = new ArrayList<String>();

    private BitmapFont font;

    public OnScreenDisplay() {
    }

    public void Init() {
        //Use LibGDX's default Arial font.
        font = new BitmapFont();
        font.setColor(Color.CYAN);
        populateF1List();

        batch = engine.getCurrentLevel().getBatch();
        shapeRenderer = engine.getCurrentLevel().getShapeRenderer();
        camera = Engine.getInstance().getCurrentLevel().getCamera();
    }

    private class LogData {
        String title;
        String value;

        LogData(String _title, String _value) {
            title = _title;
            value = _value;
        }
    }

    public void log(String title, int value) {
        log(title, Integer.toString(value));
    }

    public void log(String title, float value) {
        log(title, Float.toString(value));
    }

    public void log(String title, Vector2 value) {
        log(title, Float.toString(value.x) + "," + Float.toString(value.y));
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

    private void DrawGrids(int pixel) {

        //with and height of the container
        float viewportWidth = camera.viewportWidth;
        float viewportHeight = camera.viewportHeight;

        shapeRenderer.setProjectionMatrix(camera.combined);

        //Draw viewport Rect
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.rect(0, 0, viewportWidth - 1, viewportHeight - 1);
        shapeRenderer.end();

        //Draw grids
        shapeRenderer.setColor(Color.GRAY);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        for (int x = 0; x < viewportWidth; x += pixel) {
            shapeRenderer.line(x, 0, x, viewportHeight);
        }

        for (int y = 0; y < viewportHeight; y += pixel) {
            shapeRenderer.line(0, y, viewportWidth, y);
        }

        shapeRenderer.end();
    }

    private void DrawMouseLocation() {
        //convert mouse pointer coordinates from screen-space to world-space
        Vector3 worldCoord = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

        //Draw grids
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.line(worldCoord.x + 10, worldCoord.y, 0, worldCoord.y);
        shapeRenderer.line(worldCoord.x, worldCoord.y + 10, worldCoord.x, 0);
        shapeRenderer.end();

        if (!batch.isDrawing())
            batch.begin();

        font.draw(batch, "( " + Integer.toString((int)worldCoord.x) + ", " + Integer.toString((int)worldCoord.y) + " )", worldCoord.x + 10, worldCoord.y + 10);

        batch.end();
    }

    public void render() {
        try {

            DrawMouseLocation();

            float xMargin = 20f;
            float yMargin = 20f;
            float lineHeight = 20f;

            float x = xMargin - camera.viewportWidth / 2;
            float y = yMargin - camera.viewportHeight / 2;

            batch.setProjectionMatrix(camera.projection);

            if (!batch.isDrawing())
                batch.begin();
            if (f1) {
                for (String str : f1List) {
                    font.draw(batch, str, x, y);
                    y += lineHeight;
                }
            } else {
                for (LogData data : logList) {
                    font.draw(batch, data.title + " : " + data.value, x, y);
                    y += lineHeight;
                }
            }
            batch.end();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void dispose() {
        font.dispose();
        logList.clear();
    }

    //region utility methods

    public boolean getF1() {
        return f1;
    }

    public void SetF1(boolean _state) {
        f1 = _state;
    }

    public void populateF1List() {
        f1List.add("F1   : Show/Hide instructions");
        f1List.add("F2   : Show/Hide Logs");
        f1List.add("NUM_1  Enable/Disable Lighting: ");
        f1List.add("NUM_2: Show/Hide bounding areas");
    }
    //endregion
}
