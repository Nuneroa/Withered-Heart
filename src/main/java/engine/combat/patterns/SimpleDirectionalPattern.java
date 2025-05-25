package engine.combat.patterns;
import engine.combat.EnemyPattern;

import engine.entity.Bullet;
import engine.entity.BulletManager;
import engine.graphics.Texture;

public class SimpleDirectionalPattern implements EnemyPattern {
    private final float angle;
    private final float speed;
    private static final float DEFAULT_SPEED = 200f;

    private static final int bulletCount = 8; // Number of bullets to fire in the pattern

    public SimpleDirectionalPattern(float angle) {
        this(angle, DEFAULT_SPEED);
    }

    public SimpleDirectionalPattern(float angle, float speed) {
        this.angle = angle;
        this.speed = speed;
    }
    @Override
    public void fire(float x, float y, Texture bulletTexture, BulletManager manager) {
        double angleStep = 2 * Math.PI / bulletCount;
        for (int i = 0; i < bulletCount; i++) {
            double angle = i * angleStep;
            float vx = (float) (Math.cos(angle) * speed);
            float vy = (float) (Math.sin(angle) * speed);
            manager.spawn(x, y, vx, vy, bulletTexture, b -> {});
        }
    }
}