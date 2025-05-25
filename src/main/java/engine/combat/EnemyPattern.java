package engine.combat;

import engine.entity.BulletManager;
import engine.graphics.Texture;

public interface EnemyPattern {
    void fire(float x, float y, Texture bulletTexture, BulletManager manager);
}