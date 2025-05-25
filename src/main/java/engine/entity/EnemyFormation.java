package engine.entity;

public class EnemyFormation {
    public float[] xPositions;
    public float y;
    public float delay;
    public int patternId;

    public EnemyFormation(float[] xPositions, float y, float delay, int patternId) {
        this.xPositions = xPositions;
        this.y = y;
        this.delay = delay;
        this.patternId = patternId;
    }
}
