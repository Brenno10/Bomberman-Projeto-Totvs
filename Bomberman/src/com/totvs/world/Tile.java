package com.totvs.world;

import com.totvs.main.Game;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Tile {
    public static BufferedImage TILE_DEST_WALL = Game.tilesSpritesheet.getSprite(0, 14, 16, 16);
    public static BufferedImage TILE_WALL = Game.tilesSpritesheet.getSprite(17, 14, 16, 16);
    public static BufferedImage TILE_FLOOR = Game.tilesSpritesheet.getSprite(51, 14, 16, 16);

    protected BufferedImage sprite;
    protected int x,y;

    public Tile(int x, int y, BufferedImage sprite) {
        this.x = x;
        this.y = y;
        this.sprite = sprite;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public BufferedImage getSprite() {
        return sprite;
    }

    public void destroy() {
        for (int i = 0; i < World.tiles.length; i++) {
            if (World.tiles[i].getX() == x && World.tiles[i].getY() == y) {
                World.tiles[i] = new FloorTile(this.getX(), this.getY(), Tile.TILE_FLOOR);
            }
        }
    }

    public void tick() {

    }

    public void render(Graphics g) {
        g.drawImage(sprite, x, y, null);
    }
}
