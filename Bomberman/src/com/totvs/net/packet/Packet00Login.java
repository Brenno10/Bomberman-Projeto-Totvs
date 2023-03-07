package com.totvs.net.packet;

import com.totvs.net.GameClient;
import com.totvs.net.GameServer;

public class Packet00Login extends Packet {
    private String userName;
    private int x, y;

    public Packet00Login(byte[] data) {
        super(00);
        String[] dataArray = readData(data).split(",");
        this.userName = dataArray[0];
        this.x = Integer.parseInt(dataArray[1]);
        this.y = Integer.parseInt(dataArray[2]);
    }

    public Packet00Login(String userName, int x, int y) {
        super(00);
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
        return ("00" + this.userName + "," + getX() + "," + getY()).getBytes();
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

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
