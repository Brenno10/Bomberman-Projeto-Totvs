package com.totvs.entities;

import com.totvs.main.Game;
import com.totvs.world.World;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Entity {
    public boolean right, up, left, down;
    public final int downDir = 0, leftDir = 1, upDir = 2, rightDir = 3;
    public int dir = 0;
    public double speed = 2;
    public int maxBombsAmount = 3;
    public int placedBombs = 0;
    public int bombPower = 1;
    public boolean isDead = false;
    public int frames = 0, index, deathIndex;

    private boolean hitBomb = false;
    private final int maxFrames = 6, maxIndex = 2, maxDeathIndex = 4;
    private boolean moved = false;

    private final BufferedImage[] rightPlayer;
    private final BufferedImage[] leftPlayer;
    private final BufferedImage[] upPlayer;
    private final BufferedImage[] downPlayer;
    private final BufferedImage[] playerDeath;

    public Player(int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);

        rightPlayer = new BufferedImage[3];
        leftPlayer = new BufferedImage[3];
        upPlayer = new BufferedImage[3];
        downPlayer = new BufferedImage[3];
        playerDeath = new BufferedImage[5];

        for (int i = 0; i < rightPlayer.length; i++) {
            rightPlayer[i] = Game.player1Spritesheet.getSprite(i * 16, 39, 16, 26);
            leftPlayer[i] = Game.player1Spritesheet.getSprite(i * 16, 103, 16, 26);
            upPlayer[i] = Game.player1Spritesheet.getSprite(i * 16, 7, 16, 26);
            downPlayer[i] = Game.player1Spritesheet.getSprite(i * 16, 69, 16, 26);
        }
        for (int i = 0; i < playerDeath.length; i++) {
            playerDeath[i] = Game.player1Spritesheet.getSprite(i * 16, 139, 16, 26);
        }
    }

    public boolean hasBombs() {
        return placedBombs < maxBombsAmount;
    }

    public void deathAnimationLoop() {
        if (frames == 30) {
            
        }
    }

    @Override
    public void checkCollision() {
        hitBomb = false;
        for (int i = 0; i < Game.entities.size(); i++) {
            if (Game.entities.get(i) instanceof Bomb)
                if (this.getHitBox().intersects(Game.entities.get(i).getHitBox()))
                    hitBomb = true;
        }
    }

    @Override
    public void tick() {
        if (!isDead) {
            this.updateHitbox(3, 3);
            checkCollision();
            moved = false;

            if (right && World.isFree((int) (this.getX() + speed), this.getY()) && !hitBomb) {
                moved = true;
                dir = rightDir;
                x += speed;
            } else if (left && World.isFree((int) (this.getX() - speed), this.getY())) {
                moved = true;
                dir = leftDir;
                x -= speed - 0.7;
            }

            if (up && World.isFree(this.getX(), (int) (this.getY() - speed))) {
                moved = true;
                dir = upDir;
                y -= speed - 0.7;
            } else if (down && World.isFree(this.getX(), (int) (this.getY() + speed))) {
                moved = true;
                dir = downDir;
                y += speed;
            }

            // animação do jogador
            if (moved) {
                frames++;
                if (frames == maxFrames) {
                    frames = 0;
                    index++;
                    if (index > maxIndex) {
                        index = 0;
                    }
                }
            } else {
                index = 0;
            }
        }
        else {
            frames++;

        }deathAnimationLoop();
    }

    public void render(Graphics g) {
        switch (dir) {
            case 0 -> g.drawImage(downPlayer[index], this.getX(), this.getY() - 10, null);
            case 1 -> g.drawImage(leftPlayer[index], this.getX(), this.getY() - 10, null);
            case 2 -> g.drawImage(upPlayer[index], this.getX(), this.getY() - 10, null);
            case 3 -> g.drawImage(rightPlayer[index], this.getX(), this.getY() - 10, null);
            default -> g.drawImage(playerDeath[index], this.getX(), this.getY() - 10, null);
        }
        this.drawHitbox(g);
    }
}
