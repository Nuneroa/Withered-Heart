package engine.entity;

import engine.graphics.Shader;
import engine.graphics.SpriteRenderer;
import engine.graphics.Texture;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import engine.entity.*;
import engine.entity.Player.Player;

import org.joml.Matrix4f;

public class BulletManager {
    private final List<Bullet> bullets = new ArrayList<>();
    private final Texture bulletTexture;
    private Player player;
    public BulletManager(Texture bulletTexture) {
        this.bulletTexture = bulletTexture;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void spawn(float x, float y, float vx, float vy, Texture texture, Consumer<Bullet> configurator) {
    Bullet bullet = new Bullet(x, y, vx, vy, texture);
    configurator.accept(bullet);
    bullets.add(bullet);
    }

    public void update(float delta) {
        Iterator<Bullet> it = bullets.iterator();
        while (it.hasNext()) {
            Bullet b = it.next();
            b.update(delta);
            if (!b.active) it.remove();
        }
        if (player == null) return; // Defensive: skip collision if player not set
        Iterator<Bullet> bulletIt = bullets.iterator();
        while (bulletIt.hasNext()) {
            Bullet bullet = bulletIt.next();
            if (bullet.collidesWith(player)) {
                bulletIt.remove();
                player.onHit(); // Optionally notify the player of the hit
            }
        }
    }
    
    public void render(Shader shader, SpriteRenderer renderer, Matrix4f projection) {
        for (Bullet b : bullets) b.render(shader, renderer, projection);
    }

    public List<Bullet> getBullets() {
        return bullets;
    }
    public Player getPlayer() {
        return player;
    }
}