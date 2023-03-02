package com.totvs.net;

import com.totvs.entities.PlayerMP;
import com.totvs.graphics.BombColors;
import com.totvs.main.Game;
import com.totvs.net.packet.Packet;
import com.totvs.net.packet.Packet00Login;
import com.totvs.net.packet.Packet01Disconnect;
import com.totvs.net.packet.Packet02Move;
import com.totvs.world.World;

import java.io.IOException;
import java.net.*;

public class GameClient extends Thread {
    private InetAddress ipAddress;
    private DatagramSocket socket;
    private Game game;

    public GameClient(Game game, String ipAddress) {
        this.game = game;
        try {
            this.socket = new DatagramSocket();
            this.ipAddress = InetAddress.getByName(ipAddress);
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(true) {
            byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
        }
    }

    private void parsePacket(byte[] data, InetAddress address, int port) {
        String message = new String(data).trim();
        Packet.PacketTypes type = Packet.lookupPacket(message.substring(0, 2));
        Packet packet;

        switch (type) {
            case INVALID -> {}
            case LOGIN -> {
                packet = new Packet00Login(data);
                System.out.println("[" + address.getHostAddress() + ":" + port + "] " +
                        ((Packet00Login) packet).getUserName() +
                        " has joined the game...");

                PlayerMP player = new PlayerMP(((Packet00Login) packet).getUserName(),
                        World.playerPos.get(0).get(0),
                        World.playerPos.get(0).get(1), 8, 8,
                        Game.player1Spritesheet.getSprite(0, 69, 16, 26), BombColors.ORANGE, address, port);
                game.entities.add(player);
            }
            case DISCONECT -> {
                packet = new Packet01Disconnect(data);
                System.out.println("[" + address.getHostAddress() + ":" + port + "] " +
                        ((Packet01Disconnect) packet).getUserName() +
                        " has left the game...");
                game.removePlayerMP(((Packet01Disconnect) packet).getUserName());
            }
            case MOVE -> {
                packet = new Packet02Move(data);
                this.handleMove((Packet02Move) packet);
            }
        }
    }

    private void handleMove(Packet02Move packet) {
        this.game.movePlayer(packet.getUserName(), packet.getX(), packet.getY());
    }

    public void sendData(byte[] data) {
        DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, 1331);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
