package engine.entity.Player;

import engine.entity.Player.Player;

public class PlayerRespawnManager {
    private final Player player;
    private float deathTimer = 0;
    private float invulnTimer = 0;
    private boolean isDead = false;
    
    // Configurable values
    private float respawnDelay = 2.0f;
    private float invulnDuration = 3.0f;
    private float respawnX = 640f;
    private float respawnY = 720f;

    public PlayerRespawnManager(Player player) {
        this.player = player;
    }

    public void update(float delta) {
        if (isDead) {
            deathTimer += delta;
            if (deathTimer >= respawnDelay) {
                respawn();
            }
        } else if (invulnTimer > 0) {
            invulnTimer -= delta;
        }
    }

    public void onDeath() {
        isDead = true;
        deathTimer = 0;
        // Drop power here if needed
        // Notify script: onPlayerDeath()
    }

    public void respawn() {
        player.setPosition(respawnX, respawnY);
        player.resetHitState();
        invulnTimer = invulnDuration;
        isDead = false;
        // Notify script: onPlayerRespawn()
    }

    public boolean isInvulnerable() {
        return invulnTimer > 0;
    }

    public void setRespawnPosition(float x, float y) {
        this.respawnX = x;
        this.respawnY = y;
    }

    public void setRespawnDelay(float delay) {
        this.respawnDelay = delay;
    }

    public void setInvulnDuration(float duration) {
        this.invulnDuration = duration;
    }

    public boolean isDead() {
        return isDead;
    }
} 
