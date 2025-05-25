package engine.combat.patterns;

import engine.combat.EnemyPattern;
import engine.entity.BulletManager;
import engine.graphics.Texture;

public class SpiralPattern implements EnemyPattern {
    private static final int NUM_BULLETS = 12;
    private static final float SPIRAL_RADIUS = 100f;
    private static final float SPIRAL_SPEED = 100f;
    private final int bulletCount;
    private final float speed;

    public SpiralPattern(int bulletCount, float speed) {
        this.bulletCount = bulletCount;
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