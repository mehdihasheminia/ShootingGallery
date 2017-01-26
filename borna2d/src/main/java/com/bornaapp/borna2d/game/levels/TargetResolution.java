/*
 * Copyright (c) 2017.
 *  s. Mehdi HashemiNia
 *  All Rights Reserved.
 */

package com.bornaapp.borna2d.game.levels;

/**
 * Created by s. Mehdi HashemiNia on 1/17/2017.
 *
 * on android devices, screen resolution is forced by device hardware. some examples are:
 * Samsung Galaxy J7, J5, A5, A3, S3. Huawei Honor 4c:   720x1280
 * Samsung Galaxy S5. Huawei P8  :   1080x1920
 * Samsung Galaxy S6, S7, Note4, Note5:   1440x2560
 */
public class TargetResolution {
    public int width;
    public int height;

    public TargetResolution(Device device) {
        switch (device) {
            case SamsungGalaxyJ7_J5_A5_A3_S3:
                width = 1280;
                height = 720;
                break;
            case SamsungGalaxyS5_HuaweiP8:
                width = 1920;
                height = 1080;
                break;
            case SamsungGalaxyS6_S7_Note4_Note5:
                width = 2560;
                height = 1440;
                break;
        }
    }
}
