package engine.scene;

import engine.Stages.ExampleStage;
import engine.UI.HUD;
import engine.entity.*;
import engine.entity.Player.Player;
import engine.graphics.*;
import engine.input.InputHandler;
import engine.script.*;
import engine.util.Constants;
import engine.Stages.*;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class GameScene extends Scene {
    private long window;
    private final int WIDTH = 1280, HEIGHT = 960;
    private Player player;
    private Background background;
    private Texture borderTexture;
    private BulletManager bulletManager;
    private EnemySpawner enemySpawner;
    private BulletManager enemyBullets;
    private BulletManager playerBullets;
    private Shader shader;
    private SpriteRenderer spriteRenderer;
    private Matrix4f projection;
    private HUD hud;
    private LevelScript currentScript;

    public GameScene(long window) {
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
        projection = new Matrix4f().ortho(0, WIDTH, HEIGHT, 0, -1, 1);

        Texture bgTex = new Texture("textures/background.png");
        Texture playerTex = new Texture("textures/player.png");
        Texture enemyTex = new Texture("textures/enemy.png");
        Texture bulletTex = new Texture("textures/bullet.png");
        borderTexture = new Texture("textures/border.png");

        background = new Background(bgTex);
        bulletManager = new BulletManager(bulletTex);
        enemyBullets = new BulletManager(bulletTex);
        playerBullets = new BulletManager(bulletTex);

        enemySpawner = new EnemySpawner(enemyTex);
        enemySpawner.setPlayerBullets(playerBullets);

        Enemy.attachBulletManager(enemyBullets, bulletTex);
        Player.attachManagers(enemyBullets, playerBullets, bulletTex);

        player = new Player(WIDTH / 2f - 64, HEIGHT - 160, playerTex);
        enemyBullets.setPlayer(player);
        bulletManager.setPlayer(player);
        hud = new HUD("fonts/font.ttf", 24);

        ScriptContext context = new ScriptContext(enemySpawner, background, hud);
        currentScript = new Stage1(context);
        currentScript.onStart();
    }

    @Override
    public void update(float delta) {
        InputHandler.update();
        background.update(delta);
        bulletManager.update(delta);
        enemySpawner.update(delta);
        enemyBullets.update(delta);
        playerBullets.update(delta);
        player.update(delta);
        hud.update(player);
        if (currentScript != null) currentScript.onUpdate(delta);
    }

    @Override
    public void render(float delta, long window) {
        glClear(GL_COLOR_BUFFER_BIT);

        background.render(shader, spriteRenderer, projection);
        bulletManager.render(shader, spriteRenderer, projection);
        enemySpawner.render(shader, spriteRenderer, projection);
        playerBullets.render(shader, spriteRenderer, projection);
        player.render(shader, spriteRenderer, projection);
        enemyBullets.render(shader, spriteRenderer, projection);

        spriteRenderer.draw(borderTexture, shader, 0, 0, WIDTH, HEIGHT, projection);
        hud.render(shader, spriteRenderer, projection);
    }

    @Override
    public void exit() {
        if (currentScript != null) currentScript.onEnd();
    }
}
