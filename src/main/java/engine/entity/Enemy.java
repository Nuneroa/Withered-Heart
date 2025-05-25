package engine.entity;

import engine.graphics.Texture;
import engine.graphics.SpriteRenderer;
import engine.entity.BulletManager;
import engine.combat.EnemyPattern;
import engine.util.Constants;
import engine.graphics.Shader;
import org.joml.Matrix4f;
import engine.movement.MovementPath;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class Enemy extends Entity {
    private float shootTimer = 0;
    private final float shootCooldown = 1.5f;
    private static BulletManager bulletManager;
    private static Texture bulletTexture;
    private static final float bulletSpeed = 200f;
    private EnemyPattern pattern;
    private int hp = 3;
    private boolean active = true;
    private MovementPath movementPath;

    public static void attachBulletManager(BulletManager bm, Texture bt) {
        bulletManager = bm;
        bulletTexture = bt;
    }

    public Enemy(float x, float y, Texture texture) {
        super(x, y, texture);
    }

    public void setPattern(EnemyPattern pattern) {
        this.pattern = pattern;
    }

    public void hit() {
        hp--;
        if (hp <= 0) active = false;
    }

    public void setMovementPath(MovementPath path) {
    this.movementPath = path;
    }


    public boolean isActive() {
        return active && y <= Constants.PLAY_AREA_Y + Constants.PLAY_AREA_HEIGHT + 100;
    }

    public boolean collidesWith(Bullet bullet) {
        float dx = getCenterX() - bullet.getCenterX();
        float dy = getCenterY() - bullet.getCenterY();
        float distanceSquared = dx * dx + dy * dy;
        float radius = 16f; // Approximate radius for hitbox
        return distanceSquared < radius * radius;
    }

    public void update(float delta) {
    if (!active) return;
    if (movementPath != null) movementPath.apply(this, delta);
    else {
        y += 20 * delta;
        x += Math.sin(glfwGetTime() * 2) * 50 * delta;
    }

    shootTimer += delta;
    if (shootTimer >= shootCooldown && bulletManager != null && pattern != null) {
        float renderScale = 1f / 6f;
        float bx = x + (texture.getWidth() * renderScale) / 2f;
        float by = y + (texture.getHeight() * renderScale) / 2f;
        pattern.fire(bx, by, bulletTexture, bulletManager);
        shootTimer = 0;
    }
}

    public void render(Shader shader, SpriteRenderer renderer, Matrix4f projection) {
        if (active) {
            renderer.draw(texture, shader, x, y, getScaledWidth(), getScaledHeight(), projection);
        }
    }

    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }
    public float setX(float x) {
        this.x = x;
        return this.x;
    }
    public float setY(float y) {
        this.y = y;
        return this.y;
    }
}
