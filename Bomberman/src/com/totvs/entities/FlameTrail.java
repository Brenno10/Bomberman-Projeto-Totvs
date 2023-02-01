package com.totvs.entities;

import com.totvs.main.Game;

import java.awt.image.BufferedImage;

public class FlameTrail extends Entity {
    public FlameTrail(int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);
    }

    @Override
    public void tick() {

    }

    @Override
    public void checkCollision() {
        for (int i = 0; i < Game.entities.size(); i++) {
            if (Game.entities.get(i) instanceof Player) {
                if (Game.entities.get(i).getHitBox().intersects(this.getHitBox())) {
                    ((Player) Game.entities.get(i)).frames = 0;
                    ((Player) Game.entities.get(i)).index = 0;
                    ((Player) Game.entities.get(i)).isDead = true;
                }
            }
            if (Game.entities.get(i) instanceof Bomb && !((Bomb) Game.entities.get(i)).isOnTop) {
                if (this.getHitBox().intersects(Game.entities.get(i).getHitBox()))
                    ((Bomb) Game.entities.get(i)).setTimer(160);
            }
        }
    }
}
