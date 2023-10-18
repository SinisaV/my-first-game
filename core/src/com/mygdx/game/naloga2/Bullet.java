package com.mygdx.game.naloga2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class Bullet extends GameObject {
    private Array<Rectangle> bullets;
    private int pizzasRemoved;
    private static final float BULLET_SPEED = 300f;
    public Bullet(float x, float y) {
        super(x, y, 10, 10, Assets.bulletImg);
        bullets = new Array<>();
        pizzasRemoved = 0;
    }

    public void update(float elapsedTime, float delta, Array<Rectangle> pizzas) {
        for (Rectangle bullet : bullets) {
            bullet.y += BULLET_SPEED * delta;

            for (Iterator<Rectangle> pizzaIt = pizzas.iterator(); pizzaIt.hasNext(); ) {
                Rectangle pizza = pizzaIt.next();
                if (bullet.overlaps(pizza)) {
                    pizzasRemoved++;
                    pizzaIt.remove();
                    bullets.removeValue(bullet, true);
                    break;
                }
            }

            if (bullet.y > Gdx.graphics.getHeight()) {
                bullets.removeValue(bullet, true);
            }
        }
    }

    public void draw(SpriteBatch batch) {
        for (Rectangle bullet : bullets) {
            batch.draw(Assets.bulletImg, bullet.x, bullet.y, bullet.width, bullet.height);
        }
    }

    public void shootBullet(Rectangle backpack) {
        Rectangle bullet = new Rectangle();
        bullet.width = Assets.bulletImg.getWidth();
        bullet.height = Assets.bulletImg.getHeight();
        bullet.x = backpack.x + backpack.width / 2 - bullet.width / 2;
        bullet.y = backpack.y + backpack.height;
        bullets.add(bullet);
    }
}
