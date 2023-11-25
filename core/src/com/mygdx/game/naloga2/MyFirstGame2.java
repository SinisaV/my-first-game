package com.mygdx.game.naloga2;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.naloga2.assets.AssetDescriptors;
import com.mygdx.game.naloga2.util.ViewportUtils;
import com.mygdx.game.naloga2.util.debug.DebugCameraController;
import com.mygdx.game.naloga2.util.debug.MemoryInfo;

import java.util.Iterator;

public class MyFirstGame2 extends ApplicationAdapter {
	private BitmapFont font;
	private TextureAtlas.AtlasRegion backgroundRegion;
	private TextureAtlas.AtlasRegion backpackRegion;
	private TextureAtlas.AtlasRegion dumbbellRegion;
	private TextureAtlas.AtlasRegion pizzaRegion;
	private TextureAtlas.AtlasRegion bulletRegion;
	private TextureAtlas.AtlasRegion powerRegion;

	private Sound backPackVoice;
	private Sound dumbbellCollectVoice;
	private ParticleEffect powerUpEffect;
	private ParticleEffect bloodEffect;
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

	private OrthographicCamera camera;
	private DebugCameraController debugCameraController;
	private MemoryInfo memoryInfo;
	private boolean debug = false;

	private ShapeRenderer shapeRenderer;
	public Viewport viewport;

	@Override
	public void create() {
		batch = new SpriteBatch();
		//Assets.load();

		AssetManager assetManager = new AssetManager();
		assetManager.load(AssetDescriptors.FONT);
		assetManager.load(AssetDescriptors.GAMEPLAY);
		assetManager.load(AssetDescriptors.BACKPACK_VOICE);
		assetManager.load(AssetDescriptors.DUMBBELL_COLLECT);
		assetManager.load(AssetDescriptors.POWER_UP);
		assetManager.load(AssetDescriptors.BLOOD);
		assetManager.finishLoading();

		font = assetManager.get(AssetDescriptors.FONT); // Retrieve the font
		TextureAtlas gameplayAtlas = assetManager.get(AssetDescriptors.GAMEPLAY); // Retrieve the atlas
		backgroundRegion = gameplayAtlas.findRegion("fitness_background");
		backpackRegion = gameplayAtlas.findRegion("backpack");
		dumbbellRegion = gameplayAtlas.findRegion("dumbbell");
		pizzaRegion = gameplayAtlas.findRegion("pizza");
		bulletRegion = gameplayAtlas.findRegion("protein");
		powerRegion = gameplayAtlas.findRegion("power");
		backPackVoice = assetManager.get(AssetDescriptors.BACKPACK_VOICE);
		dumbbellCollectVoice = assetManager.get(AssetDescriptors.DUMBBELL_COLLECT);
		powerUpEffect = assetManager.get(AssetDescriptors.POWER_UP);
		bloodEffect = assetManager.get(AssetDescriptors.BLOOD);

		// Initialize camera here
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		// debug
		debugCameraController = new DebugCameraController();
		debugCameraController.setStartPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
		memoryInfo = new MemoryInfo(500);

		shapeRenderer = new ShapeRenderer();
		viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);

		bullets = new Array<>();
		backpack = new Backpack(0, 0, backpackRegion);

		dumbbells = new Array<>();
		pizzas = new Array<>();
		powers = new Array<>();

		bulletPool = new Pool<Bullet>() {
			@Override
			protected Bullet newObject() {
				return new Bullet(0, 0, bulletRegion);
			}
		};
		dumbbellPool = new Pool<Dumbbell>() {
			@Override
			protected Dumbbell newObject() {
				return new Dumbbell(0, 0, dumbbellRegion);
			}
		};

		pizzaPool = new Pool<Pizza>() {
			@Override
			protected Pizza newObject() {
				return new Pizza(0, 0, pizzaRegion);
			}
		};

