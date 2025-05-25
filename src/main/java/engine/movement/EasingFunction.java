package engine.movement;

@FunctionalInterface
public interface EasingFunction {
    float ease(float t); // t = progress from 0 to 1
}