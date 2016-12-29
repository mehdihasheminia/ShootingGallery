package com.bornaapp.borna2d.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.bornaapp.borna2d.components.BodyComponent;
import com.bornaapp.borna2d.components.ParticleComponent;
import com.bornaapp.borna2d.components.PositionComponent;
import com.bornaapp.borna2d.components.SoundComponent;
import com.bornaapp.borna2d.game.levels.Engine;
import com.bornaapp.borna2d.game.levels.LevelBase;

/**
 * Created by Mehdi on 9/25/2015.
 * ...
 */
public class SoundSystem extends IteratingSystem {

    private ComponentMapper<PositionComponent> posMap;

    public SoundSystem(LevelBase level) {
        super(Family.all(SoundComponent.class).get(), level.getSystemPriority());
        posMap = ComponentMapper.getFor(PositionComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

//        SoundComponent soundComp = soundMap.get(entity);
//
////        if (soundComp.playBackState == SoundComponent.PLAYBACK.PLAY) {
//            if (soundComp.is3D) {
//                //Calculating Pan value
//                Camera camera = Engine.getCurrentLevel().getCamera();
//                Vector2 distance = new Vector2(camera.position.x, camera.position.y);
//                distance.sub(new Vector2(getX(entity), getY(entity)));
//                //
//                soundComp.volume = Math.min(soundComp.volumeDegradationDistance / (Math.max(distance.len(), 1f)), 1f);
//                soundComp.pan = -distance.x / (Engine.ScreenWidth() / 2);
//                //
//                if (soundComp.pan > 1f)
//                    soundComp.pan = 1f;
//                else if (soundComp.pan < -1f)
//                    soundComp.pan = -1f;
//                //
//                soundComp.sound.setPan(soundComp.soundID, soundComp.pan, soundComp.volume);
//                //Calculationg Pitch Value
//                //todo:value of pich should be > 0.5 and < 2.0. Must be calculated by velocity
//                soundComp.sound.setPitch(soundComp.soundID, soundComp.pitch);
//            }
//            if (soundComp.soundID == -1) {
//                soundComp.soundID = soundComp.sound.play();
//            }
//            else
//                soundComp.sound.resume(soundComp.soundID);
//        }
//        if (soundComp.playBackState == SoundComponent.PLAYBACK.PAUSE || deltaTime == 0f) {
//            soundComp.sound.pause(soundComp.soundID);
//        }
//        if (soundComp.playBackState == SoundComponent.PLAYBACK.STOP) {
//            soundComp.sound.stop(soundComp.soundID);
//            soundComp.soundID = -1;
//        }
    }
}
