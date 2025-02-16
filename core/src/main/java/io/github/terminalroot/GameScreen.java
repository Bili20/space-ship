package io.github.terminalroot;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
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

public class GameScreen implements Screen {
    final Drop game;

    private Texture naveTexture, fundoTexture, asteroideTexture, missilTexture;
    private Sprite naveSprite;
    private Array<Asteroides> asteroides;
    private Array<Missil> misseis;
    private float tempoGeracaoAsteroide;
    private Rectangle naveRectangle, asteroiRectangle, missilRectangle;
    private int asteroideDestruido;
    private float missilCooldown;

    public GameScreen(final Drop game) {
        this.game = game;

        game.batch = new SpriteBatch();
        game.viewport = new FitViewport(10 * (float) Gdx.graphics.getWidth() / Gdx.graphics.getHeight(), 10);

        naveTexture = new Texture("idle2.png");
        fundoTexture = new Texture("fundo.png");
        asteroideTexture = new Texture("asteroide.png");
        missilTexture = new Texture("missil.png");

        naveSprite = new Sprite(naveTexture);
        float aspectRatio = (float) naveTexture.getWidth() / naveTexture.getHeight();
        float desiredWidth = 1f; // Tamanho desejado
        float desiredHeight = desiredWidth / aspectRatio;
        naveSprite.setSize(desiredWidth, desiredHeight);

        misseis = new Array<>();
        asteroides = new Array<>();
        tempoGeracaoAsteroide = 0;
        naveRectangle = new Rectangle();
        asteroiRectangle = new Rectangle();
        missilRectangle = new Rectangle();
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        draw();
        input();
        logic();
    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    private void input() {
        float speed = 10f;
        float delta = Gdx.graphics.getDeltaTime();

        if (missilCooldown > 0) {
            missilCooldown -= delta;
        }

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

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && missilCooldown <= 0) {
            float xmissil = naveSprite.getX() + naveSprite.getWidth() / 2;
            float ymissil = naveSprite.getY() + naveSprite.getHeight();
            misseis.add(new Missil(missilTexture, xmissil, ymissil, 12f));
            missilCooldown = 0.3f;
        }

        // if (Gdx.input.isTouched()) {
        // touchPos.set(Gdx.input.getX(), Gdx.input.getY());
        // viewport.unproject(touchPos);
        // naveSprite.setCenterX(touchPos.x);
        // }
    }

    private void logic() {
        float worldWidth = game.viewport.getWorldWidth();
        float worldHeight = game.viewport.getWorldHeight();

        float naveWidth = naveSprite.getWidth();
        float naveHeight = naveSprite.getHeight();

        // Não deixa a nave sair da tela.
        naveSprite.setX(MathUtils.clamp(naveSprite.getX(), 0, worldWidth - naveWidth));
        naveSprite.setY(MathUtils.clamp(naveSprite.getY(), 0, worldHeight - naveHeight));

        tempoGeracaoAsteroide += Gdx.graphics.getDeltaTime();

        naveRectangle.set(naveSprite.getX(), naveSprite.getY(), naveWidth, naveHeight);

        if (tempoGeracaoAsteroide > 1.5f) {
            asteroides.add(new Asteroides(asteroideTexture, game.viewport));
            tempoGeracaoAsteroide = 0;
        }

        for (int i = misseis.size - 1; i >= 0; i--) {
            Missil missil = misseis.get(i);
            missil.update(Gdx.graphics.getDeltaTime());

            if (missil.foraDaTela(game.viewport)) {
                misseis.removeIndex(i);
            }
        }

        for (int i = asteroides.size - 1; i >= 0; i--) {
            Asteroides asteroide = asteroides.get(i);
            asteroide.update(Gdx.graphics.getDeltaTime());

            if (asteroide.foraDaTela()) {
                asteroides.removeIndex(i);
            }
        }

        for (int i = asteroides.size - 1; i >= 0; i--) {
            Asteroides asteroide = asteroides.get(i);

            asteroiRectangle.set(asteroide.getSprite().getX(), asteroide.getSprite().getY(),
                    asteroide.getSprite().getWidth(), asteroide.getSprite().getHeight());

            for (int j = misseis.size - 1; j >= 0; j--) {
                Missil missil = misseis.get(j);

                missilRectangle.set(missil.getSprite().getX(), missil.getSprite().getY(), missil.getSprite().getWidth(),
                        missil.getSprite().getHeight());

                if (asteroiRectangle.overlaps(missilRectangle)) {
                    asteroides.removeIndex(i);
                    misseis.removeIndex(j);
                    asteroideDestruido++;

                    break;
                }

            }
            if (asteroiRectangle.overlaps(naveRectangle)) {
                game.setScreen(new GameOverScreen(game));
                asteroideDestruido = 0;
                dispose();
            }

        }

    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);
        game.batch.begin();

        float worldWidth = game.viewport.getWorldWidth();
        float worldHeight = game.viewport.getWorldHeight();

        game.batch.draw(fundoTexture, 0, 0, worldWidth, worldHeight);
        naveSprite.draw(game.batch);

        game.font.draw(game.batch, "Asteroides destruidos: " + asteroideDestruido, 0, worldHeight);

        for (Asteroides asteroide : asteroides) {
            asteroide.getSprite().draw(game.batch);
        }

        for (Missil missil : misseis) {
            missil.getSprite().draw(game.batch);
        }

        game.batch.end();
    }

    @Override
    public void dispose() {
        naveTexture.dispose();
        asteroideTexture.dispose();
        missilTexture.dispose();
    }
}
