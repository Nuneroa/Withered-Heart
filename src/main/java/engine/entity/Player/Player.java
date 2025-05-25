package engine.entity.Player;

import engine.graphics.Texture;
import engine.entity.Bullet;
import engine.entity.BulletManager;
import engine.entity.Entity;
import engine.graphics.Shader;
import engine.graphics.SpriteRenderer;
import org.joml.Matrix4f;
import engine.input.InputHandler;
import engine.util.Constants;

import static org.lwjgl.glfw.GLFW.*;

public class Player extends Entity {
    private static BulletManager enemyBullets;
    private static BulletManager playerBullets;
    private static Texture bulletTexture;

    private float shootTimer = 0;
    private float shootCooldown = 0.1f;
    private float power = 0f;
    private int lives = 3;
    private int bombs = 2;

    private boolean invulnerable = false;
    private float invulnTimer = 0f;
    private float invulnDuration = 2.0f; // seconds of invulnerability after hit
    private float flickerTimer = 0f;
    private float flickerInterval = 0.08f; // seconds between flickers
    private boolean flickerVisible = true;

    public static void attachManagers(BulletManager enemy, BulletManager player, Texture bulletTex) {
        enemyBullets = enemy;
        playerBullets = player;
        bulletTexture = bulletTex;
    }

    public Player(float x, float y, Texture texture) {
        super(x, y, texture);
    }

    @Override
public void update(float delta) {
    float speed = InputHandler.isKeyDown(GLFW_KEY_LEFT_SHIFT) ? 100f : 200f;

    if (InputHandler.isKeyDown(GLFW_KEY_LEFT)) x -= speed * delta;
    if (InputHandler.isKeyDown(GLFW_KEY_RIGHT)) x += speed * delta;
    if (InputHandler.isKeyDown(GLFW_KEY_UP)) y -= speed * delta;
    if (InputHandler.isKeyDown(GLFW_KEY_DOWN)) y += speed * delta;

    float spriteW = texture.getWidth();
    float spriteH = texture.getHeight();
    x = Math.max(Constants.PLAY_AREA_X, Math.min(x, Constants.PLAY_AREA_X + Constants.PLAY_AREA_WIDTH - spriteW));
    y = Math.max(Constants.PLAY_AREA_Y, Math.min(y, Constants.PLAY_AREA_Y + Constants.PLAY_AREA_HEIGHT - spriteH));

    shootTimer += delta;
    if (InputHandler.isKeyDown(GLFW_KEY_Z) && shootTimer >= shootCooldown) {
        float size = 9f;
        float bx = getHitboxX() - size + 2;
        float by = getHitboxY() - size + 28;
        // Updated to match new BulletManager.spawn signature
        playerBullets.spawn(bx, by, 0, -1400f, bulletTexture, b -> {});
        shootTimer = 0f;
    }

    // Invulnerability and flicker logic
    if (invulnerable) {
        invulnTimer += delta;
        flickerTimer += delta;
        if (flickerTimer >= flickerInterval) {
            flickerVisible = !flickerVisible;
            flickerTimer = 0f;
        }
        if (invulnTimer >= invulnDuration) {
            invulnerable = false;
            flickerVisible = true;
            invulnTimer = 0f;
            flickerTimer = 0f;
        }
    } else {
        flickerVisible = true;
    }
}

    @Override
    public void render(Shader shader, SpriteRenderer renderer, Matrix4f projection) {
        float renderScale = 1f;
        float spriteW = texture.getWidth() * renderScale;
        float spriteH = texture.getHeight() * renderScale;

        if (flickerVisible) {
            renderer.draw(texture, shader, x, y, spriteW, spriteH, projection);
        }

        if (InputHandler.isKeyDown(GLFW_KEY_LEFT_SHIFT)) {
            float size = 9f;
            float centerX = getHitboxX() - size + 4;
            float centerY = getHitboxY() - size + 28;
            renderer.draw(bulletTexture, shader, centerX, centerY, size, size, projection);
        }
    }

    public float getHitboxX() {
        float spriteW = texture.getWidth();
        return x + spriteW / 2f;
    }

    public float getHitboxY() {
        float spriteH = texture.getHeight();
        return y + spriteH / 2f;
    }

    public float getPowerLevel() {
        return power;
    }

    public int getLives() {
        return lives;
    }

    public int getBombs() {
        return bombs;
    }

    public void increasePower(float amount) {
        power += amount;
        if (power > 1f) power = 1f;
    }

    public void decreasePower(float amount) {
        power -= amount;
        if (power < 0f) power = 0f;
    }

    public void onHit() {
        if (!invulnerable) {
            lives--;
            if (lives < 0) lives = 0;
            invulnerable = true;
            invulnTimer = 0f;
            flickerTimer = 0f;
            flickerVisible = true;
        }
    }

    public void collidesWith(Bullet bullet) {
        if (invulnerable) return;
        float dx = getHitboxX() - bullet.getCenterX();
        float dy = getHitboxY() - bullet.getCenterY();
        float distanceSquared = dx * dx + dy * dy;
        float radius = 3f; // Touhou-style hitbox radius
        if (distanceSquared < radius * radius) {
            onHit();
            bullet.deactivate();
        }
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void resetHitState() {
        // Reset invulnerability or hit state here if needed
        invulnerable = false;
        invulnTimer = 0f;
        flickerTimer = 0f;
        flickerVisible = true;
    }
}