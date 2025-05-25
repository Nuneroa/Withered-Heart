package engine.combat;

import engine.graphics.Texture;
import engine.entity.Player.Player;
import engine.graphics.Shader;
import engine.graphics.SpriteRenderer;
import engine.physics.Collision;

import org.joml.Matrix4f;

public class PowerUp {
    public float x, y;
    private final Texture texture;
    private boolean active = true;
    private static final float SPEED = 60f;
    private static final float RADIUS = 16f;

    public PowerUp(float x, float y, Texture texture) {
        this.x = x;
        this.y = y;
        this.texture = texture;
    }

    public void update(float delta, Player player) {
        if (!active) return;
        y += SPEED * delta;
        float px = player.getCenterX();
        float py = player.getCenterY();
        if (Collision.circleCollision(x + RADIUS, y + RADIUS, RADIUS, px, py, 8f)) {
            active = false;
            player.increasePower(0.1f);
        }
    }

    public void render(Shader shader, SpriteRenderer renderer, Matrix4f projection) {
    if (active) {
        renderer.draw(texture, shader, x, y, texture.getWidth(), texture.getHeight(), projection);
    }
    }
    public boolean isActive() {
        return active;
    }
}