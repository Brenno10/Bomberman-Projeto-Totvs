package com.totvs.entities;

import java.awt.image.BufferedImage;
import java.net.InetAddress;

public class PlayerMP extends Player {
    public InetAddress ipAddress;
    public int port;

    public PlayerMP(String name, int x, int y, int width, int height, BufferedImage sprite, int[] playerColor,
                    InetAddress ipAddress, int port) {
        super(name, x, y, width, height, sprite, playerColor);
        this.ipAddress = ipAddress;
        this.port = port;
    }
}
