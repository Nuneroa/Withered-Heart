package engine.script;

import engine.combat.EnemyPattern;
import engine.combat.patterns.RadialPattern;
import engine.combat.patterns.SpiralPattern;
import engine.combat.patterns.SimpleDirectionalPattern;
import engine.combat.patterns.SlingShot;
//Soon to be deprecated

public class PatternBuilder {

    public static EnemyPattern createPattern(String type, float... args) {
        switch (type.toLowerCase()) {
            case "radial":
                int count = (int) (args.length > 0 ? args[0] : 10);
                float speed = args.length > 1 ? args[1] : 100f;
                return new RadialPattern(count, speed);

            case "spiral":
                int bullets = (int) (args.length > 0 ? args[0] : 12);
                float spiralSpeed = args.length > 1 ? args[1] : 100f;
                return new SpiralPattern(bullets, spiralSpeed);

            case "directional":
                float angle = args.length > 0 ? args[0] : 90f;
                float dirSpeed = args.length > 1 ? args[1] : 150f;
                return new SimpleDirectionalPattern(angle, dirSpeed);
            case "sling":
                float slingSpeed = args.length > 0 ? args[0] : 400f; // default speed 400
                return new SlingShot(slingSpeed);

            default:
                throw new IllegalArgumentException("Unknown pattern: " + type);
        }
    }
}
