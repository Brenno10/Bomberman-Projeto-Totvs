package com.totvs.world;

import com.totvs.main.Game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Tile {
    public static BufferedImage TILE_DEST_WALL = Game.tilesSpritesheet.getSprite(0, 14, 16, 16);
    public static BufferedImage TILE_WALL = Game.tilesSpritesheet.getSprite(17, 14, 16, 16);
    public static BufferedImage TILE_FLOOR = Game.tilesSpritesheet.getSprite(51, 14, 16, 16);

    private BufferedImage sprite;
    private int x,y;

    public Tile(int x, int y, BufferedImage sprite) {
        this.x = x;
        this.y = y;
        this.sprite = sprite;
    }

    public void render(Graphics g) {
        g.drawImage(sprite, x, y, null);
    }
}
