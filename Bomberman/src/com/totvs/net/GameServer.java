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
                        " has connected...");

                PlayerMP player = new PlayerMP(((Packet00Login) packet).getUserName(),
                        ((Packet00Login) packet).getX(), ((Packet00Login) packet).getY(), 8, 8,
                        Game.player1Spritesheet.getSprite(0, 69, 16, 26), BombColors.ORANGE, address, port);
                this.addConnection(player, ((Packet00Login) packet));
            }
            case DISCONECT -> {
                packet = new Packet01Disconnect(data);
                System.out.println("[" + address.getHostAddress() + ":" + port + "] " +
                        ((Packet01Disconnect) packet).getUserName() +
                        " has left...");
                this.removeConnection((Packet01Disconnect) packet);
            }
            case MOVE -> {
                packet = new Packet02Move(data);
                this.handleMove(((Packet02Move) packet));
            }
        }
    }

    public void addConnection(PlayerMP player, Packet00Login packet) {
        boolean alreadyConnected = false;

        for (PlayerMP p : connectedPlayers) {
            if (player.getUserName().equalsIgnoreCase(p.getUserName())) {
                if (p.ipAddress == null) {
                    p.ipAddress = player.ipAddress;
                }
                if (p.port == -1) {
                    p.port = player.port;
                }
                alreadyConnected = true;
            } else {
                sendData(packet.getData(), p.ipAddress, p.port);
                packet = new Packet00Login(p.getUserName(), p.getX(), p.getY());
                sendData(packet.getData(), player.ipAddress, player.port);
            }
        }
        if (!alreadyConnected) {
            this.connectedPlayers.add(player);
        }
    }

    public void removeConnection(Packet01Disconnect packet) {
        this.connectedPlayers.remove(getPlayerMPIndex(packet.getUserName()));
        packet.writeData(this);
    }

    public PlayerMP getPlayerMP(String userName) {
        for (PlayerMP p : connectedPlayers) {
            if (p.userName.equalsIgnoreCase(userName)) {
                return p;
            }
        }
        return null;
    }

    public int getPlayerMPIndex(String userName) {
        int index = 0;
        for (PlayerMP p : connectedPlayers) {
            if (p.userName.equalsIgnoreCase(userName)) {
                break;
            }
            index++;
        }
        return index;
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

    private void handleMove(Packet02Move packet) {
        if (getPlayerMP(packet.getUserName()) != null) {
            int index = getPlayerMPIndex(packet.getUserName());
            PlayerMP player = this.connectedPlayers.get(index);
            player.setX(packet.getX());
            player.setY(packet.getY());
            player.moved = packet.moved;
            player.dir = packet.getDir();
            player.index = packet.getIndex();
            packet.writeData(this);
        }
    }
}
