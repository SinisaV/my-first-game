package com.mygdx.game.naloga2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Assets {
    public static Texture backgroundImg;
    public static  Texture backpackImg;
    public static  Texture dumbbellImg;
    public static  Texture pizzaImg;
    public static  Texture bulletImg;
    public static Texture powerImg;
    public static  Sound backpackVoice;
    public static  Sound dumbbellCollect;

    public static  BitmapFont font;

    public static void load() {
        backgroundImg = new Texture("images/fitness_background.jpg");
        backpackImg = new Texture("images/backpack.png");
        dumbbellImg = new Texture("images/dumbbell.png");
        pizzaImg = new Texture("images/pizza.png");
        bulletImg = new Texture("images/protein.png");
        powerImg = new Texture("images/power.png");
        backpackVoice = Gdx.audio.newSound(Gdx.files.internal("sounds/open_bag_sound.mp3"));
        dumbbellCollect = Gdx.audio.newSound(Gdx.files.internal("sounds/dropped_weights.wav"));

        font = new BitmapFont(Gdx.files.internal("fonts/oswald-32.fnt"));
    }

    public static void dispose() {
        font.dispose();
        backpackImg.dispose();
        dumbbellImg.dispose();
        pizzaImg.dispose();
        backpackVoice.dispose();
        dumbbellCollect.dispose();
    }
}