		powerPool = new Pool<Power>() {
			@Override
			protected Power newObject() {
				return new Power(0, 0, powerRegion);
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

		if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) debug = !debug;
		if (debug) {
			debugCameraController.handleDebugInput(Gdx.graphics.getDeltaTime());
			memoryInfo.update();
		}

		camera.update();
		batch.setProjectionMatrix(camera.combined);

		if (!isPaused && backpack.getHealth() > 0) {
			backpack.handleInput(bulletPool, bullets, bulletRegion);
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

		if (debug) {
			renderDebug();
		}
	}

	private void renderDebug() {
		debugCameraController.applyTo(camera);
		batch.begin();
		{
			GlyphLayout layout = new GlyphLayout(font, "FPS:" + Gdx.graphics.getFramesPerSecond());
			font.setColor(Color.YELLOW);
			font.draw(batch, layout, Gdx.graphics.getWidth() - layout.width, Gdx.graphics.getHeight() - 50);

			font.setColor(Color.YELLOW);
			font.draw(batch, "RC:" + batch.totalRenderCalls, Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() - 20);

			memoryInfo.render(batch, font);
		}
		batch.end();

		batch.totalRenderCalls = 0;
		ViewportUtils.drawGrid(viewport, shapeRenderer, 50);

		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		{
			shapeRenderer.setColor(1, 1, 0, 1);
			for (Dumbbell dumbbell : dumbbells) {
				shapeRenderer.rect(dumbbell.bounds.x, dumbbell.bounds.y, dumbbellRegion.getRegionWidth(), dumbbellRegion.getRegionHeight());
			}
			for (Pizza pizza : pizzas) {
				shapeRenderer.rect(pizza.bounds.x, pizza.bounds.y, pizzaRegion.getRegionWidth(), pizzaRegion.getRegionHeight());
			}
			for (Bullet bullet : bullets) {
				shapeRenderer.rect(bullet.bounds.x, bullet.bounds.y, bulletRegion.getRegionWidth(), bulletRegion.getRegionHeight());
			}
			for (Power power : powers) {
				shapeRenderer.rect(power.bounds.x, power.bounds.y, powerRegion.getRegionWidth(), powerRegion.getRegionHeight());
			}
			shapeRenderer.rect(backpack.bounds.x, backpack.bounds.y, backpackRegion.getRegionWidth(), backpackRegion.getRegionHeight());
		}
		shapeRenderer.end();
	}

	private void update(float delta) {
		float elapsedTime = (TimeUtils.nanosToMillis(TimeUtils.nanoTime()) / 1000f);
		if (elapsedTime - Dumbbell.getDumbbellSpawnTime() > Dumbbell.getDumbbellSpawnInterval()) Dumbbell.spawnDumbbell(dumbbellPool, dumbbells, dumbbellRegion);
		if (elapsedTime - Pizza.getPizzaSpawnTime() > Pizza.getPizzaSpawnInterval()) Pizza.spawnPizza(pizzaPool, pizzas, pizzaRegion);
		if (elapsedTime - Power.getPowerSpawnTime() > Power.getPowerSpawnInterval()) Power.spawnPower(powerPool, powers, powerRegion);

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

			if (dumbbell.bounds.y + dumbbellRegion.getRegionHeight() < 0) {
				dumbbell.reset();
				dumbbellPool.free(dumbbell);
				it.remove();
			}
			if (dumbbell.bounds.overlaps(backpack.bounds)) {
				Dumbbell.setDumbbellsCollected(Dumbbell.getDumbbellsCollected() + 1);
				dumbbellCollectVoice.play();
				it.remove();
				dumbbell.reset();
				dumbbellPool.free(dumbbell);
			}
		}

		for (Iterator<Pizza> it = pizzas.iterator(); it.hasNext(); ) {
			Pizza pizza = it.next();
			pizza.update(delta);
			if (pizza.bounds.y + pizzaRegion.getRegionHeight() < 0) {
				it.remove();
				pizza.reset();
				pizzaPool.free(pizza);
			}
			if (pizza.bounds.overlaps(backpack.bounds)) {
				backpack.setHealth((int) (backpack.getHealth() - Pizza.getDAMAGE()));
				bloodEffect.start();
				backpack.updateBackpackEffectPosition(backpack.bounds.x, backpack.bounds.y);
				backPackVoice.play();
				it.remove();
				pizza.reset();
				pizzaPool.free(pizza);
			}
		}

		for (Iterator<Power> it = powers.iterator(); it.hasNext(); ) {
			Power power = it.next();
			power.update(delta);
			power.updatePowerUpEffectPosition(power.bounds.x, power.bounds.y);

			if (power.bounds.y + powerRegion.getRegionHeight() < 0) {
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

	@SuppressWarnings("SuspiciousIndentation")
	private void draw() {
		batch.draw(backgroundRegion, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if (backpack.getHealth() <= 0) {
			font.setColor(Color.RED);
			font.draw(batch,
					"GAME OVER",
					20f, Gdx.graphics.getHeight() - 20f
			);
			font.setColor(Color.NAVY);
			font.draw(batch,
					"YOUR SCORE: " + Dumbbell.getDumbbellsCollected(),
					20f, Gdx.graphics.getHeight() - 60f
			);
			return;
		}

		font.setColor(Color.NAVY);
        font.draw(batch,
                "SCORE: " + Dumbbell.getDumbbellsCollected(),
                20f, Gdx.graphics.getHeight() - 60f
        );

		font.setColor(Color.RED);
		font.draw(batch,
				"ELIMINATED PIZZAS: " + Bullet.getPizzasRemoved(),
				20f, Gdx.graphics.getHeight() - 100f
		);

		backpack.draw(batch, font);
		bloodEffect.setPosition(backpack.backpackEffectX+40, backpack.backpackEffectY+10);
		bloodEffect.draw(batch, Gdx.graphics.getDeltaTime());

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

			if (!isPaused) {
				if (powerUpEffect.isComplete()) {
					powerUpEffect.reset();
				}
			}

			powerUpEffect.setPosition(power.powerUpEffectX+25, power.powerUpEffectY+60);
			powerUpEffect.draw(batch, Gdx.graphics.getDeltaTime());
		}

		if (isPoweredUp) {
			@SuppressWarnings("DefaultLocale") String timerText = "Power-up: " + String.format("%.1f", remainingPowerUpTime) + "s";
			font.setColor(Color.GREEN);
			font.draw(batch, timerText, Gdx.graphics.getWidth() / 2f - 50f, Gdx.graphics.getHeight() - 20f);
		}
	}

	public void pause() {
		if (isPaused) {
			font.setColor(Color.WHITE);
			font.draw(batch, "PAUSED", Gdx.graphics.getWidth() / 2f - 50f, Gdx.graphics.getHeight() / 2f);
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
		//Assets.dispose();
		font.dispose();
		backPackVoice.dispose();
		dumbbellCollectVoice.dispose();
		powerUpEffect.dispose();
		bloodEffect.dispose();
		shapeRenderer.dispose();
	}
}
