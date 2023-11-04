package com.mygdx.game.neobvezne_simulacije;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class RollingWheel extends ApplicationAdapter {
    SpriteBatch batch;
    Texture wheelImg;
    private float wheelX, wheelY, wheelRotation, wheelRadius;

    private static float WHEEL_SPEED = 100f;

    @Override
    public void create() {
        batch = new SpriteBatch();
        wheelImg = new Texture("images/wheel.png");
        wheelX = 0;
        wheelY = 0;
        wheelRadius = (float)wheelImg.getWidth() / 2;
        wheelRotation = 0;
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        draw();

        batch.end();

        float distance = WHEEL_SPEED * Gdx.graphics.getDeltaTime();
        float circumference = (float) (2 * Math.PI * wheelRadius);
        float rotationAngle = (distance * 360) / circumference;

        wheelX += distance;
        wheelRotation += rotationAngle;

        if (wheelX + wheelRadius * 2 > Gdx.graphics.getWidth() || wheelX < 0) {
            WHEEL_SPEED *= -1;
        }

    }

    private void draw() {
        batch.draw(wheelImg, wheelX, wheelY, wheelRadius, wheelRadius,
                wheelImg.getWidth(), wheelImg.getHeight(), 1, 1, -wheelRotation, 0, 0,
                wheelImg.getWidth(), wheelImg.getHeight(), false, false);
    }

    @Override
    public void dispose() {
        batch.dispose();
        wheelImg.dispose();
    }

}
