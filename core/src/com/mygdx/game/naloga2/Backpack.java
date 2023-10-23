package com.mygdx.game.naloga2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class Backpack extends DynamicGameObject {
    //private Rectangle backpack;

    private int health = 100;

    private static final float SPEED = 250f;

    public Backpack (float x, float y) {
        super(x, y, Assets.backpackImg.getWidth(), Assets.backpackImg.getHeight());
        bounds.x = (int) (Gdx.graphics.getWidth() / 2f - Assets.backpackImg.getWidth() / 2f);
        bounds.y = 20f;
    }
    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(Assets.backpackImg, bounds.x, bounds.y-15);

        Assets.font.setColor(Color.RED);
        Assets.font.draw(batch,
                "HEALTH: " + getHealth(),
                20f, Gdx.graphics.getHeight() - 20f
        );
    }

    public void handleInput(Array<Bullet> bullets) {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) moveLeft(Gdx.graphics.getDeltaTime());
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) moveRight(Gdx.graphics.getDeltaTime());

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            shootBullet(bullets);
        }
    }

    private void moveLeft(float delta) {
        bounds.x -= SPEED * delta;
        if (bounds.x < 0)
            bounds.x = 0;
        updateBounds();
    }

    private void moveRight(float delta) {
        bounds.x += SPEED * delta;
        if (bounds.x > Gdx.graphics.getWidth() - Assets.backpackImg.getWidth())
            bounds.x = Gdx.graphics.getWidth() - Assets.backpackImg.getWidth();
        updateBounds();
    }

    private void shootBullet(Array<Bullet> bullets) {
        Bullet bullet = new Bullet(bounds.x + bounds.width / 2 - Assets.bulletImg.getWidth() / 2f, bounds.y + bounds.height);
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
}
