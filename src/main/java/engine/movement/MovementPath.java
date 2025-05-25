package engine.movement;

import engine.entity.Enemy;

public interface MovementPath {
    void apply(Enemy enemy, float delta);
}
