package com.mygdx.game.naloga2;

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

public class MyFirstGame2 extends ApplicationAdapter {
	private Backpack backpack;
	private Dumbbell dumbbell;
	SpriteBatch batch;
	@Override
	public void create() {
		batch = new SpriteBatch();
		Assets.load();
		backpack = new Backpack(0, 0);
		backpack.create();

		dumbbell = new Dumbbell(1000, 1000);
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0.5f, 0, 1);

		backpack.handleInput();

		if (backpack.getHealth() > 0) {
			backpack.handleInput();
			update(Gdx.graphics.getDeltaTime());
		}
		batch.begin();
		draw();
		batch.end();
	}

	private void update(float delta) {
		float elapsedTime = (TimeUtils.nanosToMillis(TimeUtils.nanoTime()) / 1000f);

		dumbbell.update(elapsedTime, delta, backpack.bounds);
	}

	private void draw() {
		batch.draw(Assets.backgroundImg, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if (backpack.getHealth() <= 0) {
			Assets.font.setColor(Color.RED);
			Assets.font.draw(batch,
					"GAME OVER",
					20f, Gdx.graphics.getHeight() - 20f
			);
			return;
		}

		backpack.draw(batch);
		dumbbell.draw(batch);
	}

	@Override
	public void dispose() {
		batch.dispose();
		Assets.dispose();
	}
}
