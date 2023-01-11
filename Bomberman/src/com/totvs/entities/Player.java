package com.totvs.entities;

import com.totvs.main.Game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Entity {
    public boolean right, up, left, down;
    public final int downDir = 0, leftDir = 1, upDir = 2, rightDir = 3;
    public int dir = 0;
    public double speed = 2.7;

    private int frames = 0, maxFrames = 6, index, maxIndex = 2;
    private boolean moved = false;
    private BufferedImage[] rightPlayer;
    private BufferedImage[] leftPlayer;
    private BufferedImage[] upPlayer;
    private BufferedImage[] downPlayer;

    public Player(int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);

        rightPlayer = new BufferedImage[3];
        leftPlayer = new BufferedImage[3];
        upPlayer = new BufferedImage[3];
        downPlayer = new BufferedImage[3];

        rightPlayer[0] = Game.spritesheet.getSprite(0, 37, 15, 25);
        rightPlayer[1] = Game.spritesheet.getSprite(16, 38, 15, 24);
        rightPlayer[2] = Game.spritesheet.getSprite(33, 39, 15, 23);

        leftPlayer[0] = Game.spritesheet.getSprite(0, 101, 15, 25);
        leftPlayer[1] = Game.spritesheet.getSprite(16, 103, 15, 23);
        leftPlayer[2] = Game.spritesheet.getSprite(32, 102, 15, 24);

        upPlayer[0] = Game.spritesheet.getSprite(0, 7, 14, 26);
        upPlayer[1] = Game.spritesheet.getSprite(16, 7, 14, 26);
        upPlayer[2] = Game.spritesheet.getSprite(32, 7, 14, 26);

        downPlayer[0] = Game.spritesheet.getSprite(0, 69, 15, 25);
        downPlayer[1] = Game.spritesheet.getSprite(16, 69, 14, 25);
        downPlayer[2] = Game.spritesheet.getSprite(32, 69, 14, 26);
    }

    @Override
    public void tick() {
        moved = false;
        if (right) {
            moved = true;
            dir = rightDir;
            x += speed;
        } else if (left) {
            moved = true;
            dir = leftDir;
            x -= speed - 0.7;
        }

        if (up) {
            moved = true;
            dir = upDir;
            y -= speed - 0.7;
        } else if (down) {
            moved = true;
            dir = downDir;
            y += speed;
        }

        if (moved) {
            frames++;
            if (frames == maxFrames) {
                frames = 0;
                index++;
                if (index > maxIndex) {
                    index = 0;
                }
            }
        }
        else {
            index = 0;
        }
    }

    public void render(Graphics g) {
        switch (dir) {
            case 0 -> g.drawImage(downPlayer[index], this.getX(), this.getY(), null);
            case 1 -> g.drawImage(leftPlayer[index], this.getX(), this.getY(), null);
            case 2 -> g.drawImage(upPlayer[index], this.getX(), this.getY(), null);
            case 3 -> g.drawImage(rightPlayer[index], this.getX(), this.getY(), null);
        }
    }
}
