package com.totvs.world;

import com.totvs.main.Game;

import java.awt.image.BufferedImage;

public class DestructibleTile extends Tile {
    private int frames = 0, index = 0;
    private final int maxFrames = 4, maxIndex = 6;
    private final BufferedImage[] tileFrame;
    public boolean wasDestroyed = false;

    public DestructibleTile(int x, int y, BufferedImage sprite) {
        super(x, y, sprite);

        tileFrame = new BufferedImage[6];

        for (int i = 0; i < 6; i++) {
            tileFrame[i] = Game.tilesSpritesheet.getSprite(i * 16 + i, 31, 16, 16);
        }
    }

    @Override
    public void tick() {
        if (this.wasDestroyed) {
            this.frames++;
            if (this.frames == this.maxFrames && this.index <= this.maxIndex) {
                if (index == maxIndex) {
                    this.destroy();
                } else {
                    this.sprite = this.tileFrame[index];
                    this.index++;
                    this.frames = 0;
                }
            }
        }
    }
}
