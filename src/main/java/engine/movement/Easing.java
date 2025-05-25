package engine.movement;

import java.util.HashMap;
import java.util.Map;

public class Easing {
    public static final Map<String, EasingFunction> registry = new HashMap<>();

    static {
        registry.put("linear", t -> t);
        registry.put("easeInQuad", t -> t * t);
        registry.put("easeOutQuad", t -> t * (2 - t));
        registry.put("easeInOutSine", t -> -(float) Math.cos(Math.PI * t) / 2f + 0.5f);
        registry.put("easeInOutCubic", t -> t < 0.5 ? 4 * t * t * t : (float)(1 - Math.pow(-2 * t + 2, 3)) / 2f);
        // Add more as needed
    }

    public static EasingFunction get(String name) {
        return registry.getOrDefault(name.toLowerCase(), registry.get("linear"));
    }
}
