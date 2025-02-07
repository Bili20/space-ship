package io.github.terminalroot;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Missil {
    private Sprite sprite;
    private float velocidade;

    /**
     * Construtor do míssil.
     * 
     * @param textura    A textura usada para representar o míssil.
     * @param x          Posição X inicial.
     * @param y          Posição Y inicial.
     * @param velocidade Velocidade com que o míssil se move.
     */
    public Missil(Texture textura, float x, float y, float velocidade) {
        sprite = new Sprite(textura);

        float aspectRatio = (float) sprite.getWidth() / sprite.getHeight();
        float desiredWidth = 0.5f; // Tamanho desejado
        float desiredHeight = desiredWidth / aspectRatio;
        sprite.setSize(desiredWidth, desiredHeight);

        // aqui é pra definir a posicao inicial
        sprite.setPosition(x - sprite.getWidth() / 2, y);
        this.velocidade = velocidade;
    }

    /**
     * Atualiza a posição do míssil.
     *
     * @param delta Tempo decorrido desde o último frame.
     */
    public void update(float delta) {
        // o missil vai se mover pra cima
        sprite.translateY(velocidade * delta);
    }

    /**
     * Verifica se o míssil saiu da tela.
     *
     * @param viewport O viewport para obter as dimensões do mundo.
     * @return true se o míssil estiver fora da tela.
     */
    public boolean foraDaTela(Viewport viewport) {
        return sprite.getY() > viewport.getWorldHeight();
    }

    public Sprite getSprite() {
        return sprite;
    }
}
