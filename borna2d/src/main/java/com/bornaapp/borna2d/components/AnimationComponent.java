package com.bornaapp.borna2d.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.bornaapp.borna2d.PlayStatus;

/**
 * Created by Mehdi on 09/02/2015.
 * ...
 */
public class AnimationComponent extends Component {
    private Animation animation;
    public float elapsedTime = 0;

    private PlayStatus playStatus = PlayStatus.Playing;

    //region Methods

    //private constructor, as components must be created
    //using Ashley Engine and initialize afterwards.
    private AnimationComponent() {
    }

    public void Init(Animation _anim) {
        animation = _anim;
    }

    public void setAnimation(Animation _animation) {
        if (animation == _animation)
            return;
        animation = _animation;
        elapsedTime = 0;
    }

    public Animation getAnimation() {
        return animation;
    }

    public void setPlayStatus(PlayStatus status){
        playStatus = status;
    }

    public PlayStatus getPlayStatus(){
        return playStatus;
    }

    //endregion
}
