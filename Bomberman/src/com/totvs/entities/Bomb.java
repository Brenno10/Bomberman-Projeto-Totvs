package com.totvs.entities;

import com.totvs.graphics.Spritesheet;
import com.totvs.main.Game;
import com.totvs.world.World;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Bomb extends Entity {
    private int frames = 0, index, timer = 0;
    private final int  maxFrames = 24, maxIndex = 3, maxTimer = 160, maxExFrames = 3;
    private Player whoPlaced;
    public boolean exploded = false;
    public boolean iExploded = false;

    private BufferedImage currentFrame;
    private final BufferedImage[] bombFrames;
    private final BufferedImage[] explosionFrames;

    private final BufferedImage[] horizontalFlameTrail;
    private final BufferedImage[] leftFlameTrailTip;
    private final BufferedImage[] rightFlameTrailTip;

    private final BufferedImage[] verticalFlameTrail;
    private final BufferedImage[] upFlameTrailTip;
    private final BufferedImage[] downFlameTrailTip;

    private List<FlameTrail> flameTrail;

    public Bomb(int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);

        bombFrames = new BufferedImage[4];
        explosionFrames = new BufferedImage[12];

        horizontalFlameTrail = new BufferedImage[5];
        leftFlameTrailTip = new BufferedImage[5];
        rightFlameTrailTip = new BufferedImage[5];

        verticalFlameTrail = new BufferedImage[5];
        upFlameTrailTip = new BufferedImage[5];
        downFlameTrailTip = new BufferedImage[5];

        flameTrail = new ArrayList<>();

        // frames da bomba
        for (int i = 0; i < bombFrames.length; i++) {
            bombFrames[i] = Game.bombSprite.getSprite(i * 16, 0, 16, 16);
        }

        // frames da explosão
        for (int i = 0; i < explosionFrames.length - 7; i++) {
            explosionFrames[i] = Game.bombSprite.getSprite(96, (i * 16) + 16, 16, 16);
        }
        for (int i = 0; i < explosionFrames.length - 5; i++) {
            explosionFrames[i + 5] = Game.bombSprite.getSprite(112, (i * 16) + 16, 16, 16);
        }
        explosionFrames[10] = Game.bombSprite.getSprite(64, 112, 16, 16);
        explosionFrames[11] = Game.bombSprite.getSprite(80, 112, 16, 16);

        // sprites da trilha de fogo horizontal
        for (int i = 0; i < horizontalFlameTrail.length; i++) {
            horizontalFlameTrail[i] = Game.bombSprite.getSprite(80, (i * 16) + 16, 16, 16);
        }
        for (int i = 0; i < leftFlameTrailTip.length; i++) {
            leftFlameTrailTip[i] = Game.bombSprite.getSprite(32, (i * 16) + 16, 16, 16);
        }
        for (int i = 0; i < rightFlameTrailTip.length; i++) {
            rightFlameTrailTip[i] = Game.bombSprite.getSprite(48, (i * 16) + 16, 16, 16);
        }

        // sprites da trilha de fogo vertical
        for (int i = 0; i < verticalFlameTrail.length; i++) {
            verticalFlameTrail[i] = Game.bombSprite.getSprite(64, (i * 16) + 16, 16, 16);
        }
        for (int i = 0; i < upFlameTrailTip.length; i++) {
            upFlameTrailTip[i] = Game.bombSprite.getSprite(0, (i * 16) + 16, 16, 16);
        }
        for (int i = 0; i < downFlameTrailTip.length; i++) {
            downFlameTrailTip[i] = Game.bombSprite.getSprite(16, (i * 16) + 16, 16, 16);
        }

        // frame atual da animação
        currentFrame = bombFrames[0];
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
        int power = whoPlaced.bombPower;
        if (power >= 4)
            power = 4;

        for (int xx = 1; xx <= power + 1; xx++) {
            if (xx <= power) {
                if (World.isFree(this.getX() + xx * 16, this.getY()))
                    flameTrail.add(new FlameTrail(this.getX() + xx * 16, this.getY(),
                            16, 16, horizontalFlameTrail[power]));
                if (World.isFree(this.getX() - xx * 16, this.getY()))
                    flameTrail.add(new FlameTrail(this.getX() - xx * 16, this.getY(),
                            16, 16, horizontalFlameTrail[power]));
            } else {
                if (World.isFree(this.getX() + xx * 16, this.getY()))
                    flameTrail.add(new FlameTrail(this.getX() + xx * 16, this.getY(),
                            16, 16, rightFlameTrailTip[power]));
                if (World.isFree(this.getX() - xx * 16, this.getY()))
                    flameTrail.add(new FlameTrail(this.getX() - xx * 16, this.getY(),
                            16, 16, leftFlameTrailTip[power]));
            }
        }

        for (int yy = 1; yy <= power + 1; yy++) {
            if (yy <= power) {
                if (World.isFree(this.getX(), this.getY() + yy * 16))
                    flameTrail.add(new FlameTrail(this.getX(), this.getY() + yy * 16,
                            16, 16, verticalFlameTrail[power]));
                if (World.isFree(this.getX(), this.getY() - yy * 16))
                    flameTrail.add(new FlameTrail(this.getX(), this.getY() - yy * 16,
                            16, 16, verticalFlameTrail[power]));
            } else {
                if (World.isFree(this.getX(), this.getY() + yy * 16))
                    flameTrail.add(new FlameTrail(this.getX(), this.getY() + yy * 16,
                        16, 16, downFlameTrailTip[power]));
                if (World.isFree(this.getX(), this.getY() - yy * 16))
                    flameTrail.add(new FlameTrail(this.getX(), this.getY() - yy * 16,
                        16, 16, upFlameTrailTip[power]));
            }
        }

        iExploded = true;
        index = power;
    }

    public void tick() {
        if (!exploded) {
            frames++;
            timer++;

            // animação
            if (frames == maxFrames && timer < maxTimer) {
                currentFrame = bombFrames[index];
                frames = 0;
                index++;

                if (index > maxIndex) {
                    index = 0;
                }
            }

            // timer da bomba
            if (timer >= maxTimer && timer < maxTimer + maxExFrames + 8) {
                explode();
                currentFrame = explosionFrames[index];
                frames = 0;
            }

            if (timer == maxTimer + maxExFrames + 8) {
                this.flameTrail.clear();
                index = 5;
                frames = 0;
            }

            if (timer >= maxTimer + maxExFrames) {
                currentFrame = explosionFrames[index];
                if (frames == maxExFrames) {
                    index++;
                    frames = 0;
                }

                if (index == this.explosionFrames.length - 1) {
                    this.destroy();
                    whoPlaced.placedBombs--;
                    exploded = true;
                    timer = 0;
                    index = 0;
                }
            }
        }
    }

    public void render(Graphics g) {
        g.drawImage(currentFrame, this.getX(), this.getY(), null);
        if (this.iExploded) {
            for (int i = 0; i < flameTrail.size(); i++) {
                g.drawImage(flameTrail.get(i).getSprite(), flameTrail.get(i).getX(), flameTrail.get(i).getY(), null);
            }
        }
    }
}
