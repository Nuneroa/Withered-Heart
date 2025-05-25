package engine.scene.Scenes;

import engine.scene.Scene;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL;
import engine.graphics.*;
import static org.lwjgl.opengl.GL11.*;

public class Loading extends Scene {
    private long window;
    private Shader shader;
    private SpriteRenderer spriteRenderer;
    private TextRenderer textRenderer;
    private Matrix4f projection;
    private Texture loadingTexture;
    private String loadingText = "Loading...";
    private float opacity = 0f;
    private boolean fadingIn = true;
    private float animationTimer = 0f;

	public Loading(long window) {
        this.window = window;
    }

    @Override
    public void enter() {
        GL.createCapabilities();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glClearColor(0f, 0f, 0f, 1f);


		loadingTexture = new Texture("textures/Cover.png");
        shader = new Shader("shaders/basic.vert", "shaders/basic.frag");
        spriteRenderer = new SpriteRenderer();
        textRenderer = new TextRenderer("fonts/font.ttf", 28);
        projection = new Matrix4f().ortho(0, 1280, 960, 0, -1, 1);
    }

    public void setText(String text) {
        this.loadingText = text;
    }

    public void resetFade() {
        this.opacity = 0f;
        this.fadingIn = true;
    }

    @Override
    public void update(float deltaTime) {
        //Load all scenes asynchronously
		// For now, just simulate loading with a timer
		animationTimer += deltaTime;
		if (animationTimer >= 3f) {
			// Switch to the next scene after 3 seconds
			// Replace 'nextScene' with your actual scene instance
			engine.scene.SceneManager.changeScene(new MainMenuScene(window));
			return;
		}

		// Fade animation logic (optional, keep if you want the fade effect)
		if (fadingIn) {
			opacity += deltaTime * 0.5f;
			if (opacity >= 1f) {
			opacity = 1f;
			fadingIn = false;
			}
		} else {
			opacity -= deltaTime * 0.5f;
			if (opacity <= 0f) {
			opacity = 0f;
			fadingIn = true;
			}
		}

    }

    @Override
    public void render(float deltaTime, long currentTime) {
        glClear(GL_COLOR_BUFFER_BIT);

        shader.bind();

		
		// Draw the loading texture
		spriteRenderer.draw(loadingTexture, shader, 0, 0, 1280, 960, projection);
        shader.unbind();
    }

    @Override
    public void exit() {
        // Cleanup if needed
    }
}