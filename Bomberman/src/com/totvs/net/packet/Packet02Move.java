package com.totvs.net.packet;

import com.totvs.net.GameClient;
import com.totvs.net.GameServer;

public class Packet02Move extends Packet {
    private String userName;
    private int x, y, dir, index;
    public boolean moved;

    public Packet02Move(byte[] data) {
        super(02);
        String[] dataArray = readData(data).split(",");
        this.userName = dataArray[0];
        this.x = Integer.parseInt(dataArray[1]);
        this.y = Integer.parseInt(dataArray[2]);
        this.moved = Integer.parseInt(dataArray[3]) == 1;
        this.dir = Integer.parseInt(dataArray[4]);
        this.index = Integer.parseInt(dataArray[5]);
    }

    public Packet02Move(String userName, int x, int y, boolean moved, int dir, int index) {
        super(02);
        this.userName = userName;
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.moved = moved;
        this.index = index;
    }

    @Override
    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    @Override
    public void writeData(GameServer server) {
        server.sendDataToAllClients(getData());
    }

    @Override
    public byte[] getData() {
        return ("02" + this.userName + "," + getX() + "," + getY() +
                "," + (moved?1:0) + "," + this.getDir() + "," + this.getIndex()).getBytes();
    }

    public String getUserName() {
        return userName;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDir() {
        return dir;
    }

    public int getIndex() {
        return index;
    }
}
