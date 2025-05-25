package engine.entity;

import engine.graphics.Texture;
import engine.graphics.Shader;
import engine.graphics.SpriteRenderer;
import org.joml.Matrix4f;

import engine.combat.patterns.RadialPattern;
import engine.combat.patterns.SpiralPattern;
import engine.combat.patterns.PatternRegistry;
import engine.movement.MovementPath;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EnemySpawner {
    private final Texture enemyTexture;
    private final List<Enemy> enemies = new ArrayList<>();
    private BulletManager playerBullets;

    public EnemySpawner(Texture enemyTexture) {
        this.enemyTexture = enemyTexture;
    }

    public void setPlayerBullets(BulletManager playerBullets) {
        this.playerBullets = playerBullets;
    }

    public void spawn(String type, float x, float y, float angle) {
        Enemy enemy = new Enemy(x, y, enemyTexture);

        switch (type.toLowerCase()) {
            case "fairy":
                enemy.setPattern(new RadialPattern(10, 100f));
                break;
            case "spirit":
                enemy.setPattern(PatternRegistry.getPattern("Sling"));
                break;
            case "Hell Fairy":
                enemy.setPattern(PatternRegistry.getPattern("directional"));
                break;
            case "ghost":
                enemy.setPattern(new SpiralPattern(12, 100f));
                break;
            default:
                System.out.println("Unknown enemy type: " + type);
                return;
        }

        enemies.add(enemy);
    }

    public void spawnWithPatternAndPath(String type, float x, float y, float angle, String patternName, MovementPath path) {
    Enemy enemy = new Enemy(x, y, enemyTexture);
    enemy.setPattern(engine.script.PatternBuilder.createPattern(patternName));
    enemy.setMovementPath(path);
    enemies.add(enemy);
    }


    public void update(float delta) {
        Iterator<Enemy> iterator = enemies.iterator();
        while (iterator.hasNext()) {
            Enemy enemy = iterator.next();
            enemy.update(delta);
            for (Bullet b : playerBullets.getBullets()) {
                if (enemy.collidesWith(b)) {
                    iterator.remove();
                    b.deactivate();
                    break;
                }
            }
        }
    }

    public void render(Shader shader, SpriteRenderer renderer, Matrix4f projection) {
        for (Enemy e : enemies) {
            e.render(shader, renderer, projection);
        }
    }

    public void destroy() {
        enemies.clear();
    }
    public List<Enemy> getEnemies() {
        return enemies;
    }
} 
