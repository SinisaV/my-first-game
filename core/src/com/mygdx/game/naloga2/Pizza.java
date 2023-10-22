package com.mygdx.game.naloga2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class Pizza extends  GameObject {
    private final Array<Rectangle> pizzas;
    private float pizzaSpawnTime;

    private static float PIZZA_SPEED = 150f;
    private static final float PIZZA_DAMAGE = 25f;
    private static final float PIZZA_SPAWN_TIME = 1.5f;

    public Pizza(float x, float y) {
        super(x, y, 10, 10, Assets.pizzaImg);
        pizzas = new Array<>();
    }

    public void update(float elapsedTime, float delta, Rectangle backpack, Backpack b) {
        //if (elapsedTime - pizzaSpawnTime > PIZZA_SPAWN_TIME) spawnPizza();

        pizzaSpawnTime += delta;

        if (pizzaSpawnTime > PIZZA_SPAWN_TIME) {
            spawnPizza();
            pizzaSpawnTime = 0;
        }

        for (Iterator<Rectangle> it = pizzas.iterator(); it.hasNext(); ) {
            Rectangle pizza = it.next();
            pizza.y -= PIZZA_SPEED * delta;
            if (pizza.y + Assets.pizzaImg.getHeight() < 0) {
                it.remove();
            }
            int health = b.getHealth();
            if (pizza.overlaps(backpack)) {
                health -= PIZZA_DAMAGE;
                b.setHealth(health);
                Assets.backpackVoice.play();
                it.remove();
            }
        }
    }

    public void draw(SpriteBatch batch) {
        for (Rectangle pizza : pizzas) {
            batch.draw(Assets.pizzaImg, pizza.x, pizza.y);
        }
    }

    private void spawnPizza() {
        Rectangle pizza = new Rectangle();
        pizza.x = MathUtils.random(0f, Gdx.graphics.getWidth() - Assets.pizzaImg.getWidth());
        pizza.y = Gdx.graphics.getHeight();
        pizza.width = Assets.pizzaImg.getWidth();
        pizza.height = Assets.pizzaImg.getHeight();
        pizzas.add(pizza);
        pizzaSpawnTime = TimeUtils.nanosToMillis(TimeUtils.nanoTime()) / 1000f;
    }

    public Array<Rectangle> getPizzas() {
        return pizzas;
    }

    public int getPizzaSpeed() {
        return (int) PIZZA_SPEED;
    }
    public void setPizzaSpeed(int speed) {
        PIZZA_SPEED = speed;
    }
}
