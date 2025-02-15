package io.github.terminalroot;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Drop extends Game {
    public SpriteBatch batch;
    public BitmapFont font;
    public FitViewport viewport;

    @Override
    public void create() {
        batch = new SpriteBatch();

        font = new BitmapFont();
        viewport = new FitViewport(10 * (float) Gdx.graphics.getWidth() / Gdx.graphics.getHeight(), 10);

        font.setUseIntegerPositions(false);
        font.getData().setScale(viewport.getWorldHeight() / Gdx.graphics.getHeight());

        this.setScreen(new MainMenuScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        super.dispose();
    }
}
