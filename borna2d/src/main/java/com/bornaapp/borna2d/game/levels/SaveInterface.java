package com.bornaapp.borna2d.game.levels;

import com.badlogic.gdx.utils.Array;

/**
 * Created by Hashemi on 12/10/2016.
 */
public class SaveInterface {
    public Array<SaveSlot> slots = new Array<SaveSlot>();

    public boolean SlotExits(String _name) {
        for (SaveSlot s : slots) {
            if (s.name.equals(_name))
                return true;
        }
        return false;
    }

    public SaveSlot getSlot(String _name) {
        for (SaveSlot s : slots) {
            if (s.name.equals(_name))
                return s;
        }
        return null;
    }
}
