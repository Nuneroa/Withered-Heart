package engine.UI;

import engine.entity.Player.Player;
import engine.graphics.Shader;
import engine.graphics.SpriteRenderer;
import engine.graphics.TextRenderer;

import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL11.*;

public class HUD {
    private int score = 0;
    private int lives = 3;
    private int bombs = 0;
    private float power = 0f;
    private int stage = 1;

    private final TextRenderer TextRenderer;

    public HUD(String fontPath, int fontSize) {
        TextRenderer = new TextRenderer(fontPath, fontSize);
    }

    public void update(Player player) {
        this.power = player.getPowerLevel();
        this.lives = player.getLives();
        this.bombs = player.getBombs();
    }

    public void addScore(int amount) {
        this.score += amount;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public void render(Shader shader, SpriteRenderer renderer, Matrix4f projection) {
        shader.bind();

        float baseX = 985f;
        float lineHeight = 30f;
        float y = 40f;

        TextRenderer.renderText("Score: " + score, baseX, y, 1f, projection);
        y += lineHeight * 1.2f;

        TextRenderer.renderText("-------------------", baseX, y, 1f, projection);
        y += lineHeight;

        TextRenderer.renderText("Lives: " + lives, baseX, y, 1f, projection);
        y += lineHeight;

        TextRenderer.renderText("Bombs: " + bombs, baseX, y, 1f, projection);
        y += lineHeight;

        TextRenderer.renderText("-------------------", baseX, y, 1f, projection);
        y += lineHeight;

        TextRenderer.renderText("Power: " + power, baseX, y, 1f, projection);
        y += lineHeight;

        TextRenderer.renderText("-------------------", baseX, y, 1f, projection);
        y += lineHeight;

        TextRenderer.renderText("Stage: " + stage, baseX, y, 1f, projection);

        shader.unbind();
    }
}