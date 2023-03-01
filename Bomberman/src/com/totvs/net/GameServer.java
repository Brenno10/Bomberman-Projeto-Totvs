package com.totvs.net;

import com.totvs.entities.PlayerMP;
import com.totvs.graphics.BombColors;
import com.totvs.main.Game;
import com.totvs.net.packet.Packet;
import com.totvs.net.packet.Packet00Login;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameServer extends Thread {
    private DatagramSocket socket;
    private Game game;
    public List<PlayerMP> connectedPlayers = new ArrayList<>();

    public GameServer(Game game) {
        this.game = game;
        try {
            this.socket = new DatagramSocket(1331);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }

            parsePacket(packet.getData(), packet.getAddress(), packet.getPort());

//            String message = new String(packet.getData()).trim();
//            System.out.println("Client [" + packet.getAddress().getHostAddress() + ":"
//                    + packet.getPort() + "] > " + message);
//
//            if (message.equalsIgnoreCase("ping")) {
//                sendData("pong".getBytes(), packet.getAddress(), packet.getPort());
//            }
        }
    }

    private void parsePacket(byte[] data, InetAddress address, int port) {
        String message = new String(data).trim();
        Packet.PacketTypes type = Packet.lookupPacket(message.substring(0, 2));
        switch (type) {
            case INVALID -> {

            }
            case LOGIN -> {
                Packet00Login packet = new Packet00Login(data);
                System.out.println("[" + address.getHostAddress() + ":" + port + "]" + packet.getUserName() +
                        " has connected...");

                PlayerMP player = null;
                if (address.getHostAddress().equalsIgnoreCase("127.0.0.1")) {
                    player = new PlayerMP(packet.getUserName(), 0, 0, 8, 8,
                            Game.player1Spritesheet.getSprite(0, 69, 16, 26), BombColors.PURPLE, address, port);
                }
                if (player != null) {
                    this.connectedPlayers.add(player);
                    game.entities.add(player);
                    game.player = player;
                }
            }
            case DISCONECT -> {

            }
            default -> {

            }
        }
    }

    public void sendData(byte[] data, InetAddress ipAddress, int port) {
        DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
        try {
            this.socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendDataToAllClients(byte[] data) {
        for (PlayerMP p : connectedPlayers) {
            sendData(data, p.ipAddress, p.port);
        }
    }
}
