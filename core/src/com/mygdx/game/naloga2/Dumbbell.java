package com.mygdx.game.naloga2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class Dumbbell extends DynamicGameObject {
    //private final Array<Rectangle> dumbbells;
    private static float spawnTime;
    private static int dumbbellsCollected = 0;

    private static final float SPEED = 300f;
    private static final float  SPAWN_TIME = 1.0f;

    public Dumbbell (float x, float y) {
        super(x, y, Assets.dumbbellImg.getWidth(), Assets.dumbbellImg.getHeight());
        //dumbbells = new Array<>();
    }

    public void update(float delta) {
        bounds.y -= SPEED * delta;
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(Assets.dumbbellImg, bounds.x, bounds.y);
    }

    public static void spawnDumbbell(Array<Dumbbell> dumbbells) {
        float randomX = MathUtils.random(0f, Gdx.graphics.getWidth() - Assets.dumbbellImg.getWidth());
        float randomY = Gdx.graphics.getHeight();
        Dumbbell dumbbell = new Dumbbell(randomX, randomY);
        dumbbells.add(dumbbell);
        //System.out.println("Dumbbell spawned at: X=" + randomX + ", Y=" + randomY);
        spawnTime = TimeUtils.nanosToMillis(TimeUtils.nanoTime()) / 1000f;
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
}
