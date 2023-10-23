package com.mygdx.game.naloga2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

public class Bullet extends DynamicGameObject {
    private final Array<Rectangle> bullets;
    private int pizzasRemoved;
    private boolean hasIncreasedSpeedThisCycle = false;
    private static final float BULLET_SPEED = 300f;
    public Bullet(float x, float y) {
        super(x, y, 10, 10, Assets.bulletImg);
        bullets = new Array<>();
        pizzasRemoved = 0;
    }

    public void update(float delta, Array<Rectangle> pizzas, Pizza myPizza) {
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

        if (pizzasRemoved > 0 && pizzasRemoved % 5 == 0 && !hasIncreasedSpeedThisCycle) {
            //PIZZA_SPEED += 50f;
            myPizza.setPizzaSpeed(myPizza.getPizzaSpeed() + 50);
            hasIncreasedSpeedThisCycle = true;
        }

        if (pizzasRemoved % 5 != 0) {
            hasIncreasedSpeedThisCycle = false;
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        for (Rectangle bullet : bullets) {
            batch.draw(Assets.bulletImg, bullet.x, bullet.y, bullet.width, bullet.height);
        }

        Assets.font.draw(batch,
                "ELIMINATED PIZZAS: " + pizzasRemoved,
                20f, Gdx.graphics.getHeight() - 100f
        );
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
