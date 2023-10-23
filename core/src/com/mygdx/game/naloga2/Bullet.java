package com.mygdx.game.naloga2;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Bullet extends DynamicGameObject {
    private static int pizzasRemoved = 0;
    private static final float SPEED = 300f;
    public Bullet(float x, float y) {
        super(x, y, Assets.bulletImg.getWidth(), Assets.bulletImg.getHeight());
    }

    public void update(float delta) {
        bounds.y += SPEED * delta;
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
