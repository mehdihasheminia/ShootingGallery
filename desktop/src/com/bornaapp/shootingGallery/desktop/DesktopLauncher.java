package com.bornaapp.shootingGallery.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.bornaapp.shootingGallery.game;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Shooting Galley";
		config.width = 1280;
		config.height = 720;
		config.foregroundFPS = 0;
		config.backgroundFPS = -1;
		config.vSyncEnabled = false;
		new LwjglApplication(new game(), config);
	}
}
