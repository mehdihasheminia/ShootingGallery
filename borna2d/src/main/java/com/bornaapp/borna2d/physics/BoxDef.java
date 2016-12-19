package com.bornaapp.borna2d.physics;

/**
 * Created by Hashemi on 12/19/2016.
 */
public class BoxDef extends ShapeDef {
    public float width;
    public float height;

    public BoxDef(float w, float h){
        width = w;
        height = h;
    }

    public BoxDef(){
        width = 0;
        height = 0;
    }
}
