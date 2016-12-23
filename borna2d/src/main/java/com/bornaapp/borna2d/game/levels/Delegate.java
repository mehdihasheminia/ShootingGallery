package com.bornaapp.borna2d.game.levels;

/**
 * Created by Mehdi on 12/22/2016.
 */
public abstract class Delegate {

    public Delegate(String _name){
        name = _name;
    }
    public String name;
    public  abstract void Execute();
}
