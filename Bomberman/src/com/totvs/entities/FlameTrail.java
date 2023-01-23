package com.totvs.entities;

import com.totvs.main.Game;

import java.awt.image.BufferedImage;

public class FlameTrail extends Entity {
    public FlameTrail(int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);
    }

    @Override
    public void checkCollision() {
        for (int i = 0; i < Game.entities.size(); i++) {
            if (Game.entities.get(i) instanceof Player) {
                if (Game.entities.get(i).getHitBox().intersects(this.getHitBox())) {

                }
            }
        }
    }
}
