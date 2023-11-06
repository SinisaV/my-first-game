package com.mygdx.game.naloga2;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class MyFirstGame2 extends ApplicationAdapter {
	private Backpack backpack;
	private Array<Dumbbell> dumbbells;
	private Array<Pizza> pizzas;
	private Array<Bullet> bullets;
	private Array<Power> powers;

	private Pool<Dumbbell> dumbbellPool;
	private Pool<Pizza> pizzaPool;
	private Pool<Bullet> bulletPool;
	private Pool<Power> powerPool;

	private boolean hasIncreasedSpeedThisCycle = false;
	private boolean isPaused = false;
	private boolean isPoweredUp = false;

	private float remainingPowerUpTime = 3f;
	private float originalSpeed;
	SpriteBatch batch;
	@Override
	public void create() {
		batch = new SpriteBatch();
		Assets.load();

		bullets = new Array<>();
		backpack = new Backpack(0, 0);

		dumbbells = new Array<>();
		pizzas = new Array<>();
		powers = new Array<>();

		bulletPool = new Pool<Bullet>() {
			@Override
			protected Bullet newObject() {
				return new Bullet(0, 0);
			}
		};
		dumbbellPool = new Pool<Dumbbell>() {
			@Override
			protected Dumbbell newObject() {
				return new Dumbbell(0, 0);
			}
		};

		pizzaPool = new Pool<Pizza>() {
			@Override
			protected Pizza newObject() {
				return new Pizza(0, 0);
			}
		};

		powerPool = new Pool<Power>() {
			@Override
			protected Power newObject() {
				return new Power(0, 0);
			}
		};

		originalSpeed = backpack.getSpeed();
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0.5f, 0, 1);

		if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
			isPaused = !isPaused;
		}


		if (!isPaused && backpack.getHealth() > 0) {
			backpack.handleInput(bulletPool, bullets);
			update(Gdx.graphics.getDeltaTime());
		} else if (backpack.getHealth() <= 0) {
			if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
				reset();
			}
		}
		batch.begin();
		draw();
		pause();
		batch.end();
	}

	private void update(float delta) {
		float elapsedTime = (TimeUtils.nanosToMillis(TimeUtils.nanoTime()) / 1000f);
		if (elapsedTime - Dumbbell.getDumbbellSpawnTime() > Dumbbell.getDumbbellSpawnInterval()) Dumbbell.spawnDumbbell(dumbbellPool, dumbbells);
		if (elapsedTime - Pizza.getPizzaSpawnTime() > Pizza.getPizzaSpawnInterval()) Pizza.spawnPizza(pizzaPool, pizzas);
		if (elapsedTime - Power.getPowerSpawnTime() > Power.getPowerSpawnInterval()) Power.spawnPower(powerPool, powers);

		for (Bullet bullet : bullets) {
			for (Iterator<Pizza> pizzaIt = pizzas.iterator(); pizzaIt.hasNext(); ) {
				Pizza pizza = pizzaIt.next();
				bullet.update(delta);
				if (bullet.bounds.overlaps(pizza.bounds)) {
					Bullet.setPizzasRemoved(Bullet.getPizzasRemoved() + 1);
					pizzaIt.remove();
					bullets.removeValue(bullet, true);
					bullet.reset();
					bulletPool.free(bullet);
					break;
				}
			}

			if (bullet.bounds.y > Gdx.graphics.getHeight()) {
				bullets.removeValue(bullet, true);
				bullet.reset();
				bulletPool.free(bullet);
			}
		}

		for (Iterator<Dumbbell> it = dumbbells.iterator(); it.hasNext(); ) {
			Dumbbell dumbbell = it.next();
			dumbbell.update(delta);

			if (dumbbell.bounds.y + Assets.dumbbellImg.getHeight() < 0) {
				dumbbell.reset();
				dumbbellPool.free(dumbbell);
				it.remove();
			}
			if (dumbbell.bounds.overlaps(backpack.bounds)) {
				Dumbbell.setDumbbellsCollected(Dumbbell.getDumbbellsCollected() + 1);
				Assets.backpackVoice.play();
				it.remove();
				dumbbell.reset();
				dumbbellPool.free(dumbbell);
			}
		}

		for (Iterator<Pizza> it = pizzas.iterator(); it.hasNext(); ) {
			Pizza pizza = it.next();
			pizza.update(delta);
			if (pizza.bounds.y + Assets.pizzaImg.getHeight() < 0) {
				it.remove();
				pizza.reset();
				pizzaPool.free(pizza);
			}
			if (pizza.bounds.overlaps(backpack.bounds)) {
				backpack.setHealth((int) (backpack.getHealth() - Pizza.getDAMAGE()));
				Assets.backpackVoice.play();
				it.remove();
				pizza.reset();
				pizzaPool.free(pizza);
			}
		}

		for (Iterator<Power> it = powers.iterator(); it.hasNext(); ) {
			Power power = it.next();
			power.update(delta);
			if (power.bounds.y + Assets.powerImg.getHeight() < 0) {
				it.remove();
				power.reset();
				powerPool.free(power);
			}
			if (power.bounds.overlaps(backpack.bounds)) {
				powerUp();
				isPoweredUp = true;
				it.remove();
				power.reset();
				powerPool.free(power);
			}
		}

		remainingPowerUpTime -= delta;

		if (remainingPowerUpTime <= 0) {
			backpack.setSpeed(originalSpeed);
			originalSpeed = backpack.getSpeed();
			isPoweredUp = false;
		}

		if (Bullet.getPizzasRemoved() > 0 && Bullet.getPizzasRemoved() % 5 == 0 && !hasIncreasedSpeedThisCycle) {
			//PIZZA_SPEED += 50f;
			Pizza.setPizzaSpeed(Pizza.getPizzaSpeed() + 50);
			hasIncreasedSpeedThisCycle = true;
		}

		if (Bullet.getPizzasRemoved() % 5 != 0) {
			hasIncreasedSpeedThisCycle = false;
		}
	}

	private void draw() {
		batch.draw(Assets.backgroundImg, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if (backpack.getHealth() <= 0) {
			Assets.font.setColor(Color.RED);
			Assets.font.draw(batch,
					"GAME OVER",
					20f, Gdx.graphics.getHeight() - 20f
			);
			Assets.font.setColor(Color.NAVY);
			Assets.font.draw(batch,
					"YOUR SCORE: " + Dumbbell.getDumbbellsCollected(),
					20f, Gdx.graphics.getHeight() - 60f
			);
			return;
		}

		Assets.font.setColor(Color.NAVY);
        Assets.font.draw(batch,
                "SCORE: " + Dumbbell.getDumbbellsCollected(),
                20f, Gdx.graphics.getHeight() - 60f
        );

		Assets.font.setColor(Color.RED);
		Assets.font.draw(batch,
				"ELIMINATED PIZZAS: " + Bullet.getPizzasRemoved(),
				20f, Gdx.graphics.getHeight() - 100f
		);

		backpack.draw(batch);

		for (Dumbbell dumbbell: dumbbells) {
			dumbbell.draw(batch);
		}

		for (Pizza pizza: pizzas) {
			pizza.draw(batch);
		}

		for (Bullet bullet: bullets) {
			bullet.draw(batch);
		}

		for (Power power: powers) {
			power.draw(batch);
		}

		if (isPoweredUp) {
			String timerText = "Power-up: " + String.format("%.1f", remainingPowerUpTime) + "s";
			Assets.font.setColor(Color.GREEN);
			Assets.font.draw(batch, timerText, Gdx.graphics.getWidth() / 2f - 50f, Gdx.graphics.getHeight() - 20f);
		}
	}

	public void pause() {
		if (isPaused) {
			Assets.font.setColor(Color.WHITE);
			Assets.font.draw(batch, "PAUSED", Gdx.graphics.getWidth() / 2f - 50f, Gdx.graphics.getHeight() / 2f);
		}
	}

	private void reset() {
		isPaused = false;
		isPoweredUp = false;
		backpack.setHealth(100);
		Dumbbell.setDumbbellsCollected(0);
		Bullet.setPizzasRemoved(0);

		for (Dumbbell dumbbell : dumbbells) {
			dumbbellPool.free(dumbbell);
		}

		for (Pizza pizza : pizzas) {
			pizzaPool.free(pizza);
		}

		for (Bullet bullet : bullets) {
			bulletPool.free(bullet);
		}

		for (Power power : powers) {
			powerPool.free(power);
		}

		dumbbells.clear();
		pizzas.clear();
		bullets.clear();
		powers.clear();
	}

	private void powerUp() {
		if (!isPoweredUp) {
			backpack.setSpeed(originalSpeed + 200f);
			remainingPowerUpTime = 3f;
			isPoweredUp = true;
		}
	}

	@Override
	public void dispose() {
		batch.dispose();
		Assets.dispose();
	}
}
