package com.bornaapp.shootingGallery.android;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.os.Bundle;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.bornaapp.shootingGallery.game;

public class AndroidLauncher extends AndroidApplication {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (supportsES2()) {

            AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
            config.useAccelerometer = false;
            config.useCompass = false;
            initialize(new game(), config);

        } else {
            Toast.makeText(getApplicationContext(), "Android device does not support OpenGLES ver 2", Toast.LENGTH_LONG).show();
            throw new RuntimeException("Android device does not support OpenGLES ver 2");
            //super.finish(); //can be used instead of throwing Exception
        }
    }

    /**
     * Checks if android devices supports openGL_ES2
     * as our rendering methods are based on ES v2.0.
     *
     * @return true if supports ES2
     */
    boolean supportsES2() {
        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        return (configurationInfo.reqGlEsVersion >= 0x20000);
    }
}
