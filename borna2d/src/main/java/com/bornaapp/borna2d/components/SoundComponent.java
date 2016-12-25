package com.bornaapp.borna2d.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.audio.Sound;
import com.bornaapp.borna2d.game.levels.Engine;
import com.bornaapp.borna2d.log;

/**
 * Created by Mehdi on 9/2/2015.
 * ...
 */
public class SoundComponent extends Component {
    public Sound sound;
    public long soundID = -1;

    public float volumeDegradationDistance = 30.0f;
    public float pan = 0.0f;
    public float pitch = 1.0f;
    public float volume = 1.0f;
    public boolean is3D = true;
    public boolean looping = true;

    //region Methods

    //private constructor, as components must be created
    //using Ashley Engine and initialize afterwards.
    private SoundComponent() {
    }

    public void Init(String _path, boolean _is3D, boolean _looping) {
        sound = Engine.getInstance().getCurrentLevel().assets.getSound(_path);
        is3D = _is3D;
        looping = _looping;
    }

    public void Play() {
        float volume = Engine.getInstance().getMasterVolume() * this.volume;
        if (Engine.getInstance().mute)
            volume = 0.0f;

        if (soundID == -1) {
            if (looping)
                soundID = sound.loop(volume);
            else
                soundID = sound.play(volume);

        } else {
            sound.setVolume(soundID, volume);
            sound.resume(soundID);
        }
    }

    public void Pause() {
        sound.pause(soundID);

    }

    public void Stop() {
        sound.stop(soundID);
        soundID = -1;
    }

    //endregion
}