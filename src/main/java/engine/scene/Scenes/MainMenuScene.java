package engine.scene.Scenes;

import org.joml.Matrix4f;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwGetKey;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;

import engine.graphics.Shader;
import engine.graphics.SpriteRenderer;
import engine.graphics.TextRenderer;
import engine.input.InputHandler;
import engine.scene.GameScene;
import engine.scene.Scene;
import engine.scene.SceneManager;

public class MainMenuScene extends Scene {
    private final long window;
    private Shader shader;
    private SpriteRenderer spriteRenderer;
    private TextRenderer textRenderer;
    private Matrix4f projection;

    public MainMenuScene(long window) {
        this.window = window;
    }

    @Override
    public void enter() {
        GL.createCapabilities();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glClearColor(0f, 0f, 0f, 1f);

        InputHandler.init(window);

        shader = new Shader("shaders/basic.vert", "shaders/basic.frag");
        spriteRenderer = new SpriteRenderer();
        textRenderer = new TextRenderer("fonts/font.ttf", 36);
        projection = new Matrix4f().ortho(0, 1280, 960, 0, -1, 1);
    }

    @Override
    public void update(float delta) {
        InputHandler.update();

        if (glfwGetKey(window, GLFW_KEY_ENTER) == GLFW_PRESS) {
            SceneManager.changeScene(new GameScene(window));
        }
    }

    @Override
    public void render(float delta, long window) {
        glClear(GL_COLOR_BUFFER_BIT);
        shader.bind();

        textRenderer.renderText("Crystal: Withered Heart", 400f, 300f, 1f, projection);
        textRenderer.renderText("Press ENTER to Start", 430f, 400f, 0.75f, projection);

        shader.unbind();
    }

    @Override
    public void exit() {
        // Cleanup if needed
    }
}
