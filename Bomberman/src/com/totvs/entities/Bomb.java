package com.totvs.entities;

import com.totvs.graphics.Spritesheet;
import com.totvs.main.Game;
import com.totvs.world.World;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Bomb extends Entity {
    private int frames = 0, maxFrames = 24, index, maxIndex = 3, timer = 0;
    public BufferedImage[] bombFrames;
    public Player whoPlaced;
    public boolean exploded = false;

    public Bomb(int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);

        bombFrames = new BufferedImage[5];

        for (int i = 0; i < bombFrames.length; i++) {
            bombFrames[i] = Game.bombSprite.getSprite(i * 16, 0, 16, 16);
        }
        bombFrames[4] = Game.bombSprite.getSprite(96, 16, 16, 16);
    }

    public static void placeBomb(int posx, int posy, Spritesheet bombSprite, Player player) {
        int x = posx;
        int y = posy;

        while (x % 16 != 0) {
            if (x % 16 >= 8)
                x++;
            else
                x--;
        }
        while (y % 16 != 0) {
            if (y % 16 >= 8)
                y++;
            else
                y--;
        }

        Bomb bomb = new Bomb(x, y, 16, 16,
                bombSprite.getSprite(0, 0, 16, 16));
        Game.entities.add(bomb);
        bomb.exploded = false;
        bomb.whoPlaced = player;
    }

    public void explode() {
//        for (int x = 1; x < whoPlaced.bombPower + 1; x++) {
//            for (int y = 1; y < whoPlaced.bombPower + 1; y++) {
//                if (whoPlaced.bombPower == x) {
//                    leftExplosionIndex = 0;
//                    rightExplosionIndex = 2;
//                }
//                else {
//                    leftExplosionIndex = 1;
//                    rightExplosionIndex = 1;
//                }
//                leftExplosionPosition = -(x * 16);
//                rightExplosionPosition = (x * 16);
//            }
//        }
//
//        for (int x = 1; x < whoPlaced.bombPower + 1; x++) {
//            for (int y = 1; y < whoPlaced.bombPower + 1; y++) {
//                if (whoPlaced.bombPower == y) {
//                    upExplosionIndex = 0;
//                    downExplosionIndex = 2;
//                }
//                else {
//                    upExplosionIndex = 1;
//                    downExplosionIndex = 1;
//                }
//                upExplosionPosition = -(x * 16);
//                downExplosionPosition = (x * 16);
//            }
//        }

        index = 4;
    }

    public void tick() {
        if (!exploded) {
            frames++;
            timer++;

            // animação
            if (frames == maxFrames && timer < 180) {
                frames = 0;
                index++;
                if (index > maxIndex) {
                    index = 0;
                }
            }

            // timer da bomba
            if (timer >= 180) {
                explode();

                if (timer >= 204) {


                    if (index == this.bombFrames.length - 1) {
                        destroy(this);
                        whoPlaced.placedBombs--;
                        exploded = true;
                        timer = 0;
                        index = 0;
                    }
                }
            }
        }
    }

    public void render(Graphics g) {
        g.drawImage(bombFrames[index], this.getX(), this.getY(), null);
    }
}
