package com.mygdx.game.naloga1;

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
	private Texture bulletImg;
	private Sound backpackVoice;
	private Sound dumbbellCollect;

	private BitmapFont font;

	private Rectangle backpack;
	private int health;
	private Array<Rectangle> dumbbells;
	private Array<Rectangle> pizzas;
	private Array<Rectangle> bullets;

	private float dumbbellSpawnTime;
	private int dumbbellsCollected;

	private float pizzaSpawnTime;
	private int pizzasRemoved;
	private boolean hasIncreasedSpeedThisCycle = false;

	private static final float BACKPACK_SPEED = 250f;
	private static final float DUMBBELL_SPEED = 300f;
	private static final float  DUMBBELL_SPAWN_TIME = 0.5f;
	private static float PIZZA_SPEED = 150f;
	private static final float PIZZA_DAMAGE = 25f;
	private static final float PIZZA_SPAWN_TIME = 1.5f;

	private static final float BULLET_SPEED = 300f;
	
	@Override
	public void create () {
		batch = new SpriteBatch();

		backgroundImg = new Texture("images/fitness_background.jpg");
		backpackImg = new Texture("images/backpack.png");
		dumbbellImg = new Texture("images/dumbbell.png");
		pizzaImg = new Texture("images/pizza.png");
		bulletImg = new Texture("images/protein.png");
		backpackVoice = Gdx.audio.newSound(Gdx.files.internal("sounds/open_bag_sound.mp3"));
		dumbbellCollect = Gdx.audio.newSound(Gdx.files.internal("sounds/dropped_weights.wav"));

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

		bullets = new Array<>();
		pizzasRemoved = 0;
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

		if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
			shootBullet();
		}
	}

	private void update(float delta) {
		float elapsedTime = (TimeUtils.nanosToMillis(TimeUtils.nanoTime()) / 1000f);
		if (elapsedTime - dumbbellSpawnTime > DUMBBELL_SPAWN_TIME) spawnDumbbell();
		if (elapsedTime - pizzaSpawnTime > PIZZA_SPAWN_TIME) spawnPizza();


		// Update bullets
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

		for (Iterator<Rectangle> it = dumbbells.iterator(); it.hasNext(); ) {
			Rectangle dumbbell = it.next();
			dumbbell.y -= DUMBBELL_SPEED * delta;
			if (dumbbell.y + dumbbellImg.getHeight() < 0) {
				it.remove();
			}
			if (dumbbell.overlaps(backpack)) {
				dumbbellsCollected++;
				dumbbellCollect.play();
				it.remove();
			}
		}

		for (Iterator<Rectangle> it = pizzas.iterator(); it.hasNext(); ) {
			Rectangle pizza = it.next();
			pizza.y -= PIZZA_SPEED * delta;
			if (pizza.y + pizzaImg.getHeight() < 0) {
				it.remove();
			}
			if (pizza.overlaps(backpack)) {
				health -= PIZZA_DAMAGE;
				backpackVoice.play();
				it.remove();
			}
		}


		if (pizzasRemoved > 0 && pizzasRemoved % 5 == 0 && !hasIncreasedSpeedThisCycle) {
			PIZZA_SPEED += 50f;
			hasIncreasedSpeedThisCycle = true;
		}

		if (pizzasRemoved % 5 != 0) {
			hasIncreasedSpeedThisCycle = false;
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
					"YOUR SCORE: " + dumbbellsCollected,
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

		for (Rectangle bullet : bullets) {
			batch.draw(bulletImg, bullet.x, bullet.y, bullet.width, bullet.height);
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

		font.draw(batch,
				"ELIMINATED PIZZAS: " + pizzasRemoved,
				20f, Gdx.graphics.getHeight() - 100f
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

	private void shootBullet() {
		Rectangle bullet = new Rectangle();
		bullet.width = bulletImg.getWidth();
		bullet.height = bulletImg.getHeight();
		bullet.x = backpack.x + backpack.width / 2 - bullet.width / 2;
		bullet.y = backpack.y + backpack.height;
		bullets.add(bullet);
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
		backpackImg.dispose();
		dumbbellImg.dispose();
		pizzaImg.dispose();
		backpackVoice.dispose();
		dumbbellCollect.dispose();
	}
}
