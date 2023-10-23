package com.mygdx.game.naloga2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class Dumbbell extends DynamicGameObject {
    private final Array<Rectangle> dumbbells;
    private float dumbbellSpawnTime;
    private int dumbbellsCollected;

    private static final float DUMBBELL_SPEED = 300f;
    private static final float  DUMBBELL_SPAWN_TIME = 0.5f;

    public Dumbbell (float x, float y) {
        super(x, y, 10, 10, Assets.dumbbellImg);
        dumbbells = new Array<>();
        dumbbellsCollected = 0;
    }

    public void update(float elapsedTime, float delta, Rectangle backpack) {
        //if (elapsedTime - dumbbellSpawnTime > DUMBBELL_SPAWN_TIME) spawnDumbbell();

        dumbbellSpawnTime += delta; // Increment the spawn timer based on delta

        if (dumbbellSpawnTime > DUMBBELL_SPAWN_TIME) {
            spawnDumbbell();
            dumbbellSpawnTime = 0; // Reset the spawn timer
        }

        for (Iterator<Rectangle> it = dumbbells.iterator(); it.hasNext(); ) {
            Rectangle dumbbell = it.next();
            dumbbell.y -= DUMBBELL_SPEED * delta;
            if (dumbbell.y + Assets.dumbbellImg.getHeight() < 0) {
                it.remove();
            }
            if (dumbbell.overlaps(backpack)) {
                dumbbellsCollected++;
                Assets.dumbbellCollect.play();
                it.remove();
            }
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        for (Rectangle dumbbell : dumbbells) {
            batch.draw(Assets.dumbbellImg, dumbbell.x, dumbbell.y);
        }

        Assets.font.setColor(Color.NAVY);
        Assets.font.draw(batch,
                "SCORE: " + dumbbellsCollected,
                20f, Gdx.graphics.getHeight() - 60f
        );
    }

    private void spawnDumbbell() {
        Rectangle dumbbell = new Rectangle();
        dumbbell.x = MathUtils.random(0f, Gdx.graphics.getWidth() - Assets.backpackImg.getWidth());
        dumbbell.y = Gdx.graphics.getHeight();
        dumbbell.width = Assets.dumbbellImg.getWidth();
        dumbbell.height = Assets.dumbbellImg.getHeight();
        dumbbells.add(dumbbell);
        System.out.println(dumbbells.size);
        dumbbellSpawnTime = TimeUtils.nanosToMillis(TimeUtils.nanoTime()) / 1000f;
    }

    public int getDumbbellsCollected() {
        return dumbbellsCollected;
    }
}
