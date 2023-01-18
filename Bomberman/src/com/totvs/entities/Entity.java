package com.totvs.entities;

import com.totvs.main.Game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class Entity {

    protected int x;
    protected int y;
    protected int width;
    protected int height;
    private BufferedImage sprite;
    private Rectangle bounds;

    public Entity(int x, int y, int width, int height, BufferedImage sprite) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.sprite = sprite;

        bounds = new Rectangle(0, 0, 16, 16);
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

    public void destroy() {
        Game.entities.remove(this);
    }

    public Rectangle getCollisionBounds(double xOffset, double yOffset) {
        return new Rectangle((int) (this.getX() + bounds.x + xOffset), (int) (this.getY() + bounds.y + yOffset),
                (int) bounds.getWidth(), (int) bounds.getHeight());
    }

    public boolean checkEntityCollision(double xOffset, double yOffset) {
        List<Entity> e = Game.entities;

        for (int i = 0; i < Game.entities.size(); i++) {
            if (e.get(i).equals(this) && e instanceof Player)
                continue;
            if (e.get(i).getCollisionBounds(0, 0).intersects(getCollisionBounds(xOffset, yOffset)))
                return true;
        }
        return false;
    }

    public void tick() {

    }

    public void render(Graphics g) {
        g.drawImage(sprite, this.getX(), this.getY(), null);
    }
}
