package com.bornaapp.borna2d.components;

import com.badlogic.ashley.core.Component;

public class PositionComponent extends Component {

    // normally we should consider x & y as integers but we used float
    // because of its compatibility with Vector2 class
    public float x, y;

    //region Methods

    //private constructor, as components must be created
    //using Ashley Engine and initialize afterwards.
    private PositionComponent() {
    }

    public void Init(float _x, float _y) {
        x = _x;
        y = _y;
    }
}
