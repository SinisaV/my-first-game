package com.mygdx.game.naloga2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.TimeUtils;

public class Power extends DynamicGameObject implements Pool.Poolable {
    private final TextureRegion powerRegion;
    private static float spawnTime;
    private static final float SPEED = 200f;
    private static final float SPAWN_TIME = 7.0f;
    private static boolean initialSpawn = false;
    public float powerUpEffectX;
    public float powerUpEffectY;

    public Power(float x, float y, TextureRegion powerRegion) {
        super(x, y, powerRegion.getRegionWidth(), powerRegion.getRegionHeight());
        this.powerRegion = powerRegion;
    }

    public void update(float delta) {
        bounds.y -= SPEED * delta;
    }

    @Override
    public void reset() {
        bounds.set(0, 0, powerRegion.getRegionWidth(), powerRegion.getRegionHeight());
    }

    public void draw(SpriteBatch batch) {
        batch.draw(powerRegion, bounds.x, bounds.y);
    }

    public static void spawnPower(Pool<Power> powerPool, Array<Power> powers, TextureRegion powerRegion) {
        if (!initialSpawn) {
            spawnTime = TimeUtils.nanosToMillis(TimeUtils.nanoTime()) / 1000f;
            initialSpawn = true;
        }
        else {
            Power power = powerPool.obtain();
            float randomX = MathUtils.random(0f, Gdx.graphics.getWidth() - powerRegion.getRegionWidth());
            float randomY = Gdx.graphics.getHeight();
            // Power power = new Power(randomX, randomY);
            power.bounds.setPosition(randomX, randomY);
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

    public void updatePowerUpEffectPosition(float x, float y) {
        powerUpEffectX = x;
        powerUpEffectY = y;
    }
}
