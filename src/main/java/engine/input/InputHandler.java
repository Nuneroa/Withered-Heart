package engine.input;

import static org.lwjgl.glfw.GLFW.*;

public class InputHandler {
    private static long window;

    public static void init(long win) {
        window = win;
    }

    public static void update() {
        // Placeholder if needed later
    }

    public static boolean isKeyDown(int key) {
        return glfwGetKey(window, key) == GLFW_PRESS;
    }
}