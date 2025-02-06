package io.github.terminalroot;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.Array;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */
public class SpaceShip extends ApplicationAdapter {
    private SpriteBatch batch;
    private FitViewport viewport;
    private Texture nave, fundo, asteroide;
    private Sprite naveSprite;
    private Array<Asteroides> asteroides;
    private float tempoGeracaoAsteroide;
    // private Vector2 touchPos;

    @Override
    public void create() {
        batch = new SpriteBatch();
        viewport = new FitViewport(15 * (float) Gdx.graphics.getWidth() / Gdx.graphics.getHeight(), 15);
        nave = new Texture("nave.png");
        fundo = new Texture("fundo.png");
        asteroide = new Texture("asteroide.png");
        naveSprite = new Sprite(nave);
        naveSprite.setSize(5, 5);
        asteroides = new Array<>();
        tempoGeracaoAsteroide = 0;
        // touchPos = new Vector2();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render() {
        draw();
        input();
        logic();
    }

    @Override
    public void dispose() {
        batch.dispose();
        nave.dispose();
    }

    private void input() {
        float speed = 10f;
        float delta = Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            naveSprite.translateX(speed * delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            naveSprite.translateX(-speed * delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            naveSprite.translateY(speed * delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            naveSprite.translateY(-speed * delta);
        }

        // if (Gdx.input.isTouched()) {
        // touchPos.set(Gdx.input.getX(), Gdx.input.getY());
        // viewport.unproject(touchPos);
        // naveSprite.setCenterX(touchPos.x);
        // }
    }

    private void logic() {

        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        float naveWidth = naveSprite.getWidth();
        float naveHeight = naveSprite.getHeight();

        naveSprite.setX(MathUtils.clamp(naveSprite.getX(), 0, worldWidth - naveWidth));
        naveSprite.setY(MathUtils.clamp(naveSprite.getY(), 0, worldHeight - naveHeight));

        tempoGeracaoAsteroide += Gdx.graphics.getDeltaTime();
        if (tempoGeracaoAsteroide > 1.5f) {
            asteroides.add(new Asteroides(asteroide, viewport));
            tempoGeracaoAsteroide = 0;
        }

        for (int i = asteroides.size - 1; i >= 0; i--) {
            Asteroides asteroide = asteroides.get(i);
            asteroide.update(Gdx.graphics.getDeltaTime());

            if (asteroide.foraDaTela()) {
                asteroides.removeIndex(i);
            }
        }
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        batch.draw(fundo, 0, 0, worldWidth, worldHeight);
        naveSprite.draw(batch);

        for (Asteroides asteroide : asteroides) {
            asteroide.getSprite().draw(batch);
        }

        batch.end();
    }
}
