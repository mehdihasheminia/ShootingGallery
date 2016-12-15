package com.bornaapp.borna2d.game.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.bornaapp.borna2d.log;

/**
 * Engine class uses singleton pattern to Ensure it has
 * only one instance, and provide a global point of access to it.
 * We didn't use a static class because we need it to be instantiated
 * like a class
 */
public final class Engine {

    private final static Engine instance = new Engine();

    private final String saveFilepath = "save.json";
    public SaveInterface saveInterface = new SaveInterface();
    private EngineConfig engineConfig = new EngineConfig();

    private Array<LevelBase> levels = new Array<LevelBase>();
    private LevelBase currentLevel = null;

    private float masterVolume = 1.0f;
    private boolean mute = false;

    //region constructor & initialization
    private Engine(){}

    public static Engine getInstance() {
        return instance;
    }

    public void Init() {

        LoadEngineConfigFromFile();

        if (Gdx.files.internal(saveFilepath).exists())
            ReadSaveFile();
        else
            WriteSaveFile();
    }
    //endregion

    //region Load/save game data and configurations

    public SaveInterface ReadSaveFile() {
        try {
            FileHandle file = Gdx.files.internal(saveFilepath);
            Json json = new Json();
            saveInterface = json.fromJson(SaveInterface.class, file);
            return saveInterface;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public void WriteSaveFile() {
        try {
            Json json = new Json();

            json.setUsePrototypes(false);
            json.setOutputType(JsonWriter.OutputType.json);
            FileHandle file = new FileHandle(saveFilepath);
            file.writeString(json.prettyPrint(saveInterface), false);
            //confirmation message
            System.out.println(file + " : created");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void LoadEngineConfigFromFile() {
        engineConfig = null;
        try {
            FileHandle file = Gdx.files.internal("engineConf.json");
            Json json = new Json();
            engineConfig = json.fromJson(EngineConfig.class, file);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public EngineConfig getConfig() {
        return engineConfig;
    }
    //endregion

    //region level manager

    public void setLevel(LevelBase newLevel, boolean replacePrevious) {
        //
        for (int i = 0; i < levels.size; i++) {
            LevelBase l = levels.get(i);
            if (newLevel.getClass().getName().equals(l.getClass().getName())) {
                //level is previously loaded
                if (replacePrevious) {
                    //replace previous instance of this class
                    l.inResponseToEngine_dispose();
                    levels.removeIndex(i);
                } else {
                    currentLevel = l;
                    newLevel.inResponseToEngine_dispose();
                    return;
                }
                //cancel new level & load previous one
            }
        }
        levels.add(newLevel);
        currentLevel = newLevel;
        currentLevel.inResponseToEngine_create();
        currentLevel.inResponseToEngine_resume();
        currentLevel.inResponseToEngine_resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());//todo: Engine.width
    }

    public void setLevel(LevelBase newLevel) {
        setLevel(newLevel, true);
    }

    /**
     * @return
     */
    public LevelBase getCurrentLevel() {
        return currentLevel;
    }

    //endregion

    //region Handling application requests

    public void dispose() {
        //Dispose all levels
        for (int i = 0; i < levels.size; i++) {
            LevelBase l = levels.get(i);
            if (l != null) {
                l.inResponseToEngine_dispose();
            }
        }
        levels.clear();
        //collect all garbage memory
        System.gc();
    }

    public void resize(int width, int height) {
        try {
            currentLevel.inResponseToEngine_resize(width, height);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public void render() {
        try {
            currentLevel.inResponseToEngine_render(Gdx.graphics.getDeltaTime());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public void pause() {
        try {
            currentLevel.inResponseToEngine_pause();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public void resume() {
        try {
            currentLevel.inResponseToEngine_resume();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
    //endregion

    //region music & sound Volume control

    public float getMasterVolume() {
        return masterVolume;
    }

    public void setMasterVolume(float value) {
        if (value > 1.0f)
            value = 1.0f;
        else if (value < 0.0f)
            value = 0.0f;
        masterVolume = value;
    }

    public boolean isMute() {
        return mute;
    }

    public void setMute(boolean mute) {
        mute = mute;
    }

    //endregion

    //region Utilities
    public float getJavaHeap() {
        return (Gdx.app.getJavaHeap() / 1048576);
    }

    public float getNativeHeap() {
        return (Gdx.app.getNativeHeap() / 1048576);
    }

    public int WindowWidth() {
        return Gdx.graphics.getWidth();
    }

    public int WindowHeight() {
        return Gdx.graphics.getHeight();
    }

    public int frameRate() {
        int frameRate = Gdx.graphics.getFramesPerSecond();
        return (frameRate == 0 ? 60 : frameRate);
    }
    //endregion

}
