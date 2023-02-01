package com.totvs.world;

import com.totvs.entities.Player;
import com.totvs.main.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class World {
    public static Tile[] tiles;
    public static int WIDTH, HEIGHT;
    public static final int TILE_SIZE = 16;
    public static List<Player> players = new ArrayList<>();
    public static int playerCount = 0;
    public static BufferedImage map;

    public World(String path) {
        try {
            map = ImageIO.read(Objects.requireNonNull(getClass().getResource(path)));
            players.add(Game.player1);
            players.add(Game.player2);

            int[] pixels = new int[map.getWidth() * map.getHeight()];
            tiles = new Tile[map.getWidth() * map.getHeight()];
            WIDTH = map.getWidth();
            HEIGHT = map.getHeight();

            map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());

            // cria o mapa e coloca o jogador nele usando pixels coloridos (preto, branco, vermelho, azul)
            for (int xx = 0; xx < map.getWidth(); xx++) {
                for (int yy = 0; yy < map.getHeight(); yy++) {
                    int pixelAtual = pixels[xx + (yy * map.getWidth())];

                    if (pixelAtual == 0xff000000)
                        // chão / preto
                        tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
                    else if (pixelAtual == 0xffffffff)
                        // paredes indestrutivel / branco
                        tiles[xx + (yy * WIDTH)] = new UndestructibleTile(xx * 16, yy * 16, Tile.TILE_WALL);
                    else if (pixelAtual == 0xffff0000) {
                        // parede destrutivel / vermelho
                        tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
                        tiles[xx + (yy * WIDTH)] = new DestructibleTile(xx * 16, yy * 16, Tile.TILE_DEST_WALL);
                    } else if (pixelAtual == 0xff0000ff && players.size() > playerCount) {
                        // player / azul
                        tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
                        players.get(playerCount).setX(xx * 16);
                        players.get(playerCount).setY(yy * 16);
                        playerCount++;
                    } else
                        tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // verifica se ha colisões em blocos do mapa
    public static boolean isFree(int xNext, int yNext) {
        try {
            int x1 = xNext / TILE_SIZE;
            int y1 = yNext / TILE_SIZE;

            int x2 = (xNext + TILE_SIZE - 1) / TILE_SIZE;
            int y2 = yNext / TILE_SIZE;

            int x3 = xNext / TILE_SIZE;
            int y3 = (yNext + TILE_SIZE - 1) / TILE_SIZE;

            int x4 = (xNext + TILE_SIZE - 1) / TILE_SIZE;
            int y4 = (yNext + TILE_SIZE - 1) / TILE_SIZE;

            return !((tiles[x1 + (y1 * World.WIDTH)] instanceof UndestructibleTile) ||
                    (tiles[x2 + (y2 * World.WIDTH)] instanceof UndestructibleTile) ||
                    (tiles[x3 + (y3 * World.WIDTH)] instanceof UndestructibleTile) ||
                    (tiles[x4 + (y4 * World.WIDTH)] instanceof UndestructibleTile) ||
                    (tiles[x1 + (y1 * World.WIDTH)] instanceof DestructibleTile) ||
                    (tiles[x2 + (y2 * World.WIDTH)] instanceof DestructibleTile) ||
                    (tiles[x3 + (y3 * World.WIDTH)] instanceof DestructibleTile) ||
                    (tiles[x4 + (y4 * World.WIDTH)] instanceof DestructibleTile));
        } catch (Exception e) {
            return false;
        }
    }

    public static int whichTileWasHit(int xNext, int yNext) {
        try {
            int x1 = xNext / TILE_SIZE;
            int y1 = yNext / TILE_SIZE;

            int x2 = (xNext + TILE_SIZE - 1) / TILE_SIZE;
            int y2 = yNext / TILE_SIZE;

            int x3 = xNext / TILE_SIZE;
            int y3 = (yNext + TILE_SIZE - 1) / TILE_SIZE;

            int x4 = (xNext + TILE_SIZE - 1) / TILE_SIZE;
            int y4 = (yNext + TILE_SIZE - 1) / TILE_SIZE;

            if ((tiles[x1 + (y1 * World.WIDTH)] instanceof UndestructibleTile) ||
                    (tiles[x2 + (y2 * World.WIDTH)] instanceof UndestructibleTile) ||
                    (tiles[x3 + (y3 * World.WIDTH)] instanceof UndestructibleTile) ||
                    (tiles[x4 + (y4 * World.WIDTH)] instanceof UndestructibleTile)) {
                return 0;
            }
            if ((tiles[x1 + (y1 * World.WIDTH)] instanceof DestructibleTile) ||
                    (tiles[x2 + (y2 * World.WIDTH)] instanceof DestructibleTile) ||
                    (tiles[x3 + (y3 * World.WIDTH)] instanceof DestructibleTile) ||
                    (tiles[x4 + (y4 * World.WIDTH)] instanceof DestructibleTile)) {
                return 1;
            }
        } catch (Exception e) {
            return -1;
        }
        return -1;
    }

    public static void removeTile(int x, int y) {
        for (int i = 0; i < tiles.length; i++) {
            if (tiles[i].getX() == x && tiles[i].getY() == y)
                ((DestructibleTile) tiles[i]).wasDestroyed = true;
        }
    }

    public void tick() {

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
