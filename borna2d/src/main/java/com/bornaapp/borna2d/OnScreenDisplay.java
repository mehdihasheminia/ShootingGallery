package com.bornaapp.borna2d;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.bornaapp.borna2d.game.levels.Engine;

import java.util.ArrayList;
import java.util.List;

public class OnScreenDisplay implements iDispose {

    private boolean f1 = false;

    private List<LogData> logList = new ArrayList<LogData>();
    private List<String> f1List = new ArrayList<String>();

    private BitmapFont font;

    public OnScreenDisplay() {
        font = new BitmapFont();//Use LibGDX's default Arial font.
        font.setColor(Color.CYAN);
        populateF1List();
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

    public void render(SpriteBatch batch) {

        float xMargin = 20f;
        float yMargin = 20f;
        float lineHeight = 20f;

        Camera camera = Engine.getInstance().getCurrentLevel().getCamera();

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
