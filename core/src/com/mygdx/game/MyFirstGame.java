package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class MyFirstGame extends ApplicationAdapter {
	SpriteBatch batch;
	private Texture backgroundImg;
	private Texture backpackImg;
	private Texture dumbbellImg;
	private Texture pizzaImg;

	private BitmapFont font;

	private Rectangle backpack;
	private int health;
	private Array<Rectangle> dumbbells;
	private Array<Rectangle> pizzas;

	private float dumbbellSpawnTime;
	private int dumbbellsCollected;

	private float pizzaSpawnTime;

	private static final float BACKPACK_SPEED = 250f;
	private static final float DUMBBELL_SPEED = 300f;
	private static final float  DUMBBELL_SPAWN_TIME = 0.5f;
	private static final float PIZZA_SPEED = 150f;
	private static final float PIZZA_DAMAGE = 25f;
	private static final float PIZZA_SPAWN_TIME = 2f;
	
	@Override
	public void create () {
		batch = new SpriteBatch();

		backgroundImg = new Texture("images/fitness_background2.jpg");
		backpackImg = new Texture("images/small_backpack.png");
		dumbbellImg = new Texture("images/small_dumbbell.png");
		pizzaImg = new Texture("images/small_pizza.png");
		font = new BitmapFont(Gdx.files.internal("fonts/oswald-32.fnt"));

		backpack = new Rectangle();
		backpack.x = Gdx.graphics.getWidth() / 2f - backpackImg.getWidth() / 2f;
		backpack.y = 20f;
		backpack.width = backpackImg.getWidth();
		backpack.height = backpackImg.getHeight();

		dumbbells = new Array<>();
		dumbbellsCollected = 0;
		spawnDumbbell();

		pizzas = new Array<>();
		health = 100;
		spawnPizza();
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0.5f, 0, 1);

		if (health > 0) {
			handleInput();
			update(Gdx.graphics.getDeltaTime());
		}

		batch.begin();
		draw();
		batch.end();
	}

	private void handleInput() {
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) moveLeft(Gdx.graphics.getDeltaTime());
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) moveRight(Gdx.graphics.getDeltaTime());
	}

	private void update(float delta) {
		float elapsedTime = (TimeUtils.nanosToMillis(TimeUtils.nanoTime()) / 1000f);
		if (elapsedTime - dumbbellSpawnTime > DUMBBELL_SPAWN_TIME) spawnDumbbell();
		if (elapsedTime - pizzaSpawnTime > PIZZA_SPAWN_TIME) spawnPizza();

		for (Iterator<Rectangle> it = dumbbells.iterator(); it.hasNext(); ) {
			Rectangle coin = it.next();
			coin.y -= DUMBBELL_SPEED * delta;
			if (coin.y + dumbbellImg.getHeight() < 0) {
				it.remove();
			}
			if (coin.overlaps(backpack)) {
				dumbbellsCollected++;
				//coinCollect.play();
				it.remove();
			}
		}

		for (Iterator<Rectangle> it = pizzas.iterator(); it.hasNext(); ) {
			Rectangle hammer = it.next();
			hammer.y -= PIZZA_SPEED * delta;
			if (hammer.y + pizzaImg.getHeight() < 0) {
				it.remove();
			}
			if (hammer.overlaps(backpack)) {
				health -= PIZZA_DAMAGE;
				//piggyVoice.play();
				it.remove();
			}
		}
	}

	private void draw() {
		batch.draw(backgroundImg, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if (health <= 0) {
			font.setColor(Color.RED);
			font.draw(batch,
					"GAME OVER",
					20f, Gdx.graphics.getHeight() - 20f
			);
			font.setColor(Color.NAVY);
			font.draw(batch,
					"YOUR IMPROVED FITNESS HEALTH: " + (dumbbellsCollected),
					20f, Gdx.graphics.getHeight() - 60f
			);
			return;
		}

		for (Rectangle dumbbell : dumbbells) {
			batch.draw(dumbbellImg, dumbbell.x, dumbbell.y);
		}

		for (Rectangle pizza : pizzas) {
			batch.draw(pizzaImg, pizza.x, pizza.y);
		}

		batch.draw(backpackImg, backpack.x, backpack.y-15);

		font.setColor(Color.RED);
		font.draw(batch,
				"HEALTH: " + health,
				20f, Gdx.graphics.getHeight() - 20f
		);

		font.setColor(Color.NAVY);
		font.draw(batch,
				"SCORE: " + dumbbellsCollected,
				20f, Gdx.graphics.getHeight() - 60f
		);
	}

	private void moveLeft(float delta) {
		backpack.x -= BACKPACK_SPEED * delta;
		if (backpack.x < 0)
			backpack.x = 0f;
	}

	private void moveRight(float delta) {
		backpack.x += BACKPACK_SPEED * delta;
		if (backpack.x > Gdx.graphics.getWidth() - backpackImg.getWidth())
			backpack.x = Gdx.graphics.getWidth() - backpackImg.getWidth();
	}

	private void spawnDumbbell() {
		Rectangle dumbbell = new Rectangle();
		dumbbell.x = MathUtils.random(0f, Gdx.graphics.getWidth() - backpackImg.getWidth());
		dumbbell.y = Gdx.graphics.getHeight();
		dumbbell.width = dumbbellImg.getWidth();
		dumbbell.height = dumbbellImg.getHeight();
		dumbbells.add(dumbbell);
		dumbbellSpawnTime = TimeUtils.nanosToMillis(TimeUtils.nanoTime()) / 1000f;
	}

	private void spawnPizza() {
		Rectangle pizza = new Rectangle();
		pizza.x = MathUtils.random(0f, Gdx.graphics.getWidth() - pizzaImg.getWidth());
		pizza.y = Gdx.graphics.getHeight();
		pizza.width = pizzaImg.getWidth();
		pizza.height = pizzaImg.getHeight();
		pizzas.add(pizza);
		pizzaSpawnTime = TimeUtils.nanosToMillis(TimeUtils.nanoTime()) / 1000f;
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
		backpackImg.dispose();
		dumbbellImg.dispose();
		pizzaImg.dispose();
	}
}
