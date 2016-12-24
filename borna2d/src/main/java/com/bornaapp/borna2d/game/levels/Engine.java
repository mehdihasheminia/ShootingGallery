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

    public Progress progress = new Progress();

    private EngineConfig engineConfig = new EngineConfig();

    private Array<LevelBase> levels = new Array<LevelBase>();
    private LevelBase currentLevel = null;

    private float masterVolume = 1.0f;
    public boolean mute = false;

    //region Singleton construction & instantiation

    private Engine() {
    }

    public static Engine getInstance() {
        return instance;
    }

    //endregion

    //region Load/save game Progress
    public class Progress {

        private final String saveFilePath = "save.json";
        private SlotCollection slotCollection = new SlotCollection();

        public boolean fileExist() {
            return Gdx.files.internal(saveFilePath).exists();
        }

        public void AddSlot(Slot slot) {
            if (!SlotExits(slot.name))
                slotCollection.slots.add(slot);
        }

        public boolean SlotExits(String _name) {
            for (Slot s : slotCollection.slots) {
                if (s.name.equals(_name))
                    return true;
            }
            return false;
        }

        public SlotCollection Load() {
            try {
                FileHandle file = Gdx.files.internal(saveFilePath);
                Json json = new Json();
                slotCollection = json.fromJson(SlotCollection.class, file);
                return slotCollection;
            } catch (Exception e) {
                log.error(e.getMessage());
                return null;
            }
        }

        public void Save() {
            try {
                Json json = new Json();
                json.setUsePrototypes(false);
                json.setOutputType(JsonWriter.OutputType.json);
                FileHandle file = new FileHandle(saveFilePath);
                file.writeString(json.prettyPrint(slotCollection), false);
                //confirmation message
                System.out.println(file + " : created");
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }
    //endregion

    //region Engine configurations

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

        for (int i = 0; i < levels.size; i++) {
            LevelBase level = levels.get(i);
            //Check if level is previously loaded
            if (newLevel.getClass().getName().equals(level.getClass().getName())) {

                if (replacePrevious) {
                    //remove currently available instance of this class to start a fresh instance
                    level.inResponseToEngine_dispose();
                    levels.removeIndex(i);
                } else {
                    //cancel newLevel & keep previous instance
                    newLevel.inResponseToEngine_dispose();
                    currentLevel = level;
                    return;
                }
            }
        }
        levels.add(newLevel);
        currentLevel = newLevel;
        currentLevel.inResponseToEngine_create();
        currentLevel.inResponseToEngine_resume();
        currentLevel.inResponseToEngine_resize(WindowWidth(), WindowHeight());
    }

    public void setLevel(LevelBase newLevel) {
        setLevel(newLevel, true);
    }

    public LevelBase getCurrentLevel() {
        return currentLevel;
    }

    //endregion

    //region Handling application requests

    public void create() {

        LoadEngineConfigFromFile();

        try {
            if (progress.fileExist())
                progress.Load();
            else
                progress.Save();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public void dispose() {
        //Dispose data of each level
        for (LevelBase level : levels) {
            try {
                level.inResponseToEngine_dispose();
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        //clear list of levels
        try {
            levels.clear();
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

    public void resize(int width, int height) {
        try {
            currentLevel.inResponseToEngine_resize(width, height);
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

    //region Master Volume control

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
//        return (frameRate == 0 ? 60 : frameRate);
        return frameRate;
    }
    //endregion
}
