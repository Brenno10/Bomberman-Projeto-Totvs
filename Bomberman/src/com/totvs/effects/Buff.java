package com.totvs.effects;

import com.totvs.entities.Player;
import com.totvs.graphics.Spritesheet;
import com.totvs.main.Game;

import java.awt.image.BufferedImage;

public class Buff extends Effect {
    public Buff(int x, int y, int width, int height, BufferedImage sprite, int effect, int amount) {
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
        System.out.println("sou um buff");
        switch (effect) {
            case 0 -> player.bombPower += amount;
            case 1 -> player.maxBombsAmount += amount;
            case 3 -> player.penetrationPower += amount;
        }
    }
}
