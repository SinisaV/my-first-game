package com.mygdx.game.naloga2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.TimeUtils;

public class Dumbbell extends DynamicGameObject implements Pool.Poolable {
    //private final Array<Rectangle> dumbbells;
    private final TextureRegion dumbbellRegion;
    private static float spawnTime;
    private static int dumbbellsCollected = 0;

    private static final float SPEED = 300f;
    private static final float  SPAWN_TIME = 1.0f;

    public Dumbbell (float x, float y, TextureRegion dumbbellRegion) {
        super(x, y, dumbbellRegion.getRegionWidth(), dumbbellRegion.getRegionHeight());
        this.dumbbellRegion = dumbbellRegion;
        //dumbbells = new Array<>();
        Gdx.app.log("Dumbbell", "Created: X=" + x + ", Y=" + y);
    }

    public void update(float delta) {
        bounds.y -= SPEED * delta;
    }

    @Override
    public void reset() {
        bounds.y = 0;
        bounds.x = 0;
        Gdx.app.log("Dumbbell", "Reset: X=" + bounds.x + ", Y=" + bounds.y);
    }

    public void draw(SpriteBatch batch) {
        batch.draw(dumbbellRegion, bounds.x, bounds.y);
    }

    public static void spawnDumbbell(Pool<Dumbbell> dumbbellPool, Array<Dumbbell> dumbbells, TextureRegion dumbbellRegion) {
        Dumbbell dumbbell = dumbbellPool.obtain();
        float randomX = MathUtils.random(0f, Gdx.graphics.getWidth() - dumbbellRegion.getRegionWidth());
        float randomY = Gdx.graphics.getHeight();
        // Dumbbell dumbbell = new Dumbbell(randomX, randomY);
        dumbbell.bounds.setPosition(randomX, randomY);
        dumbbells.add(dumbbell);
        //System.out.println("Dumbbell spawned at: X=" + randomX + ", Y=" + randomY);
        spawnTime = TimeUtils.nanosToMillis(TimeUtils.nanoTime()) / 1000f;
        Gdx.app.log("Dumbbell", "Obtained from pool: X=" + randomX + ", Y=" + randomY);
    }

    public static int getDumbbellsCollected() {
        return dumbbellsCollected;
    }

    public static float getDumbbellSpawnTime() {
        return spawnTime;
    }

    public static float getDumbbellSpawnInterval() {
        return SPAWN_TIME;
    }

    public static void setDumbbellsCollected(int dumbbellsCollected) {
        Dumbbell.dumbbellsCollected = dumbbellsCollected;
    }

    public static int getDumbbellsInPool(Pool<Dumbbell> pool) {
        return pool.getFree();
    }
}
