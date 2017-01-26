package com.bornaapp.shootingGallery.android;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.bornaapp.borna2d.game.application.PlatformSpecific;
import com.bornaapp.shootingGallery.game;

public class AndroidLauncher extends AndroidApplication {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (supportsES2()) {

            AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
            config.useAccelerometer = false;
            config.useCompass = false;
            initialize(new game(new AndroidSpecific()), config);

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

    /**
     *  Android Specific methods
     */
    public class AndroidSpecific extends PlatformSpecific {

        @Override
        public void share() {
            Context context = getApplicationContext();
            //Prepare share content
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, R.string.app_name);
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, R.string.txt_share);
            //Start share App
            try {
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(context, context.getString(R.string.txt_share_error), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void support() {
            Context context = getApplicationContext();
            //Prepare Email content
            Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.setType("text/plain");
            String toList[] = {context.getString(R.string.txt_email_Address)};
            emailIntent.putExtra(Intent.EXTRA_EMAIL, toList);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.txt_email_subject));
            emailIntent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.txt_email_message));
            //start Email App
            try {
                startActivity(Intent.createChooser(emailIntent, context.getString(R.string.txt_email_chooser)));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(context, context.getString(R.string.txt_share_error), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
