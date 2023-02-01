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
    private boolean iExploded = false;
    private boolean upFree = true, downFree = true, leftFree = true, rightFree = true;
    protected boolean canBePlaced = true;
    protected boolean isOnTop = false;

    private BufferedImage currentFrame;
    private final BufferedImage[] bombFrames;
    private final BufferedImage[] explosionFrames;

    private final BufferedImage[] horizontalFlameTrail;
    private final BufferedImage[] leftFlameTrailTip;
    private final BufferedImage[] rightFlameTrailTip;

    private final BufferedImage[] verticalFlameTrail;
    private final BufferedImage[] upFlameTrailTip;
    private final BufferedImage[] downFlameTrailTip;

    private final List<FlameTrail> flameTrail;
    private FlameTrail explosionCenter;

    public Bomb(int x, int y, int width, int height, BufferedImage sprite, int[] bombColor) {
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
            bombFrames[i] =
                    Game.bombSprite.getSprite(bombColor[0] + (i * 16), bombColor[1], 16, 16);
        }

        // frames da explosão
        for (int i = 0; i < explosionFrames.length - 7; i++) {
            explosionFrames[i] =
                    Game.bombSprite.getSprite(bombColor[0] + 96, bombColor[1] + (i * 16) + 16, 16, 16);
        }
        for (int i = 0; i < explosionFrames.length - 5; i++) {
            explosionFrames[i + 5] =
                    Game.bombSprite.getSprite(bombColor[0] + 112, bombColor[1] + (i * 16) + 16, 16, 16);
        }
        explosionFrames[10] =
                Game.bombSprite.getSprite(bombColor[0] + 64, bombColor[1] + 112, 16, 16);
        explosionFrames[11] =
                Game.bombSprite.getSprite(80, 112, 16, 16);

        // sprites da trilha de fogo horizontal
        for (int i = 0; i < horizontalFlameTrail.length; i++) {
            horizontalFlameTrail[i] =
                    Game.bombSprite.getSprite(bombColor[0] + 80, bombColor[1] + (i * 16) + 16, 16, 16);
        }
        for (int i = 0; i < leftFlameTrailTip.length; i++) {
            leftFlameTrailTip[i] =
                    Game.bombSprite.getSprite(bombColor[0] + 32, bombColor[1] + (i * 16) + 16, 16, 16);
        }
        for (int i = 0; i < rightFlameTrailTip.length; i++) {
            rightFlameTrailTip[i] =
                    Game.bombSprite.getSprite(bombColor[0] + 48, bombColor[1] + (i * 16) + 16, 16, 16);
        }

        // sprites da trilha de fogo vertical
        for (int i = 0; i < verticalFlameTrail.length; i++) {
            verticalFlameTrail[i] =
                    Game.bombSprite.getSprite(bombColor[0] + 64, bombColor[1] + (i * 16) + 16, 16, 16);
        }
        for (int i = 0; i < upFlameTrailTip.length; i++) {
            upFlameTrailTip[i] =
                    Game.bombSprite.getSprite(bombColor[0], bombColor[1] + (i * 16) + 16, 16, 16);
        }
        for (int i = 0; i < downFlameTrailTip.length; i++) {
            downFlameTrailTip[i] =
                    Game.bombSprite.getSprite(bombColor[0] + 16, bombColor[1] + (i * 16) + 16, 16, 16);
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
                bombSprite.getSprite(0, 0, 16, 16), player.playerColor);
        bomb.whoPlaced = player;
        bomb.checkCollision();

        if (bomb.canBePlaced) {
            Game.entities.add(bomb);
            bomb.isOnTop = true;
            bomb.whoPlaced.placedBombs++;
        } else
            bomb.destroy();
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }

    public void explode() {
        int power = whoPlaced.bombPower;
        if (power >= 4)
            power = 4;

        explosionCenter = new FlameTrail(this.getX(), this.getY(), 16, 16, null);
        this.flameTrail.add(explosionCenter);

        for (int xx = 1; xx <= whoPlaced.bombPower + 1; xx++) {
            if (xx <= whoPlaced.bombPower) {
                if (World.isFree(this.getX() + xx * 16, this.getY()) && rightFree) {
                    flameTrail.add(new FlameTrail(this.getX() + xx * 16, this.getY(),
                            16, 16, horizontalFlameTrail[power]));
                } else {
                    if (World.whichTileWasHit(this.getX() + xx * 16, this.getY()) == 1 && rightFree) {
                        World.removeTile(this.getX() + xx * 16, this.getY());
                    }
                    rightFree = false;
                }
                if (World.isFree(this.getX() - xx * 16, this.getY()) && leftFree) {
                    flameTrail.add(new FlameTrail(this.getX() - xx * 16, this.getY(),
                            16, 16, horizontalFlameTrail[power]));
                } else {
                    if (World.whichTileWasHit(this.getX() - xx * 16, this.getY()) == 1 && leftFree){
                        World.removeTile(this.getX() - xx * 16, this.getY());
                    }
                    leftFree = false;
                }
            } else {
                if (World.isFree(this.getX() + xx * 16, this.getY()) && rightFree) {
                    flameTrail.add(new FlameTrail(this.getX() + xx * 16, this.getY(),
                            16, 16, rightFlameTrailTip[power]));
                } else {
                    if (World.whichTileWasHit(this.getX() + xx * 16, this.getY()) == 1 && rightFree){
                        World.removeTile(this.getX() + xx * 16, this.getY());
                    }
                    rightFree = false;
                }
                if (World.isFree(this.getX() - xx * 16, this.getY()) && leftFree) {
                    flameTrail.add(new FlameTrail(this.getX() - xx * 16, this.getY(),
                            16, 16, leftFlameTrailTip[power]));
                } else {
                    if (World.whichTileWasHit(this.getX() - xx * 16, this.getY()) == 1 && leftFree){
                        World.removeTile(this.getX() - xx * 16, this.getY());
                    }
                    leftFree = false;
                }
            }
        }

        for (int yy = 1; yy <= whoPlaced.bombPower + 1; yy++) {
            if (yy <= whoPlaced.bombPower) {
                if (World.isFree(this.getX(), this.getY() + yy * 16) && downFree) {
                    flameTrail.add(new FlameTrail(this.getX(), this.getY() + yy * 16,
                            16, 16, verticalFlameTrail[power]));
                } else {
                    if (World.whichTileWasHit(this.getX(), this.getY() + yy * 16) == 1 && downFree){
                        World.removeTile(this.getX(), this.getY() + yy * 16);
                    }
                    downFree = false;
                }
                if (World.isFree(this.getX(), this.getY() - yy * 16) && upFree) {
                    flameTrail.add(new FlameTrail(this.getX(), this.getY() - yy * 16,
                            16, 16, verticalFlameTrail[power]));
                } else {
                    if (World.whichTileWasHit(this.getX(), this.getY() - yy * 16) == 1 && upFree){
                        World.removeTile(this.getX(), this.getY() - yy * 16);
                    }
                    upFree = false;
                }
            } else {
                if (World.isFree(this.getX(), this.getY() + yy * 16) && downFree) {
                    flameTrail.add(new FlameTrail(this.getX(), this.getY() + yy * 16,
                            16, 16, downFlameTrailTip[power]));
                } else {
                    if (World.whichTileWasHit(this.getX(), this.getY() + yy * 16) == 1 && downFree){
                        World.removeTile(this.getX(), this.getY() + yy * 16);
                    }
                    downFree = false;
                }
                if (World.isFree(this.getX(), this.getY() - yy * 16) && upFree) {
                    flameTrail.add(new FlameTrail(this.getX(), this.getY() - yy * 16,
                            16, 16, upFlameTrailTip[power]));
                } else {
                    if (World.whichTileWasHit(this.getX(), this.getY() - yy * 16) == 1 && upFree){
                        World.removeTile(this.getX(), this.getY() - yy * 16);
                    }
                    upFree = false;
                }
            }
        }

        iExploded = true;
        this.destroyHitBox();
        index = power;
    }

    @Override
    public void checkCollision() {
        if (canBePlaced) {
            for (Entity e : Game.entities) {
                if (this.getHitBox().x == e.getHitBox().x && this.getHitBox().y == e.getHitBox().y) {
                    canBePlaced = false;
                    return;
                }
            }
        }
        if (this.isOnTop)
            this.isOnTop = whoPlaced.getHitBox().intersects(this.getHitBox());
    }

    @Override
    public void tick() {
        this.updateHitbox(0, 0);
        this.checkCollision();

        for (int i = 0; i < flameTrail.size(); i++) {
            flameTrail.get(i).checkCollision();
        }
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
            explosionCenter.destroy();
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
                timer = 0;
                index = 0;
            }
        }
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(currentFrame, this.getX(), this.getY(), null);
        if (this.iExploded) {
            for (FlameTrail trail : flameTrail) {
                g.drawImage(trail.getSprite(), trail.getX(), trail.getY(), null);
            }
        }
//        this.drawHitbox(g);
//        for (int i = 0; i < flameTrail.size(); i++) {
//            flameTrail.get(i).drawHitbox(g);
//        }
    }
}
