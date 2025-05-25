package engine.movement;

import engine.entity.Enemy;
import org.joml.Vector2f;
import engine.movement.Easing;
import engine.movement.EasingFunction;
import java.util.ArrayList;
import java.util.List;

public class WaypointPath implements MovementPath {
    private final List<Vector2f> waypoints;
    private int current = 0;
    private float speed;
    private float progress = 0f;
    private EasingFunction easing = Easing.registry.get("linear"); // default to linear

    public WaypointPath(float speed) {
        this.waypoints = new ArrayList<>();
        this.speed = speed;
    }

    public void setEasing(EasingFunction easing) {
        this.easing = easing;
    }

    public void addWaypoint(float x, float y) {
        waypoints.add(new Vector2f(x, y));
    }

    @Override
    public void apply(Enemy enemy, float delta) {
        if (current >= waypoints.size() - 1) return;

        Vector2f start = waypoints.get(current);
        Vector2f end = waypoints.get(current + 1);

        float distance = start.distance(end);
        float moveDelta = (speed * delta) / distance;

        progress += moveDelta;
        if (progress >= 1f) {
            progress = 0f;
            current++;
            return;
        }

        float eased = easing.ease(progress);
        float x = start.x + (end.x - start.x) * eased;
        float y = start.y + (end.y - start.y) * eased;

        enemy.setX(x);
        enemy.setY(y);

        if (waypoints.size() < 2) {
            System.err.println("[⚠️] WaypointPath has insufficient points: " + waypoints.size());
            return;
        }
    }
}