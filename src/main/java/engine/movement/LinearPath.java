package engine.movement;

import engine.entity.Enemy;

public class LinearPath implements MovementPath {
    private final float vx, vy;

    public LinearPath(float vx, float vy) {
        this.vx = vx;
        this.vy = vy;
    }

    @Override
    public void apply(Enemy enemy, float delta) {
        enemy.setX(enemy.getX() + vx * delta);
        enemy.setY(enemy.getY() + vy * delta);
    }
}
