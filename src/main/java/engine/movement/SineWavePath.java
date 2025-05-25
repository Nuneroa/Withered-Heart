package engine.movement;

import engine.entity.Enemy;

public class SineWavePath implements MovementPath {
    private final float amplitude;
    private final float frequency;
    private float time = 0f;

    public SineWavePath(float amplitude, float frequency) {
        this.amplitude = amplitude;
        this.frequency = frequency;
    }

    @Override
    public void apply(Enemy enemy, float delta) {
        time += delta;
        float baseY = enemy.getY();
        enemy.setX(enemy.getX() + (float) Math.sin(time * frequency) * amplitude * delta);
        enemy.setY(baseY + 20f * delta);
    }
}
