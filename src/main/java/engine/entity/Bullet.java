package engine.entity;

import engine.graphics.Texture;
import engine.physics.Collision;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import org.joml.Matrix4f;

import engine.entity.Player.Player;
import engine.graphics.Shader;
import engine.graphics.SpriteRenderer;
import engine.util.Constants;

public class Bullet {
    public float x, y, vx, vy;
    public boolean active = true;
    private final Texture texture;

    private Map<String, Integer> customInts = new HashMap<>();
    private BiConsumer<Bullet, Float> customUpdate;


    public Bullet(float x, float y, float vx, float vy, Texture texture) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.texture = texture;
    }


    public void update(float delta) {
        if (customUpdate != null) customUpdate.accept(this, delta);

        x += vx * delta;
        y += vy * delta;
        if (x < Constants.PLAY_AREA_X || x > Constants.PLAY_AREA_X + Constants.PLAY_AREA_WIDTH ||
            y < Constants.PLAY_AREA_Y || y > Constants.PLAY_AREA_Y + Constants.PLAY_AREA_HEIGHT) {
            active = false;
        }
    }

    public void render(Shader shader, SpriteRenderer renderer, Matrix4f projection) {
        if (active) {
            renderer.draw(texture, shader, x, y, texture.getWidth(), texture.getHeight(), projection);
        }
    }

    public void setCustomInt(String key, int value) { customInts.put(key, value); }
    public int getCustomInt(String key, int defaultVal) {
        return customInts.getOrDefault(key, defaultVal);
    }
    private Map<String, Float> customFloats = new HashMap<>();

    public void setCustomFloat(String key, float value) {
        customFloats.put(key, value);
    }

    public float getCustomFloat(String key, float defaultVal) {
        return customFloats.getOrDefault(key, defaultVal);
    }

    public void setCustomUpdate(BiConsumer<Bullet, Float> updater) {
        this.customUpdate = updater;
    }

    public float getVx() { return vx; }
    public float getVy() { return vy; }
    public void setVx(float vx) { this.vx = vx; }
    public void setVy(float vy) { this.vy = vy; }

    public float getCenterX() { return x + texture.getWidth() / 2f; }
    public float getCenterY() { return y + texture.getHeight() / 2f; }

    public void deactivate() { active = false; }


    public boolean collidesWith(Player player) {
        if (player == null) return false;
        float size = 9f;
        float playerX = player.getHitboxX() - size + 4;
        float playerY = player.getHitboxY() - size + 28;
        float radius = 3f; // Touhou-style precise collision radius
        return Collision.circleCollision(getCenterX(), getCenterY(), radius, playerX, playerY, radius);
    }
}
