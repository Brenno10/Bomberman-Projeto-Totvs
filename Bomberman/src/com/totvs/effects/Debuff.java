package com.totvs.effects;

import com.totvs.entities.Player;
import com.totvs.graphics.Spritesheet;
import com.totvs.main.Game;

import java.awt.image.BufferedImage;

public class Debuff extends Effect {
    public Debuff(int x, int y, int width, int height, BufferedImage sprite, int effect, int amount) {
        super(x, y, width, height, sprite, effect, amount);
    }

    public static void create(int xx, int yy, int effect, int amount) {
        Buff buff = new Buff(xx, yy, 16, 16,
                new Spritesheet("/mais_bombas_test.png").getSprite(0, 0, 16, 16),
                effect, amount);
        Game.entities.add(buff);
    }

    @Override
    public void pickUp(Player player) {
//        System.out.println("sou um debuff");
        switch (effect) {
            case 0 -> {
                if (player.bombPower - amount > 0)
                    player.bombPower -= amount;
                else
                    player.bombPower = 1;
            }
            case 1 -> {
                if (player.maxBombsAmount - amount > 0)
                    player.maxBombsAmount -= amount;
                else
                    player.maxBombsAmount = 1;
            }
            case 3 -> {
                if (player.penetrationPower - amount > 0)
                    player.penetrationPower -= amount;
                else
                    player.penetrationPower = 0;
            }
        }
    }
}
