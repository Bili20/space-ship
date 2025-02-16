package io.github.terminalroot;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;

public class TextUtils {

    public static void drawCenteredText(SpriteBatch batch, BitmapFont font, String text, Viewport viewport) {
        GlyphLayout layout = new GlyphLayout();
        layout.setText(font, text);
        float centerX = viewport.getWorldWidth() / 2;
        float centerY = viewport.getWorldHeight() / 2;
        float posX = centerX - layout.width / 2;
        float posY = centerY + layout.height / 2;
        font.draw(batch, layout, posX, posY);
    }

}
