package com.mygdx.game.naloga2;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Pool;

public class Bullet extends DynamicGameObject implements Pool.Poolable {
    private static int pizzasRemoved = 0;
    private static final float SPEED = 300f;
    public Bullet(float x, float y) {
        super(x, y, Assets.bulletImg.getWidth(), Assets.bulletImg.getHeight());
    }

    public void update(float delta) {
        bounds.y += SPEED * delta;
    }

    @Override
    public void reset() {
        bounds.set(0, 0, Assets.bulletImg.getWidth(), Assets.bulletImg.getHeight());
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(Assets.bulletImg, bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public static int getPizzasRemoved() {
        return pizzasRemoved;
    }

    public static void setPizzasRemoved(int pizzasRemoved) {
        Bullet.pizzasRemoved = pizzasRemoved;
    }
}
