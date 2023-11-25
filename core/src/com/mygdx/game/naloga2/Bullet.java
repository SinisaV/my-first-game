package com.mygdx.game.naloga2;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;

public class Bullet extends DynamicGameObject implements Pool.Poolable {
    private final TextureRegion bulletRegion;
    private static int pizzasRemoved = 0;
    private static final float SPEED = 300f;
    public Bullet(float x, float y, TextureRegion bulletRegion) {
        super(x, y, bulletRegion.getRegionWidth(), bulletRegion.getRegionHeight());
        this.bulletRegion = bulletRegion;
    }

    public void update(float delta) {
        bounds.y += SPEED * delta;
    }

    @Override
    public void reset() {
        bounds.set(0, 0, bulletRegion.getRegionWidth(), bulletRegion.getRegionHeight());
    }

    public void draw(SpriteBatch batch) {
        batch.draw(bulletRegion, bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public static int getPizzasRemoved() {
        return pizzasRemoved;
    }

    public static void setPizzasRemoved(int pizzasRemoved) {
        Bullet.pizzasRemoved = pizzasRemoved;
    }
}
