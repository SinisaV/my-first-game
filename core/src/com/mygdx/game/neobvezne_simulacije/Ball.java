package com.mygdx.game.neobvezne_simulacije;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Ball {

    private final Vector2 velocity;
    private final Vector2 position;
    private final float radius;
    private final Color color;

    public Ball(float x, float y) {
        radius = MathUtils.random(25, 50);
        color = new Color(MathUtils.random(), MathUtils.random(), MathUtils.random(), 1);
        velocity = new Vector2(MathUtils.random(-200, 200), MathUtils.random(-200, 200));
        position = new Vector2(x, y);
    }

    public void update() {
        position.x += velocity.x * Gdx.graphics.getDeltaTime();
        position.y += velocity.y * Gdx.graphics.getDeltaTime();

        // Check left screen boundary
        if (position.x - radius < 0) {
            position.x = radius;
            velocity.x = Math.abs(velocity.x);
        }
        // Check right screen boundary
        else if (position.x + radius > Gdx.graphics.getWidth()) {
            position.x = Gdx.graphics.getWidth() - radius;
            velocity.x = -Math.abs(velocity.x);
        }

        // Check bottom screen boundary
        if (position.y - radius < 0) {
            position.y = radius;
            velocity.y = Math.abs(velocity.y);
            velocity.scl(0.7f);
        }

        // Apply gravity
        velocity.y -= 400 * Gdx.graphics.getDeltaTime();
    }

    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(color);
        shapeRenderer.circle(position.x, position.y, radius);
        shapeRenderer.end();
    }
}
