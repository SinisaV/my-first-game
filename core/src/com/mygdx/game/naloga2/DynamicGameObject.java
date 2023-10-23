package com.mygdx.game.naloga2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class DynamicGameObject extends GameObject {
    public final Vector2 velocity;
    public final Vector2 accel;

    public DynamicGameObject (float x, float y, float width, float height) {
        super(x, y, width, height);
        velocity = new Vector2();
        accel = new Vector2();
    }

    @Override
    public void draw(SpriteBatch batch) {
        Assets.font.setColor(Color.RED);
        Assets.font.draw(batch,
                "MY GAME",
                20f, Gdx.graphics.getHeight() - 140f
        );
    }
}
