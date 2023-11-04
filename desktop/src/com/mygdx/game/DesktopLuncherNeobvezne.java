package com.mygdx.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.mygdx.game.neobvezne_simulacije.BallSimulation;
import com.mygdx.game.neobvezne_simulacije.RollingWheel;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLuncherNeobvezne {
    public static void main (String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setForegroundFPS(60);
        config.setTitle("my-first-game");
        new Lwjgl3Application(new BallSimulation(), config);
    }
}
