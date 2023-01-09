package com.totvs.entities;

import java.awt.image.BufferedImage;

public class Player extends Entity {
    public boolean right, up, left, down;
    private int speed = 4;

    public Player(int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);
    }

    @Override
    public void tick() {
        if (right) {
            x += speed;
        } else if (left) {
            x -= speed;
        }

        if (right) {
            y -= speed;
        } else if (left) {
            y += speed;
        }
    }
}
