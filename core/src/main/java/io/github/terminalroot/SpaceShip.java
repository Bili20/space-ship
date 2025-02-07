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
import com.badlogic.gdx.math.Rectangle;
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
    private Texture nave, fundo, asteroide, missilTexture;
    private Sprite naveSprite;
    private Array<Asteroides> asteroides;
    private Array<Missil> misseis;
    private float tempoGeracaoAsteroide;
    private Rectangle naveRectangle, asteroiRectangle;
    // private Vector2 touchPos;

    @Override
    public void create() {
        batch = new SpriteBatch();
        viewport = new FitViewport(10 * (float) Gdx.graphics.getWidth() / Gdx.graphics.getHeight(), 10);
        nave = new Texture("idle2.png");
        fundo = new Texture("fundo.png");
        asteroide = new Texture("asteroide.png");
        missilTexture = new Texture("missil.png");

        naveSprite = new Sprite(nave);
        float aspectRatio = (float) nave.getWidth() / nave.getHeight();
        float desiredWidth = 1f; // Tamanho desejado
        float desiredHeight = desiredWidth / aspectRatio;
        naveSprite.setSize(desiredWidth, desiredHeight);

        misseis = new Array<>();
        asteroides = new Array<>();
        tempoGeracaoAsteroide = 0;
        naveRectangle = new Rectangle();
        asteroiRectangle = new Rectangle();
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

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            float xmissil = naveSprite.getX() + naveSprite.getWidth() / 2;
            float ymissil = naveSprite.getY() + naveSprite.getHeight();
            misseis.add(new Missil(missilTexture, xmissil, ymissil, 15f));
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

        // Não deixa a nave sair da tela.
        naveSprite.setX(MathUtils.clamp(naveSprite.getX(), 0, worldWidth - naveWidth));
        naveSprite.setY(MathUtils.clamp(naveSprite.getY(), 0, worldHeight - naveHeight));

        tempoGeracaoAsteroide += Gdx.graphics.getDeltaTime();

        naveRectangle.set(naveSprite.getX(), naveSprite.getY(), naveWidth, naveHeight);

        if (tempoGeracaoAsteroide > 1.5f) {
            asteroides.add(new Asteroides(asteroide, viewport));
            tempoGeracaoAsteroide = 0;
        }

        for (int i = asteroides.size - 1; i >= 0; i--) {
            Asteroides asteroide = asteroides.get(i);
            asteroide.update(Gdx.graphics.getDeltaTime());

            asteroiRectangle.set(asteroide.getSprite().getX(), asteroide.getSprite().getY(),
                    asteroide.getSprite().getWidth(), asteroide.getSprite().getHeight());

            if (asteroide.foraDaTela()) {
                asteroides.removeIndex(i);
            } else if (naveRectangle.overlaps(asteroiRectangle)) { // verifica se encostou na nave
                asteroides.removeIndex(i);
            }
        }

        for (int i = misseis.size - 1; i >= 0; i--) {
            Missil missil = misseis.get(i);
            missil.update(Gdx.graphics.getDeltaTime());
            if (missil.foraDaTela(viewport)) {
                misseis.removeIndex(i);
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

        for (Missil missil : misseis) {
            missil.getSprite().draw(batch);
        }

        batch.end();
    }
}
