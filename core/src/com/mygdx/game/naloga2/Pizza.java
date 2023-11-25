package com.mygdx.game.naloga2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.TimeUtils;


public class Pizza extends DynamicGameObject implements Pool.Poolable {
    private final TextureRegion pizzaRegion;
    private static float spawnTime;
    private static float SPEED = 150f;
    private static final float DAMAGE = 25f;
    private static final float SPAWN_TIME = 1.5f;

    public Pizza(float x, float y, TextureRegion pizzaRegion) {
        super(x, y, pizzaRegion.getRegionWidth(), pizzaRegion.getRegionHeight());
        this.pizzaRegion = pizzaRegion;
    }

    public void update(float delta) {
        bounds.y -= SPEED * delta;
    }

    @Override
    public void reset() {
        bounds.set(0, 0, pizzaRegion.getRegionWidth(), pizzaRegion.getRegionHeight());
    }

    public void draw(SpriteBatch batch) {
        batch.draw(pizzaRegion, bounds.x, bounds.y);
    }

    public static void spawnPizza(Pool<Pizza> pizzaPool, Array<Pizza> pizzas, TextureRegion pizzaRegion) {
        Pizza pizza = pizzaPool.obtain();
        float randomX = MathUtils.random(0f, Gdx.graphics.getWidth() - pizzaRegion.getRegionWidth());
        float randomY = Gdx.graphics.getHeight();
        //Pizza pizza = new Pizza(randomX, randomY);
        pizza.bounds.setPosition(randomX, randomY);
        pizzas.add(pizza);
        spawnTime = TimeUtils.nanosToMillis(TimeUtils.nanoTime()) / 1000f;
    }

    public static int getPizzaSpeed() {
        return (int) SPEED;
    }

    public static void setPizzaSpeed(int speed) {
        SPEED = speed;
    }

    public static float getPizzaSpawnTime() {
        return spawnTime;
    }

    public static float getPizzaSpawnInterval() {
        return SPAWN_TIME;
    }

    public static float getDAMAGE() {
        return DAMAGE;
    }
}
