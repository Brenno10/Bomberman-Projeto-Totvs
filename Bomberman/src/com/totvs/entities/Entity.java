package com.totvs.entities;

import com.totvs.main.Game;
import com.totvs.world.World;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Entity {

    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected BufferedImage sprite;
    protected Rectangle hitBox;

    public Entity(int x, int y, int width, int height, BufferedImage sprite) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.sprite = sprite;
        this.hitBox = new Rectangle(x, y, width, height);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public BufferedImage getSprite() {
        return sprite;
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public void drawHitbox(Graphics g) {
        // for debbug
        g.setColor(Color.RED);
        g.drawRect(hitBox.x, hitBox.y, hitBox.width, hitBox.height);
    }

    public void updateHitbox(int xOffSet, int yOffSet) {
        hitBox.x = x + xOffSet;
        hitBox.y = y + yOffSet;
    }

    public void checkCollision() {

    }

    public void destroy() {
        Game.entities.remove(this);
    }

    public void destroyHitBox() {
        this.hitBox.width = 0;
        this.hitBox.height = 0;
    }

    public void tick() {
    }

    public void render(Graphics g) {
        g.drawImage(sprite, this.getX(), this.getY(), null);
    }
}
