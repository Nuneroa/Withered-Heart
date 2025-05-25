package engine.combat.patterns;

import engine.combat.EnemyPattern;
import engine.entity.BulletManager;
import engine.graphics.Texture;
import java.util.Random;
import engine.entity.Player.*;

public class SlingShot implements EnemyPattern {
    private static final int MIN_BULLETS = 3;
    private static final int MAX_BULLETS = 7;
    private static final float SPREAD_ANGLE = 20f; // degrees

    private static final float MIN_INITIAL_SPEED = 80.0f; // Lower bound for random speed
    private static final float MAX_INITIAL_SPEED = 280.0f; // Upper bound for random speed
    private static final float FINAL_SPEED = 20.0f;   // Slow speed after deceleration
    private static final int DECELERATION_TICKS = 300000; // Frames to decelerate

    private final float speed; // Not used anymore, but kept for compatibility

    public SlingShot(float speed) {
        this.speed = speed;
    }

    private final Random random = new Random();

    @Override
    public void fire(float x, float y, Texture bulletTexture, BulletManager manager) {
        // Get player position
        Player player = manager.getPlayer();
        float playerX = player.getHitboxX();
        float playerY = player.getHitboxY();

        // Calculate angle towards player
        float dx = playerX - x;
        float dy = playerY - y;
        float baseAngle = (float) Math.atan2(dy, dx);

        int bulletCount = MIN_BULLETS + random.nextInt(MAX_BULLETS - MIN_BULLETS + 1);

        for (int i = 0; i < bulletCount; i++) {
            float spread = (random.nextFloat() - 0.5f) * SPREAD_ANGLE;
            float angle = baseAngle + (float) Math.toRadians(spread);

            float initialSpeed = MIN_INITIAL_SPEED + random.nextFloat() * (MAX_INITIAL_SPEED - MIN_INITIAL_SPEED);

            float vx = (float) Math.cos(angle) * initialSpeed;
            float vy = (float) Math.sin(angle) * initialSpeed;

            manager.spawn(x, y, vx, vy, bulletTexture, (bullet) -> {
                bullet.setCustomFloat("initialSpeed", initialSpeed);
                bullet.setCustomUpdate((b, delta) -> {
                    int ticks = b.getCustomInt("ticks", 0);
                    float currVx = b.getVx();
                    float currVy = b.getVy();
                    float bulletInitialSpeed = b.getCustomFloat("initialSpeed", initialSpeed);
                    if (ticks < DECELERATION_TICKS) {
                        float t = (float) ticks / DECELERATION_TICKS;
                        float speed = bulletInitialSpeed + t * (FINAL_SPEED - bulletInitialSpeed);
                        float ang = (float) Math.atan2(currVy, currVx);
                        currVx = (float) Math.cos(ang) * speed;
                        currVy = (float) Math.sin(ang) * speed;
                        b.setVx(currVx);
                        b.setVy(currVy);
                        b.setCustomInt("ticks", ticks + 1);
                    }
                });
            });
        }
    }
}