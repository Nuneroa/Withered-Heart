package engine.scene;

import java.util.Objects;

public class SceneManager {
    private static Scene currentScene;
    private static long window;

    public static void init(long windowHandle) {
        window = windowHandle;
    }

    public static void changeScene(Scene newScene) {
        if (currentScene != null) {
            currentScene.exit();
        }

        currentScene = Objects.requireNonNull(newScene);
        currentScene.enter();
    }

    public static void update(float delta) {
        if (currentScene != null) {
            currentScene.update(delta);
        }
    }

    public static void render(float delta) {
        if (currentScene != null) {
            currentScene.render(delta, window);
        }
    }

    public static Scene getCurrentScene() {
        return currentScene;
    }
}
