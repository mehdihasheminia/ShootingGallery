/*
 * Copyright (c) 2016.
 *  s. Mehdi HashemiNia
 *  All Rights Reserved.
 */

package com.bornaapp.borna2d.Debug;

import java.util.EnumSet;

/**
 * Created by s. Mehdi HashemiNia on 12/31/2016.
 */
public enum osdFlag {
     ShowValue,
     ShowStrings,
     ShowGrids,
     ShowMousePosition;

     public static final EnumSet<osdFlag> ALL_OPTS = EnumSet.allOf(osdFlag.class);
}
