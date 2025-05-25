package engine.combat;

public class PowerSystem {
    private float power = 0f;
    private final float maxPower = 4.0f;

    public void increase(float amount) {
        power = Math.min(power + amount, maxPower);
    }

    public int getLevel() {
        return (int) power;
    }

    public float getPower() {
        return power;
    }

    public void reset() {
        power = 0f;
    }
}
