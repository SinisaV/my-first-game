package com.mygdx.game.neobvezne_simulacije;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class BallSimulation extends ApplicationAdapter {
    private ShapeRenderer shapeRenderer;
    private Array<Ball> balls;


    @Override
    public void create() {
        shapeRenderer = new ShapeRenderer();
        balls = new Array<>();

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Vector2 worldCords = new Vector2(screenX, Gdx.graphics.getHeight() - screenY);
                Ball newBall = new Ball(worldCords.x, worldCords.y);
                balls.add(newBall);
                return true;
            }
        });
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        for (Ball ball : balls) {
            ball.update();
            ball.draw(shapeRenderer);
        }
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}
