package com.bornaapp.shootingGallery.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.bornaapp.borna2d.game.application.PlatformSpecific;
import com.bornaapp.shootingGallery.game;

import java.awt.Desktop;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Shooting Galley";
        config.width = 1280;
        config.height = 720;
        config.foregroundFPS = 0;
        config.backgroundFPS = -1;
        config.vSyncEnabled = false;
        new LwjglApplication(new game(new DesktopSpecific()), config);
    }

    /**
     *  Desktop Specific methods
     */
    public static class DesktopSpecific extends PlatformSpecific {
        @Override
        public void share() {

            String mailTo = "";
            String subject = "";
            String body = "";

            try {
                subject = URLEncoder.encode("Shooting Gallery", "utf-8").replace("+", "%20");
                body = URLEncoder.encode("I've recently installed Shooting gallery game and loved it! I suggest you play it too.\nhttps://cafebazaar.ir/developer/bornaapp/", "utf-8").replace("+", "%20");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            openMailApp(mailTo, subject, body);
        }

        @Override
        public void support() {

            String mailTo = "bornaapp@gmail.com";
            String subject = "";
            String body = "";

            try {
                subject = URLEncoder.encode("Technical support", "utf-8").replace("+", "%20");
                body = URLEncoder.encode("Please specify your technical difficulty with the app and your contact information here.", "utf-8").replace("+", "%20");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            openMailApp(mailTo, subject, body);
        }

        private void openMailApp(String mailTo, String subject, String body) {

            final String mailURIStr = String.format("mailto:%s?subject=%s&cc=%s&body=%s", mailTo, subject, "%20", body);

            try {
                Desktop.getDesktop().mail(new URI(mailURIStr));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }
}
