package com.mygdx.game.naloga2;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class MyFirstGame2 extends ApplicationAdapter {
	private Backpack backpack;
	private Array<Dumbbell> dumbbells;
	private Array<Pizza> pizzas;
	private Array<Bullet> bullets;

	private boolean hasIncreasedSpeedThisCycle = false;
	SpriteBatch batch;
	@Override
	public void create() {
		batch = new SpriteBatch();
		Assets.load();

		bullets = new Array<>();
		backpack = new Backpack(0, 0);

		dumbbells = new Array<>();
		pizzas = new Array<>();
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0.5f, 0, 1);

		if (backpack.getHealth() > 0) {
			backpack.handleInput(bullets);
			update(Gdx.graphics.getDeltaTime());
		}
		batch.begin();
		draw();
		batch.end();
	}
	private void update(float delta) {
		float elapsedTime = (TimeUtils.nanosToMillis(TimeUtils.nanoTime()) / 1000f);
		if (elapsedTime - Dumbbell.getDumbbellSpawnTime() > Dumbbell.getDumbbellSpawnInterval()) Dumbbell.spawnDumbbell(dumbbells);
		if (elapsedTime - Pizza.getPizzaSpawnTime() > Pizza.getPizzaSpawnInterval()) Pizza.spawnPizza(pizzas);

		for (Bullet bullet : bullets) {
			for (Iterator<Pizza> pizzaIt = pizzas.iterator(); pizzaIt.hasNext(); ) {
				Pizza pizza = pizzaIt.next();
				bullet.update(delta);
				if (bullet.bounds.overlaps(pizza.bounds)) {
					Bullet.setPizzasRemoved(Bullet.getPizzasRemoved() + 1);
					pizzaIt.remove();
					bullets.removeValue(bullet, true);
					break;
				}
			}

			if (bullet.bounds.y > Gdx.graphics.getHeight()) {
				bullets.removeValue(bullet, true);
			}
		}

		for (Iterator<Dumbbell> it = dumbbells.iterator(); it.hasNext(); ) {
			Dumbbell dumbbell = it.next();
			dumbbell.update(delta);

			if (dumbbell.bounds.y + Assets.dumbbellImg.getHeight() < 0) {
				it.remove();
			}
			if (dumbbell.bounds.overlaps(backpack.bounds)) {
				Dumbbell.setDumbbellsCollected(Dumbbell.getDumbbellsCollected() + 1);
				Assets.backpackVoice.play();
				it.remove();
			}
		}

		for (Iterator<Pizza> it = pizzas.iterator(); it.hasNext(); ) {
			Pizza pizza = it.next();
			pizza.update(delta);
			if (pizza.bounds.y + Assets.pizzaImg.getHeight() < 0) {
				it.remove();
			}
			if (pizza.bounds.overlaps(backpack.bounds)) {
				backpack.setHealth((int) (backpack.getHealth() - Pizza.getDAMAGE()));
				Assets.backpackVoice.play();
				it.remove();
			}
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
	}

	@Override
	public void dispose() {
		batch.dispose();
		Assets.dispose();
	}
}
