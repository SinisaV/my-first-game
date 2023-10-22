package com.mygdx.game.naloga2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.awt.Rectangle;

public class Backpack extends GameObject {
    private Rectangle backpack;
    private final Bullet bullet;

    private int health = 100;

    private static final float BACKPACK_SPEED = 250f;

    public Backpack (float x, float y, Bullet bullet) {
        super(x, y, 10, 10, Assets.backpackImg);
        this.bullet = bullet;
    }

    public void create() {
        backpack = new Rectangle();
        backpack.x = (int) (Gdx.graphics.getWidth() / 2f - Assets.backpackImg.getWidth() / 2f);
        backpack.y = (int) 20f;
        backpack.width = Assets.backpackImg.getWidth();
        backpack.height = Assets.backpackImg.getHeight();
    }
    public void draw(SpriteBatch batch) {
        batch.draw(Assets.backpackImg, backpack.x, backpack.y-15);

        Assets.font.setColor(Color.RED);
        Assets.font.draw(batch,
                "HEALTH: " + getHealth(),
                20f, Gdx.graphics.getHeight() - 20f
        );
    }

    public void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) moveLeft(Gdx.graphics.getDeltaTime());
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) moveRight(Gdx.graphics.getDeltaTime());

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            bullet.shootBullet(bounds);
        }
    }

    private void moveLeft(float delta) {
        backpack.x -= BACKPACK_SPEED * delta;
        if (backpack.x < 0)
            backpack.x = 0;
        updateBounds();
    }

    private void moveRight(float delta) {
        backpack.x += BACKPACK_SPEED * delta;
        if (backpack.x > Gdx.graphics.getWidth() - Assets.backpackImg.getWidth())
            backpack.x = Gdx.graphics.getWidth() - Assets.backpackImg.getWidth();
        updateBounds();
    }

    private void updateBounds() {
        bounds.set(backpack.x, backpack.y, backpack.width, backpack.height);
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}
