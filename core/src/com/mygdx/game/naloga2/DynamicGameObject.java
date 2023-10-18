package com.mygdx.game.naloga2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class DynamicGameObject extends GameObject {
    public final Vector2 velocity;
    public final Vector2 accel;

    public DynamicGameObject (float x, float y, float width, float height, Texture texture) {
        super(x, y, width, height, texture);
        velocity = new Vector2();
        accel = new Vector2();
    }
}