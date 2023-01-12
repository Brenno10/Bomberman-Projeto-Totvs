package com.totvs.world;

import com.totvs.main.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class World {
    public static Tile[] tiles;
    public static int WIDTH, HEIGHT;
    public static final int TILE_SIZE = 16;

    public World(String path) {
        try {
            BufferedImage map = ImageIO.read(getClass().getResource(path));

            int[] pixels = new int[map.getWidth() * map.getHeight()];
            tiles = new Tile[map.getWidth() * map.getHeight()];
            WIDTH = map.getWidth();
            HEIGHT = map.getHeight();

            map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());

            for (int xx = 0; xx < map.getWidth(); xx++) {
                for (int yy = 0; yy < map.getHeight(); yy++) {
                    int pixelAtual = pixels[xx + (yy * map.getWidth())];

                    if (pixelAtual == 0xff000000) {
                        // chão / preto
                        tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
                    } else if (pixelAtual == 0xffffffff) {
                        // paredes indestrutivel / branco
                        tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_WALL);
                    } else if (pixelAtual == 0xffff0000) {
                        // parede destrutivel / vermelho
                        tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
                        tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_DEST_WALL);
                    } else if (pixelAtual == 0xff0000ff) {
                        // player / azul
                        tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
                        Game.player.setX(xx * 16);
                        Game.player.setY((yy - 1) * 16);
                    } else {
                        tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isFree(int xNext, int yNext) {
        int x1 = xNext / TILE_SIZE;
        int y1 = yNext / TILE_SIZE;

        int x2 = (xNext + TILE_SIZE - 1) / TILE_SIZE;
        int y2 = xNext / TILE_SIZE;

        int x3 = xNext / TILE_SIZE;
        int y3 = (yNext + TILE_SIZE - 1) / TILE_SIZE;

        int x4 = (xNext + TILE_SIZE - 1) / TILE_SIZE;
        int y4 = (yNext + TILE_SIZE - 1) / TILE_SIZE;

        return !(tiles[x1 +(y1 * World.WIDTH)] instanceof WallTile) ||
                !(tiles[x2 +(y2 * World.WIDTH)] instanceof WallTile) ||
                !(tiles[x3 +(y3 * World.WIDTH)] instanceof WallTile) ||
                !(tiles[x4 +(y4 * World.WIDTH)] instanceof WallTile) ||
                !(tiles[x1 +(y1 * World.WIDTH)] instanceof DestructibleTile) ||
                !(tiles[x2 +(y2 * World.WIDTH)] instanceof DestructibleTile) ||
                !(tiles[x3 +(y3 * World.WIDTH)] instanceof DestructibleTile) ||
                !(tiles[x4 +(y4 * World.WIDTH)] instanceof DestructibleTile);
    }

    public void render(Graphics g) {
        for (int xx = 0; xx < WIDTH; xx++) {
            for (int yy = 0; yy < HEIGHT; yy++) {
                Tile tile = tiles[xx + (yy * WIDTH)];
                tile.render(g);
            }
        }
    }
}
