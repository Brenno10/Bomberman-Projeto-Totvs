package com.totvs.net.packet;

import com.totvs.net.GameClient;
import com.totvs.net.GameServer;

public class Packet02Move extends Packet {
    private String userName;
    private int x, y;

    public Packet02Move(byte[] data) {
        super(02);
        String[] dataArray = readData(data).split(",");
        this.userName = dataArray[0] = userName;
        this.x = Integer.parseInt(dataArray[1]);
        this.x = Integer.parseInt(dataArray[2]);
    }

    public Packet02Move(String userName, int x, int y) {
        super(02);
        this.userName = userName;
        this.x = x;
        this.y = y;
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
        return ("02" + this.userName + "," + this.x + "," + this.y).getBytes();
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
}
