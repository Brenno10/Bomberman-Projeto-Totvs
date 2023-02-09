package com.totvs.effects;

import com.totvs.entities.Entity;
import com.totvs.entities.Player;
import com.totvs.main.Game;

import java.awt.image.BufferedImage;

public abstract class Effect extends Entity {
    protected int effect;
    protected int amount;

    public Effect(int x, int y, int width, int height, BufferedImage sprite, int effect, int amount) {
        super(x, y, width, height, sprite);
        this.effect = effect;
        this.amount = amount;
    }

    public void pickUp(Player player) {

    }

    @Override
    public void tick() {
        for (int i = 0; i < Game.entities.size(); i++) {
            if (Game.entities.get(i) instanceof Player) {
                if (Game.entities.get(i).getHitBox().intersects(this.hitBox)) {
                    this.pickUp((Player) Game.entities.get(i));
                    this.destroy();
                }
            }
        }
    }
}
