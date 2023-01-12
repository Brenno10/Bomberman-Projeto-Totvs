package com.totvs.world;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class World {
    private Tile[] tiles;
    public static int WIDTH, HEIGHT;

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
                        tiles[xx + (yy * WIDTH)] = new FloorTile((1 + xx) * 16, (1 + yy) * 16, Tile.TILE_FLOOR);
                    } else if (pixelAtual == 0xffffffff) {
                        // paredes indestrutivel / branco
                        tiles[xx + (yy * WIDTH)] = new FloorTile((1 + xx) * 16, (1 + yy) * 16, Tile.TILE_WALL);
                    } else if (pixelAtual == 0xffff0000) {
                        // parede destrutivel / vermelho
                        tiles[xx + (yy * WIDTH)] = new FloorTile((1 + xx) * 16, (1 + yy) * 16, Tile.TILE_DEST_WALL);
                    } else if (pixelAtual == 0xff0000ff) {
                        // player
                        tiles[xx + (yy * WIDTH)] = new FloorTile((1 + xx) * 16, (1 + yy) * 16, Tile.TILE_FLOOR);
                    } else {
                        tiles[xx + (yy * WIDTH)] = new FloorTile((1 + xx) * 16, (1 + yy) * 16, Tile.TILE_FLOOR);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
