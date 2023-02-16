package com.totvs.entities;

import com.totvs.main.Game;
import com.totvs.world.World;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public class Player extends Entity {
    public String name;

    public boolean right, up, left, down;
    public final int downDir = 0, leftDir = 1, upDir = 2, rightDir = 3;
    public int dir = 0;
    public int placedBombs = 0;
    public int maxBombsAmount = 3, bombPower = 1, penetrationPower = 1;
    public int frames = 0, deathAnimation = 14  , index;
    public int[] playerColor;
    public double speed = 2;
    public boolean hitBomb = false, isDead = false;

    private final int maxFrames = 6, maxIndex = 2;
    private boolean moved = false;

    private final BufferedImage[] rightPlayer;
    private final BufferedImage[] leftPlayer;
    private final BufferedImage[] upPlayer;
    private final BufferedImage[] downPlayer;
    private final BufferedImage[] playerDeath;

    public Player(String name, int x, int y, int width, int height, BufferedImage sprite, int[] playerColor) {
        super(x, y, width, height, sprite);
        this.name = name;
        this.playerColor = playerColor;

        rightPlayer = new BufferedImage[3];
        leftPlayer = new BufferedImage[3];
        upPlayer = new BufferedImage[3];
        downPlayer = new BufferedImage[3];
        playerDeath = new BufferedImage[6];

        for (int i = 0; i < rightPlayer.length; i++) {
            rightPlayer[i] = Game.player1Spritesheet.getSprite(i * 16, 39, 16, 26);
            leftPlayer[i] = Game.player1Spritesheet.getSprite(i * 16, 103, 16, 26);
            upPlayer[i] = Game.player1Spritesheet.getSprite(i * 16, 7, 16, 26);
            downPlayer[i] = Game.player1Spritesheet.getSprite(i * 16, 69, 16, 26);
        }
        for (int i = 0; i < playerDeath.length - 1; i++) {
            playerDeath[i] = Game.player1Spritesheet.getSprite(i * 16, 359, 15, 23);
        }
        playerDeath[5] = playerDeath[4];
        playerDeath[4] = playerDeath[2];
    }

    public boolean hasBombs() {
        return placedBombs < maxBombsAmount;
    }

    @Override
    public void checkCollision() {
        hitBomb = false;
        for (int i = 0; i < Game.entities.size(); i++) {
            if (Game.entities.get(i) instanceof Bomb && !((Bomb) Game.entities.get(i)).isOnTop) {
                if (this.getHitBox().intersects(Game.entities.get(i).getHitBox())) {
                    switch (dir) {
                        case 0 -> {
                            if (World.isFree(this.x, this.y -= this.speed))
                                this.y -= this.speed;
                        }
                        case 1 -> {
                            if (World.isFree(this.x += this.speed, this.y))
                                this.x += this.speed;
                        }
                        case 2 -> {
                            if (World.isFree(this.x, this.y += this.speed))
                                this.y += this.speed;
                        }
                        case 3 -> {
                            if (World.isFree(this.x -= this.speed, this.y))
                                this.x -= this.speed;
                        }
                    }
                    hitBomb = true;
                }
            }
        }
    }

    @Override
    public void tick() {
        if (!isDead) {
            this.updateHitbox(3, 2);
            checkCollision();
            moved = false;
            boolean isOnTop = false;

            for (int i = 0; i < Game.entities.size(); i++) {
                if (Game.entities.get(i) instanceof Bomb) {
                    isOnTop = ((Bomb) Game.entities.get(i)).isOnTop;
                }
            }

            if (right && World.isFree((int) (this.getX() + speed), this.getY()) && !hitBomb ||
            right && World.isFree((int) (this.getX() + speed), this.getY()) && isOnTop) {
                moved = true;
                dir = rightDir;
                x += speed;
            } else if (left && World.isFree((int) (this.getX() - speed), this.getY()) && !hitBomb ||
                    left && World.isFree((int) (this.getX() - speed), this.getY()) && isOnTop) {
                moved = true;
                dir = leftDir;
                x -= speed - 0.7;
            }

            if (up && World.isFree(this.getX(), (int) (this.getY() - speed)) && !hitBomb ||
                    up && World.isFree(this.getX(), (int) (this.getY() - speed)) && isOnTop) {
                moved = true;
                dir = upDir;
                y -= speed - 0.7;
            } else if (down && World.isFree(this.getX(), (int) (this.getY() + speed)) && !hitBomb ||
                    down && World.isFree(this.getX(), (int) (this.getY() + speed)) && isOnTop) {
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
            dir = -1;
            frames++;

            if (frames == deathAnimation) {
                frames = 0;
                index++;
                if (index > 2 && index < playerDeath.length - 1)
                    deathAnimation = 9;
                if (index > playerDeath.length - 1)
                    index = 2;
            }
        }
    }

    public void render(Graphics g) {
        switch (dir) {
            case 0 -> g.drawImage(downPlayer[index], this.getX(), this.getY() - 10, null);
            case 1 -> g.drawImage(leftPlayer[index], this.getX(), this.getY() - 10, null);
            case 2 -> g.drawImage(upPlayer[index], this.getX(), this.getY() - 10, null);
            case 3 -> g.drawImage(rightPlayer[index], this.getX(), this.getY() - 10, null);
            default -> g.drawImage(playerDeath[index], this.getX(), this.getY() - 10, null);
        }
    }
}
