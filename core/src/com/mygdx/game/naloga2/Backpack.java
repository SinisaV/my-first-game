package com.mygdx.game.naloga2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class Backpack extends DynamicGameObject {
    //private Rectangle backpack;

    private final TextureRegion backpackRegion;
    private int health = 100;

    private static float SPEED = 250f;

    public float backpackEffectX;
    public float backpackEffectY;

    public Backpack (float x, float y, TextureRegion backpackRegion) {
        super(x, y, backpackRegion.getRegionWidth(), backpackRegion.getRegionHeight());
        this.backpackRegion = backpackRegion;
        bounds.x = (int) (Gdx.graphics.getWidth() / 2f - backpackRegion.getRegionWidth() / 2f);
        bounds.y = 20f;
    }

    public void draw(SpriteBatch batch, BitmapFont font) {
        batch.draw(backpackRegion, bounds.x, bounds.y);

        font.setColor(Color.RED);
        font.draw(batch,
                "HEALTH: " + getHealth(),
                20f, Gdx.graphics.getHeight() - 20f
        );
    }

    public void handleInput(Pool<Bullet> bulletPool, Array<Bullet> bullets, TextureRegion bulletRegion) {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) moveLeft(Gdx.graphics.getDeltaTime());
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) moveRight(Gdx.graphics.getDeltaTime());

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            shootBullet(bulletPool, bullets, bulletRegion);
        }
    }

    private void moveLeft(float delta) {
        bounds.x -= SPEED * delta;
        if (bounds.x < 0)
            bounds.x = 0f;
        updateBounds();
    }

    private void moveRight(float delta) {
        bounds.x += SPEED * delta;
        if (bounds.x > Gdx.graphics.getWidth() - backpackRegion.getRegionWidth())
            bounds.x = Gdx.graphics.getWidth() - backpackRegion.getRegionWidth();
        updateBounds();
    }

    private void shootBullet(Pool<Bullet> bulletPool, Array<Bullet> bullets, TextureRegion bulletRegion) {
        Bullet bullet = bulletPool.obtain();
        // Bullet bullet = new Bullet(bounds.x + bounds.width / 2 - Assets.bulletImg.getWidth() / 2f, bounds.y + bounds.height);
        bullet.bounds.setPosition(bounds.x + bounds.width / 2 - bulletRegion.getRegionWidth() / 2f, bounds.y + bounds.height);
        bullets.add(bullet);
    }

    private void updateBounds() {
        bounds.set(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public float getSpeed() {
    	return SPEED;
    }

    public void setSpeed(float speed) {
    	SPEED = speed;
    }

    public void updateBackpackEffectPosition(float x, float y) {
        backpackEffectX = x;
        backpackEffectY = y;
    }
}
