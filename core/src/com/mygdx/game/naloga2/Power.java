package com.mygdx.game.naloga2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class Power extends DynamicGameObject {
    private static float spawnTime;
    private static final float SPEED = 200f;
    private static final float SPAWN_TIME = 7.0f;
    private static boolean initialSpawn = false;

    public Power(float x, float y) {
        super(x, y, Assets.powerImg.getWidth(), Assets.powerImg.getHeight());
    }

    public void update(float delta) {
        bounds.y -= SPEED * delta;
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(Assets.powerImg, bounds.x, bounds.y);
    }

    public static void spawnPower(Array<Power> powers) {
        if (!initialSpawn) {
            spawnTime = TimeUtils.nanosToMillis(TimeUtils.nanoTime()) / 1000f;
            initialSpawn = true;
        }
        else {
            float randomX = MathUtils.random(0f, Gdx.graphics.getWidth() - Assets.pizzaImg.getWidth());
            float randomY = Gdx.graphics.getHeight();
            Power power = new Power(randomX, randomY);
            powers.add(power);
            spawnTime = TimeUtils.nanosToMillis(TimeUtils.nanoTime()) / 1000f;
        }
    }

    public static float getPowerSpawnTime() {
        return spawnTime;
    }

    public static float getPowerSpawnInterval() {
        return SPAWN_TIME;
    }

}
