package engine.physics;

public class Collision {
    public static boolean circleCollision(float x1, float y1, float r1, float x2, float y2, float r2) {
        float dx = x1 - x2;
        float dy = y1 - y2;
        float distSq = dx * dx + dy * dy;
        float radiusSum = r1 + r2;
        return distSq < radiusSum * radiusSum;
    }
}