package com.totvs.entities;

import com.totvs.graphics.Spritesheet;
import com.totvs.main.Game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Bomb extends Entity {
    private static int frames = 0, maxFrames = 24, index, maxIndex = 3, timer = 0;
    public static BufferedImage[] bombFrames;
    public static Player whoPlaced;
    public static boolean exploded = false;

    public Bomb(int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);
        bombFrames = new BufferedImage[5];
    }

    public static void placeBomb(int posx, int posy, Spritesheet bombSprite, Player player) {
        exploded = false;
        whoPlaced = player;

        Bomb bomb = new Bomb(posx, posy, 16, 16,
                bombSprite.getSprite(0, 0, 16, 16));
        Game.entities.add(bomb);

        for (int i = 0; i < bombFrames.length; i++) {
            bombFrames[i] = Game.bombSprite.getSprite(i * 16, 0, 16, 16);
        }
        bombFrames[4] = Game.bombSprite.getSprite(96, 16, 16, 16);
    }

    public void explode() {
        index = 4;
    }

    public void tick() {
        if (!exploded) {
            frames++;
            timer++;

            // animação
            if (frames == maxFrames) {
                frames = 0;
                index++;
                if (index > maxIndex) {
                    index = 0;
                }
            }

            // timer da bomba
            if (timer >= 180) {
                explode();
                if (timer == 204) {
                    destroy(this);
                    whoPlaced.placedBombs--;
                    exploded = true;
                    timer = 0;
                    index = 0;
                }
            }
        }
        else {
            index = 0;
        }
    }

    public void render(Graphics g) {
        g.drawImage(bombFrames[index], this.x, this.y, null);
    }
}
