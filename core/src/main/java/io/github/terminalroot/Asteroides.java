package io.github.terminalroot;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Asteroides {
    private Sprite sprite;
    private float velocidade;

    public Asteroides(Texture texture, FitViewport viewport) {
        sprite = new Sprite(texture);
        sprite.setSize(1, 1);

        float x = MathUtils.random(0, viewport.getWorldWidth() - sprite.getWidth());
        float y = viewport.getWorldHeight();
        sprite.setPosition(x, y);

        velocidade = MathUtils.random(2f, 5f);
    }

    public void update(float delta) {
        sprite.setY(sprite.getY() - velocidade * delta);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public boolean foraDaTela() {
        return sprite.getY() + sprite.getHeight() < 0;
    }
}
